/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.12.2009 12:30:58
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Transaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SpringTransaction implements Transaction {

    private PlatformTransactionManager tm;
    private TransactionStatus ts;
    private boolean committed;

    public SpringTransaction(PlatformTransactionManager transactionManager, SpringPersistenceProvider persistenceProvider, boolean join) {
        this.tm = transactionManager;

        DefaultTransactionDefinition td = new DefaultTransactionDefinition();
        if (join)
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        else
            td.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        ts = tm.getTransaction(td);

        TransactionSynchronizationManager.registerSynchronization(persistenceProvider.createSynchronization());
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
