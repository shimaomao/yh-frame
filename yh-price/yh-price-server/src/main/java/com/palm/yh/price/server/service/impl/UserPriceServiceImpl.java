package com.palm.yh.price.server.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.palm.vertx.mongo.support.MongoSupport;
import com.palm.vertx.redis.support.RedisSupport;
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
    
    @Autowired
    private RedisSupport redisSupport;

    @Value("${mlh.ret.code.prefix}")
    private String retCodeRedisPrefix;

    //过期时间（单位：s）
    private final static long RET_CODE_EXPIRED = 7 * 24 * 60 * 60;

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
        //查询爬取数据信息
        logger.debug("[findList]查询条件：{}", query);
        mongoSupport.getMongoClient().find(YhCollectionUtil.T_CONTENT, query, resultHandler -> {
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
        logger.debug("[count]查询条件：{}", query);
        mongoSupport.getMongoClient().count(YhCollectionUtil.PRICE_PRODUCT, pushJson(query), resultHandler -> {
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
		JsonObject product  = crawLingJson(jsonObject);
		product.put("source", "1");
		product.put("op", OpType.ACT);
        logger.debug("[add] json内容：{}", product);
        mongoSupport.getMongoClient().save(YhCollectionUtil.PRICE_PRODUCT, product, result -> {
            logger.debug("[add]结果 {} result：{}", result.succeeded(), result.result());
            resultHandler.handle(result);
        });
	}
    
	/**
     * {@inheritDoc}
     */
	@Override
	public void findProduct(JsonObject query, Consumer<List<JsonObject>> result) {
		if (null == query || query.isEmpty()) {
            result.accept(null);
            return;
        }
        //过滤主体
        JsonObject matchJson = new JsonObject().put("$match", pushJson(query));
             
        //排序条件
        JsonObject sortJson = new JsonObject().put("$sort", sortJson(query));
        
        //project
        JsonObject projectJson = new JsonObject().put("$project", productJson());
   
        //组装查询条件
        Integer skip = query.getInteger("skip")-1>0?(query.getInteger("skip")-1)*query.getInteger("limit"):0;
        JsonArray pipelineArrayJson = new JsonArray().add(matchJson).add(sortJson).add(projectJson)
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
		if (null == query || query.isEmpty()) {
            result.accept(null);
            return;
        }
		//.put("miDiameterMax", new JsonObject().put("$gt", 0)).put("miDiameterMin", new JsonObject().put("$gt", 0))
        //过滤主体
        JsonObject matchJson = new JsonObject().put("$match", pushJson(query).put("startingFare", new JsonObject().put("$gt", 0)));
             
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
     * {@inheritDoc}
     */
	@Override
	public void supplierTotal(JsonObject query, Consumer<List<JsonObject>> result) {
		if (null == query || query.isEmpty()) {
            result.accept(null);
            return;
        }

        //过滤主体
        JsonObject matchJson = new JsonObject().put("$match", pushJson(query));
             
        //分组获得供应商
        JsonObject groupJson = new JsonObject().put("$group", new JsonObject().put("_id", "$supplier"));
        JsonObject groupJson1 = new JsonObject().put("$group", new JsonObject().put("_id", "").put("count", new JsonObject().put("$sum", 1)));
        
        //组装查询条件
        JsonArray pipelineArrayJson = new JsonArray().add(matchJson).add(groupJson).add(groupJson1);
        
        //组装aggregate命令
        JsonObject command = new JsonObject()
                .put("aggregate", YhCollectionUtil.PRICE_PRODUCT)
                .put("pipeline", pipelineArrayJson);

        dynamicRunCommand(result, command);		
		
	}

	@Override
	public void findArce(JsonObject query, Consumer<List<JsonObject>> result) {
		 String key = query.getString("key");
		 JsonObject matchJson = new JsonObject().put("$match", new JsonObject());
		 JsonObject projectJson = new JsonObject().put("$project", new JsonObject().put("_id", 0).put("areaNo", 1).put("areaName", 1));
		 JsonArray pipelineArrayJson = new JsonArray().add(matchJson).add(projectJson);
	   
		  JsonObject command = new JsonObject()
	                .put("aggregate", YhCollectionUtil.PRICE_ARCE)
	                .put("pipeline", pipelineArrayJson);

	        dynamicRunCommand(result, command);		
	        /*Future<String> redisFuture = Future.future();

	        //1.缓存查询
	        redisSupport.getRedisClient().get(retCodeRedisPrefix + key, redishandler -> {
	            logger.debug("[findOneRetCode]redis查询结果 {} result：{}", redishandler.succeeded(), redishandler.result());
	            redisFuture.complete(redishandler.result());
	        });

	        redisFuture.setHandler(handler -> {
	            logger.debug("redisFuture setHandler {}, result : {}", handler.succeeded(), handler.result());
	            //2.redis中已经有值
	            if (handler.succeeded() && StringUtils.isNotBlank(handler.result())) {
	            	
	            	List<JsonObject> jsons = handler.result().getList();
	            	resultHandler.accept(jsons);
	            } else {
	                Future<List<JsonObject>>  mongodbFuture = Future.future();
	                //3.查询数据库
	                mongoSupport.getMongoClient().find(YhCollectionUtil.RET_CODE_COLLECTION, query, findOneHandler -> {
	                    logger.debug("[findOne]数据库查询结果{}, result: {}", findOneHandler.succeeded(), findOneHandler.result());
	                    if (findOneHandler.succeeded()) {
	                        mongodbFuture.complete(findOneHandler.result());
	                    } else {
	                        resultHandler.accept(null);
	                    }
	                });

	                //4.放入redis
	                mongodbFuture.setHandler(ar -> {
	                    List<JsonObject> json = ar.result();
	                    logger.debug("[set]数据库查询结果{}, result: {}", ar.succeeded(), ar.result());
	                    if (null != json && !json.isEmpty()) {
	                        //5. 重新set到redis
	                        redisSupport.getRedisClient().setex(retCodeRedisPrefix + key, RET_CODE_EXPIRED, Json.encode(json), setexHandler -> {
	                            logger.debug("[setex]存入redis结果：{}", setexHandler.result());
	                            if (setexHandler.succeeded()) {
	                                resultHandler.accept(json);
	                            } else {
	                                resultHandler.accept(null);
	                            }
	                        });
	                    } else {
	                        resultHandler.accept(null);
	                    }
	                });
	            }

	        });*/
	}

	/**
	 * 过滤字段(_id、id、op、、createTime、breedName)
	 */
	static JsonObject productJson(){
		JsonObject proJson = new JsonObject().put("_id", 0).put("area",1)
			.put("miDiameterMax", 1).put("miDiameterMin", 1).put("areaNo",1)
			.put("totalPrice", 1).put("updateTime",1).put("source", 1)
			.put("details", 1).put("productName",1).put("supplier", 1).put("invoiceType", 1)
			.put("startingFare", 1).put("tel", 1).put("contacts", 1).put("heightMax", 1)
			.put("heightMin", 1).put("crownMax", 1).put("crownMin", 1);
		return proJson;
	}
	
	/**
	 * 刷选条件（产品名、地区、米径/胸径、时间、高度、冠幅、数据源、发票类型）
	 * @param query
	 * @return
	 */
	static JsonObject pushJson(JsonObject query){
		JsonObject matchJson = new JsonObject();
		if(query.getString("areaNo") != null && query.getString("areaNo") != ""){
			matchJson.put("areaNo", new JsonObject().put("$regex", "^"+query.getString("areaNo")+".*$"));	
		}			
        if(query.getString("breedName") != null && query.getString("breedName") != ""){
        	matchJson.put("breedName", new JsonObject().put("$regex", query.getString("breedName")));
        }
        if(query.getString("productName") != null && query.getString("productName") != ""){
        	matchJson.put("productName", new JsonObject().put("$regex", query.getString("productName")));
        }
		if((query.getDouble("miDiameterMax") != null && query.getDouble("miDiameterMax") != 0)){
			matchJson.put("miDiameterMax", new JsonObject().put("$lte", query.getDouble("miDiameterMax")));
		}
		if((query.getDouble("miDiameterMin") != null && query.getDouble("miDiameterMin") != 0)){		
			matchJson.put("miDiameterMin", new JsonObject().put("$gte", query.getDouble("miDiameterMin")));  	
		}
		if((query.getDouble("heightMax") != null && query.getDouble("heightMax") != 0)){
			matchJson.put("heightMax", new JsonObject().put("$lte", query.getDouble("heightMax")));
		}
		if(query.getDouble("heightMin") != null && query.getDouble("heightMin") != 0){
        	matchJson.put("heightMin", new JsonObject().put("$gte", query.getDouble("heightMin")));
		}
		if((query.getDouble("crownMax") != null && query.getDouble("crownMax") != 0)){
			matchJson.put("crownMax", new JsonObject().put("$lte", query.getDouble("crownMax")));
		}
		if((query.getDouble("crownMin") != null && query.getDouble("crownMin") != 0)){
			matchJson.put("crownMin", new JsonObject().put("$gte", query.getDouble("crownMin")));
		}
		if(query.getString("updateTimeMax") != null || query.getString("updateTimeMin") != null){
			JsonObject updateTime = new JsonObject();
        	if(query.getString("updateTimeMax") != null)updateTime.put("$lte", query.getString("updateTimeMax"));
        	if(query.getString("updateTimeMin") != null)updateTime.put("$gte", query.getString("updateTimeMin"));
        	matchJson.put("updateTime", updateTime);
		}
		if(query.getString("invoiceTypeNo") != null && query.getString("invoiceTypeNo") != ""){
			matchJson.put("invoiceTypeNo", query.getString("invoiceTypeNo"));
		}
		if(query.getString("sourceNo") != null && query.getString("sourceNo") !=""){
			matchJson.put("source", query.getString("sourceNo"));
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
		groupJson.put("miDiameterMax", new JsonObject().put("$max", "$miDiameterMax"));
		groupJson.put("miDiameterMin", new JsonObject().put("$min", "$miDiameterMin"));
		//上车范围 max-min
		groupJson.put("FareMax", new JsonObject().put("$max", "$startingFare"));
		groupJson.put("FareMin", new JsonObject().put("$min", "$startingFare"));
		//到货价范围 max-min
		groupJson.put("TotalPriceMax", new JsonObject().put("$max", "$totalPrice"));
		groupJson.put("TotalPriceMin", new JsonObject().put("$min", "$totalPrice"));
		//上车平均价、到货平均价、总报价数
		groupJson.put("FareAvg", new JsonObject().put("$avg", "$startingFare"));
		groupJson.put("PriceAvg", new JsonObject().put("$avg", "$totalPrice"));
	/*	groupJson.put("countTotal", new JsonObject().put("$sum", 1));*/
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
	 * 筛选爬取数据
	 * @param query
	 * @return
	 */
	static JsonObject crawLingJson(JsonObject query){
		JsonObject crawLing = new JsonObject();
		crawLing.put("productName", query.getString("title"));
		//截取时间
		query.getString("releasetime");
		//匹配数字
		crawLing.put("startingFare", query.getString("price"));
		//需要去模糊匹配
		crawLing.put("area", query.getString("price"));
		//供应商
		crawLing.put("supplier", query.getString("company"));
		//联系人
		crawLing.put("contacts", query.getString("contacts"));
		//电话
		crawLing.put("tel", query.getString("tel"));
		//胸径/米径(cm)需要截取最大最小值
		crawLing.put("midiaMeter", query.getString("midiameter"));
		//高度(cm)需要截取最大最小值
		crawLing.put("height", query.getString("height"));
		//冠幅(cm)
		crawLing.put("crown", query.getString("crown"));
		return crawLing;
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
