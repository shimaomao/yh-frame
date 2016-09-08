package com.palm.yh.common.service.impl;

import com.palm.yh.common.service.RetCodeService;
import com.palm.vertx.mongo.support.MongoSupport;
import com.palm.vertx.redis.support.RedisSupport;
import com.palm.yh.common.util.OpType;
import com.palm.yh.common.util.YhCollectionUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

/**
 * 返回码 {@see RetCodeService}
 * Created by fengzt on 2016/5/24.
 */
@Service("retCodeService")
public class RetCodeServiceImpl implements RetCodeService {
    private Logger logger = LoggerFactory.getLogger(RetCodeServiceImpl.class);

    @Autowired
    private MongoSupport mongoSupport;

    @Autowired
    private RedisSupport redisSupport;

    @Value("${mlh.ret.code.prefix}")
    private String retCodeRedisPrefix;

    //过期时间（单位：s）
    private final static long RET_CODE_EXPIRED = 7 * 24 * 60 * 60;

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRetCode(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler) {
        jsonObject.put("op", OpType.ACT);
        String key = jsonObject.getString("key");
        Future<Boolean> redisFuture = Future.future();
        Future<AsyncResult<String>> mongodbFuture = Future.future();

        //添加到缓存
        redisSupport.getRedisClient().setex(retCodeRedisPrefix + key, RET_CODE_EXPIRED, Json.encode(jsonObject), handler -> {
            logger.debug("[RetCodeServiceImpl#addRetCode] redis执行结果{} : {}", handler.succeeded(), handler.result());
            redisFuture.complete(handler.succeeded());
        });

        //添加到数据库
        logger.debug("[RetCodeServiceImpl#addRetCode] 插入到数据库json :{}", jsonObject);
        mongoSupport.getMongoClient().insert(YhCollectionUtil.RET_CODE_COLLECTION, jsonObject, result -> {
            mongodbFuture.complete(result);
        });

        //插入 数据库和redis成功后
        CompositeFuture.all(redisFuture, mongodbFuture).setHandler(ar -> {
            logger.debug("#1 succeeded: {}, result:{}", ar.succeeded(), ar.result());
            logger.debug("#2 size :  redisFuture result:{}, mongodbFuture result: {}",
                     ar.result().result(0), ar.result().result(1));
            if (ar.succeeded()) {
                //拿到mongodbFuture结果 set resultHandler
                resultHandler.handle(ar.result().result(1));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delRetCode(JsonObject query, Handler<AsyncResult<Void>> resultHandler) {
        String key = query.getString("key");
        //这里会先于两个future执行，构造update字段
        JsonObject update = new JsonObject().put("$set",
                new JsonObject().put("op", OpType.LDEL).put("modifyDate", new Date().getTime()));
        logger.debug("[RetCodeServiceImpl#delRetCode] 更新数据库 query：{}, update :{}", query, update);

        Future<Boolean> redisFuture = Future.future();
        Future<AsyncResult<Void>> mongodbFuture = Future.future();

        //删除缓存
        redisSupport.getRedisClient().del(retCodeRedisPrefix + key, handler -> {
            logger.debug("[RetCodeServiceImpl#delRetCode] 删除缓存结果{} - result: {}",
                    handler.succeeded(), handler.result());
            redisFuture.complete(handler.succeeded());
        });

        //更新数据库
        mongoSupport.getMongoClient().update(YhCollectionUtil.RET_CODE_COLLECTION, query, update, result -> {
            mongodbFuture.complete(result);
        });

        CompositeFuture.all(redisFuture, mongodbFuture).setHandler(ar -> {
            logger.debug(" redisFuture result:{}, mongodbFuture result: {}",
                   ar.result().result(0), ar.result().result(1));
            if (ar.succeeded()) {
                //拿到mongodbFuture结果 set resultHandler
                resultHandler.handle(ar.result().result(1));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findOneRetCode(JsonObject query, Consumer<JsonObject> resultHandler) {
        String key = query.getString("key");
        query.put("op", OpType.ACT);

        Future<String> redisFuture = Future.future();

        //1.缓存查询
        redisSupport.getRedisClient().get(retCodeRedisPrefix + key, handler -> {
            logger.debug("[findOneRetCode]redis查询结果 {} result：{}", handler.succeeded(), handler.result());
            redisFuture.complete(handler.result());
        });

        redisFuture.setHandler(handler -> {
            logger.debug("redisFuture setHandler {}, result : {}", handler.succeeded(), handler.result());
            //2.redis中已经有值
            if (handler.succeeded() && StringUtils.isNotBlank(handler.result())) {
                resultHandler.accept(new JsonObject(handler.result()));
            } else {
                Future<JsonObject> mongodbFuture = Future.future();
                //3.查询数据库
                mongoSupport.getMongoClient().findOne(YhCollectionUtil.RET_CODE_COLLECTION, query, null, findOneHandler -> {
                    logger.debug("[findOne]数据库查询结果{}, result: {}", findOneHandler.succeeded(), findOneHandler.result());
                    if (findOneHandler.succeeded()) {
                        mongodbFuture.complete(findOneHandler.result());
                    } else {
                        resultHandler.accept(null);
                    }
                });

                //4.放入redis
                mongodbFuture.setHandler(ar -> {
                    JsonObject json = ar.result();
                    logger.debug("[set]数据库查询结果{}, result: {}", ar.succeeded(), ar.result());
                    if (null != json && !json.isEmpty()) {
                        //5. 重新set到redis
                        redisSupport.getRedisClient().setex(retCodeRedisPrefix + key, RET_CODE_EXPIRED, Json.encode(json), setexHandler -> {
                            logger.debug("[setex]存入redis结果：{}", setexHandler.result());
                            if (setexHandler.succeeded()) {
                                resultHandler.accept(json);
                            } else {
                                resultHandler.accept(null);
                            }
                        });
                    } else {
                        resultHandler.accept(null);
                    }
                });
            }

        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findRetCode(JsonObject query, Handler<AsyncResult<List<JsonObject>>> resultHandler) {
        logger.debug("[RetCodeServiceImpl#findRetCode] 查询条件：{}", query);
        //op是可用的
        query.put("op", OpType.ACT);
        //查询数据库
        mongoSupport.getMongoClient().find(YhCollectionUtil.RET_CODE_COLLECTION, query, resultHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findRetCodeByKey(String key, Consumer<JsonObject> result) {
        findRetCodeByKey(key, "zh-CN", result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findRetCodeByKey(String key, String location, Consumer<JsonObject> result) {
        JsonObject query = new JsonObject().put("key", key);
        logger.debug("查询条件：{}", query);
        //查询retCode
        findOneRetCode(query, findOneResult -> {
            logger.debug("findOneRetCode");
            if (null != findOneResult) {
                JsonObject json = new JsonObject();
                json.put("code", findOneResult.getString("code"));
                //根据浏览器语言环境获取当地msg信息，如果没有默认中国
                if (StringUtils.isNotBlank(location)) {
                    json.put("msg", findOneResult.getString("msg_" + location));
                } else {
                    json.put("msg", findOneResult.getString("msg_zh-CN"));
                }
                result.accept(json);
            } else {
                result.accept(null);
            }
        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CompletionStage<JsonObject> replaceRetCode(RoutingContext ctx, JsonObject resultJson) {
        CompletableFuture<JsonObject> next = new CompletableFuture<JsonObject>();
        //如果msg不为空，替换成本地消息
        if (StringUtils.isNotBlank(resultJson.getString("msg"))) {
            //浏览器本地语言
            String locale = ctx.preferredLocale().toString();
            logger.debug("浏览器语言环境为：{}", locale);
            findRetCodeByKey(resultJson.getString("msg"), locale, retCode -> {
                if (null != retCode && !retCode.isEmpty()) {
                    resultJson.put("code", retCode.getString("code")).put("msg", retCode.getString("msg"));
                }
                next.complete(resultJson);
            });
        } else {
            next.complete(resultJson);
        }
        return next;
    }
}
