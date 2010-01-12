/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 24.12.2009 11:48:24
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.EntityManagerFactory;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.sys.persistence.EntityLifecycleListener;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SpringPersistenceProvider extends PersistenceProvider {

    private OpenJPAEntityManagerFactory jpaEmf;
    private EntityManagerFactory emf;

    private volatile boolean softDeletion = true;

    public void setJpaEmf(OpenJPAEntityManagerFactory jpaEmf) {
        this.jpaEmf = jpaEmf;
        initJpaFactory(jpaEmf);
    }

    protected void initJpaFactory(OpenJPAEntityManagerFactory jpaFactory) {
        ((OpenJPAEntityManagerFactorySPI) jpaFactory).addLifecycleListener(
                new EntityLifecycleListener(),
                null
        );
    }

    @Override
    protected EntityManagerFactory __getEntityManagerFactory() {
        if (emf == null) {
            synchronized (this) {
                if (emf == null) {
                    emf = new EntityManagerFactoryImpl(jpaEmf);
                }
            }
        }
        return emf;
    }

    @Override
    protected EntityManager __getEntityManager() {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new IllegalStateException("No active transaction");

        OpenJPAEntityManager jpaEm = (OpenJPAEntityManager)
                EntityManagerFactoryUtils.doGetTransactionalEntityManager(jpaEmf, null);

        EntityManagerImpl impl = new EntityManagerImpl(jpaEm);
        EntityManagerContext ctx = contextHolder.get();
        if (ctx != null) {
            impl.setSoftDeletion(ctx.isSoftDeletion());
        }

        EntityManager em = (EntityManager) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { EntityManager.class },
                new EntityManagerInvocationHandler(impl)
        );
        return em;
    }

    @Override
    protected boolean __isSoftDeletion() {
        return softDeletion;
    }

    @Override
    protected void __setSoftDeletion(boolean value) {
        softDeletion = value;
    }

    @Override
    protected EntityManagerContext __getEntityManagerContext() {
        EntityManagerContext emCtx = contextHolder.get();
        if (emCtx == null)
            emCtx = new EntityManagerContext();
        return emCtx;
    }

    public TransactionSynchronization createSynchronization() {
        return new EntityManagerContextSynchronization();
    }

    private class EntityManagerInvocationHandler implements InvocationHandler {

        private EntityManagerImpl impl;

        private EntityManagerInvocationHandler(EntityManagerImpl impl) {
            this.impl = impl;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("setSoftDeletion")) {
                EntityManagerContext ctx = contextHolder.get();
                if (ctx == null) {
                    ctx = new EntityManagerContext();
                }
                ctx.setSoftDeletion((Boolean) args[0]);
                contextHolder.set(ctx);
            }
            return method.invoke(impl, args);
        }
    }

    private ThreadLocal<EntityManagerContext> contextHolder = new ThreadLocal<EntityManagerContext>();

    private class EntityManagerContextSynchronization implements TransactionSynchronization {

        private EntityManagerContext context;

        public void suspend() {
            context = contextHolder.get();
            contextHolder.remove();
        }

        public void resume() {
            contextHolder.set(context);
        }

        public void flush() {
        }

        public void beforeCommit(boolean readOnly) {
        }

        public void beforeCompletion() {
        }

        public void afterCommit() {
        }

        public void afterCompletion(int status) {
            contextHolder.remove();
        }
    }
}
