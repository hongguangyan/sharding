package com.simple.sharding.utils;

import com.simple.sharding.config.ErrorCode;
import com.simple.sharding.exception.ShardingException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * MyBatis方式的 工具类
 *
 * @author: wuyu
 * @since: 12-8-29 下午1:21
 * @version: 1.0.0
 */
public class MyBatisUtils {

    private static Logger logger = Logger.getLogger(MyBatisUtils.class);

    /**
     * 获取SqlSession对象
     * @param dataSource 数据源
     * @param sqlSessionFactory SqlSessionFactory
     * @return SqlSession对象
     * @throws ShardingException Sharding异常类
     * @throws SQLException SQL异常类
     */
    public static SqlSession getSqlSession(DataSource dataSource,SqlSessionFactory sqlSessionFactory) throws ShardingException, SQLException {
        if(dataSource == null){
            throw new ShardingException(ErrorCode.DATASOURCE_IS_NOT_EXIST);
        }

        if(sqlSessionFactory == null){
            logger.error("获取MyBatis的SqlSessionFactory对象为空！");
            throw new ShardingException(ErrorCode.MYBATIS_IS_ERROR);
        }

      
        SqlSession sqlSession = sqlSessionFactory.openSession( JdbcUtils.getConnection(dataSource));

        if(sqlSession == null){
             logger.error("获取MyBatis的SqlSession对象为空！");
             throw new ShardingException(ErrorCode.MYBATIS_IS_ERROR);
        }

        return sqlSession;
    }

    /**
     * 获取SqlSession对象
     * @param dataSource 数据源
     * @param sqlSessionFactory SqlSessionFactory
     * @return SqlSession对象
     * @throws ShardingException Sharding异常类
     * @throws SQLException SQL异常类
     */
    public static SqlSession getSqlSession(DataSource dataSource,SqlSessionFactoryBean sqlSessionFactory) throws
            ShardingException, SQLException {
        if(dataSource == null){
            throw new ShardingException(ErrorCode.DATASOURCE_IS_NOT_EXIST);
        }

        if(sqlSessionFactory == null){
            logger.error("获取MyBatis的SqlSessionFactory对象为空！");
            throw new ShardingException(ErrorCode.MYBATIS_IS_ERROR);
        }
        sqlSessionFactory.setDataSource(dataSource);

        SqlSession sqlSession = null;
        try {

            sqlSession = sqlSessionFactory.getObject().openSession();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if(sqlSession == null){
             logger.error("获取MyBatis的SqlSession对象为空！");
             throw new ShardingException(ErrorCode.MYBATIS_IS_ERROR);
        }

        return sqlSession;
    }
}
