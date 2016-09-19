package com.palm.yh.price.server.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.palm.vertx.core.annotation.Consume;
import com.palm.vertx.core.verticle.PalmConsumer;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import com.palm.yh.price.server.service.UserPriceService;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * UserPriceConsumer
 */
@Consume
public class UserPriceConsumer extends PalmConsumer {
    private Logger logger = LoggerFactory.getLogger(UserPriceConsumer.class);

    @Autowired
    private UserPriceService userDemoService;
    
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
        if(body == null){
        	handler.reply(new JsonObject().put("code", "-1").put("msg", "QUERY_ERROR").put("data", "{}").put("count", "{}")); 
        	return;
        }
        userDemoService.findProduct(body, resultHandler ->{
        	logger.debug("查询的结果：{}",resultHandler);
        	JsonObject result=new JsonObject().put("code","0").put("msg", "").put("result", resultHandler);
        	if(resultHandler.size()==0)
        	{
        		body.remove("heightMax");body.remove("heightMin");
        		body.remove("crownMax");body.remove("crownMin");
        		userDemoService.findProduct(body, repeatHandler ->{
        			if(repeatHandler.size()==0){
        				body.put("productName", body.getString("breedName"));
        				body.remove("breedName");
        				userDemoService.findProduct(body, thirdHandler ->{
        					result.put("result", thirdHandler);
        				});
        			}else{
        				result.put("result", repeatHandler);
        			}
        		});
        	}
        	userDemoService.findProductTotal(body, resultTotalHandler ->{
        		userDemoService.supplierTotal(body, supplierHandler ->{
        			resultTotalHandler.add(new JsonObject().put("supplierTotal", supplierHandler.size()));
        			result.put("count", resultTotalHandler);
             		logger.debug("统计结果：{}",resultTotalHandler);
             		handler.reply(result);        
        		});
        		
        	});
        });
    };
    
    @Override
    public void initConsumer() throws Exception {
        logger.debug("[initConsumer]----start-------");
         /** priceSystem **/
        //新增产品信息
        logger.debug("新增产品信息");
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_ADD_PRODUCT).handler(addProductHandler);
        
        //查询产品信息（分页）
        logger.debug("查询产品信息（分页）");
        vertx.eventBus().<JsonObject>consumer(YhConsumerAddressUtil.PRICE_FIND_PRODUCT).handler(findProductHandler);
        
        logger.debug("[initConsumer]----end-------");
    }

    @Override
    public void stopConsumer() throws Exception {
        logger.debug("---------stopConsumer--------");
    }
}
