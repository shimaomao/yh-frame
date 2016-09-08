package com.palm.yh.client.web.handler;

import com.palm.vertx.web.support.HttpSupport;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 打开页面
 * Created by fengzt on 2016/9/8.
 */
@Component
public class OpenPageHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(OpenPageHandler.class);
    @Autowired
    private HttpSupport httpSupport;

    @Override
    public void handle(RoutingContext routingContext) {
        httpSupport.sendTemplate(routingContext, new JsonObject(), "/templates/login/success.html", result ->{
            logger.debug("结果：{}", result);
        });
    }
}
