package com.palm.yh.client.web.handler;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.palm.vertx.auth.util.service.UserAuthService;
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
        Future<JsonObject> productFuture = Future.future();
        String param = routingContext.request().getParam("param");
        if (StringUtils.isNotBlank(param)) {
            JsonObject findParam = new JsonObject(param);
            logger.debug("查询条件：{}",findParam);
            palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.PRICE_FIND_PRODUCT, findParam, reply -> {
                if (reply.succeeded()) {
                    JsonObject res = reply.result().body();
                    logger.debug("处理结果：{}", res);
                    if (null != res && !res.isEmpty()) {
                    	productFuture.complete(res);
                    } else {
                    	productFuture.complete(null);
                    }
                } else {
                	productFuture.complete(null);
                }
            });
        }else{
        	productFuture.complete(null);
        }

        productFuture.setHandler(handler ->{
            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
            //查询后跳转的页面
            httpSupport.sendTemplate(routingContext, handler.result(), "/templates/login/loginPage.html", sendHandler->{

            });
        });
    }

}