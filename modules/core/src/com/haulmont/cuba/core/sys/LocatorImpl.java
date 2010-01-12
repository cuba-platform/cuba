/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 03.11.2008 19:02:51
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Transaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class LocatorImpl extends Locator
{
    private Context jndiContext;

    private PlatformTransactionManager transactionManager;

    private SpringPersistenceProvider persistenceProvider;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPersistenceProvider(SpringPersistenceProvider persistenceProvider) {
        this.persistenceProvider = persistenceProvider;
    }

    protected Context __getJndiContextImpl() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
        return jndiContext;
    }

    protected Object __lookup(String name) {
        return AppContext.getApplicationContext().getBean(name);
    }

    protected DataSource __getDataSource() {
        return (DataSource) AppContext.getApplicationContext().getBean("dataSource");
    }

    @Deprecated
    protected Object __lookupLocal(String name) {
        return AppContext.getApplicationContext().getBean(name);
    }

    @Deprecated
    protected <T> T __lookupMBean(Class<T> mbeanClass, String objectName) {
        String beanName = objectName.replaceAll("[\\.=:]", "_");
        return AppContext.getApplicationContext().getBean(beanName, mbeanClass);
    }

    protected Transaction __createTransaction() {
        return new SpringTransaction(transactionManager, persistenceProvider, false);
    }

    protected Transaction __getTransaction() {
        return new SpringTransaction(transactionManager, persistenceProvider, true);
    }

    protected boolean __isInTransaction() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }
}
