package com.simple.sharding.rule;

import com.simple.sharding.exception.ShardingException;

/**
 * Sharing策略的接口
 * 
 * @author: wuyu
 * @since: 12-8-15 下午4:37
 * @version: 1.0.0
 */
public interface ShardingRule {

    /**
	 * 获取实际的数据源
	 * @param sharingId Sharing的id
	 * @return Sharding物理主机id实体类
	 */
	int getTargetDBInfo(String sharingId) throws ShardingException;

}
