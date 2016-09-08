package com.palm.yh.common.util;

import org.springframework.stereotype.Component;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * 一个jsonObject对象的分析的辅助函数,解析时发现数据类型出错不抛出异常，而是改为返回null
 * 
 * @author hxl
 */
@Component
public class JsonObjectParseUtil {
	
	private String integerRegex="[+-]?[0-9]+";

	/**
	 * 判断jsonObject是否有某个属性
	 * 
	 * @param jsonObject
	 * @param propertyName
	 * @return
	 * boolean
	 */
	public boolean hasProperty(JsonObject jsonObject,String propertyName){
		boolean res=false;
		if(jsonObject!=null && jsonObject.containsKey(propertyName)){
			res=true;
		}
		return res;
	}
	
	/**
	 * 判断某个属性值类型是否是JsonObject
	 * 
	 * @param jsonObject
	 * @param propertyName
	 * @return
	 * boolean
	 */
	public boolean isPropertyJsonObject(JsonObject jsonObject,String propertyName){
		boolean res=false;
		if(hasProperty(jsonObject, propertyName)){
			Object value=jsonObject.getValue(propertyName);
			if(value!=null && value instanceof JsonObject){
				res=true;
			}
			
		}
		return res;
	}
	
	/**
	 * 判断某个属性值是否是JsonArray
	 * 
	 * @param jsonObject
	 * @param propertyName
	 * @return
	 * boolean
	 */
	public boolean isPropertyJsonArray(JsonObject jsonObject,String propertyName){
		boolean res=false;
		if(hasProperty(jsonObject, propertyName)){
			Object value=jsonObject.getValue(propertyName);
			if(value!=null && value instanceof JsonArray){
				res=true;
			}
			
		}
		return res;
	}
	
	/**
	 * 当一个JsonObject中属性名为propertyName的值为JsonObject类型时，返回该值，否则返回null
	 * 
	 * @param jsonObject
	 * @param propertyName
	 * @return
	 * JsonObject
	 */
	public JsonObject getPropertyAsJsonObject(JsonObject jsonObject,String propertyName){
		JsonObject object=null;
		if(isPropertyJsonObject(jsonObject, propertyName)){
			object=jsonObject.getJsonObject(propertyName);
		}
		return object;
	}
	
	/**
	 * 当jsonObject的属性propertyName的值类型为JsonArray时，返回该值，否则返回null
	 * 
	 * @param jsonObject
	 * @param propertyName
	 * @return
	 * JsonArray
	 */
	public JsonArray getPropertyAsJsonArray(JsonObject jsonObject,String propertyName){
		JsonArray object=null;
		if(isPropertyJsonArray(jsonObject, propertyName)){
			object=jsonObject.getJsonArray(propertyName);
		}
		return object;
	}
	
	/**
	 * 当jsonObject的propertyName属性可以转换为整型时，返回该整数，否则返回null
	 * 
	 * @param jsonObject
	 * @param propertyName
	 * @return
	 * Integer
	 */
	public Integer getPropertyAsInteger(JsonObject jsonObject,String propertyName){
		Integer result=null;
		if(hasProperty(jsonObject, propertyName)){
			Object value=jsonObject.getValue(propertyName);
			if(value!=null){
				String vString=value.toString();
				//TODO 整数太大时这里有问题
				if(vString.matches(integerRegex)){
					result=Integer.valueOf(vString);
				}
			}
			
		}
		return result;
	}
}
