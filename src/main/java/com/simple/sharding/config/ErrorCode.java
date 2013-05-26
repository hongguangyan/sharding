package com.simple.sharding.config;

/**
 * Sharding错误编码
 * @author: wuyu
 * @since: 12-8-7 上午10:46
 * @version: 1.0.0
 */
public class ErrorCode{
    /**
     * 成功
     */
    public static String SHARDING_SUCCESS_CODE = "Sharding-200";

    /**
     * Sharidng号为空
     */
    public static String SHARDING_ID_IS_NULL = "Sharding-103";
    /**
     * 物理数据库不存在
     */
    public static String PHYSICE_HOST_NOT_EXIST = "Sharding-101";
    /**
     * 虚拟id不存在
     */
    public static String VIRYUAL_ID_IS_NULL = "Sharding-102";

    /**
     * 卡号为空
     */
    public static String CARD_NUMBER_IS_NULL = "Sharding-105"; //

    /**
     * 数据源不存在
     */
    public static String DATASOURCE_IS_NOT_EXIST = "Sharding-401"; //

    /**
     * 数据库连接为空
     */
    public static String CONNECTION_IS_NULL = "Sharding-402";
    /**
     * SQL语句为空
     */
    public static String SQL_IS_NULL = "Sharding-403" ;
    /**
     * 没有发现CLASS对象
     */
    public static String CLASS_IS_NOT_FAUND = "Sharding-404" ;

    /**
     * MYBATIS 其他错误
     */
    public static final String MYBATIS_IS_ERROR = "Sharding-501" ;
    /**
     * MYBATIS 的id不存在
     */
    public static String MYBATIS_ID_IS_NULL = "Sharding-502"; //
    public static final String MYBATIS_EXECUTE_FAILED = "Sharding-503";
}
