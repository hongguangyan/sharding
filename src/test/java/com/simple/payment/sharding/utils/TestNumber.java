package com.simple.payment.sharding.utils;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author:
 * @since: 12-9-17 下午6:33
 * @version: 1.0.0
 */
public class TestNumber {
    /*static ApplicationContext applicationContext;
    static DataSource dynamicDataSource;

    static {
        applicationContext = new ClassPathXmlApplicationContext("classpath*:spring*//*/spring-test-datasoruce.xml");
        dynamicDataSource = (DataSource) applicationContext.getBean("dataSource");
    }

    public static void main(String[] args) throws SQLException {
        for (int i = 0; i < 15; i++) {
            Runnable runn = new Runnable() {
                @Override
                public void run() {
                    //To change body of implemented methods use File | Settings | File Templates.
                    try {
                        doSomething();
                    } catch (SQLException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            };

            Thread thread = new Thread(runn);
            thread.start();
        }
    }

    private static void doSomething() throws SQLException {
        Connection connection = null;
        ResultSet rs = null;
        try {
            for (int i = 0; i < 1000; i++) {
                connection = JdbcUtils.getConnection(dynamicDataSource);
                int value = JdbcUtils.executeInsertOrUpdate(connection, "update tbl_test_number set status=1 where status=0");
                
                if (value != 1) {
                    JdbcUtils.closeConnection(connection);
                    continue;
                }

                 rs = JdbcUtils.executeQuery(connection, "select num ,id from tbl_test_number where status=1");

                while (rs.next()) {
                    int number = rs.getInt("num");
                    int id = rs.getInt("id");
                    System.out.println("id="+id+",num="+number);
                    int val = number + 1;

                    JdbcUtils.executeInsertOrUpdate(connection, "update tbl_test_number set status=0 ," +
                            "num=" + val + " where id=" + id);
                }


            }
        } catch(Exception e){
            System.out.println("-----------------------"+e.getMessage());
        }finally {
            System.out.println("-----------------------");
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeConnection(connection);
        }
    }
*/
    @Test
    public void test(){
        System.out.println("-----------------------------");
    }
}
