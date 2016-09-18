package com.palm.yh.price.server.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientUpdateResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * 用户资料
 * Created by fengzt on 2016/7/15.
 */
public interface UserPriceService {

    /**
     * 添加
     *
     * @param jsonObject
     * @param resultHandler 返回结果
     */
    void add(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler);

    /**
     * 根据query 查找
     *
     * @param query
     * @param handler 返回用户信息
     */
    void findOne(JsonObject query, Handler<AsyncResult<JsonObject>> handler);

    /**
     * 根据query 查找一组消息
     *
     * @param query
     * @param handler 返回用户列表
     */
    void findList(JsonObject query, Handler<AsyncResult<List<JsonObject>>> handler);

    /**
     * 根据用户传入参数，更新
     *
     * @param query   查询条件
     * @param update  更新的字段
     * @param handler
     */
    void update(JsonObject query, JsonObject update, Handler<AsyncResult<MongoClientUpdateResult>> handler);

    /**
     * 根据用户传入参数，删除(非物理删除，更新op字段)
     *
     * @param query   查询条件
     * @param handler
     */
    void delete(JsonObject query, Handler<AsyncResult<MongoClientUpdateResult>> handler);

    /**
     * 计算条数
     * @param query
     * @param handler
     */
    void count(JsonObject query, Handler<AsyncResult<Long>> handler);
    
    /**
     * 添加产品信息
     *
     * @param jsonObject
     * @param resultHandler 返回结果
     */
    void addProduct(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler);

    /**
     * 查询产品信息
     *
     * @param jsonObject
     * @param resultHandler 返回结果
     */
    void findProduct(JsonObject query, Consumer<List<JsonObject>> result);
    
    /**
     * 查询产品对应（总报价数，胸径范围 max-min，上车范围 max-min，上车平均价，到货价范围 max-min，到货平均价）
     *
     * @param jsonObject
     * @param resultHandler 返回结果
     */
    void findProductTotal(JsonObject query, Consumer<List<JsonObject>> result);
    
    /**
     * 供应商统计
     * 
     * @param query
     * @param result
     */
    void supplierTotal(JsonObject query, Consumer<List<JsonObject>> result);
}
