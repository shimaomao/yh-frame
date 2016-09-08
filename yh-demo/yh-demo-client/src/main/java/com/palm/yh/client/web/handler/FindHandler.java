package com.palm.yh.client.web.handler;

import com.palm.vertx.auth.util.service.UserAuthService;
import com.palm.vertx.core.application.PalmVert;
import com.palm.vertx.web.support.HttpSupport;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 查找一个用户的handler
 * Created by fengzt on 2016/9/7.
 */
@Component
public class FindHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(FindHandler.class);

    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;

    @Autowired
    private UserAuthService userAuthService;


    @Override
    public void handle(RoutingContext routingContext) {
        Future<JsonObject> userFuture = Future.future();

        String userId = routingContext.request().getParam("userId");
        if (StringUtils.isNotBlank(userId)) {
            JsonObject findOneUserParam = new JsonObject().put("_id", userId);
            //2. 查询用户账号
            palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.FIND_ONE, findOneUserParam, reply -> {
                if (reply.succeeded()) {
                    JsonObject res = reply.result().body();
                    logger.debug("处理结果：{}", res);
                    if (null != res && !res.isEmpty()) {
                        userFuture.complete(res);
                    } else {
                        userFuture.complete(null);
                    }
                } else {
                    userFuture.complete(null);
                }
            });
        }else{
            userFuture.complete(null);
        }

        userFuture.setHandler(handler ->{
            logger.debug("handler:{}, result:{}", handler.succeeded(), handler.result());
            httpSupport.sendTemplate(routingContext, handler.result(), "/html/regiter.html", sendHandler->{

            });
        });
    }

}