package com.palm.yh.demo.server.service.impl;

import com.palm.vertx.mongo.support.MongoSupport;
import com.palm.yh.common.util.OpType;
import com.palm.yh.common.util.YhCollectionUtil;
import com.palm.yh.demo.server.service.UserDemoService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 用户
 * Created by fengzt on 2016/7/15.
 */
@Service
public class UserDemoServiceImpl implements UserDemoService {
    private Logger logger = LoggerFactory.getLogger(UserDemoServiceImpl.class);

    @Autowired
    private MongoSupport mongoSupport;

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler) {
        jsonObject.put("createTime", new Date().getTime());
        jsonObject.put("op", OpType.ACT);
        logger.debug("[add] json内容：{}", jsonObject);
        mongoSupport.getMongoClient().save(YhCollectionUtil.USER_INFO_COLLECTION, jsonObject, result -> {
            logger.debug("【add】 结果 {} result：{}", result.succeeded(), result.result());
            resultHandler.handle(result);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findOne(JsonObject query, Handler<AsyncResult<JsonObject>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        //查询单个站内消息
        query.put("op", OpType.ACT);
        logger.debug("[findOne]查询条件：{}", query);
        mongoSupport.getMongoClient().findOne(YhCollectionUtil.USER_INFO_COLLECTION, query, null, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[findOne]返回的结果 :{}", resultHandler.result());
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findList(JsonObject query, Handler<AsyncResult<List<JsonObject>>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        //查询站内消息
        query.put("op", OpType.ACT);
        logger.debug("[findList]查询条件：{}", query);
        mongoSupport.getMongoClient().find(YhCollectionUtil.USER_INFO_COLLECTION, query, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[findList]返回的结果 :{}", resultHandler.result());
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(JsonObject query, JsonObject update, Handler<AsyncResult<MongoClientUpdateResult>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        logger.debug("[update]查询条件：{}, 更新json : {}", query, update);

        //更新
        mongoSupport.getMongoClient().updateCollection(YhCollectionUtil.USER_INFO_COLLECTION, query, update, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[update] result : {}", resultHandler);
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(JsonObject query, Handler<AsyncResult<MongoClientUpdateResult>> handler) {
        if (null == query) {
            handler.handle(null);
            return;
        }

        JsonObject update = new JsonObject().put("$set", new JsonObject().put("op", OpType.LDEL)
                .put("modifyTime", new Date().getTime()));
        logger.debug("[update]查询条件：{}, 更新json : {}", query, update);
        //更新消息状态
        this.update(query, update, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void count(JsonObject query, Handler<AsyncResult<Long>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        //查询单个站内消息
        query.put("op", OpType.ACT);
        logger.debug("[count]查询条件：{}", query);
        mongoSupport.getMongoClient().count(YhCollectionUtil.USER_INFO_COLLECTION, query, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[count]返回的结果 :{}", resultHandler.result());
            }
            handler.handle(resultHandler);
        });
    }
}
