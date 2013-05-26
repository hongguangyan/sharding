package com.simple.sharding.dataSource;

import com.simple.sharding.dataSource.Context.ContextHolder;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: wuyu
 * @since: 12-9-5 下午4:34
 * @version: 1.0.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    public Object determineCurrentLookupKey() {
        return ContextHolder.getContext();
    }

}
