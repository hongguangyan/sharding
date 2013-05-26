package com.simple.sharding.service.impl;

import com.simple.sharding.config.ErrorCode;
import com.simple.sharding.config.ShardingMapping;
import com.simple.sharding.dataSource.Context.ContextHolder;
import com.simple.sharding.dataSource.DynamicDataSource;
import com.simple.sharding.dataSource.DynamicSqlSessionDaoSupport;
import com.simple.sharding.exception.ShardingException;
import com.simple.sharding.service.ShardingFacade;
import com.simple.sharding.utils.HandleDataUtils;
import com.simple.sharding.utils.JdbcUtils;
import com.simple.sharding.utils.ShardingUtils;
import org.apache.ibatis.session.SqlSession;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Sharding统一访问接口
 * <p/>
 * <p>使用该类的注入方式，如下</p>
 * <pre class="code">
 * <p/>
 * &nbsp;&nbsp;&lt;bean id="dataSource1" class="org.springframework.jdbc.datasource.DriverManagerDataSource"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="driverClassName" value="${db.driverClassName}"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="url" value="${db2.url}"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="username" value="${db2.username}"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="password" value="${db2.password}"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * <p/>
 * &nbsp;&nbsp;&lt;bean id="dataSource2" class="org.springframework.jdbc.datasource.DriverManagerDataSource"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="driverClassName" value="${db.driverClassName}"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="url" value="${db3.url}"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="username" value="${db3.username}"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="password" value="${db3.password}"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * <p/>
 * &nbsp;&nbsp;&lt;bean id="dynamicDataSource" class="com.simple.sharding.dataSource.DynamicDataSource"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="targetDataSources"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;map&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="0" value-ref="dataSource"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="1" value-ref="dataSource1"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="2" value-ref="dataSource2"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/map&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="defaultTargetDataSource" ref="dataSource"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * <p/>
 * <p/>
 * &nbsp;&nbsp;&lt;bean id="sqlSessionFactory1" class="org.mybatis.spring.SqlSessionFactoryBean"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dataSource" ref="dataSource1"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="configLocation" value="classpath:mybatis/mybatis-config-base.xml"/&gt;
 * &lt;/bean&gt;
 * &lt;bean id="sqlSessionFactory2" class="org.mybatis.spring.SqlSessionFactoryBean"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dataSource" ref="dataSource2"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="configLocation" value="classpath:mybatis/mybatis-config-base.xml"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * <p/>
 * &nbsp;&nbsp;&lt;!-- 动态切换SqlSessionFactory  --&gt;
 * &nbsp;&nbsp;&lt;bean id="dynamicSqlSessionDaoSupport" class="com.simple.sharding.dataSource.DynamicSqlSessionDaoSupport"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="targetSqlSessionFactorys"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;map value-type="org.apache.ibatis.session.SqlSessionFactory"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="0" value-ref="sqlSessionFactory"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="1" value-ref="sqlSessionFactory1"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;entry key="2" value-ref="sqlSessionFactory2"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/map&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/property&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="defaultTargetSqlSessionFactory" ref="sqlSessionFactory"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * &nbsp;&nbsp;&lt;!----&gt;
 * &nbsp;&nbsp;&lt;bean id="shardingTemplate" class="com.simple.sharding.service.impl.ShardingTemplate"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dynamicDataSource" ref="dynamicDataSource"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dynamicSqlSessionDaoSupport" ref="dynamicSqlSessionDaoSupport"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * </pre>
 *
 * @author: wuyu
 * @since: 12-8-15 下午4:43
 * @version: 1.0.0
 */
