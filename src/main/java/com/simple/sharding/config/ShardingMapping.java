package com.simple.sharding.config;

import com.simple.sharding.exception.ShardingException;
import com.simple.sharding.rule.impl.ShardingRuleByCN;
import com.simple.sharding.utils.ShardingUtils;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.*;

/**
 * sharding物理主机和虚拟主机对应关系,
 * 具体：
 * <ui>每个物理机对应多少个虚拟机</ui>
 * <ui>将虚拟机动态分配给每个物理机</ui>
 *
 * @author: wuyu
 * @since: 12-8-7 上午11:38
 * @version: 1.0.0
 */
public class ShardingMapping {

    private static Logger logger = Logger.getLogger(ShardingMapping.class);
    private static List<DataSource> dataSourceList = new ArrayList<DataSource>();


    /**
     * 获取物理数据源的个数
     *
     * @return 物理机个数据
     */
    public static int getDataSourceLength() {
        if (logger.isInfoEnabled())
            logger.info("ShardingMapping.getDataSourceLength().dataSources.size =" + dataSourceList.size());
        return dataSourceList.size() == 0 ? 0 : dataSourceList.size();
    }

    /**
     * 每个物理对应的虚拟机个数
     *
     * @return 虚拟机个数
     */
    public static int assignVirtualHost() {
        if (logger.isInfoEnabled())
            logger.info("ShardingMapping.assignVirtualHost().return =" + getDataSourceLength());

        int returnResult = 0;
        if (getDataSourceLength() != 0) {
            returnResult = ShardingConfiguration.getVIRTUAL_HOST_LENGTH() / getDataSourceLength();
        }

        return returnResult;
    }

    /**
     * 为每个物理主机分配虚拟机范围
     *
     * @return map对象 key为物理主机的号，val是范围，值的格式：0-9
     */
    public static Map<Integer, String> hostScopeMap() {
        if (logger.isInfoEnabled()) {
            logger.info("ShardingMapping.hostScopeMap().begin");
        }
        Map<Integer, String> map = new HashMap<Integer, String>();

        int dataSourceLength = dataSourceList.size();
        for (int i = 0; i < dataSourceLength; i++) {
            if (i + 1 == dataSourceLength) {
                map.put(i, "" + i * assignVirtualHost() + "-" + (assignVirtualHost() * dataSourceList.size() - 1));
            } else {
                map.put(i, "" + i * assignVirtualHost() + "-" + ((i + 1) * assignVirtualHost() - 1));
            }
        }

        return map;
    }

    /**
     * 根据虚拟主机找到对应的物理主机
     *
     * @param virtualID 物理机地址
     * @return
     */
    public static int getPhysicsHostNumberByVirtualID(int virtualID) throws ShardingException {
        if (logger.isInfoEnabled()) {
            logger.info("ShardingMapping.getPhysicsHostNumberByVirtualID().begin");
        }

        Map<Integer, String> map = hostScopeMap();

        Iterator it = map.entrySet().iterator();
        Integer physicsHostNumber = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();            //Integer mapKey = (Integer)entry.getKey();
            String mapValue = (String) entry.getValue();

            String beginStr = "";
            String endStr = "";
            if (mapValue != null && !"".equals(mapValue)) {
                beginStr = mapValue.substring(0, mapValue.lastIndexOf("-"));
                endStr = mapValue.substring(mapValue.indexOf("-") + 1, mapValue.length());

                if (Integer.valueOf(beginStr) <= virtualID && virtualID <= Integer.valueOf(endStr)) {
                    physicsHostNumber = (Integer) entry.getKey();

                }
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("ShardingMapping.getPhysicsHostNumberByVirtualID().physicsHostNumber=" + physicsHostNumber);
        }

        return physicsHostNumber;
    }

     /**
     * 根据虚拟地址获取物理主机的地址
     *
     * @param targetDataSources 数据源列表 map 方式
     * @param shardingId        sharding id
     * @return
     * @throws ShardingException
     */
    public static int getPhysicsHostNumberByVirtualID(Map<Object, DataSource> targetDataSources,
                                                      String shardingId) throws ShardingException {
        if (targetDataSources == null || targetDataSources.size() <= 0) {
            throw new ShardingException(ErrorCode.DATASOURCE_IS_NOT_EXIST);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }

        return getPhysicsHostNumberByVirtualID(ShardingUtils.getDataSourceListForMap(targetDataSources),shardingId);
    }

    /**
     * 根据虚拟地址获取物理主机的地址
     *
     * @param targetDataSources 数据源列表 list 方式
     * @param shardingId        sharding id
     * @return
     * @throws ShardingException
     */
    public static int getPhysicsHostNumberByVirtualID(List<DataSource> targetDataSources,
                                                      String shardingId) throws ShardingException {
        if (targetDataSources == null || targetDataSources.size() <= 0) {
            throw new ShardingException(ErrorCode.DATASOURCE_IS_NOT_EXIST);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }

        //获取虚拟地址
        int shardingVirtualId = ShardingRuleByCN.getTargetDBInfo(shardingId);

        //设置数据源
        setDataSourceList(targetDataSources);
        //获取物理主机号
        int physiceHostNumber = getPhysicsHostNumberByVirtualID(shardingVirtualId);
        logger.info("-----------physiceHostNumber----------------------+" + physiceHostNumber);
        return physiceHostNumber;
    }

    /**
     * 获取数据源的集合
     *
     * @return 数据源的集合
     */
    public static List<DataSource> getDataSourceList() {
        return dataSourceList;
    }

    /**
     * 设置数据源集合
     *
     * @param dataSourceList
     */
    public static void setDataSourceList(List<DataSource> dataSourceList) {
        ShardingMapping.dataSourceList = dataSourceList;
    }
}
