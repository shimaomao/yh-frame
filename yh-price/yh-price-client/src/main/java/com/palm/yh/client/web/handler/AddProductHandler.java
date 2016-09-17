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
 * 新增产品
 */
@Component
public class AddProductHandler implements Handler<RoutingContext> {

    private Logger logger = LoggerFactory.getLogger(AddProductHandler.class);

    @Autowired
    private HttpSupport httpSupport;

    @Autowired
    private PalmVert palmVert;

    @Autowired
    private RetCodeService retCodeService;

    /**
     * 新增产品
     */
    @Override
    public void handle(RoutingContext ctx) {
        httpSupport.getRequestParam(ctx, (isSuc, query) -> {
            if (isSuc) {
                logger.debug("[AddHandler]传入的数据为：{}", query);
                if (null != query && !query.isEmpty()) {
                	//获取param参数
                    String param = query.getString("param");
                    JsonObject result = new JsonObject();
                    if (StringUtils.isNotBlank(param)) {
                        result = new JsonObject(param);
                    }

                    CompletableFuture<JsonObject> future = new CompletableFuture<JsonObject>();
                    future.complete(result);
                    future.thenCompose(res -> {
                        CompletableFuture<JsonObject> next = new CompletableFuture<JsonObject>();
                        palmVert.getVertx().eventBus().<String>send(YhConsumerAddressUtil.PRICE_ADD_PRODUCT, res, reply -> {
                            if (reply.succeeded()) {
                                String id = reply.result().body();
                                next.complete(new JsonObject().put("code", "0").put("data", new JsonObject().put("_id", id)));
                            } else {
                                next.complete(new JsonObject().put("code", "-1").put("msg", "CONSUME_TIME_OUT"));
                            }
                        });
                        //返回
                        return next;
                    }).thenCompose(resultJson -> {
                        //替换成本地化异常信息  错误信息转换为本地语言信息
                        return retCodeService.replaceRetCode(ctx, resultJson);
                    }).whenComplete((retJson, ex) -> {
                        if (null == ex) {
                            if ("0".equals(retJson.getString("code"))) {
                            	//新增后重定向跳转
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