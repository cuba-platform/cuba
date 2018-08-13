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
import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Stores;
import com.haulmont.cuba.core.sys.persistence.DbTypeConverter;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.PersistenceImplSupport;
import org.eclipse.persistence.internal.helper.CubaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Component(Persistence.NAME)
public class PersistenceImpl implements Persistence {

    public static final String RUN_BEFORE_COMMIT_ATTR = "cuba.runBeforeCommit";

    /**
     * DEPRECATED. Use {@link TransactionSynchronizationManager#registerSynchronization}.
     */
    @Deprecated
    public static final String RUN_AFTER_COMPLETION_ATTR = "cuba.runAfterCompletion";

    private static final Logger log = LoggerFactory.getLogger(PersistenceImpl.class);

    protected volatile boolean softDeletion = true;

    protected EntityManagerContextHolder contextHolder = new EntityManagerContextHolder();

    @Inject
    private BeanLocator beanLocator;

    @Inject
    protected PersistenceTools tools;

    @Inject
    protected PersistenceImplSupport support;

    protected EntityManagerFactory jpaEmf;

    @Inject
    protected Transactions transactions;

    @Inject
    protected MiddlewareStatisticsAccumulator statisticsAccumulator;

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
        return transactions.create(params);
    }

    @Override
    public Transaction createTransaction(String store, TransactionParams params) {
        return transactions.create(store, params);
    }

    @Override
    public Transaction createTransaction() {
        return transactions.create();
    }

    @Override
    public Transaction createTransaction(String store) {
        return transactions.create(store);
    }

    @Override
    public Transaction getTransaction() {
        return transactions.get();
    }

    @Override
    public Transaction getTransaction(String store) {
        return transactions.get(store);
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
            emf = beanLocator.get("entityManagerFactory_" + store);

        javax.persistence.EntityManager jpaEm = EntityManagerFactoryUtils.doGetTransactionalEntityManager(emf, null, true);

        if (!jpaEm.isJoinedToTransaction())
            throw new IllegalStateException("No active transaction for " + store + " database");

        EntityManager entityManager = createEntityManager(jpaEm);

        EntityManagerContext ctx = contextHolder.get(store);
        if (ctx != null) {
            entityManager.setSoftDeletion(ctx.isSoftDeletion());
        } else {
            ctx = new EntityManagerContext();
            ctx.setSoftDeletion(isSoftDeletion());
            contextHolder.set(ctx, store);
            entityManager.setSoftDeletion(isSoftDeletion());
        }

        EntityManager emProxy = (EntityManager) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{EntityManager.class},
                new EntityManagerInvocationHandler(entityManager, store)
        );
        return emProxy;
    }

    protected EntityManager createEntityManager(javax.persistence.EntityManager jpaEm) {
        return (EntityManager) beanLocator.getPrototype(EntityManager.NAME, jpaEm);
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
            return (DataSource) beanLocator.get("cubaDataSource");
        else
            return (DataSource) beanLocator.get("cubaDataSource_" + store);
    }

    @Override
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

    /**
     * INTERNAL.
     * Destroys the persistence configuration. Further use of this bean instance is impossible.
     */
    public void dispose() {
        jpaEmf.close();
        for (String store : Stores.getAdditional()) {
            EntityManagerFactory emf = beanLocator.get("entityManagerFactory_" + store);
            emf.close();
        }
    }

    /**
     * INTERNAL.
     * Register synchronizations with a just started transaction.
     */
    public void registerSynchronizations(String store) {
        log.trace("registerSynchronizations for store '{}'", store);
        TransactionSynchronizationManager.registerSynchronization(createSynchronization(store));
        support.getInstanceContainerResourceHolder(store);

        statisticsAccumulator.incStartedTransactionsCount();
        TransactionSynchronizationManager.registerSynchronization(new StatisticsTransactionSynchronization());
    }

    protected TransactionSynchronization createSynchronization(String store) {
        return new EntityManagerContextSynchronization(store);
    }

    /**
     * INTERNAL.
     * Adds an action to be executed before commit of the current transaction.
     * Can be invoked from {@link TransactionSynchronization#beforeCommit(boolean)} code.
     */
    public void addBeforeCommitAction(Runnable action) {
        List<Runnable> list = getEntityManagerContext().getAttribute(PersistenceImpl.RUN_BEFORE_COMMIT_ATTR);
        if (list == null) {
            list = new ArrayList<>();
            getEntityManagerContext().setAttribute(PersistenceImpl.RUN_BEFORE_COMMIT_ATTR, list);
        }
        list.add(action);
    }

    protected class EntityManagerContextSynchronization implements TransactionSynchronization, Ordered {

        protected final boolean prevSoftDeletion;
        protected EntityManagerContext context;
        protected String store;

        public EntityManagerContextSynchronization(String store) {
            this.store = store;
            prevSoftDeletion = CubaUtil.setSoftDeletion(softDeletion);
            CubaUtil.setOriginalSoftDeletion(softDeletion);
        }

        @Override
        public void suspend() {
            context = contextHolder.get(store);
            contextHolder.remove(store);
            CubaUtil.setSoftDeletion(prevSoftDeletion);
            CubaUtil.setOriginalSoftDeletion(prevSoftDeletion);
        }

        @Override
        public void resume() {
            contextHolder.set(context, store);
            if (context != null) {
                CubaUtil.setSoftDeletion(context.isSoftDeletion());
                CubaUtil.setOriginalSoftDeletion(context.isSoftDeletion());
            }
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
            EntityManagerContext context = contextHolder.get(store);
            if (context != null) {
                List<Consumer<Integer>> list = context.getAttribute(RUN_AFTER_COMPLETION_ATTR);
                if (list != null && !list.isEmpty()) {
                    contextHolder.remove(store);
                    for (Consumer<Integer> consumer : list) {
                        consumer.accept(status);
                    }
                }
            }
            contextHolder.remove(store);
            CubaUtil.setSoftDeletion(prevSoftDeletion);
            CubaUtil.setOriginalSoftDeletion(prevSoftDeletion);
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
            return beanLocator.get("entityManagerFactory_" + store);
    }

    protected class EntityManagerInvocationHandler implements InvocationHandler {

        protected EntityManager entityManager;
        protected String store;

        public EntityManagerInvocationHandler(EntityManager entityManager, String store) {
            this.entityManager = entityManager;
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
                return method.invoke(entityManager, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }

    protected class StatisticsTransactionSynchronization extends TransactionSynchronizationAdapter {
        @Override
        public void afterCompletion(int status) {
            if (status == TransactionSynchronization.STATUS_COMMITTED)
                statisticsAccumulator.incCommittedTransactionsCount();
            else
                statisticsAccumulator.incRolledBackTransactionsCount();
        }
    }
}