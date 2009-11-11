/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2009 14:29:48
 *
 * $Id$
 */
package com.haulmont.cuba.testsupport;

import javax.transaction.*;

public class TestTransactionManager implements TransactionManager {

    private Transaction current;

    public void begin() throws NotSupportedException, SystemException {
        if (current != null && current.getStatus() != Status.STATUS_NO_TRANSACTION) {
            throw new SystemException("Tx exists: " + current);
        }
        current = new TestTransaction();
    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        if (current != null) {
            current.commit();
            current = null;
        }
        else
            throw new SystemException("No Tx");
    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        if (current != null) {
            current.rollback();
            current = null;
        }
        else
            throw new SystemException("No Tx");
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        if (current != null)
            current.setRollbackOnly();
    }

    public int getStatus() throws SystemException {
        if (current != null)
            return current.getStatus();
        else
            return Status.STATUS_UNKNOWN;
    }

    public Transaction getTransaction() throws SystemException {
        return current;
    }

    public void setTransactionTimeout(int i) throws SystemException {
    }

    public Transaction suspend() throws SystemException {
        if (current == null)
            throw new SystemException("No Tx");

        Transaction result = current;
        current = null;
        return result;
    }

    public void resume(Transaction transaction) throws InvalidTransactionException, IllegalStateException, SystemException {
        current = transaction;
    }
}