public class ShardingTemplate<T> implements ShardingFacade {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ShardingTemplate.class);

    private DataSource defaultTargetDataSource;
    private List<DataSource> targetDataSources = new ArrayList<DataSource>();

    private DynamicSqlSessionDaoSupport dynamicSqlSessionDaoSupport;

    private DynamicDataSource dynamicDataSource;


    /**
     * 增加或更新数据，通过MyBatis方式，需要传入MyBatis对象作为参数
     *
     * @param myBatisId  mybatis 的Mappging文件的id
     * @param shardingId sharding的id
     * @param object     ibatis的参数
     * @return Object
     * @throws ShardingException Sharding异常
     */
    @Override
    public int insertOrUpdateForMyBatis(String myBatisId, String shardingId,
                                        Object object) throws ShardingException {

        logger.info("ShardingTemplate.insertOrUpdateForMyBatis.myBatisId=" + myBatisId);
        logger.info("ShardingTemplate.insertOrUpdateForMyBatis.shardingId=" + shardingId);
        logger.warn("ShardingTemplate.object=" + object);
        if (myBatisId == null || "".equals(myBatisId)) {
            throw new ShardingException(ErrorCode.MYBATIS_ID_IS_NULL);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }

        int returnValue = 0;
        int physicsHostNumber = ShardingMapping.getPhysicsHostNumberByVirtualID(dynamicDataSource.getResolvedDataSources(), shardingId);
        ContextHolder.setContext(String.valueOf(physicsHostNumber));
        SqlSession sqlSession = this.getDynamicSqlSessionDaoSupport().getSqlSession();
        try {
            if (object == null) {
                returnValue = sqlSession.insert(myBatisId);
            } else {
                returnValue = sqlSession.insert(myBatisId, object);
            }
        } catch (Exception e) {
            logger.warn("--------------------shardingId=" + shardingId);
            throw new ShardingException(ErrorCode.MYBATIS_EXECUTE_FAILED, e.getMessage());
        } finally {

        }

        return returnValue;
    }

    /**
     * 增加或者更新数据，MyBatis方式，不需要传入MyBatis对象作为参数
     *
     * @param myBatisId  mybatis 的Mappging文件的id
     * @param shardingId sharding的id
     * @return Object
     * @throws ShardingException Sharding异常
     */
    @Override
    public int insertOrUpdateForMyBatis(String myBatisId, String shardingId) throws ShardingException {
        return this.insertOrUpdateForMyBatis(myBatisId, shardingId, null);

    }

    /**
     * 增加或者更新数据，JDBC方式
     *
     * @param sql        sql语句
     * @param shardingId sharing的id
     * @return Object 影响的记录行数
     * @throws ShardingException Sharding异常
     */
    @Override
    public int insertOrUpdateForSQL(String sql, String shardingId) throws ShardingException {
        logger.info("ShardingTemplate.insertOrUpdateForSQL.sql=[" + sql + "]");
        logger.info("ShardingTemplate.insertOrUpdateForSQL.shardingId=[" + shardingId + "]");
        if (sql == null || "".equals(sql)) {
            throw new ShardingException(ErrorCode.SQL_IS_NULL);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }
        //得到具体的数据源
        DataSource dataSource = ShardingUtils.createDataSourceByMap(this.dynamicDataSource.getResolvedDataSources(),
                this.dynamicDataSource.getResolvedDefaultDataSource(), shardingId);
        Connection connection = null;
        int value = 0;
        logger.info("dataSource.size()=" + dataSource);
        try {
            connection = JdbcUtils.getConnection(dataSource);
            value = JdbcUtils.executeInsertOrUpdate(connection, sql);
        } catch (SQLException e) {
            logger.error("增加或者更新数据，JDBC方式,执行出错，执行的sql语句=" + sql);
            throw new ShardingException("增加或者更新数据，JDBC方式,执行出错，执行的sql语句=" + e.getMessage());
        } finally {
            JdbcUtils.closeConnection(connection);
        }

        return value;
    }

    /**
     * 查询数据 myBatis方式，需要传入MyBatis对象作为参数  查询的一条记录
     *
     * @param myBatisId  myBatis的id
     * @param shardingId sharding的id
     * @param params     参数 可以使Map、Object 、String
     * @return Object Object
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    @Override
    public  Object queryObjectForMyBatis(String myBatisId, String shardingId, Object params) throws ShardingException {
        logger.info("ShardingTemplate.queryObjectForMyBatis.myBatisId=[" + myBatisId + "]");
        logger.info("ShardingTemplate.queryObjectForMyBatis.shardingId=[" + shardingId + "]");
        logger.info("ShardingTemplate.queryObjectForMyBatis.params=[" + params + "]");
        if (myBatisId == null || "".equals(myBatisId)) {
            throw new ShardingException(ErrorCode.MYBATIS_ID_IS_NULL);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }
        Object returnObject = null;
        SqlSession sqlSession = null;
        try {
            targetDataSources = ShardingUtils.getDataSourceListForMap(dynamicDataSource.getResolvedDataSources());
            int physicsHostNumber = ShardingMapping.getPhysicsHostNumberByVirtualID(targetDataSources, shardingId);
            ContextHolder.setContext(String.valueOf(physicsHostNumber));
            sqlSession = this.getDynamicSqlSessionDaoSupport().getSqlSession();
            returnObject = sqlSession.selectOne(myBatisId, params);

        } catch (Exception e) {
            throw new ShardingException(ErrorCode.MYBATIS_EXECUTE_FAILED, e.getMessage());
        } finally {
        }
        return returnObject;
    }

    /**
     * 查询数据 myBatis方式，不需要传入MyBatis对象作为参数  查询是多条记录
     *
     * @param myBatisId  myBatis的id
     * @param shardingId sharding的id
     * @return Object
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    @Override
    public  List<T> queryListForMyBatis(String myBatisId, String shardingId,
                                          Object params) throws ShardingException {
        logger.info("ShardingTemplate.queryListForMyBatis.myBatisId=[" + myBatisId + "]");
        logger.info("ShardingTemplate.queryListForMyBatis.shardingId=[" + shardingId + "]");
        logger.info("ShardingTemplate.queryListForMyBatis.params=[" + params + "]");
        if (myBatisId == null || "".equals(myBatisId)) {
            throw new ShardingException(ErrorCode.MYBATIS_ID_IS_NULL);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }
        List<T> list = new ArrayList<T>();
        SqlSession sqlSession = null;

        try {
            targetDataSources = ShardingUtils.getDataSourceListForMap(dynamicDataSource.getResolvedDataSources());
            int physicsHostNumber = ShardingMapping.getPhysicsHostNumberByVirtualID(targetDataSources, shardingId);
            ContextHolder.setContext(String.valueOf(physicsHostNumber));
            sqlSession = this.getDynamicSqlSessionDaoSupport().getSqlSession();
            list = sqlSession.selectList(myBatisId, params);
        } catch (Exception e) {
            throw new ShardingException(ErrorCode.MYBATIS_EXECUTE_FAILED, e.getMessage());
        } finally {
        }
        return list;
    }


    /**
     * 查询数据 myBatis方式，需要传入MyBatis对象作为参数  查询时多条记录
     *
     * @param myBatisId myBatis的id
     * @param object
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    @Override
    public  List<T> queryAllDataSourceListForMyBatis(String myBatisId, Object object) throws ShardingException {
        logger.info("ShardingTemplate.queryAllDataSourceListForMyBatis.myBatisId=[" + myBatisId + "]");
        logger.info("ShardingTemplate.queryAllDataSourceListForMyBatis.object=[" + object + "]");
        if (myBatisId == null || "".equals(myBatisId)) {
            throw new ShardingException(ErrorCode.MYBATIS_ID_IS_NULL);
        }
        List<T> lists = new ArrayList<T>();
        DataSource dataSource = null;
        SqlSession sqlSession = null;
        for (Iterator it = dynamicDataSource.getResolvedDataSources().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            dataSource = (DataSource) entry.getValue();
            if (dataSource != null) {
                try {
                    ContextHolder.setContext(String.valueOf(entry.getKey()));
                    sqlSession = this.getDynamicSqlSessionDaoSupport().getSqlSession();
                    List<T> list = sqlSession.selectList(myBatisId, object);
                    lists.addAll(list);
                } catch (Exception e) {
                    throw new ShardingException(ErrorCode.MYBATIS_EXECUTE_FAILED, e.getMessage());
                } finally {
                }
            } else {
                logger.info("queryListForSQL.dataSource.=" + dataSource);
                continue;
            }

        }

        return lists;
    }


     /**
     * 查询所有库数据 myBatis方式，需要传入MyBatis对象作为参数 (优化)
     *  如果第一个库查到，后面就不去查询
     * @param myBatisId myBatis的id
     * @param object
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    @Override
    public Object queryAllDataSourceObjectForMyBatis(String myBatisId,Object object) throws ShardingException {
        logger.info("ShardingTemplate.queryAllDataSourceObjectForMyBatis.myBatisId=[" + myBatisId + "]");
        logger.info("ShardingTemplate.queryAllDataSourceObjectForMyBatis.object=[" + object + "]");
        if (myBatisId == null || "".equals(myBatisId)) {
            throw new ShardingException(ErrorCode.MYBATIS_ID_IS_NULL);
        }
        Object returnObject = null;
        DataSource dataSource = null;
        SqlSession sqlSession = null;
        for (Iterator it = dynamicDataSource.getResolvedDataSources().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            dataSource = (DataSource) entry.getValue();
            if (dataSource != null) {
                try {
                    ContextHolder.setContext(String.valueOf(entry.getKey()));
                    sqlSession = this.getDynamicSqlSessionDaoSupport().getSqlSession();
                    returnObject = sqlSession.selectOne(myBatisId, object);
                    if(returnObject != null ){
                        return  returnObject;
                    }else{
                        continue;
                    }

                } catch (Exception e) {
                    throw new ShardingException(ErrorCode.MYBATIS_EXECUTE_FAILED, e.getMessage());
                } finally {
                }
            } else {
                logger.info("queryListForSQL.dataSource.=" + dataSource);
                continue;
            }

        }

        return  returnObject;
    }

    /**
     * 根据sql语句查询 JDBC方式
     *
     * @param sql        sql语句
     * @param shardingId sharding的id
     * @return List<Map>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    @Override
    public List<Map> queryObjectForSQL(String sql, String shardingId) throws ShardingException {
        logger.info("ShardingTemplate.queryObjectForSQL.sql=[" + sql + "]");
        logger.info("ShardingTemplate.queryObjectForSQL.shardingId=[" + shardingId + "]");
        if (sql == null || "".equals(sql)) {
            throw new ShardingException(ErrorCode.SQL_IS_NULL);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }

        //得到具体的数据源
        DataSource dataSource = ShardingUtils.createDataSourceByMap(this.dynamicDataSource.getResolvedDataSources(),
                this.dynamicDataSource.getResolvedDefaultDataSource(), shardingId);
        List<Map> list = new ArrayList<Map>();
        Connection connection = null;
        ResultSet rs = null;

        logger.info("dataSource.size()=" + dataSource);
        try {
            connection = JdbcUtils.getConnection(dataSource);
            rs = JdbcUtils.executeQuery(connection, sql);

            list = HandleDataUtils.handleDataForList(rs);
        } catch (SQLException e) {
            logger.error("根据sql语句查询 JDBC方式,执行出错，执行的sql语句=" + sql);
            throw new ShardingException("根据sql语句查询 JDBC方式,执行出错，执行的sql语句=" + e.getMessage());
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(connection);
        }

        return list;
    }

    /**
     * 根据sql语句查询 JDBC方式 （不建议使用）
     *
     * @param sql sql语句
     * @return List<Object>
     * @throws ShardingException Sharding异常
     * @throws SQLException      SQL异常
     */
    @Override
    public List<Map> queryListForSQL(String sql) throws ShardingException {
        logger.info("ShardingTemplate.insertOrUpdateForSQL.sql=[" + sql + "]");
        if (sql == null || "".equals(sql)) {
            throw new ShardingException(ErrorCode.SQL_IS_NULL);
        }

        List<Map> list = new ArrayList<Map>();
        logger.info("targetDataSources.size()=" + targetDataSources.size());
        DataSource dataSource = null;
        Connection connection = null;
        ResultSet rs = null;
        for (Iterator it = dynamicDataSource.getResolvedDataSources().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            dataSource = (DataSource) entry.getValue();
            logger.info("queryListForSQL.dataSource.=" + dataSource);
            if (dataSource != null) {
                try {
                    connection = JdbcUtils.getConnection(dataSource);
                    rs = JdbcUtils.executeQuery(connection, sql);
                    logger.info("rs.size()=" + rs.getFetchSize());
                    List<? extends Map> listValue = HandleDataUtils.handleDataForList(rs);
                    list.addAll(listValue);
                } catch (SQLException e) {
                    logger.error("根据sql语句查询 JDBC方式 （所有数据库都执行）,执行出错，执行的sql语句=" + sql);
                    throw new ShardingException("根据sql语句查询 JDBC方式 （所有数据库都执行）,执行出错，执行的sql语句=" + e.getMessage());
                } finally {
                    JdbcUtils.closeResultSet(rs);
                    JdbcUtils.closeConnection(connection);
                }
            } else {
                logger.info("queryListForSQL.dataSource.=" + ErrorCode.DATASOURCE_IS_NOT_EXIST);
                continue;
            }
        }

        return list;
    }

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
    @Override
    public List<Object> queryListForSQL(String sql, String shardingId, Class clazz) throws ShardingException {
        logger.info("ShardingTemplate.queryListForSQL.sql=[" + sql + "]");
        logger.info("ShardingTemplate.queryListForSQL.shardingId=[" + shardingId + "]");
        logger.info("ShardingTemplate.queryListForSQL.clazz=[" + clazz + "]");
        if (sql == null || "".equals(sql)) {
            throw new ShardingException(ErrorCode.SQL_IS_NULL);
        }

        if (shardingId == null || "".equals(shardingId)) {
            throw new ShardingException(ErrorCode.SHARDING_ID_IS_NULL);
        }

        if (clazz == null) {
            throw new ShardingException(ErrorCode.CLASS_IS_NOT_FAUND);
        }
        //得到具体的数据源
        DataSource dataSource = ShardingUtils.createDataSourceByMap(this.dynamicDataSource.getResolvedDataSources(),
                this.dynamicDataSource.getResolvedDefaultDataSource(), shardingId);
        List<Object> list = new ArrayList<Object>();
        Connection connection = null;
        ResultSet rs = null;

        logger.info("dataSource.size()=" + dataSource);
        try {
            connection = JdbcUtils.getConnection(dataSource);
            rs = JdbcUtils.executeQuery(connection, sql);
            list = HandleDataUtils.handleDatazForOBject(rs, clazz);
        } catch (SQLException e) {
            logger.error("根据sql语句查询 JDBC方式,执行出错，执行的sql语句=" + sql);
            throw new ShardingException("根据sql语句查询 JDBC方式,执行出错，执行的sql语句=" + e.getMessage());
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(connection);
        }

        return list;

    }


    /**
     * 根据sql语句查询 JDBC方式 并且将结果集合中的对象转成对象
     *
     * @param sql   sql语句
     * @param clazz 对象
     * @return List<Object> list
     * @throws ShardingException Sharding异常
     */
    @Override
    public List<Object> queryListForSQL(String sql, Class clazz) throws ShardingException {
        logger.info("ShardingTemplate.queryListForSQL.sql=[" + sql + "]");
        logger.info("ShardingTemplate.queryListForSQL.clazz=[" + clazz + "]");
        if (sql == null || "".equals(sql)) {
            throw new ShardingException(ErrorCode.SQL_IS_NULL);
        }
        if (clazz == null) {
            throw new ShardingException(ErrorCode.CLASS_IS_NOT_FAUND);
        }
        targetDataSources = ShardingUtils.getDataSourceListForMap(this.dynamicDataSource.getResolvedDataSources());
        //得到具体的数据源
        List<Object> list = new ArrayList<Object>();
        logger.info("targetDataSources.size()=" + targetDataSources.size());
        DataSource dataSource = null;
        Connection connection = null;
        ResultSet rs = null;
        for (int i = 0; i < targetDataSources.size(); i++) {
            dataSource = (DataSource) targetDataSources.get(i);
            logger.info("dataSource.size()=" + dataSource);
            try {
                connection = JdbcUtils.getConnection(dataSource);
                rs = JdbcUtils.executeQuery(connection, sql);

                List<? extends Object> objects = HandleDataUtils.handleDatazForOBject(rs, clazz);
                list.addAll(objects);

            } catch (SQLException e) {
                logger.error("根据sql语句查询 JDBC方式,执行出错，执行的sql语句=" + sql);
                throw new ShardingException("sharding执行sql异常：" + e.getMessage());
            } finally {
                JdbcUtils.closeResultSet(rs);
                JdbcUtils.closeConnection(connection);
            }
        }
        return list;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public DataSource getDefaultTargetDataSource() {
        return defaultTargetDataSource;
    }

    public void setDefaultTargetDataSource(DataSource defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    public List<DataSource> getTargetDataSources() {
        return targetDataSources;
    }

    public void setTargetDataSources(List<DataSource> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    public DynamicSqlSessionDaoSupport getDynamicSqlSessionDaoSupport() {
        return dynamicSqlSessionDaoSupport;
    }

    public void setDynamicSqlSessionDaoSupport(DynamicSqlSessionDaoSupport dynamicSqlSessionDaoSupport) {
        this.dynamicSqlSessionDaoSupport = dynamicSqlSessionDaoSupport;
    }

    public DynamicDataSource getDynamicDataSource() {
        return dynamicDataSource;
    }

    public void setDynamicDataSource(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }


}
