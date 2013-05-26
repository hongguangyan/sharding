package com.simple.sharding.rule.impl;

import com.simple.sharding.config.ErrorCode;
import com.simple.sharding.config.ShardingConfiguration;
import com.simple.sharding.config.ShardingConfiguration;
import com.simple.sharding.exception.ShardingException;
import com.simple.sharding.utils.HashAlgorithm;
import org.apache.log4j.Logger;

/**
 * Sharing策略的接口，该类是根据卡号生成.
 *
 * @author: wuyu
 * @since: 12-8-15 下午4:55
 * @version: 1.0.0
 */
public class ShardingRuleByCN {
    private static Logger logger = Logger.getLogger(ShardingRuleByCN.class);

    /**
     * 根据卡号获取虚拟库地址
     *
     * @param cardNumber 卡号
     * @return
     */
    public static int getTargetDBInfo(String cardNumber) throws ShardingException {
        if (cardNumber == null || "".equals(cardNumber)) {
            logger.error("卡号为空");
            throw new ShardingException(ErrorCode.CARD_NUMBER_IS_NULL);
        }
        logger.info("HashAlgorithm.hash(id.getBytes())=" + HashAlgorithm.hash(cardNumber));
        long shardingId =  HashAlgorithm.hash(cardNumber) % ShardingConfiguration.getVIRTUAL_HOST_LENGTH();
        logger.info("getTargetDBInfo.shardingId=" + shardingId);
        return (int)shardingId;
    }

    public static void main(String[] args) throws ShardingException {
        String str = "1016222022012091113382610004000845";
        //System.out.println(HashAlgorithm.hash(str));
        //System.out.println(ShardingConfiguration.getVIRTUAL_HOST_LENGTH());
        //System.out.println(HashAlgorithm.hash(str)%ShardingConfiguration.getVIRTUAL_HOST_LENGTH());
        //System.out.println(getTargetDBInfo(str));
    }

}
