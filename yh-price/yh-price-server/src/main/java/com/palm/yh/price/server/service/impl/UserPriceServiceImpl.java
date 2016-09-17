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
        Integer skip = query.getInteger("skip")-1>0?(query.getInteger("skip")-1)*query.getInteger("limit"):0;
        JsonArray pipelineArrayJson = new JsonArray().add(matchJson).add(sortJson)
        			.add(new JsonObject().put("$skip",skip))
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
		String breedName = query.getString("breedName");
        if (null == breedName || breedName.isEmpty()) {
            result.accept(null);
            return;
        }
        //去掉页码和条数
        query.remove("page");
        query.remove("pageNumber"); 
        //过滤主体
        JsonObject matchJson = new JsonObject().put("$match", pushJson(query));
             
        //分组
        JsonObject groupJson = new JsonObject().put("$group", groupJson(query));
   
        //project
        JsonObject projectJson = new JsonObject().put("$project", projectJson(query));
        
        //组装查询条件
        JsonArray pipelineArrayJson = new JsonArray().add(matchJson).add(groupJson).add(projectJson);
        
        //组装aggregate命令
        JsonObject command = new JsonObject()
                .put("aggregate", YhCollectionUtil.PRICE_PRODUCT)
                .put("pipeline", pipelineArrayJson);

        dynamicRunCommand(result, command);		
	}

	/**
	 * 刷选条件（产品名、地区、米径/胸径、时间、高度、冠幅、数据源、发票类型）
	 * @param query
	 * @return
	 */
	static JsonObject pushJson(JsonObject query){
		JsonObject matchJson = new JsonObject().put("breedName", query.getString("breedName")).put("area", query.getString("area"))
				.put("sourceNo", query.getString("sourceNo"));
        if(query.getDouble("midiaMeterMax") != null || query.getDouble("midiaMeterMin") != null){
        	JsonObject midiaMeter = new JsonObject();
        	if(query.getDouble("midiaMeterMax") != null)midiaMeter.put("$lte", query.getDouble("midiaMeterMax"));
        	if(query.getDouble("midiaMeterMin") != null)midiaMeter.put("$gte", query.getDouble("midiaMeterMin"));
        	matchJson.put("midiaMeter", midiaMeter);
        }
		if(query.getDouble("heightMax") != null || query.getDouble("heightMin") != null){
			JsonObject height = new JsonObject();
        	if(query.getDouble("heightMax") != null)height.put("$lte", query.getDouble("heightMax"));
        	if(query.getDouble("heightMin") != null)height.put("$gte", query.getDouble("heightMin"));
        	matchJson.put("height", height);
        }
		if(query.getDouble("crownMax") != null || query.getDouble("crownMin") != null){
			JsonObject crown = new JsonObject();
        	if(query.getDouble("crownMax") != null)crown.put("$lte", query.getDouble("crownMax"));
        	if(query.getDouble("crownMin") != null)crown.put("$gte", query.getDouble("crownMin"));
        	matchJson.put("crown", crown);
		}
		if(query.getLong("updateTimeMax") != null || query.getLong("updateTimeMin") != null){
			JsonObject updateTime = new JsonObject();
        	if(query.getLong("updateTimeMax") != null)updateTime.put("$lte", query.getLong("updateTimeMax"));
        	if(query.getLong("updateTimeMin") != null)updateTime.put("$gte", query.getLong("updateTimeMin"));
        	matchJson.put("updateTime", updateTime);
		}
		if(query.getString("invoiceTypeNo") != null){
			matchJson.put("invoiceTypeNo", query.getString("invoiceTypeNo"));
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
	 * 统计（总报价数，胸径范围 max-min，上车价范围 max-min，上车平均价，到货价范围 max-min，到货平均价）
	 * @param query
	 * @return
	 */
	static JsonObject groupJson(JsonObject query){
		JsonObject groupJson = new JsonObject().put("_id", "_id");
		//胸径范围 max-min
		groupJson.put("miDiameterMax", new JsonObject().put("$max", "$miDiameter"));
		groupJson.put("miDiameterMin", new JsonObject().put("$min", "$miDiameter"));
		//上车范围 max-min
		groupJson.put("FareMax", new JsonObject().put("$max", "$startingFare"));
		groupJson.put("FareMin", new JsonObject().put("$min", "$startingFare"));
		//到货价范围 max-min
		groupJson.put("TotalPriceMax", new JsonObject().put("$max", "$totalPrice"));
		groupJson.put("TotalPriceMin", new JsonObject().put("$min", "$totalPrice"));
		//上车平均价、到货平均价、总报价数
		groupJson.put("FareAvg", new JsonObject().put("$avg", "$startingFare"));
		groupJson.put("PriceAvg", new JsonObject().put("$avg", "$totalPrice"));
		groupJson.put("countTotal", new JsonObject().put("$sum", 1));
		return groupJson;
	}
	
	/**
	 * project（总报价数，胸径范围 max-min，上车价范围 max-min，上车平均价，到货价范围 max-min，到货平均价）
	 * @param query
	 * @return
	 */
	static JsonObject projectJson(JsonObject query){
		JsonObject proJson = new JsonObject().put("_id", 0).put("miDiameterMax", 1)
		.put("miDiameterMin", 1).put("FareMax", 1).put("FareMin", 1).put("TotalPriceMax", 1)
		.put("TotalPriceMin",1).put("FareAvg", 1).put("PriceAvg", 1).put("countTotal", 1);
		return proJson;
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
