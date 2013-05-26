package com.simple.sharding.utils;

import com.simple.sharding.config.ErrorCode;
import com.simple.sharding.config.ShardingMapping;
import com.simple.sharding.exception.ShardingException;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.*;

/**
 * Sharding 工具类
 *
 * @author: wuyu
 * @since: 12-8-29 上午11:06
 * @version: 1.0.0
 */
public class ShardingUtils {
    private static Logger logger = Logger.getLogger(ShardingUtils.class);


    /**
     * 根据数据源的物理地址从数据源集合中获取数据源
     *
     * @param targetDataSources 数据源集合
     * @param dataSourceNumber  数据源物理地址
     * @return 数据源
     * @throws ShardingException
     */
    public static DataSource createDataSource(List<DataSource> targetDataSources, DataSource defaultTargetDataSource,
                                              int dataSourceNumber) throws ShardingException {
        logger.info("DataSourceFactory parameters of dataSourceNumber=" + dataSourceNumber);
        logger.info("DataSourceFactory parameters of targetDataSources.length=" + targetDataSources.size());

        if (dataSourceNumber > targetDataSources.size()) {
            throw new ShardingException(ErrorCode.PHYSICE_HOST_NOT_EXIST);
        }

        DataSource dataSource = null;
        if (targetDataSources.size() > 0) {
            dataSource = targetDataSources.get(dataSourceNumber);
        }

        if (dataSource == null && defaultTargetDataSource == null) {
            logger.info("DataSourceFactory . dataSource is null");
            throw new ShardingException(ErrorCode.DATASOURCE_IS_NOT_EXIST);
        }

        if (dataSource == null) {
            if (defaultTargetDataSource != null) {
                dataSource = defaultTargetDataSource;
            }
        }


        logger.info("DataSourceFactory createDataSource().datasource=" + dataSource);
        return dataSource;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * 根据数Sharding的值从数据源集合中获取数据源
     *
     * @param targetDataSources 数据源集合
     * @param shardingId        Sharding的Id
     * @return 数据源
     * @throws ShardingException
     */
    public static DataSource createDataSource(List<DataSource> targetDataSources, DataSource defaultTargetDataSource, String shardingId) throws ShardingException {

        logger.info("DataSourceFactory .targetDataSources.length=" + targetDataSources.size());
        //获取物理主机号
        int dataSourceNumber = ShardingMapping.getPhysicsHostNumberByVirtualID(targetDataSources, shardingId);

        DataSource dataSource = createDataSource(targetDataSources, defaultTargetDataSource, dataSourceNumber);
        logger.info("DataSourceFactory createDataSource().datasource=" + dataSource);
        return dataSource;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 根据数Sharding的值从数据源集合中获取数据源
     *
     * @param targetDataSourcesMap 数据源集合
     * @param shardingId           Sharding的Id
     * @return 数据源
     * @throws ShardingException
     */
    public static DataSource createDataSourceByMap(Map<Object, DataSource> targetDataSourcesMap,
                                                   DataSource defaultTargetDataSource,
                                                   String shardingId) throws ShardingException {

        logger.info("DataSourceFactory .targetDataSources.length=" + targetDataSourcesMap.size());

        List<DataSource> targetDataSources = ShardingUtils.getDataSourceListForMap(targetDataSourcesMap);
        DataSource dataSource = null;
        Set set = targetDataSourcesMap.entrySet();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            //获取物理主机号
            int dataSourceNumber = ShardingMapping.getPhysicsHostNumberByVirtualID(targetDataSources, shardingId);
            if (key.equals(String.valueOf(dataSourceNumber))) {
                dataSource = targetDataSourcesMap.get(key);
                break;
            }
        }

        if (dataSource == null && defaultTargetDataSource == null) {
            logger.info("DataSourceFactory . dataSource is null");
            throw new ShardingException(ErrorCode.DATASOURCE_IS_NOT_EXIST);
        }

        if (dataSource == null) {
            if (defaultTargetDataSource != null) {
                dataSource = defaultTargetDataSource;
            }

        }

        logger.info("DataSourceFactory createDataSource().datasource=" + dataSource);
        return dataSource;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @param targetDataSources
     * @return
     * @throws ShardingException
     */
    public static List<DataSource> getDataSourceListForMap(Map<Object, DataSource> targetDataSources) throws
            ShardingException {
        if (targetDataSources == null) {
            throw new ShardingException("targetDataSources is null");
        }

        List<DataSource> list = new ArrayList<DataSource>(targetDataSources.size());

        for (Iterator it = targetDataSources.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            DataSource dataSource = (DataSource) entry.getValue();
            list.add(dataSource);
        }

        return list;
    }

}
