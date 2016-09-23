package com.palm.yh.client.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palm.vertx.web.support.HttpSupport;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * 打开运费页面
 */
@Component
public class FreightPageHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(FreightPageHandler.class);
  
    @Autowired
    private HttpSupport httpSupport;
    
    @Override
    public void handle(RoutingContext routingContext) {
	    	JsonObject param = new JsonObject();
	    	String destination = routingContext.request().getParam("destination");
	    	param.put("destination", !destination.equals("null")?destination:"");
	    	logger.debug("接收参数:{}",param);
	        httpSupport.sendTemplate(routingContext, param, "/webroot/freight.html", result ->{
	                logger.debug("结果：{}", result);
            });
    }
}
