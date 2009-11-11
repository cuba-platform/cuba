/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2009 18:57:55
 *
 * $Id$
 */
package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.Locator;

import javax.transaction.*;
import javax.naming.NamingException;

public class TestUserTransaction implements UserTransaction {

    private TransactionManager getTm() {
        try {
            return (TransactionManager) Locator.getJndiContext().lookup("java:/TransactionManager");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public void begin() throws NotSupportedException, SystemException {
        getTm().begin();
    }

    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        getTm().commit();
    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        getTm().rollback();
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        getTm().setRollbackOnly();
    }

    public int getStatus() throws SystemException {
        return getTm().getStatus();
    }

    public void setTransactionTimeout(int i) throws SystemException {
        getTm().setTransactionTimeout(i);
    }
}
