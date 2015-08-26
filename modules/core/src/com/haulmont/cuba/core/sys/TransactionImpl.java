/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TransactionParams;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TransactionImpl implements Transaction {

    private final PlatformTransactionManager tm;
    private final PersistenceImpl persistence;
    private final DefaultTransactionDefinition td;
    private TransactionStatus ts;
    private boolean committed;

    public TransactionImpl(PlatformTransactionManager transactionManager, PersistenceImpl persistence, boolean join,
                           TransactionParams params) {
        this.tm = transactionManager;
        this.persistence = persistence;

        td = new DefaultTransactionDefinition();
        if (join)
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        else
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        if (params != null) {
            if (params.getTimeout() != 0)
                td.setTimeout(params.getTimeout());
        }

        ts = tm.getTransaction(td);

        TransactionSynchronizationManager.registerSynchronization(persistence.createSynchronization());
    }

    @Override
    public <T> T execute(Callable<T> callable) {
        try {
            T result = callable.call(persistence.getEntityManager());
            commit();
            return result;
        } finally {
            end();
        }
    }

    @Override
    public void commit() {
        if (committed)
            return;

        try {
            tm.commit(ts);
            committed = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commitRetaining() {
        if (committed)
            return;

        try {
            tm.commit(ts);

            ts = tm.getTransaction(td);
            TransactionSynchronizationManager.registerSynchronization(persistence.createSynchronization());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void end() {
        if (committed)
            return;

        if (!ts.isCompleted())
            tm.rollback(ts);
    }

    @Override
    public void close() throws Exception {
        end();
    }
}