package com.palm.yh.common.util;

/**
 * 记录eventbus的consumer的地址
 * eventBus 消息队列地址 publish/send/consume
 * 格式：类型[P2P|BROCAST].项目名称.工程名称.模块名称.方法名称.。
 *
 * @author fengzt
 */
public class YhConsumerAddressUtil {
	
	public YhConsumerAddressUtil() {
		
	}
	/**
	 * 新增用户
     */
	public static final String USER_ADD = "P2P.com.palm.yh.demo.server.service.add";

	/**
	 * 查询一个用户
     */
	public static final String FIND_ONE = "P2P.com.palm.yh.demo.server.service.findOne";

	/**
	 * 更新用户信息
     */
	public static final String USER_UPDATE = "P2P.com.palm.yh.demo.server.service.update";
	
	/**
	 * 新增用户
     */
	public static final String PRICE_ADD = "P2P.com.palm.yh.price.server.service.add";

	/**
	 * 查询一个用户
     */
	public static final String PRICE_UPDATE = "P2P.com.palm.yh.price.server.service.findOne";

	/**
	 * 更新用户信息
     */
	public static final String PRICE_FIND_ONE = "P2P.com.palm.yh.price.server.service.update";
	
	/**
	 * 查询所有用户信息
     */
	public static final String PRICE_USER_LIST = "P2P.com.palm.yh.price.server.service.findList";
	
	/**
	 * 新增产品信息
     */
	public static final String PRICE_ADD_PRODUCT = "P2P.com.palm.yh.price.server.service.addProduct";
	
	/**
	 * 查询产品信息（分页）
     */
	public static final String PRICE_FIND_PRODUCT = "P2P.com.palm.yh.price.server.service.findProduct";
	/**
	 * 查询地区
     */
	public static final String PRICE_FIND_AREA = "P2P.com.palm.yh.price.server.service.area";
	/**
	 * 统计产品信息
     */
	public static final String PRICE_TOTAL_PRODUCT = "P2P.com.palm.yh.price.server.service.total";
	
	
}
