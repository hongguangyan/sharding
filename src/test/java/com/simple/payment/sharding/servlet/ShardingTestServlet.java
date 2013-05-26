package com.simple.payment.sharding.servlet;


import com.simple.sharding.exception.ShardingException;
import com.simple.sharding.service.impl.ShardingTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @author:
 * @since: 12-8-23 下午3:23
 * @version: 1.0.0
 */
public class ShardingTestServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=gb2312");

        String numL = (String) req.getParameter("numL");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");


        ShardingTemplate shardingTemplate = (ShardingTemplate) applicationContext.getBean("ShardingTemplate");

        List<DataSource> list = shardingTemplate.getTargetDataSources();

        for (DataSource dataSource : list) {
            System.out.println(dataSource);
        }
        java.util.Random r = new java.util.Random(Integer.parseInt(numL));
        long beginTime = System.currentTimeMillis();
        for (int i = 1; i < 10; i++) {

            System.out.println("begin time=" + beginTime);
            System.out.println("i=" + i);
            try {
                System.out.println("-------------------------------------------------hahah:"+r.nextInt());
                shardingTemplate.insertOrUpdateForSQL("insert into tb2 (id, val) values (" + i + ", " +
                        "'shardingValue_" + i + "')",
                        "" + r.nextInt());
            } catch (ShardingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("使用时间：" + (endTime - beginTime) + "MS");  //大概42分钟  十万的数据
        long beginTime1 = System.currentTimeMillis();
        List<Map> list1 = new ArrayList<Map>();
        try {
            list1 = (List<Map>) shardingTemplate.queryListForSQL("select * from tb2 where id=7");
        } catch (ShardingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用时间：" + (endTime1 - beginTime1) + "MS");  //大概42分钟  十万的数据
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head>");
        out.println("<title>HelloWorld</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");
        out.println("<hr>");
        out.println("使用时间：" + (endTime - beginTime) + "MS");
        out.println("<br>");
        out.println("使用时间：" + (endTime1 - beginTime1) + "MS");
        out.println("<br>");
        out.println("list1.size()=" + list1.size());

            out.println("<br>");
            for (int j = 0; j < list1.size(); j++) {
                Map<String, Object> rowData = (Map<String, Object>) list1.get(j);
                for (Map.Entry<String, Object> entry : rowData.entrySet()) {
                    out.println(entry.getKey() + ":" + entry.getValue() + "\t");
                }
            }
            out.println("<hr>");


        try {
            List<Object> lists = (List<Object>)shardingTemplate.queryListForSQL("select * from tb2 where id=1",
                    "-1148943845",
                    Object.class);
             out.println(lists.size()+ "\t");
            for (int j = 0; j < lists.size(); j++) {
                /*User user = (User) lists.get(j);
                out.println(user.getId() + ":" + user.getVal() + "\t");*/
            }
        } catch (ShardingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        out.println("</body></html>");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("ShardingTestServlet.init");
    }
}
