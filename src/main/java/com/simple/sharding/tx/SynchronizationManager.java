package com.simple.sharding.tx;

/**
 * @author: wuyu
 * @since: 12-9-7 下午1:23
 * @version: 1.0.0
 */
public interface SynchronizationManager {
    void initSynchronization();

    boolean isSynchronizationActive();

    void clearSynchronization();
}
