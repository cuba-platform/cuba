/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class TransactionImpl implements Transaction {

    private PlatformTransactionManager tm;
    private TransactionStatus ts;
    private boolean committed;

    public TransactionImpl(PlatformTransactionManager transactionManager, PersistenceImpl persistence, boolean join,
                           TransactionParams params)
    {
        this.tm = transactionManager;

        DefaultTransactionDefinition td = new DefaultTransactionDefinition();
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

    public void commitRetaining() {
        if (committed)
            return;

        try {
            tm.commit(ts);

            DefaultTransactionDefinition td = new DefaultTransactionDefinition();
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            ts = tm.getTransaction(td);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void end() {
        if (committed)
            return;

        if (TransactionSynchronizationManager.isSynchronizationActive())
            tm.rollback(ts);
    }
}
