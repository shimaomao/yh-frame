package com.palm.yh.price.server.service.impl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.palm.vertx.test.annotation.TestConsumer;
import com.palm.vertx.test.run.PalmTest;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import com.palm.yh.price.server.consumer.UserPriceConsumer;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

/**
 * 单元测试consumer
 * Created by fengzt on 2016/7/15.
 */
@TestConsumer(comsumer = {UserPriceConsumer.class})
public class UserPriceConsumerTest extends PalmTest {

    private Logger logger = LoggerFactory.getLogger(UserPriceConsumerTest.class);

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
    
    @Test
    public void find(TestContext context) throws Exception {
    	Async async = context.async();
    	JsonObject userInfoJson = new JsonObject().put("userName", "fengzt").put("$limit", "1");
    	palmVert.getVertx().eventBus().<String>send(YhConsumerAddressUtil.PRICE_USER_LIST, userInfoJson, handler ->{
    		logger.debug("更新结果：{}，result:{}", handler.succeeded(),handler.result().body());    	
    		async.complete();
    	});
    }
    
    @Test
    public void addProduct(TestContext context) throws Exception {
    	for(int a=0,j=8;a<j;a++){
    	Async async = context.async();
    	JsonObject productJson = new JsonObject().put("productName", "香樟").put("breedName", "香樟")
    			.put("miDiameter", 150).put("height",140).put("crown", 10).put("startingFare", "")
    			.put("invoiceType", "p1").put("totalPrice", "").put("area", "全国").put("supplier", "苗圃店")
    			.put("contacts", "苗农").put("tel", "13812345678").put("source", "1");
    	palmVert.getVertx().eventBus().<String>send(YhConsumerAddressUtil.PRICE_ADD_PRODUCT, productJson, handler ->{
    		logger.debug("新增结果：{}，result:{}", handler.succeeded(),handler.result().body());    	
    		async.complete();
    	});
    	}
    }
    
    @Test
    public void findProduct(TestContext context) throws Exception {
    	Async async = context.async();
    	JsonObject userInfoJson = new JsonObject().put("breedName", "香樟").put("area", "全国").put("heightMax", 150).put("skip", 1).put("limit",4);
    	palmVert.getVertx().eventBus().<String>send(YhConsumerAddressUtil.PRICE_FIND_PRODUCT, userInfoJson, handler ->{
    		logger.debug("更新结果：{}，result:{}", handler.succeeded(),handler.result().body());    	
    		async.complete();
    	});
    }
}