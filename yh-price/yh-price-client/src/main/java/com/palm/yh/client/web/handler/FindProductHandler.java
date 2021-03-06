package com.palm.yh.client.web.handler;

import org.apache.commons.lang.StringUtils;
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
 * 查找产品（分页、多条件）
 */
@Component
public class FindProductHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(FindProductHandler.class);

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
        			Future<JsonObject> productFuture = Future.future();
        			String param = query.getString("param");
        	        if (StringUtils.isNotBlank(param)) {
        	            JsonObject findParam = new JsonObject(param);
        	            palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.PRICE_FIND_PRODUCT, findParam, reply -> {
        	                if (reply.succeeded()) {
        	                    JsonObject res = reply.result().body();
        	                    logger.debug("处理结果：{}", res);
        	                    productFuture.complete(res);
        	                } else {
        	                	productFuture.complete(new JsonObject().put("code", "-1").put("msg", "QUERY_TIME_OUT").put("result", "{}").put("count", "{}"));
        	                }
        	            });
        	        }else{
        	        	productFuture.complete(new JsonObject().put("code", "-1").put("msg", "QUERY_TIME_OUT").put("result", "{}").put("count", "{}"));
        	        }

        	        productFuture.setHandler(handler ->{
        	            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
        	            httpSupport.sendJson(routingContext, handler.result());
        	        });
        		 }
        	}
        });
      
    }

}