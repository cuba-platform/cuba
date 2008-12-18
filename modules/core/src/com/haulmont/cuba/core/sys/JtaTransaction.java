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

public class JtaTransaction implements Transaction
{
    private TransactionManager tm;

    private boolean committed;

    public JtaTransaction(TransactionManager tm) {
        this.tm = tm;
        try {
            if (tm.getTransaction() == null) {
                tm.begin();
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        } catch (NotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            tm.commit();
            committed = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void commitRetaining() {
        try {
            tm.commit();
            tm.begin();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void end() {
        if (!committed) {
            try {
                if (tm.getStatus() == Status.STATUS_ACTIVE) {
                    tm.rollback();
                }
            } catch (SystemException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
