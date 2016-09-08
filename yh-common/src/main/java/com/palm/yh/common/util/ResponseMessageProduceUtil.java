package com.palm.yh.common.util;

import org.springframework.stereotype.Component;

import io.vertx.core.json.JsonObject;

/**
 * 构造返回结果辅助类
 * 
 * @author hxl
 */
@Component
public class ResponseMessageProduceUtil {
	/**
	 * 构造成功的返回结果
	 * 
	 * @param data 返回的数据
	 * @return
	 * Object
	 */
	public Object getSuccessMessage(String data){
		return getSuccessMessage(null, data);
	}

	/**
	 * 构造成功的返回结果
	 * 
	 * @param message 提醒信息
	 * @param data 返回的数据
	 * @return
	 * Object
	 */
	public Object getSuccessMessage(String message,String data){
		return getMessage("0", message, data);
	}
	
	
	public Object getMessage(String code,String message,String data){
		JsonObject jsonObject=new JsonObject();
		jsonObject.put("code", code);
		jsonObject.put("msg", message);
		jsonObject.put("data", data);
		return jsonObject;
	}
	
	
	/**
	 * 构造失败的返回结果
	 * 
	 * @param message 错误的信息
	 * @return
	 * Object
	 */
	public Object getErrorMessage(String message){
		return getMessage("-1", message, null);
	}
	
	
	
}
