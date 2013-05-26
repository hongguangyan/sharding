package com.simple.sharding.service;

import com.simple.sharding.dataSource.Context.ContextHolder;
import com.simple.sharding.exception.ShardingException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Sharding统一访问接口
 *
 * @author: wuyu
 * @since: 12-8-23 下午3:12
 * @version: 1.0.0
 */
public interface ShardingFacade<T> {

    /**
     * 增加或更新数据，通过MyBatis方式，需要传入MyBatis对象作为参数
     *
     * @param myBatisId  mybatis 的Mappging文件的id
     * @param shardingId sharding的id
     * @param object     ibatis的参数
     * @return Object
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public int insertOrUpdateForMyBatis(String myBatisId, String shardingId,
                                        Object object) throws ShardingException;

    /**
     * 增加或者更新数据，MyBatis方式，不需要传入MyBatis对象作为参数
     *
     * @param myBatisId  mybatis 的Mappging文件的id
     * @param shardingId sharding的id
     * @return Object
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public int insertOrUpdateForMyBatis(String myBatisId, String shardingId) throws ShardingException;

    /**
     * 增加或者更新数据，JDBC方式
     *
     * @param sql        sql语句
     * @param shardingId sharing的id
     * @return Object 影响的记录行数
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public int insertOrUpdateForSQL(String sql, String shardingId) throws ShardingException;

    /**
     * 查询数据 myBatis方式，需要传入MyBatis对象作为参数
     *
     * @param myBatisId  myBatis的id
     * @param shardingId sharding的id
     * @param params     参数 可以使Map、Object 、String
     * @return Object Object
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public Object queryObjectForMyBatis(String myBatisId, String shardingId, Object params) throws ShardingException;

    /**
     * 查询数据 myBatis方式，不需要传入MyBatis对象作为参数
     *
     * @param myBatisId  myBatis的id
     * @param shardingId sharding的id
     * @param params params
     * @return Object
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public T queryListForMyBatis(String myBatisId, String shardingId, Object params) throws ShardingException;

      /**
     * 查询数据 myBatis方式，需要传入MyBatis对象作为参数 优化
     *
     * @param myBatisId myBatis的id
     * @param object
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public Object queryAllDataSourceObjectForMyBatis(String myBatisId,Object object) throws ShardingException;


    /**
     * 查询数据 myBatis方式，需要传入MyBatis对象作为参数
     *
     * @param myBatisId myBatis的id
     * @param object
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public  List<T> queryAllDataSourceListForMyBatis(String myBatisId, Object object) throws ShardingException;

    /**
     * 根据sql语句查询 JDBC方式
     *
     * @param sql        sql语句
     * @param shardingId sharding的id
     * @return List<Map>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public List<Map> queryObjectForSQL(String sql, String shardingId) throws ShardingException;

    /**
     * 根据sql语句查询 JDBC方式 （不建议使用）
     *
     * @param sql sql语句
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public List<Map> queryListForSQL(String sql) throws ShardingException;

    /**
     * 根据sql语句查询 JDBC方式 并且将结果集合中的对象转成对象
     *
     * @param sql        sql语句
     * @param shardingId sharding的id
     * @param clazz      clazz 对象
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public List<Object> queryListForSQL(String sql, String shardingId, Class clazz) throws ShardingException;


    /**
     * 根据sql语句查询 JDBC方式 并且将结果集合中的对象转成对象
     *
     * @param sql   sql语句
     * @param clazz 对象
     * @return List<Object> list
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    public List<Object> queryListForSQL(String sql, Class clazz) throws ShardingException;
}
