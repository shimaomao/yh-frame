package com.palm.yh.demo.server.service.impl;

import com.palm.vertx.test.annotation.TestConsumer;
import com.palm.vertx.test.run.PalmTest;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import com.palm.yh.demo.server.consumer.UserDemoConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单元测试consumer
 * Created by fengzt on 2016/7/15.
 */
@TestConsumer(comsumer = {UserDemoConsumer.class})
public class UserDemoConsumerTest extends PalmTest {

    private Logger logger = LoggerFactory.getLogger(UserDemoConsumerTest.class);

    @Test
    public void add(TestContext context) throws Exception {
        Async async = context.async();
        JsonObject userInfoJson = new JsonObject().put("userName", "fengzt").put("gender", "F");
        palmVert.getVertx().eventBus().<String>send(YhConsumerAddressUtil.USER_ADD, userInfoJson, handler -> {
            logger.debug("更新结果：{}， result: {}", handler.succeeded(), handler.result());
            async.complete();
        });
    }

    @Test
    public void update(TestContext context) throws Exception {
        Async async = context.async();
        JsonObject userInfoJson = new JsonObject().put("_id", "57cfdd69f6dbc82b688d809c").put("gender", "M");
        palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.USER_UPDATE, userInfoJson, handler -> {
            logger.debug("更新结果：{}， result: {}", handler.succeeded(), handler.result());
            async.complete();
        });
    }

    @Test
    public void findOne(TestContext context) throws Exception {
        Async async = context.async();
        JsonObject userInfoJson = new JsonObject().put("_id", "57cfdd69f6dbc82b688d809c");
        palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.FIND_ONE, userInfoJson, handler -> {
            logger.debug("更新结果：{}， result: {}", handler.succeeded(), handler.result());
            async.complete();
        });
    }
}