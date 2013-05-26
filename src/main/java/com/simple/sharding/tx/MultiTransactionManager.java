package com.simple.sharding.tx;

import org.apache.log4j.Logger;
import org.springframework.transaction.*;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * 支持分布式的事务管理
 *
 * @author: wuyu
 * @since: 12-9-7 上午10:24
 * @version: 1.0.0
 */
public class MultiTransactionManager implements PlatformTransactionManager {

    private final static Logger logger = Logger.getLogger(MultiTransactionManager.class);

    private final List<PlatformTransactionManager> transactionManagers;
    private final SynchronizationManager synchronizationManager;

    private TransactionDefinition transactionDefinition;

    public MultiTransactionManager(PlatformTransactionManager... transactionManagers) {
        this(new DefaultSynchronizationManager(), new DefaultTransactionDefinition(), transactionManagers);
    }

    public MultiTransactionManager(SynchronizationManager synchronizationManager, PlatformTransactionManager... transactionManagers) {
        this.synchronizationManager = synchronizationManager;
        this.transactionManagers = asList(transactionManagers);
        logger.info("MultiTransactionManager.synchronizationManager=" + synchronizationManager);
        logger.info("MultiTransactionManager.transactionManagers.length=" + transactionManagers.length);
    }

    public MultiTransactionManager(SynchronizationManager synchronizationManager,
                                   TransactionDefinition transactionDefinition, PlatformTransactionManager... transactionManagers) {
        this.synchronizationManager = synchronizationManager;
        this.transactionManagers = asList(transactionManagers);
        this.transactionDefinition = transactionDefinition;

    }

    /**
     * 返回一个已经激活的事务或创建一个新的事务（根据给定的TransactionDefinition类型参数定义的事务属性），
     * 返回的是TransactionStatus对象代表了当前事务的状态，
     * 其中该方法抛出TransactionException（未检查异常）表示事务由于某种原因失败。
     *
     * @param definition TransactionDefinition接口定义
     * @return
     * @throws TransactionException
     */
    @Override
    public MultiTransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {

        MultiTransactionStatus mts = new MultiTransactionStatus(transactionManagers.get(0)/*First TM is main TM*/);

        if (!synchronizationManager.isSynchronizationActive()) {
            synchronizationManager.initSynchronization();
            mts.setNewSynchonization();
        }

        for (PlatformTransactionManager transactionManager : transactionManagers) {
            mts.registerTransactionManager(definition, transactionManager);
        }

        return mts;
    }

    /**
     * 用于提交TransactionStatus参数代表的事务
     *
     * @param status
     * @throws TransactionException
     */
    @Override
    public void commit(TransactionStatus status) throws TransactionException {

        MultiTransactionStatus multiTransactionStatus = (MultiTransactionStatus) status;

        boolean commit = true;
        Exception commitException = null;
        PlatformTransactionManager commitExceptionTransactionManager = null;

        for (PlatformTransactionManager transactionManager : reverse(transactionManagers)) {
            if (commit) {
                try {
                    multiTransactionStatus.commit(transactionManager);

                } catch (Exception ex) {
                    commit = false;
                    commitException = ex;
                    commitExceptionTransactionManager = transactionManager;
                    break;
                }
            } else {
                //after unsucessfull commit we must try to rollback remaining transaction managers
                try {
                    multiTransactionStatus.rollback(transactionManager);
                } catch (Exception ex) {
                    logger.warn("Rollback exception (after commit) (" + transactionManager + ") " + ex.getMessage(), ex);
                }
            }
        }

        if (multiTransactionStatus.isNewSynchonization()) {
            synchronizationManager.clearSynchronization();
        }

        if (commitException != null) {
            boolean firstTransactionManagerFailed = commitExceptionTransactionManager == getLastTransactionManager();
            int transactionState = firstTransactionManagerFailed ? HeuristicCompletionException.STATE_ROLLED_BACK : HeuristicCompletionException.STATE_MIXED;
            rollback(multiTransactionStatus);
            throw new HeuristicCompletionException(transactionState, commitException);
        }

    }

    /**
     * 用于回滚TransactionStatus参数代表的事务
     *
     * @param status
     * @throws TransactionException
     */
    @Override
    public void rollback(TransactionStatus status) throws TransactionException {

        Exception rollbackException = null;
        PlatformTransactionManager rollbackExceptionTransactionManager = null;


        MultiTransactionStatus multiTransactionStatus = (MultiTransactionStatus) status;

        for (PlatformTransactionManager transactionManager : reverse(transactionManagers)) {
            try {
                multiTransactionStatus.rollback(transactionManager);
            } catch (Exception ex) {
                if (rollbackException == null) {
                    rollbackException = ex;
                    rollbackExceptionTransactionManager = transactionManager;
                } else {
                    logger.warn("Rollback exception (" + transactionManager + ") " + ex.getMessage(), ex);
                }
            }
        }

        if (multiTransactionStatus.isNewSynchonization()) {
            synchronizationManager.clearSynchronization();
        }

        if (rollbackException != null) {
            throw new UnexpectedRollbackException("Rollback exception, originated at (" + rollbackExceptionTransactionManager + ") " +
                    rollbackException.getMessage(), rollbackException);
        }
    }

    private <T> Iterable<T> reverse(Collection<T> collection) {
        List<T> list = new ArrayList<T>(collection);
        Collections.reverse(list);
        return list;
    }


    private PlatformTransactionManager getLastTransactionManager() {
        return transactionManagers.get(lastTransactionManagerIndex());
    }

    private int lastTransactionManagerIndex() {
        return transactionManagers.size() - 1;
    }

    public TransactionDefinition getTransactionDefinition() {
        return transactionDefinition;
    }

    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }

}
