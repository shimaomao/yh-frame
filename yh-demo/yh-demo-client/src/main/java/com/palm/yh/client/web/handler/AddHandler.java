package com.palm.yh.client.web.handler;

import com.palm.vertx.core.application.PalmVert;
import com.palm.vertx.web.support.HttpSupport;
import com.palm.yh.common.service.RetCodeService;
import com.palm.yh.common.util.YhConsumerAddressUtil;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 新增用户
 * Created by fengzt on 2016/9/7.
 */
@Component
public class AddHandler implements Handler<RoutingContext> {

    private Logger logger = LoggerFactory.getLogger(AddHandler.class);

    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;

    @Autowired
    private RetCodeService retCodeService;

    /**
     * 用户注册
     */
    @Override
    public void handle(RoutingContext ctx) {
        httpSupport.getRequestParam(ctx, (isSuc, query) -> {
            if (isSuc) {
                logger.debug("[AddHandler]传入的数据为：{}", query);
                if (null != query && !query.isEmpty()) {
                    String param = query.getString("param");
                    JsonObject result = new JsonObject();
                    if (StringUtils.isNotBlank(param)) {
                        result = new JsonObject(param);
                    }

                    CompletableFuture<JsonObject> future = new CompletableFuture<JsonObject>();
                    future.complete(result);
                    future.thenCompose(res -> {
                        CompletableFuture<JsonObject> next = new CompletableFuture<JsonObject>();
                        // 传递给注册
                        palmVert.getVertx().eventBus().<String>send(YhConsumerAddressUtil.USER_ADD, res, reply -> {
                            if (reply.succeeded()) {
                                String id = reply.result().body();
                                next.complete(new JsonObject().put("code", "0").put("data", new JsonObject().put("_id", id)));
                            } else {
                                next.complete(new JsonObject().put("code", "-1").put("msg", "CONSUME_TIME_OUT"));
                            }
                        });
                        return next;
                    }).thenCompose(resultJson -> {
                        //替换成本地化异常信息
                        return retCodeService.replaceRetCode(ctx, resultJson);
                    }).whenComplete((retJson, ex) -> {
                        if (null == ex) {
                            if ("0".equals(retJson.getString("code"))) {
                                retJson.put("redirectUri", "/user/register/success");
                            }
                            logger.debug("添加用户的结果：{}", retJson);
                        }
                        httpSupport.sendJson(ctx, retJson);
                    });
                }
            }
        });
    }

}