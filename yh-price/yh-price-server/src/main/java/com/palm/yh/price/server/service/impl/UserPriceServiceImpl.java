package com.palm.yh.price.server.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hazelcast.util.StringUtil;
import com.palm.vertx.mongo.support.MongoSupport;
import com.palm.yh.common.util.OpType;
import com.palm.yh.common.util.YhCollectionUtil;
import com.palm.yh.price.server.service.UserPriceService;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClientUpdateResult;

/**
 * 用户
 * Created by fengzt on 2016/7/15.
 */
@Service
public class UserPriceServiceImpl implements UserPriceService {
    private Logger logger = LoggerFactory.getLogger(UserPriceServiceImpl.class);

    @Autowired
    private MongoSupport mongoSupport;

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler) {
        jsonObject.put("createTime", new Date().getTime());
        jsonObject.put("op", OpType.ACT);
        logger.debug("[add] json内容：{}", jsonObject);
        mongoSupport.getMongoClient().save(YhCollectionUtil.USER_INFO_COLLECTION, jsonObject, result -> {
            logger.debug("【add】 结果 {} result：{}", result.succeeded(), result.result());
            resultHandler.handle(result);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findOne(JsonObject query, Handler<AsyncResult<JsonObject>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        //查询单个站内消息
        query.put("op", OpType.ACT);
        logger.debug("[findOne]查询条件：{}", query);
        mongoSupport.getMongoClient().findOne(YhCollectionUtil.USER_INFO_COLLECTION, query, null, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[findOne]返回的结果 :{}", resultHandler.result());
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findList(JsonObject query, Handler<AsyncResult<List<JsonObject>>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        //查询站内消息
        query.put("op", OpType.ACT);
        logger.debug("[findList]查询条件：{}", query);
        mongoSupport.getMongoClient().find(YhCollectionUtil.USER_INFO_COLLECTION, query, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[findList]返回的结果 :{}", resultHandler.result());
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(JsonObject query, JsonObject update, Handler<AsyncResult<MongoClientUpdateResult>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        logger.debug("[update]查询条件：{}, 更新json : {}", query, update);
        //更新
        mongoSupport.getMongoClient().updateCollection(YhCollectionUtil.USER_INFO_COLLECTION, query, update, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[update] result : {}", resultHandler);
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(JsonObject query, Handler<AsyncResult<MongoClientUpdateResult>> handler) {
        if (null == query) {
            handler.handle(null);
            return;
        }

        JsonObject update = new JsonObject().put("$set", new JsonObject().put("op", OpType.LDEL)
                .put("modifyTime", new Date().getTime()));
        logger.debug("[update]查询条件：{}, 更新json : {}", query, update);
        //更新消息状态
        this.update(query, update, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void count(JsonObject query, Handler<AsyncResult<Long>> handler) {
        if (null == query || query.isEmpty()) {
            handler.handle(null);
            return;
        }
        //查询单个站内消息
        query.put("op", OpType.ACT);
        logger.debug("[count]查询条件：{}", query);
        mongoSupport.getMongoClient().count(YhCollectionUtil.USER_INFO_COLLECTION, query, resultHandler -> {
            if (resultHandler.succeeded()) {
                logger.debug("[count]返回的结果 :{}", resultHandler.result());
            }
            handler.handle(resultHandler);
        });
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void addProduct(JsonObject jsonObject, Handler<AsyncResult<String>> resultHandler) {
		jsonObject.put("createTime", new Date().getTime());
		jsonObject.put("updateTime", new Date().getTime());
        jsonObject.put("op", OpType.ACT);
        logger.debug("[add] json内容：{}", jsonObject);
        mongoSupport.getMongoClient().save(YhCollectionUtil.PRICE_PRODUCT, jsonObject, result -> {
            logger.debug("[add]结果 {} result：{}", result.succeeded(), result.result());
            resultHandler.handle(result);
        });
	}
    
	/**
     * {@inheritDoc}
     */
	@Override
	public void findProduct(JsonObject query, Consumer<List<JsonObject>> result) {
		String breedName = query.getString("breedName");
        if (null == breedName || breedName.isEmpty()) {
            result.accept(null);
            return;
        }
        //过滤主体
        JsonObject matchJson = new JsonObject().put("$match", pushJson(query));
             
        //排序条件
        JsonObject sortJson = new JsonObject().put("$sort", sortJson(query));
   
        //组装查询条件
        JsonArray pipelineArrayJson = new JsonArray().add(matchJson).add(sortJson)
        			.add(new JsonObject().put("$skip",query.getInteger("skip")))
        			.add(new JsonObject().put("$limit", query.getInteger("limit")));
        
        //组装aggregate命令
        JsonObject command = new JsonObject()
                .put("aggregate", YhCollectionUtil.PRICE_PRODUCT)
                .put("pipeline", pipelineArrayJson);

        dynamicRunCommand(result, command);		
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void findProductTotal(JsonObject query, Consumer<List<JsonObject>> result) {
		
		
	}

	/**
	 * 刷选条件（产品名、地区、米径/胸径、时间、高度、冠幅）
	 * @param query
	 * @return
	 */
	static JsonObject pushJson(JsonObject query){
		JsonObject matchJson = new JsonObject().put("breedName", query.getString("breedName")).put("area", query.getString("area"));
        if(query.getDouble("midiaMeterMax") != null || query.getDouble("midiaMeterMin") != null){
        	JsonObject midiaMeter = new JsonObject();
        	if(query.getDouble("midiaMeterMax") != null)midiaMeter.put("$lte", query.getDouble("midiaMeterMax"));
        	if(query.getDouble("midiaMeterMin") != null)midiaMeter.put("$gt", query.getDouble("midiaMeterMin"));
        	matchJson.put("midiaMeter", midiaMeter);
        }
		if(query.getDouble("heightMax") != null || query.getDouble("heightMin") != null){
			JsonObject height = new JsonObject();
        	if(query.getDouble("heightMax") != null)height.put("$lte", query.getDouble("heightMax"));
        	if(query.getDouble("heightMin") != null)height.put("$gt", query.getDouble("heightMin"));
        	matchJson.put("height", height);
        }
		if(query.getDouble("crownMax") != null || query.getDouble("crownMin") != null){
			JsonObject crown = new JsonObject();
        	if(query.getDouble("crownMax") != null)crown.put("$lte", query.getDouble("crownMax"));
        	if(query.getDouble("crownMin") != null)crown.put("$gt", query.getDouble("crownMin"));
        	matchJson.put("crown", crown);
		}
		return matchJson;
	}
	
	/**
	 * 排序条件（名称，胸径/米径，高度，冠幅，上车价格，到货价，地区，数据来源，更新时间）
	 * 默认按完成时间降序
	 * @param query
	 * @return
	 */
	static JsonObject sortJson(JsonObject query){
		JsonObject sort = new JsonObject();
		//排序字段
		String sortField = Optional.ofNullable(query.getString("sortField")).map(Function.identity()).orElse("updateTime");
		//降序（-1）or升序（1）
		Integer order = Optional.ofNullable(query.getInteger("sort")).map(Function.identity()).orElse(1);
		//排序
		 sort.put(sortField, order);
		return sort; 
	}
	
	/**
     * 运行aggregate命令
     * @param result
     * @param command
     */
    private void dynamicRunCommand(Consumer<List<JsonObject>> result, JsonObject command) {
        logger.debug("aggregate 查询条件：{}", command);
        mongoSupport.getMongoClient().runCommand("aggregate", command, res -> {
            logger.debug("数据库运行结果 {} --- result: {}", res.succeeded(), res.result());
            if (res.succeeded()) {
                JsonArray resArr = res.result().getJsonArray("result");
                //查询结果
                List<JsonObject> jsons = resArr.getList();
                logger.debug("aggregate result : {}", resArr);
                result.accept(jsons);
            } else {
                logger.error("[dynamicRunCommand] 运行出错：{}", res.cause().getMessage());
                result.accept(null);
            }
        });
    }
}
