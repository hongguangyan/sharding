package com.simple.payment.sharding.service;

import com.simple.sharding.config.ShardingMapping;
import com.simple.sharding.dataSource.DynamicDataSource;
import com.simple.payment.sharding.entity.BankOrderEntity;
import com.simple.sharding.exception.ShardingException;
import com.simple.sharding.service.impl.ShardingTemplate;
import com.simple.sharding.service.impl.ShardingTransactionTemplate;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * Created by IntelliJ IDEA.
 *
 * @author:
 * @since: 12-8-29 下午2:51
 * @version: 1.0.0
 */

public class ShardingTamplateTest {

    private Logger logger = Logger.getLogger(ShardingTamplateTest.class);


    private String fieldValue = "ID, ORIGINAL_ID, EXT_REQ_SN, TRADE_DN, EXT_TRADE_DN, " +
            " TRADE_TYPE, API_VERSION, MERCHANT_NO, TERMINAL_NO, SETTLE_MERCHANT_NO, NOTIFY_URL, BUSINESS_TYPE," +
            "TRADE_AMOUNT, CURRENCY_TYPE, BANK_CARD_NO, CARD_EXPIRE_DATE, CVV, ID_CARD_TYPE, ID_CARD_NO," +
            "CARD_HOLDER_NAME, KEY_VERSION, AUTHORIZE_NO, SPLIT_TYPE, SPLIT_INFO, TRADE_SOURCE, EXT_TRADE_REQ_TIME," +
            " CREATE_TRADE_REQ_TIME, BACK_UP";

    ApplicationContext applicationContext;
    ShardingTemplate shardingTemplate;
    DynamicDataSource dynamicDataSource;
    ShardingTransactionTemplate transactionTemplate;

    @Before
    public void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("classpath*:spring*//spring-sharding-shard.xml");
        shardingTemplate = (ShardingTemplate) applicationContext.getBean("shardingTemplate");
        transactionTemplate = (ShardingTransactionTemplate) applicationContext.getBean("shardingTransactionTemplate");
//        System.out.println("shardingTemplate------------" + dynamicDataSource.getResolvedDataSources().size());
    }

    @Test
    public void testAddByMyBatis() {
        /*  long beginTime = System.currentTimeMillis();

try {
  BankOrderEntity entity = new BankOrderEntity();
  int val =0;
  for (int i = 1000; i < 1123; i++) {

      java.util.Random r = new java.util.Random(i);
      entity.setId(r.nextLong());


      shardingTemplate.insertOrUpdateForMyBatis("BankOrderEntity.insert", "" + r.nextInt(), entity);
       val++;
      System.out.println("执行了多少次：" + val);
  }
} catch (ShardingException e) {
  e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
} catch (SQLException e) {
  e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
}

long endTime = System.currentTimeMillis();
System.out.println("使用时间：" + (endTime - beginTime) + "MS");  //大概42分钟  十万的数*/
//System.out.println("transactionTemplate="+transactionTemplate.getTransactionManager());

       /* final MultiTransactionManager transactionManager = (MultiTransactionManager) applicationContext.getBean
                ("multiTransactionManager");
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        System.out.println("transactionDefinition.getIsolationLevel()=" + transactionDefinition.getIsolationLevel());
               System.out.println("transactionDefinition.getPropagationBehavior()=" + transactionDefinition.getPropagationBehavior());
                System.out.println("transactionDefinition.getTimeout()=" + transactionDefinition.getTimeout());

        final TransactionStatus status = transactionManager.getTransaction(transactionDefinition);*/
        
       /* System.out.println("transactionManager=" + transactionManager);
        final MultiTransactionStatus status = (MultiTransactionStatus) applicationContext.getBean
                ("multiTransactionStatus");
        System.out.println("status=" + status);*/
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                BankOrderEntity entity = new BankOrderEntity();
                entity.setId(System.currentTimeMillis());
                //更新银行订单表
                try {

                   shardingTemplate.insertOrUpdateForMyBatis("BankOrderEntity.insert", "" + 9999999 , entity);

                    shardingTemplate.insertOrUpdateForMyBatis("BankOrderEntity.insert", "" , entity);
                    //更新支付表
                   // System.out.println("dsadasdadasdssssssss============================================");
                    //  ContextHolder.setContext(String.valueOf(2));
                    shardingTemplate.insertOrUpdateForMyBatis("BankOrderEntity.insert", "" + 0000000 , entity);
                   // transactionManager.commit(status);
                    System.out.println("_________________________________________________________");
                } catch (ShardingException e) {
                   // throw new RuntimeException(e);
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    
                }catch (Exception e) {
                     //transactionManager.rollback(status);
                   //throw new RuntimeException(e);  //To change body of catch statement use File | Settings | File
                   // Templates.
                }finally {
                     //transactionManager.commit(status);
                }

            }
        });


    }

    public void testAddBySQL() {
        //java.util.Random r = new java.util.Random(12 * 138);

       /* long beginTime = System.currentTimeMillis();
        for (int i = 1000; i < 1010; i++) {  //总共会执行4000次  用时：369710MS (第二次357731MS)  大概是：6.1619分钟  大概每条写的时间：92.4275MS
            // mypayment库的总数 =1286 mypayment_shard1库的总数=1417  mypayment_shard2库的总数=1297
            System.out.println("------------String.valueOf(i)=" + String.valueOf(i).getBytes().length);
            java.util.Random r = new java.util.Random(i);
            System.out.println("------------r.nextInt()=" + r.nextInt());
            System.out.println("------------r.nextInt()=" + r.nextLong());
            try {

                shardingTemplate.insertOrUpdateForSQL("insert into TBL_TRADE_REQUEST (ID, ORIGINAL_ID, EXT_REQ_SN, TRADE_DN, EXT_TRADE_DN, " +
                        " TRADE_TYPE, API_VERSION, MERCHANT_NO, TERMINAL_NO, SETTLE_MERCHANT_NO, NOTIFY_URL, BUSINESS_TYPE, " +
                        "TRADE_AMOUNT, CURRENCY_TYPE, BANK_CARD_NO, CARD_EXPIRE_DATE, CVV, ID_CARD_TYPE, ID_CARD_NO," +
                        "CARD_HOLDER_NAME, KEY_VERSION, AUTHORIZE_NO, SPLIT_TYPE, SPLIT_INFO, TRADE_SOURCE, EXT_TRADE_REQ_TIME," +
                        "CREATE_TRADE_REQ_TIME, BACK_UP) values (" + r.nextLong() + ", 1345727225493, '123123213123', " +
                        "'12312312312', '1321313231', 1, '1.0v', '10000101','10101010', '123123123', " +
                        "'localhost', 1, 2.0000, 'CNY', '1233333333333333', '1234', '123', 1,  " +
                        "'123123123123', 'yangkai', '12', '123123', 1, '123123', 1, sysdate, " +
                        "sysdate, '123')",
                        "" + r.nextInt());
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ShardingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("使用时间：" + (endTime - beginTime) + "MS");  //大概42分钟  十万的数据*/
    }
