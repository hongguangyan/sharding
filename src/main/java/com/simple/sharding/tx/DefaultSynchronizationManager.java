package com.simple.sharding.tx;

import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author: wuyu
 * @since: 12-9-7 下午1:23
 * @version: 1.0.0
 */
public class DefaultSynchronizationManager implements SynchronizationManager {
    @Override
    public void initSynchronization() {
        TransactionSynchronizationManager.initSynchronization();
    }

    @Override
    public boolean isSynchronizationActive() {
        return TransactionSynchronizationManager.isSynchronizationActive();
    }

    @Override
    public void clearSynchronization() {
        TransactionSynchronizationManager.clear();
    }
}
