/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simple.sharding.utils;

import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Jdbc方式的工具类
 * @author: wuyu
 * @since: 12-8-7 上午10:54
 * @version: 1.0.0
 */
public class JdbcUtils {

    private static final Logger logger = Logger.getLogger(JdbcUtils.class);

    /**
     * 创建一个数据库连接
     *
     * @return 一个数据库连接
     */
    public static Connection getConnection(DataSource dataSource) throws SQLException {
        Connection conn = null;
        //创建数据库连接
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(":创建数据库连接发生异常", e);
            throw new SQLException();
        }
        return conn;
    }

    /**
     * 创建Statement
     *
     * @throws SQLException
     */
    public static Statement getStatement(DataSource dataSource) throws SQLException {
        Statement statement = null;
        //创建数据库连接
        try {
            statement = getConnection(dataSource).createStatement();
        } catch (SQLException e) {
            logger.error(":创建Statement发生异常", e);
            throw new SQLException();
        }
        return statement;
    }

    /**
     * 在一个数据库连接上执行一个查询的SQL语句
     *
     * @param conn 数据库连接
     * @param sql  静态SQL语句字符串
     * @return 返回查询结果集ResultSet对象
     */
    public static ResultSet executeQuery(Connection conn, String sql) throws SQLException {
        logger.info("JdbcUtils.executeQuery().conn"+conn);
        logger.info("JdbcUtils.executeQuery().sql"+sql);
        ResultSet rs = null;
        try {
            //创建执行SQL的对象
            Statement stmt = conn.createStatement();
            //执行SQL，并获取返回结果
            rs = stmt.executeQuery(sql);
            logger.info("JdbcUtils.executeQuery().rs"+rs);
        } catch (SQLException e) {
            logger.error("执行SQL语句出错\n" + sql, e);
            throw new SQLException("执行SQL语句出错\n" + sql, e);
        }
        return rs;
    }

    /**
     * 在一个数据库连接上执行一个静态SQL语句
     *
     * @param conn 数据库连接
     * @param sql  静态SQL语句字符串
     */
    public static int executeInsertOrUpdate(Connection conn, String sql) throws SQLException {
        int returnValue = 0;
        try {
            //创建执行SQL的对象
            Statement stmt = conn.createStatement();
            //执行SQL，并获取返回结果
            returnValue = stmt.executeUpdate(sql);
            //conn.commit();
        } catch (SQLException e) {
            logger.error("执行SQL语句出错\n" + sql, e);
            //conn.rollback();
            throw new SQLException(e.getMessage());
        }
        return returnValue;
    }



    /**
     * 关闭Connection连接
     *
     * @param con Connection对象
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                logger.debug("关闭Connection连接出错", ex);
            }

        }
    }

    /**
     * 关闭Statement对象
     * @param stmt Statement对象
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.debug("关闭Statement出错", ex);
            }

        }
    }

    /**
     * 关闭ResultSet对象
     * @param rs ResultSet对象
     */
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.debug("关闭结果集出错", ex);
            }
        }
    }


}
