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
        	handler.reply(new JsonObject().put("code", "-1").put("msg", "QUERY_ERROR").put("data", "{}").put("count", "{}")); 
        	return;
        }
        userPriceService.findProduct(body, resultHandler ->{
        	logger.debug("查询的结果：{}",resultHandler);
        	JsonObject result=new JsonObject().put("code","0").put("msg", "").put("result", resultHandler);
        	if(resultHandler.size()==0)
        	{
        		body.remove("heightMax");body.remove("heightMin");
        		body.remove("crownMax");body.remove("crownMin");
        		userPriceService.findProduct(body, repeatHandler ->{
        			if(repeatHandler.size()==0){
        				body.put("productName", body.getString("breedName"));
        				body.remove("breedName");
        				userPriceService.findProduct(body, thirdHandler ->{
        					result.put("result", thirdHandler);
        					userPriceService.findProductTotal(body, resultTotalHandler ->{
        		        		userPriceService.supplierTotal(body, supplierHandler ->{
        		        			resultTotalHandler.add(new JsonObject().put("supplierTotal", supplierHandler.size()));
        		        			result.put("count", resultTotalHandler);
        		             		logger.debug("统计结果：{}",resultTotalHandler);
        		             		handler.reply(result);        
        		        		});
        		        	  });
        				});
        			}else{
        				result.put("result", repeatHandler);
        				userPriceService.findProductTotal(body, resultTotalHandler ->{
        	        		userPriceService.supplierTotal(body, supplierHandler ->{
        	        			resultTotalHandler.add(new JsonObject().put("supplierTotal", supplierHandler.size()));
        	        			result.put("count", resultTotalHandler);
        	             		logger.debug("统计结果：{}",resultTotalHandler);
        	             		handler.reply(result);        
        	        		});
        	        	  });
        			}
        		});
        	}else{
        		userPriceService.findProductTotal(body, resultTotalHandler ->{
        		userPriceService.supplierTotal(body, supplierHandler ->{
        			resultTotalHandler.add(new JsonObject().put("supplierTotal", supplierHandler.size()));
        			result.put("count", resultTotalHandler);
             		logger.debug("统计结果：{}",resultTotalHandler);
             		handler.reply(result);        
        		});
        	  });
        	}
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
        
        logger.debug("[initConsumer]----end-------");
    }

    @Override
    public void stopConsumer() throws Exception {
        logger.debug("---------stopConsumer--------");
    }
}
