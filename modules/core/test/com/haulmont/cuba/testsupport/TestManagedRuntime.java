/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 11.11.2009 14:44:35
 *
 * $Id$
 */
package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.JndiContextHolder;
import org.apache.openjpa.ee.AbstractManagedRuntime;
import org.apache.openjpa.ee.ManagedRuntime;

import javax.transaction.TransactionManager;

public class TestManagedRuntime extends AbstractManagedRuntime implements ManagedRuntime {

    public TransactionManager getTransactionManager() throws Exception {
        return (TransactionManager) JndiContextHolder.getContext().lookup("java:/TransactionManager");
    }

    public void setRollbackOnly(Throwable cause) throws Exception {
        getTransactionManager().setRollbackOnly();
    }

    public Throwable getRollbackCause() throws Exception {
        return null;
    }
}
