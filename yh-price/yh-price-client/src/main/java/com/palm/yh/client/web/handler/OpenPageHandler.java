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
 * 打开查询页面
 */
@Component
public class OpenPageHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(OpenPageHandler.class);
  
    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;
    
    @Override
    public void handle(RoutingContext routingContext) {
		/*Future<JsonObject> arceFuture = Future.future();

            palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.PRICE_FIND_AREA, new JsonObject().put("key", "arce"), reply -> {
                if (reply.succeeded()) {
                    JsonObject res = reply.result().body();
                    logger.debug("处理结果：{}", res);
                    arceFuture.complete(new JsonObject().put("code", "0").put("msg", "").put("result", res.getJsonArray("arce")));
                } else {
                	arceFuture.complete(new JsonObject().put("code", "-1").put("msg", "QUERY_TIME_OUT").put("result", "{}"));
                }
            });

            arceFuture.setHandler(handler ->{
	            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
	            httpSupport.sendTemplate(routingContext, handler.result(), "/webroot/danpin.html", result ->{
	                logger.debug("结果：{}", result);
	            });
            });*/
    	  httpSupport.sendTemplate(routingContext, new JsonObject(), "/webroot/danpin.html", result ->{
              logger.debug("结果：{}", result);
    	  });
    }
}
