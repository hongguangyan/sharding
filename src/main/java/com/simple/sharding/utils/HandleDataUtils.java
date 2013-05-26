package com.simple.sharding.utils;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将ResultSet转换为OBject的工具类
 *
 * @author: wuyu
 * @since: 12-8-27 下午6:58
 * @version: 1.0.0
 */
public class HandleDataUtils {

    private static Logger logger = Logger.getLogger(HandleDataUtils.class);

    private static String removeCharacter(String str, String character) {
        if (str == null || "".equals(str) || str.indexOf(character) == -1) {
            return str;
        }

        StringBuffer returnStr = new StringBuffer();

        String[] strs = str.split(character);
        for (int i = 0; i < strs.length; i++) {
            String strTmp = (String) strs[i];
            char beanPropertyChars[] = strTmp.toCharArray();

            for (int j = 0; j < beanPropertyChars.length; j++) {
                if (j == 0) {
                    beanPropertyChars[0] = Character.toUpperCase(beanPropertyChars[0]);
                } else {
                    beanPropertyChars[j] = Character.toLowerCase(beanPropertyChars[j]);
                }
            }

            returnStr.append(beanPropertyChars);
        }

        return returnStr.toString();
    }

    /**
     * 匹配指定class中数据,并返回包含get和set方法的object
     *
     * @param clazz
     * @param beanProperty
     * @return
     * @author chenpeng
     */
    private static Object[] beanMatch(Class clazz, String beanProperty) {
        Object[] result = new Object[2];
        String s = removeCharacter(beanProperty, "_");
        /*char beanPropertyChars[] = changeStr.toCharArray();


        beanPropertyChars[0] = Character.toUpperCase(beanPropertyChars[0]);
        String s = new String(beanPropertyChars);*/

        String names[] = {("set" + s).intern(), ("get" + s).intern(), ("is" + s).intern(), ("write" + s).intern(), ("read" + s).intern()};
        Method getter = null;
        Method setter = null;
        Method methods[] = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            // 只取公共字段
            if (!Modifier.isPublic(method.getModifiers()))
                continue;
            String methodName = method.getName().intern();
            for (int j = 0; j < names.length; j++) {
                String name = names[j];
                if (!name.equals(methodName))
                    continue;
                if (methodName.startsWith("set")
                        || methodName.startsWith("read"))
                    setter = method;
                else
                    getter = method;
            }
        }
        logger.debug("beanProperty= " + beanProperty + ";object=" + clazz.getClass().getName() +
                "getter=" + getter + ";setter=" + setter);
        result[0] = getter;
        result[1] = setter;
        return result;
    }

    /**
     * 为bean自动注入数据
     *
     * @param object
     * @param beanProperty
     * @author chenpeng
     */
    private static void beanRegister(Object object, String beanProperty, String value) {
        Object[] beanObject = beanMatch(object.getClass(), beanProperty);
        Object[] cache = new Object[1];
        Method getter = (Method) beanObject[0];

        Method setter = (Method) beanObject[1];

        try {
            // 通过get获得方法类型
            String methodType = getter.getReturnType().getName();
            logger.info("methodType===============" + methodType);
            if (methodType.equalsIgnoreCase("long") || methodType.equalsIgnoreCase("java.lang.Long")) {
                logger.debug("methodTyp1e = Long");
                cache[0] = new Long(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("int")
                    || methodType.equalsIgnoreCase("integer") || methodType.equalsIgnoreCase("java.lang.Integer")) {
                logger.debug("methodTyp1e = Integer");
                cache[0] = new Integer(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("short") || methodType.equalsIgnoreCase("java.lang.Short")) {
                logger.debug("methodTyp1e = short");
                cache[0] = new Short(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("float") || methodType.equalsIgnoreCase("java.lang.Float")) {
                logger.debug("methodTyp1e = float");
                cache[0] = new Float(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("double") || methodType.equalsIgnoreCase("java.lang.Double")) {
                logger.debug("methodTyp1e = double");
                cache[0] = new Double(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("boolean") || methodType.equalsIgnoreCase("java.lang.Boolean")) {
                logger.debug("methodTyp1e = boolean");
                cache[0] = new Boolean(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("java.lang.String")) {
                logger.debug("methodTyp1e = String");
                cache[0] = value;
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("java.lang.StringBuffer")) {
                logger.debug("methodTyp1e = StringBuffer");
                cache[0] = new StringBuffer(value);
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("java.io.InputStream")) {
            } else if (methodType.equalsIgnoreCase("char")) {
                logger.debug("methodTyp1e = char");
                cache[0] = (Character.valueOf(value.charAt(0)));
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("java.sql.Date")) {
                logger.debug("methodTyp1e = java.sql.Date");
                cache[0] = new Date(value.charAt(0));
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("java.util.Date")) {
                logger.debug("methodTyp1e = java.util.Date");
                cache[0] = new java.util.Date(value.charAt(0));
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("java.math.BigDecimal")) {  //BigDecimal
                logger.debug("methodTyp1e = BigDecimal");
                cache[0] = new java.math.BigDecimal(value.charAt(0));
                setter.invoke(object, cache);
            } else if (methodType.equalsIgnoreCase("com.simple.payment.utils.common.enums.CurrencyTypeEnum")) {
                logger.debug("methodTyp1e = CurrencyTypeEnum=" + String.valueOf(value.charAt(0)));//com.simple
                // .payment.utils.common.enums
                // .CurrencyTypeEnum
                cache[0] = String.valueOf(value.charAt(0));
                //setter.invoke(object, cache);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将result对象转换为clazz类型对象
     * <pre class="code">
     * <p/>
     * &nbsp;&nbsp;&nbsp;&nbsp;使用该方法需要注意以下几点：
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、数据库字段必须和实体的字段名称必须一样，并且，数据库字段之间是用"_"区分，而实体类字段必须是标准的java命名规则，驼峰方式
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;举例：实体字段：extRequestSn  数据库字段：EXT_REQ_SN   注意：Sn的写法，不是SN
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、如果出现类似：CVV这样的简称，数据库字段名称和实体类字段必须一样，要么全大写，要么全小写     *
     * <p/>
     * </p>
     *
     * @param result
     * @param clazz
     * @return
     * @throws SQLException
     */
    public static List<Object> handleDatazForOBject(ResultSet result, Class clazz) throws SQLException {
        // 创建collection
        List<Object> list = null;

        ResultSetMetaData rsmd = result.getMetaData();
        // 获得数据列数
        int cols = rsmd.getColumnCount();
        logger.debug("rsmd.getColumnCount()=" + rsmd.getColumnCount());
        // 创建等同数据列数的arraylist类型collection实例
        list = new ArrayList<Object>(cols);
        // 遍历结果集

        while (result.next()) {
            // 创建对象
            Object object = null;
            try {
                // 从class获得对象实体
                object = clazz.newInstance();
            } catch (Exception e) {
            }
            // 循环每条记录
           
            for (int i = 1; i <= cols; i++) {

                beanRegister(object, rsmd.getColumnName(i), result.getString(i));

            }
            // 将数据插入collection
            list.add(object);
        }

        return list;
    }

    /**
     * 处理数据，将ResultSet对象转换为MAP
     *
     * @param rs ResultSet对象
     * @return Map
     */
    public static Map<String, ArrayList<Object>> handleDataForMap(ResultSet rs) throws SQLException {

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        Map<String, ArrayList<Object>> map = new HashMap<String, ArrayList<Object>>();
        for (int i = 0; i < columnCount; ++i) {
            map.put(rsmd.getColumnName(i + 1), new ArrayList<Object>());
        }
        while (rs.next()) {
            for (int i = 0; i < rsmd.getColumnCount(); ++i) {
                String columnName = rsmd.getColumnName(i + 1);
                map.get(columnName).add(rs.getObject(columnName));
            }
        }
        return map;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * 处理数据 ，将ResultSet对象转换为List
     *
     * @param rs ResultSet对象
     * @return List
     */
    public static List<Map> handleDataForList(ResultSet rs) throws SQLException {

        List<Map> list = new ArrayList<Map>();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
       
        logger.info("rs.getMetaData()=" + rs.getMetaData());
        while (rs.next()) {
            Map<String, Object> rowData = new HashMap<String, Object>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }


}
