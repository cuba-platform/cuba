/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.PersistenceImplSupport;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(Persistence.NAME)
public class PersistenceImpl implements Persistence {

    public static final String RUN_BEFORE_COMMIT_ATTR = "cuba.runBeforeCommit";

    private volatile boolean softDeletion = true;

    private ThreadLocal<EntityManagerContext> contextHolder = new ThreadLocal<>();

    @Inject
    private PersistenceTools tools;

    @Inject
    private Metadata metadata;

    @Inject
    private FetchGroupManager fetchGroupMgr;

    @Inject
    private EntityListenerManager entityListenerMgr;

    private EntityManagerFactory jpaEmf;

    @Inject
    private PlatformTransactionManager transactionManager;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private PersistenceImplSupport support;

    @Inject
    public void setFactory(LocalContainerEntityManagerFactoryBean factoryBean) {
        this.jpaEmf = factoryBean.getObject();
    }

    @Override
    public PersistenceTools getTools() {
        return tools;
    }

    @Override
    public DbTypeConverter getDbTypeConverter() {
        return DbmsSpecificFactory.getDbTypeConverter();
    }

    @Override
    public void runInTransaction(Transaction.Runnable runnable) {
        createTransaction().execute(runnable);
    }

    @Override
    public <T> T callInTransaction(Transaction.Callable<T> callable) {
        return createTransaction().execute(callable);
    }

    @Override
    public Transaction createTransaction(TransactionParams params) {
        return new TransactionImpl(transactionManager, this, false, params);
    }

    @Override
    public Transaction createTransaction() {
        return new TransactionImpl(transactionManager, this, false, null);
    }

    @Override
    public Transaction getTransaction() {
        return new TransactionImpl(transactionManager, this, true, null);
    }

    @Override
    public boolean isInTransaction() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    @Override
    public EntityManager getEntityManager() {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new IllegalStateException("No active transaction");

        javax.persistence.EntityManager jpaEm = EntityManagerFactoryUtils.doGetTransactionalEntityManager(jpaEmf, null);

        UserSession userSession = userSessionSource.checkCurrentUserSession() ? userSessionSource.getUserSession() : null;

        EntityManagerImpl impl = new EntityManagerImpl(
                jpaEm, userSession, metadata, fetchGroupMgr, entityListenerMgr, support);

        EntityManagerContext ctx = contextHolder.get();
        if (ctx != null) {
            impl.setSoftDeletion(ctx.isSoftDeletion());
        } else {
            ctx = new EntityManagerContext();
            ctx.setSoftDeletion(isSoftDeletion());
            contextHolder.set(ctx);
            impl.setSoftDeletion(isSoftDeletion());
        }

        EntityManager em = (EntityManager) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{EntityManager.class},
                new EntityManagerInvocationHandler(impl)
        );
        return em;
    }

    @Override
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    @Override
    public void setSoftDeletion(boolean value) {
        softDeletion = value;
    }

    @Override
    public DataSource getDataSource() {
        return (DataSource) AppBeans.get("cubaDataSource");
    }

    @Override
    @Nonnull
    public EntityManagerContext getEntityManagerContext() {
        EntityManagerContext emCtx = contextHolder.get();
        if (emCtx == null)
            emCtx = new EntityManagerContext();
        return emCtx;
    }

    @Override
    public void dispose() {
        jpaEmf.close();
    }

    public TransactionSynchronization createSynchronization() {
        return new EntityManagerContextSynchronization();
    }

    private class EntityManagerContextSynchronization implements TransactionSynchronization, Ordered {

        private EntityManagerContext context;

        @Override
        public void suspend() {
            context = contextHolder.get();
            contextHolder.remove();
        }

        @Override
        public void resume() {
            contextHolder.set(context);
        }

        @Override
        public void flush() {
        }

        @Override
        public void beforeCommit(boolean readOnly) {
            if (readOnly)
                return;

            EntityManagerContext context = contextHolder.get();
            if (context != null) {
                List<Runnable> list = context.getAttribute(RUN_BEFORE_COMMIT_ATTR);
                if (list != null && !list.isEmpty()) {
                    for (Runnable runnable : list) {
                        runnable.run();
                    }
                }
            }
        }

        @Override
        public void beforeCompletion() {
        }

        @Override
        public void afterCommit() {
        }

        @Override
        public void afterCompletion(int status) {
            contextHolder.remove();
        }

        @Override
        public int getOrder() {
            return 200;
        }
    }

    protected EntityManagerFactory getJpaEmf() {
        return jpaEmf;
    }

    private class EntityManagerInvocationHandler implements InvocationHandler {

        private EntityManagerImpl impl;

        private EntityManagerInvocationHandler(EntityManagerImpl impl) {
            this.impl = impl;
        }

        @Override
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
}
