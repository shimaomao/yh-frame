package com.palm.yh.common.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

/**
 * 马良行返回码：
 * 格式：
 * {
 * "_id" : "57454ec11f1b7345d8ad3dbc",
 * "code" : "10000",  //非0表示异常
 * "key" : "EMAIL_EXIST",
 * "msg_zh-CN" : "邮箱已存在", //提示信息
 * "op" : "ACT"
 * }
 * Created by fengzt on 2016/5/24.
 */
public interface RetCodeService {
    /**
     * 增加一条retCode
     *
     * @param jsonObject
     * @param resultHandler
     */
    void addRetCode(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler);

    /**
     * 删除retCode
     *
     * @param jsonObject
     * @param resultHandler
     */
    void delRetCode(JsonObject jsonObject, Handler<AsyncResult<Void>> resultHandler);

    /**
     * 查找一条retCode
     *
     * @param query
     * @param resultHandler
     */
    void findOneRetCode(JsonObject query, Consumer<JsonObject> resultHandler);

    /**
     * 查找retCode
     *
     * @param query
     * @param resultHandler
     */
    void findRetCode(JsonObject query, Handler<AsyncResult<List<JsonObject>>> resultHandler);

    /**
     * 替换成本地化的提示, 默认返回zh-CN
     * 返回值的格式：{"code" : "0", "msg" : "操作成功"}
     *
     * @param key
     * @param result
     */
    void findRetCodeByKey(String key, Consumer<JsonObject> result);

    /**
     * 替换成本地化的提示
     *
     * @param key
     * @param location i.e:中国 -- zh-CN
     * @param result
     */
    void findRetCodeByKey(String key, String location, Consumer<JsonObject> result);

    /**
     * 替换成本地化的提示
     *
     * @param ctx
     * @param resultJson
     * @return CompletionStage<JsonObject>
     */
    CompletionStage<JsonObject> replaceRetCode(RoutingContext ctx, JsonObject resultJson);
}
