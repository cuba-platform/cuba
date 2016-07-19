/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
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
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

@Component(Persistence.NAME)
public class PersistenceImpl implements Persistence {

    public static final String RUN_BEFORE_COMMIT_ATTR = "cuba.runBeforeCommit";

    private volatile boolean softDeletion = true;

    private EntityManagerContextHolder contextHolder = new EntityManagerContextHolder();

    @Inject
    private PersistenceTools tools;

    private EntityManagerFactory jpaEmf;

    @Inject @Named("transactionManager")
    private PlatformTransactionManager transactionManager;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject @Named("entityManagerFactory")
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
    public DbTypeConverter getDbTypeConverter(String store) {
        return DbmsSpecificFactory.getDbTypeConverter(store);
    }

    @Override
    public void runInTransaction(Transaction.Runnable runnable) {
        createTransaction().execute(runnable);
    }

    @Override
    public void runInTransaction(String store, Transaction.Runnable runnable) {
        createTransaction(store).execute(store, runnable);
    }

    @Override
    public <T> T callInTransaction(Transaction.Callable<T> callable) {
        return createTransaction().execute(callable);
    }

    @Override
    public <T> T callInTransaction(String store, Transaction.Callable<T> callable) {
        return createTransaction(store).execute(store, callable);
    }

    @Override
    public Transaction createTransaction(TransactionParams params) {
        return new TransactionImpl(transactionManager, this, false, params, Stores.MAIN);
    }

    @Override
    public Transaction createTransaction(String store, TransactionParams params) {
        return new TransactionImpl(getTransactionManager(store), this, false, params, store);
    }

    @Override
    public Transaction createTransaction() {
        return new TransactionImpl(transactionManager, this, false, null, Stores.MAIN);
    }

    @Override
    public Transaction createTransaction(String store) {
        return new TransactionImpl(getTransactionManager(store), this, false, null, store);
    }

    @Override
    public Transaction getTransaction() {
        return new TransactionImpl(transactionManager, this, true, null, Stores.MAIN);
    }

    @Override
    public Transaction getTransaction(String store) {
        return new TransactionImpl(getTransactionManager(store), this, true, null, store);
    }

    @Override
    public boolean isInTransaction() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    @Override
    public EntityManager getEntityManager() {
        return getEntityManager(Stores.MAIN);
    }

    @Override
    public EntityManager getEntityManager(String store) {
        if (!TransactionSynchronizationManager.isActualTransactionActive())
            throw new IllegalStateException("No active transaction");

        EntityManagerFactory emf;
        if (Stores.isMain(store))
            emf = this.jpaEmf;
        else
            emf = AppBeans.get("entityManagerFactory_" + store);

        javax.persistence.EntityManager jpaEm = EntityManagerFactoryUtils.doGetTransactionalEntityManager(emf, null);

        if (!jpaEm.isJoinedToTransaction())
            throw new IllegalStateException("No active transaction for " + store + " database");

        UserSession userSession = userSessionSource.checkCurrentUserSession() ? userSessionSource.getUserSession() : null;

        EntityManagerImpl impl = new EntityManagerImpl(jpaEm, userSession);

        EntityManagerContext ctx = contextHolder.get(store);
        if (ctx != null) {
            impl.setSoftDeletion(ctx.isSoftDeletion());
        } else {
            ctx = new EntityManagerContext();
            ctx.setSoftDeletion(isSoftDeletion());
            contextHolder.set(ctx, store);
            impl.setSoftDeletion(isSoftDeletion());
        }

        EntityManager em = (EntityManager) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{EntityManager.class},
                new EntityManagerInvocationHandler(impl, store)
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
        return getDataSource(Stores.MAIN);
    }

    @Override
    public DataSource getDataSource(String store) {
        if (Stores.isMain(store))
            return (DataSource) AppBeans.get("cubaDataSource");
        else
            return (DataSource) AppBeans.get("cubaDataSource_" + store);
    }

    @Override
    @Nonnull
    public EntityManagerContext getEntityManagerContext() {
        return getEntityManagerContext(Stores.MAIN);
    }

    @Override
    public EntityManagerContext getEntityManagerContext(String store) {
        EntityManagerContext emCtx = contextHolder.get(store);
        if (emCtx == null)
            emCtx = new EntityManagerContext();
        return emCtx;
    }

    @Override
    public void dispose() {
        jpaEmf.close();
        for (String store : Stores.getAdditional()) {
            EntityManagerFactory emf = AppBeans.get("entityManagerFactory_" + store);
            emf.close();
        }
    }

    public TransactionSynchronization createSynchronization(String store) {
        return new EntityManagerContextSynchronization(store);
    }

    private class EntityManagerContextSynchronization implements TransactionSynchronization, Ordered {

        private EntityManagerContext context;
        private String store;

        public EntityManagerContextSynchronization(String store) {
            this.store = store;
        }

        @Override
        public void suspend() {
            context = contextHolder.get(store);
            contextHolder.remove(store);
        }

        @Override
        public void resume() {
            contextHolder.set(context, store);
        }

        @Override
        public void flush() {
        }

        @Override
        public void beforeCommit(boolean readOnly) {
            if (readOnly)
                return;

            EntityManagerContext context = contextHolder.get(store);
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
            contextHolder.remove(store);
        }

        @Override
        public int getOrder() {
            return 200;
        }
    }

    protected EntityManagerFactory getJpaEmf(String store) {
        if (Stores.isMain(store))
            return jpaEmf;
        else
            return AppBeans.get("entityManagerFactory_" + store);
    }

    protected PlatformTransactionManager getTransactionManager(String store) {
        PlatformTransactionManager tm;
        if (Stores.isMain(store))
            tm = this.transactionManager;
        else
            tm = AppBeans.get("transactionManager_" + store, PlatformTransactionManager.class);
        return tm;
    }

    private class EntityManagerInvocationHandler implements InvocationHandler {

        private EntityManagerImpl impl;
        private String store;

        private EntityManagerInvocationHandler(EntityManagerImpl impl, String store) {
            this.impl = impl;
            this.store = store;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("setSoftDeletion")) {
                EntityManagerContext ctx = contextHolder.get(store);
                if (ctx == null) {
                    ctx = new EntityManagerContext();
                }
                ctx.setSoftDeletion((Boolean) args[0]);
                contextHolder.set(ctx, store);
            }
            try {
                return method.invoke(impl, args);
            } catch (IllegalAccessException | IllegalArgumentException e) {
                throw e;
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}