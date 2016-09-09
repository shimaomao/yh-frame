package com.palm.yh.common.util;

/**
 * 记录eventbus的consumer的地址
 * eventBus 消息队列地址 publish/send/consume
 * 格式：类型[P2P|BROCAST].项目名称.工程名称.模块名称.方法名称.。
 *
 * @author fengzt
 */
public class YhConsumerAddressUtil {


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
}
