package com.simple.sharding.tx;

import org.apache.log4j.Logger;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * MultiTransactionStatus接口定义
 *
 * @author wuyu
 * @since 14.02.11
 */
public class MultiTransactionStatus implements TransactionStatus {

    private Logger logger = Logger.getLogger(getClass());

    public PlatformTransactionManager getMainTransactionManager() {
        return mainTransactionManager;
    }

    public void setMainTransactionManager(PlatformTransactionManager mainTransactionManager) {
        this.mainTransactionManager = mainTransactionManager;
    }

    private PlatformTransactionManager mainTransactionManager;

    private Map<PlatformTransactionManager, TransactionStatus> transactionStatuses =
            Collections.synchronizedMap(new HashMap<PlatformTransactionManager, TransactionStatus>());

    private boolean newSynchonization;

    public MultiTransactionStatus(PlatformTransactionManager mainTransactionManager) {
        this.mainTransactionManager = mainTransactionManager;
    }


    private Map<PlatformTransactionManager, TransactionStatus> getTransactionStatuses() {
        logger.info("Map<PlatformTransactionManager, TransactionStatus>=" + transactionStatuses);
        return transactionStatuses;
    }

    private TransactionStatus getMainTransactionStatus() {
        return transactionStatuses.get(mainTransactionManager);
    }

    /**
     *
     */
    public void setNewSynchonization() {
        this.newSynchonization = true;
    }

    public boolean isNewSynchonization() {
        return newSynchonization;
    }

    /**
     * 返回当前事务状态是否是新事务
     *
     * @return
     */
    @Override
    public boolean isNewTransaction() {
        return getMainTransactionStatus().isNewTransaction();
    }

    /**
     * 返回当前事务是否有保存点
     *
     * @return
     */
    @Override
    public boolean hasSavepoint() {
        return getMainTransactionStatus().hasSavepoint();
    }

    /**
     * 设置当前事务应该回滚
     */
    @Override
    public void setRollbackOnly() {
        for (TransactionStatus ts : transactionStatuses.values()) {
            ts.setRollbackOnly();
        }
    }

    /**
     * 返回当前事务是否应该回滚
     *
     * @return
     */
    @Override
    public boolean isRollbackOnly() {
        return getMainTransactionStatus().isRollbackOnly();
    }

    /**
     * 当前事务否已经完成
     *
     * @return
     */
    @Override
    public boolean isCompleted() {
        return getMainTransactionStatus().isCompleted();
    }


    private static class SavePoints {
        Map<TransactionStatus, Object> savepoints = new HashMap<TransactionStatus, Object>();

        private void addSavePoint(TransactionStatus status, Object savepoint) {
            this.savepoints.put(status, savepoint);
        }

        private void save(TransactionStatus transactionStatus) {
            Object savepoint = transactionStatus.createSavepoint();
            addSavePoint(transactionStatus, savepoint);
        }


        public void rollback() {
            for (TransactionStatus transactionStatus : savepoints.keySet()) {
                transactionStatus.rollbackToSavepoint(savepointFor(transactionStatus));
            }
        }

        private Object savepointFor(TransactionStatus transactionStatus) {
            return savepoints.get(transactionStatus);
        }

        public void release() {
            for (TransactionStatus transactionStatus : savepoints.keySet()) {
                transactionStatus.releaseSavepoint(savepointFor(transactionStatus));
            }
        }
    }

    @Override
    public Object createSavepoint() throws TransactionException {
        SavePoints savePoints = new SavePoints();

        for (TransactionStatus transactionStatus : transactionStatuses.values()) {
            savePoints.save(transactionStatus);
        }
        return savePoints;
    }

    @Override
    public void rollbackToSavepoint(Object savepoint) throws TransactionException {
        SavePoints savePoints = (SavePoints) savepoint;
        savePoints.rollback();
    }

    @Override
    public void releaseSavepoint(Object savepoint) throws TransactionException {
        ((SavePoints) savepoint).release();
    }

    public void registerTransactionManager(TransactionDefinition definition, PlatformTransactionManager transactionManager) {
        getTransactionStatuses().put(transactionManager, transactionManager.getTransaction(definition));
    }

    void commit(PlatformTransactionManager transactionManager) {
        TransactionStatus transactionStatus = getTransactionStatus(transactionManager);
        transactionManager.commit(transactionStatus);
    }

    private TransactionStatus getTransactionStatus(PlatformTransactionManager transactionManager) {
        //System.out.println("this.getTransactionStatuses().get(transactionManager)=" + this.getTransactionStatuses().get(transactionManager));
        return this.getTransactionStatuses().get(transactionManager);
    }

    void rollback(PlatformTransactionManager transactionManager) {
        transactionManager.rollback(getTransactionStatus(transactionManager));
    }

    @Override
    public void flush() {
        for (TransactionStatus transactionStatus : transactionStatuses.values()) {
            transactionStatus.flush();
        }
    }
}