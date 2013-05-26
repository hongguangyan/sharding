package com.simple.sharding.dataSource;

import com.simple.sharding.dataSource.Context.ContextHolder;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;

import java.util.Map;

/**
 * 动态切换SqlSessionFactory的SqlSessionDaoSupport
 *
 * @see org.mybatis.spring.support.SqlSessionDaoSupport
 */
public class DynamicSqlSessionDaoSupport extends DaoSupport {

    private Map<Object, SqlSessionFactory> targetSqlSessionFactorys;

    private SqlSessionFactory defaultTargetSqlSessionFactory;

    private SqlSession sqlSession;

    private boolean externalSqlSession;

    @Autowired(required = false)
    public final void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (!this.externalSqlSession) {
            this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
        }
    }

    @Autowired(required = false)
    public final void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSession = sqlSessionTemplate;
        this.externalSqlSession = true;
    }

    /**
     * 动态获取getSqlSession
     *
     * @return Spring managed thread safe SqlSession
     */
    public final SqlSession getSqlSession() {
        logger.info("ContextHolder.getContext()=" + ContextHolder.getContext());
        SqlSessionFactory targetSqlSessionFactory = targetSqlSessionFactorys.get(ContextHolder.getContext());

        if (targetSqlSessionFactory != null) {
            this.externalSqlSession = false;
            setSqlSessionFactory(targetSqlSessionFactory);
        } else if (defaultTargetSqlSessionFactory != null) {
            this.externalSqlSession = false;
            setSqlSessionFactory(defaultTargetSqlSessionFactory);
        }
        return this.sqlSession;
    }

    /**
     * {@inheritDoc}
     */
    protected void checkDaoConfig() {
       /* Assert.notNull(this.sqlSession,
                "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");*/
    }

    public Map<Object, SqlSessionFactory> getTargetSqlSessionFactorys() {
        return targetSqlSessionFactorys;
    }

    /**
     * Specify the map of target SqlSessionFactory, with the lookup key as key.
     *
     * @param targetSqlSessionFactorys
     */
    public void setTargetSqlSessionFactorys(Map<Object, SqlSessionFactory> targetSqlSessionFactorys) {
        this.targetSqlSessionFactorys = targetSqlSessionFactorys;
    }

    public SqlSessionFactory getDefaultTargetSqlSessionFactory() {
        return defaultTargetSqlSessionFactory;
    }

    /**
     * Specify the default target SqlSessionFactory, if any.
     *
     * @param defaultTargetSqlSessionFactory
     */
    public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {
        this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;
    }

}