package com.palm.yh.client.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palm.vertx.core.application.PalmVert;
import com.palm.vertx.web.support.HttpSupport;
import com.palm.yh.common.util.YhConsumerAddressUtil;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * 统计报价数，胸径范围 max-min，上车范围 max-min，上车平均价，到货价范围 max-min，到货平均价，供应商
 */
@Component
public class TotalHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(TotalHandler.class);
  
    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;
    
    @Override
    public void handle(RoutingContext routingContext) {
        httpSupport.getRequestParam(routingContext, (isSuc, query) -> {
        	if(isSuc){
        		  logger.debug("传入的数据为：{}", query);
        		  if(null != query && !query.isEmpty()){
        			Future<JsonObject> totalFuture = Future.future();
        	            JsonObject param =  new JsonObject(query.getString("param"));
        	            palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.PRICE_TOTAL_PRODUCT, param, reply -> {
        	                if (reply.succeeded()) {
        	                    JsonObject res = reply.result().body();
        	                    logger.debug("处理结果：{}", res);
        	                    totalFuture.complete(res);
        	                }
        	            });
        	        totalFuture.setHandler(handler ->{
        	            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
        	            httpSupport.sendJson(routingContext, handler.result());
        	        });
        		 }
        	}
       });
    }
}
