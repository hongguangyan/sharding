package com.simple.payment.sharding.rule;

import com.simple.sharding.utils.HashAlgorithm;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 *
 * @author:
 * @since: 12-8-29 上午11:20
 * @version: 1.0.0
 */
public class ShardingRuleByCNTest {
    private static Logger logger = Logger.getLogger(ShardingRuleByCNTest.class);

    @Test
    public void testGetTargetDBInfo(){
        int length = HashAlgorithm.computeMd5("abcd").length;
        String str = "";
        for (int i = 0; i < length; i++) {
            str += i;
        }
        long l = HashAlgorithm.hash("abcd12121212".getBytes(), 0);
        logger.info((int) l % 100);
    }
}
