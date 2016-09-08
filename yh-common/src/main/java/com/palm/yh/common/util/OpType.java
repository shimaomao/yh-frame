package com.palm.yh.common.util;
/**
 * 用于每个表的OP类型表述
 * @author fengzt
 */
public enum OpType {
	/**
	 * 逻辑删除
	 */
	LDEL, 
	/**
	 * 物理删除
	 */
	PDEL, 
	/**
	 * 启用
	 */
	ACT, 
	/**
	 * 停用
	 */
	NAN
}
