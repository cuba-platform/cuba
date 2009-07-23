/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.11.2008 18:27:55
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Transaction;

import javax.transaction.TransactionManager;
import javax.transaction.SystemException;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;

import org.apache.commons.lang.builder.ToStringBuilder;

public class JtaTransaction implements Transaction
{
    private TransactionManager tm;

    private boolean started;
    private boolean committed;

    public JtaTransaction(TransactionManager tm, boolean join) {
        this.tm = tm;
        try {
            if (tm.getTransaction() == null) {
                tm.begin();
                started = true;
            }
            else if (!join) {
                throw new IllegalStateException("JTA transaction exists while join = false : " + tm.getTransaction());
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        if (committed)
            return;

        if (!started) {
            committed = true;
            return;
        }

        try {
            tm.commit();
            committed = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitRetaining() {
        if (committed)
            return;

        if (!started) {
            committed = true;
            return;
        }

        try {
            tm.commit();
            tm.begin();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void end() {
        try {
            if (!started) {
                if (!committed && (tm.getStatus() == Status.STATUS_ACTIVE)) {
                    tm.setRollbackOnly();
                }
                return;
            }

            if (!committed) {
                if (tm.getStatus() == Status.STATUS_ACTIVE || tm.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                    tm.rollback();
                }
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String toString() {
        javax.transaction.Transaction tx = null;
        try {
            tx = tm.getTransaction();
        } catch (SystemException e) { // ignore
        }
        ToStringBuilder builder = new ToStringBuilder(this)
                .append("started", started)
                .append("committed", committed)
                .append("tx", tx);
        return builder.toString();
    }
}
