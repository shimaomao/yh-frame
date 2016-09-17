package com.palm.yh.price.server.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.palm.vertx.core.annotation.Consume;
import com.palm.vertx.core.verticle.PalmConsumer;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import com.palm.yh.price.server.service.UserPriceService;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * 用户consumer
 * Created by fengzt on 2016/7/15.
 */
@Consume
public class UserPriceConsumer extends PalmConsumer {
    private Logger logger = LoggerFactory.getLogger(UserPriceConsumer.class);

    @Autowired
    private UserPriceService userDemoService;

    //新增
    private Handler<Message<JsonObject>> addUserInfoHandler = handler ->{
        JsonObject body = handler.body();
        userDemoService.add(body, resultHandler ->{
            if (resultHandler.succeeded()) {
                handler.reply(resultHandler.result());
            }else{
                handler.reply(null);
            }
        });
    };

    //更新
    private Handler<Message<JsonObject>> updateUserInfoHandler = handler ->{
        JsonObject body = handler.body();
        if (body.containsKey("_id")) {
            JsonObject query = new JsonObject().put("_id", body.getString("_id"));
            Future<JsonObject> findFuture = Future.future();

            //查询当前用户资料是否已经存在
            userDemoService.findOne(query, countHandler ->{
                if (countHandler.succeeded()) {
                    findFuture.complete(countHandler.result());
                }
            });

            findFuture.setHandler(countHandler ->{
                JsonObject userInfoJson = countHandler.result();
                if (null != userInfoJson && userInfoJson.containsKey("_id")) {
                    //用户更新
                    body.remove("_id");
                    JsonObject update = new JsonObject().put("$set", body);
                    logger.debug("更新 query:{}, update:{}", query, update);
                    userDemoService.update(query, update, resultHandler ->{
                        logger.debug("更新结果:{}, result:{}", resultHandler.succeeded(), resultHandler.result());
                        if (resultHandler.succeeded()) {
                            handler.reply(new JsonObject().put("code", "0"));
                        }else{
                            handler.reply(new JsonObject().put("code", "-1"));
                        }
                    });
                }else{
                    //新增
                    userDemoService.add(body, addHandler ->{
                        if (addHandler.succeeded()) {
                            handler.reply(new JsonObject().put("code", "0"));
                        }else{
                            handler.reply(new JsonObject().put("code", "-1"));
                        }
                    });
                }
            });
        }else{
            handler.reply(new JsonObject().put("code", "-1"));
        }
    };

    //查找用户资料
    private Handler<Message<JsonObject>> findOneUserInfoHandler = handler ->{
        JsonObject body = handler.body();
        userDemoService.findOne(body, resultHandler ->{
            if (resultHandler.succeeded()) {
                handler.reply(resultHandler.result());
            }else{
                handler.reply(null);
            }
        });
    };

    //查找所有用户
    private Handler<Message<JsonObject>> findUserInfoHandler = handler ->{
        JsonObject body = handler.body();
        userDemoService.findList(body, resultHandler ->{
            if (resultHandler.succeeded()) {
                handler.reply(Json.encode(resultHandler.result()));
            }else{
                handler.reply(null);
            }
        });
    };
    
    //新增产品
    private Handler<Message<JsonObject>> addProductHandler = handler ->{
        JsonObject body = handler.body();
        userDemoService.addProduct(body, resultHandler ->{
            if (resultHandler.succeeded()) {
                handler.reply(Json.encode(resultHandler.result()));
            }else{
                handler.reply(null);
            }
        });
    };
    
    //查找产品
    private Handler<Message<JsonObject>> findProductHandler = handler ->{
        JsonObject body = handler.body();
        userDemoService.findProduct(body, resultHandler ->{
        	logger.debug("查询的结果：{}",resultHandler);
                handler.reply(Json.encode(resultHandler));        
        });
    };
    
    @Override
    public void initConsumer() throws Exception {
        logger.debug("[initConsumer]----start-------");
        //新增用户信息
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_ADD).handler(addUserInfoHandler);

        //更新用户信息
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_UPDATE).handler(updateUserInfoHandler);

        //根据用户ID查询用户资料
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_FIND_ONE).handler(findOneUserInfoHandler);
        
        //查询所有用户资料
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_USER_LIST).handler(findUserInfoHandler);
         /** priceSystem **/
        //新增产品信息
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_ADD_PRODUCT).handler(addProductHandler);
        
        //查询产品信息（分页）
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_FIND_PRODUCT).handler(findProductHandler);

        logger.debug("[initConsumer]----end-------");
    }

    @Override
    public void stopConsumer() throws Exception {
        logger.debug("---------stopConsumer--------");
    }
}
