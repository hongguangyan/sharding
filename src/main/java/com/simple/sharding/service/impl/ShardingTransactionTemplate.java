package com.simple.sharding.service.impl;

import com.simple.sharding.tx.MultiTransactionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.*;
import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionOperations;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * 使用Sharding分布式事务模板
 * <p/>
 * <pre class="code">
 * <p/>
 * &nbsp;&nbsp;&lt;bean id="transactionManager1" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dataSource" ref="dataSource1"/&gt;
 *&nbsp;&nbsp; &lt;/bean&gt;
 * &nbsp;&nbsp;&lt;bean id="transactionManager2" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="dataSource" ref="dataSource2"/&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * &nbsp;&nbsp;&lt;!--分布式事务配置--&gt;
 * &nbsp;&nbsp;&lt;bean id="shardingTransactionTemplate" class="com.simple.payment.sharding.service.impl.ShardingTransactionTemplate"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;property name="multiTransactionManager"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;ref bean="multiTransactionManager"/&gt;
 * &nbsp;&nbsp;&lt;/property&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * <p/>
 * &nbsp;&nbsp;&lt;bean id="multiTransactionManager" class="com.simple.payment.sharding.tx.MultiTransactionManager"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;constructor-arg index="0"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;list&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ref bean="transactionManager"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ref bean="transactionManager1"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ref bean="transactionManager2"/&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/list&gt;
 *&nbsp;&nbsp;&nbsp;&nbsp; &lt;/constructor-arg&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * &nbsp;&nbsp;&lt;bean id="multiTransactionStatus" class="com.simple.payment.sharding.tx.MultiTransactionStatus"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;constructor-arg index="0"&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ref bean="multiTransactionManager"&gt;&lt;/ref&gt;
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/constructor-arg&gt;
 * &nbsp;&nbsp;&lt;/bean&gt;
 * <pre>
 *
 * @author: wuyu
 * @since: 12-9-7 下午1:23
 * @version: 1.0.0
 */
public class ShardingTransactionTemplate extends DefaultTransactionDefinition
        implements TransactionOperations, InitializingBean {

    /**
     * Logger available to subclasses
     */
    protected final Logger logger = Logger.getLogger(getClass());

    public MultiTransactionManager getMultiTransactionManager() {
        return multiTransactionManager;
    }

    public void setMultiTransactionManager(MultiTransactionManager multiTransactionManager) {
        this.multiTransactionManager = multiTransactionManager;
    }

    private MultiTransactionManager multiTransactionManager;


    /**
     * Construct a new TransactionTemplate for bean usage.
     * <p>Note: The PlatformTransactionManager needs to be set before
     * any <code>execute</code> calls.
     *
     * @see #setTransactionManager
     */
    public ShardingTransactionTemplate() {
    }

    /**
     * Construct a new TransactionTemplate using the given transaction manager.
     *
     * @param multiTransactionManager the transaction management strategy to be used
     */
    public ShardingTransactionTemplate(MultiTransactionManager multiTransactionManager) {
        this.multiTransactionManager = multiTransactionManager;
    }

    /**
     * Construct a new TransactionTemplate using the given transaction manager,
     * taking its default settings from the given transaction definition.
     *
     * @param multiTransactionManager the transaction management strategy to be used
     * @param transactionDefinition   the transaction definition to copy the
     *                                default settings from. Local properties can still be set to change values.
     */
    public ShardingTransactionTemplate(MultiTransactionManager multiTransactionManager, TransactionDefinition transactionDefinition) {
        super(transactionDefinition);
        this.multiTransactionManager = multiTransactionManager;
    }


    /**
     * Set the transaction management strategy to be used.
     */
    public void setTransactionManager(MultiTransactionManager multiTransactionManager) {
        this.multiTransactionManager = multiTransactionManager;
    }

    /**
     * Return the transaction management strategy to be used.
     */
    public PlatformTransactionManager getTransactionManager() {
        return this.multiTransactionManager;
    }

    public void afterPropertiesSet() {
        if (this.multiTransactionManager == null) {
            throw new IllegalArgumentException("Property 'transactionManager' is required");
        }
    }


    public <T> T execute(TransactionCallback<T> action) throws TransactionException {
        if (this.multiTransactionManager instanceof CallbackPreferringPlatformTransactionManager) {
            return ((CallbackPreferringPlatformTransactionManager) this.multiTransactionManager).execute(this, action);
        } else {
            TransactionStatus status = this.multiTransactionManager.getTransaction(this);
            T result;
            try {
                result = action.doInTransaction(status);
            } catch (RuntimeException ex) {
                // Transactional code threw application exception -> rollback
                rollbackOnException(status, ex);
                throw ex;
            } catch (Error err) {
                // Transactional code threw error -> rollback
                rollbackOnException(status, err);
                throw err;
            } catch (Exception ex) {
                // Transactional code threw unexpected exception -> rollback
                rollbackOnException(status, ex);
                throw new UndeclaredThrowableException(ex, "TransactionCallback threw undeclared checked exception");
            }
            this.multiTransactionManager.commit(status);
            return result;
        }
    }

    /**
     * Perform a rollback, handling rollback exceptions properly.
     *
     * @param status object representing the transaction
     * @param ex     the thrown application exception or error
     * @throws TransactionException in case of a rollback error
     */
    private void rollbackOnException(TransactionStatus status, Throwable ex) throws TransactionException {
        logger.debug("Initiating transaction rollback on application exception", ex);
        try {
            this.multiTransactionManager.rollback(status);
        } catch (TransactionSystemException ex2) {
            logger.error("Application exception overridden by rollback exception", ex);
            ex2.initApplicationException(ex);
            throw ex2;
        } catch (RuntimeException ex2) {
            logger.error("Application exception overridden by rollback exception", ex);
            throw ex2;
        } catch (Error err) {
            logger.error("Application exception overridden by rollback error", ex);
            throw err;
        }
    }

}

