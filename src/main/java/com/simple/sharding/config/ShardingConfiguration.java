package com.simple.sharding.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * 读取配置信息.
 *
 * @author: wuyu
 * @since: 12-9-10 下午1:51
 * @version: 1.0.0
 */
public class ShardingConfiguration {
      private static Logger logger = Logger.getLogger(ShardingMapping.class);

    public static int getVIRTUAL_HOST_LENGTH() {
        return VIRTUAL_HOST_LENGTH;
    }

   

    private static int VIRTUAL_HOST_LENGTH = 100;

    static {
        try {
            XMLConfiguration config =new XMLConfiguration("sharding-configuration.xml");
            VIRTUAL_HOST_LENGTH = config.getInt("systemconfig.virtualaddress");
            logger.info("VIRTUAL_HOST_LENGTH = " + VIRTUAL_HOST_LENGTH);
        } catch (ConfigurationException e) {
            logger.error(e.getMessage());
        }

    }
}
