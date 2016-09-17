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
 * 更新用户
 * Created by fengzt on 2016/9/7.
 */
@Component
public class UpdateHandler implements Handler<RoutingContext> {
    private Logger logger = LoggerFactory.getLogger(UpdateHandler.class);

    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public void handle(RoutingContext routingContext) {
        httpSupport.getRequestParam(routingContext, (isSuc, query) -> {
            if (isSuc) {
                logger.debug("传入的数据为：{}", query);
                if (null != query && !query.isEmpty()) {
                    Future<JsonObject> future = Future.future();
                    String param = query.getString("param");
                    JsonObject result = new JsonObject();
                    if (StringUtils.isNotBlank(param)) {
                        result = new JsonObject(param);
                    }

                    //从cookie中获取userId
//                    String userId = userAuthService.getUserIdFromCookie(routingContext);
                    String userId = "576390d221500e7eb377a7dd";
                    if (null == userId ){
                        userId = "576390d221500e7eb377a7dd";
                    }
                    if(StringUtils.isNotBlank(userId)){
                        result.put("userId", userId);
                        //更新
                        palmVert.getVertx().eventBus().<JsonObject>send(YhConsumerAddressUtil.USER_UPDATE, result, reply -> {
                            if (reply.succeeded()) {
                                JsonObject res = reply.result().body();
                                logger.debug("处理结果：{}", res);
                                if (null != res && !res.isEmpty()) {
                                    future.complete(res);
                                }
                            } else {
                                future.complete(new JsonObject().put("code", "-1").put("msg", "CONSUME_TIME_OUT"));
                            }
                        });
                    }else {
                        future.complete(new JsonObject().put("code", "-1").put("msg", "USER_NO_LOGIN")
                                .put("redirectUri", "/user/loginPage"));
                    }
                    future.setHandler(handler->{
                        logger.debug("返回结果:{}", handler.result());
                        httpSupport.sendJson(routingContext, handler.result());
                    });
                }
            }
        });
    }
}