/* 
    public void testQueryByMyBatis() {
        long beginTime1 = System.currentTimeMillis();
        List<Map> list1 = new ArrayList<Map>();
        try {
            *//*RequestEntity entity = new RequestEntity();*//*
            list1 = (List<Map>) shardingTemplate.queryListForMyBatis("RequestDao.select", null);
            //使用时间：14586MS
            // 4000
            // 每条查询的时间：3.6465
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ShardingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用时间：" + (endTime1 - beginTime1) + "MS");  //大概42分钟  十万的数据
        System.out.println("list1.size()：" + list1.size());  //大概42分钟  十万的数据
    }

    public void testQueryBySQL() {
        long beginTime1 = System.currentTimeMillis();
        List<Map> list1 = new ArrayList<Map>();
        try {
            list1 = (List<Map>) shardingTemplate.queryListForSQL("select " + fieldValue + " from TBL_TRADE_REQUEST");
            //使用时间：14586MS
            // 4000
            // 每条查询的时间：3.6465
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ShardingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用时间：" + (endTime1 - beginTime1) + "MS");  //大概42分钟  十万的数据
        System.out.println("list1.size()：" + list1.size());  //大概42分钟  十万的数据
        *//*   for (int j = 0; j < list1.size(); j++) {
            Map<String, Object> rowData = (Map<String, Object>) list1.get(j);
            for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue() + "\t");
            }
        }*//*

    }*/

    /* @Test
    public void MultiRequestsTest() {

        // 构造一个Runner
        TestRunnable runner = new TestRunnable() {
            @Override
            public void runTest() throws Throwable {
                try {
                   // RequestEntity entity = new RequestEntity();
                    int j = 0;
                    for (int i = 1000; i < 1010; i++) {

                        java.util.Random r = new java.util.Random(i);
                       // entity.setId(r.nextLong());
                        j++;
                        System.out.println("执行了多少次：" + j);

                        shardingTemplate.insertOrUpdateForMyBatis("RequestDao.insert", "" + r.nextInt(), null);
                    }
                } catch (ShardingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };

        int runnerCount = 100;
        //Rnner数组，想当于并发多少个。
        TestRunnable[] trs = new TestRunnable[runnerCount];
        for (int i = 0; i < runnerCount; i++) {
            trs[i] = runner;
        }
        // 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
        try {
              long beginTime = System.currentTimeMillis();
            // 开发并发执行数组里定义的内容
            mttr.runTestRunnables();
            long endTime1 = System.currentTimeMillis();
        System.out.println("使用时间：" + (endTime1 - beginTime) + "MS");  //大概42分钟  十万的数据
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/
private static  int VIRTUAL_HOST_LENGTH = 100;
    @Test
    public void testDataSource() {
        ShardingMapping shardingMapping = new ShardingMapping();
       
    }
}
