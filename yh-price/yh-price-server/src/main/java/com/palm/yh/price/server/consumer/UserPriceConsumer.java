package com.palm.yh.price.server.consumer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.palm.vertx.core.annotation.Consume;
import com.palm.vertx.core.verticle.PalmConsumer;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import com.palm.yh.price.server.service.UserPriceService;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * 用户consumer
 * Created by fengzt on 2016/7/15.
 */
@Consume
public class UserPriceConsumer extends PalmConsumer {
    private Logger logger = LoggerFactory.getLogger(UserPriceConsumer.class);

    @Autowired
    private UserPriceService userPriceService;
    
    //查询地区
    private Handler<Message<JsonObject>> findArceHandler = handler ->{
    	 JsonObject body = handler.body();
    	 userPriceService.findArce(body, resultHandler ->{
               if (resultHandler.size()>0) {
            	   logger.debug("地区:{}",resultHandler);
                   handler.reply(new JsonObject().put("arce", resultHandler));
               }else{
                   handler.reply(new JsonObject());
               }
          });
    };
    
    //新增产品
    private Handler<Message<JsonObject>> addProductHandler = handler ->{
        userPriceService.findList(new JsonObject(), listHandler ->{
                logger.debug("爬取的结果 {}：{}", listHandler.succeeded(), listHandler.result());
                int result = listHandler.result().size();
                 for(int i =0,j= listHandler.result().size();i<j;i++){
                     userPriceService.addProduct( listHandler.result().get(i), resultHandler ->{
                     });
                 }
                 handler.reply(new JsonObject().put("result", result));
        });
    };
    
    //查找产品
    private Handler<Message<JsonObject>> findProductHandler = handler ->{
        JsonObject body = handler.body();
        if(body == null){
        	handler.reply(new JsonObject().put("code", "-1").put("msg", "QUERY_ERROR").put("data", "{}").put("body", body)); 
        	return;
        }
        CompletableFuture<Message<JsonObject>> future = new CompletableFuture<Message<JsonObject>>();
        future.complete(null);
        future.thenCompose(first ->{
        	//全条件查询
        	CompletableFuture<List<JsonObject>> next = new CompletableFuture<List<JsonObject>>();
            userPriceService.findProduct(body, resultHandler ->{
            	logger.debug("查询的结果：{}",resultHandler);
            	next.complete(resultHandler);
            });
            return next;
        }).thenCompose(second ->{
        	CompletableFuture<List<JsonObject>> next = new CompletableFuture<List<JsonObject>>();
        	//全条件查询为0，则排除高度和冠幅
        	if(second.size() == 0){
        		body.remove("heightMax");body.remove("heightMin");
        		body.remove("crownMax");body.remove("crownMin");
                userPriceService.findProduct(body, resultHandler ->{
                	logger.debug("查询的结果：{}",resultHandler);
                	next.complete(resultHandler);
                }); 
        	}else {
                next.complete(second);
            }
        	return next;    	
        }).thenCompose(third ->{
        	CompletableFuture<List<JsonObject>> next = new CompletableFuture<List<JsonObject>>();
        	//排除高度和冠幅也为0，产品名替换品种名
        	if(third.size() == 0){
        		body.put("productName", body.getString("breedName"));
				body.remove("breedName");
                userPriceService.findProduct(body, resultHandler ->{
                	logger.debug("查询的结果：{}",resultHandler);
                	next.complete(resultHandler);
                }); 
        	}else {
                next.complete(third);
            }
        	return next;    	
        }).whenComplete( (result, ex) ->{
        	JsonObject product=new JsonObject().put("code","0").put("msg", "").put("result", result).put("body", body);
	        handler.reply(product); 
        });
    };
    
    //统计产品
    private Handler<Message<JsonObject>> totalProductHandler = handler ->{
    	   JsonObject body = handler.body();
           if(body == null){
           	handler.reply(new JsonObject().put("code", "-1").put("msg", "QUERY_ERROR").put("count", "{}")); 
           	return;
           }
           Future<List<JsonObject>> productTotal = Future.future();
           Future<List<JsonObject>> supplierTotal = Future.future();
           userPriceService.findProductTotal(body, resultTotalHandler ->{
        		productTotal.complete(resultTotalHandler);
           });
           userPriceService.supplierTotal(body, supplierHandler ->{
        		supplierTotal.complete(supplierHandler);
           });
           CompositeFuture.all(productTotal, supplierTotal).setHandler(futureHandler ->{
          		List<JsonObject> productTotalResult = futureHandler.result().result(0);
                List<JsonObject> supplierTotalResult = futureHandler.result().result(1);
                productTotalResult.add(new JsonObject().put("supplierTotal", supplierTotalResult.get(0).getInteger("count")));
                logger.debug("获取结果productTotalResult :{}, supplierTotalResult: {}", productTotalResult, supplierTotalResult.get(0).getInteger("count"));
	           	handler.reply(new JsonObject().put("count", productTotalResult)); 
       	   });
    };
    
    @Override
    public void initConsumer() throws Exception {
        logger.debug("[initConsumer]----start-------");
         /** priceSystem **/
        //新增产品信息
        logger.debug("查询地区");
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_FIND_AREA).handler(findArceHandler);
        
        //新增产品信息
        logger.debug("新增产品信息");
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_ADD_PRODUCT).handler(addProductHandler);
        
        //查询产品信息（分页）
        logger.debug("查询产品信息（分页）");
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_FIND_PRODUCT).handler(findProductHandler);
        
        //统计产品信息
        logger.debug("统计产品信息");
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_TOTAL_PRODUCT).handler(totalProductHandler);
        
        logger.debug("[initConsumer]----end-------");
    }

    @Override
    public void stopConsumer() throws Exception {
        logger.debug("---------stopConsumer--------");
    }
}
