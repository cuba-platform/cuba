/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.persistence.*;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.ManagedBean;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Persistence.NAME)
public class PersistenceImpl implements Persistence {

    private DbDialect dbDialect;

    private DbTypeConverter dbTypeConverter;

    private volatile boolean softDeletion = true;

    private ThreadLocal<EntityManagerContext> contextHolder = new ThreadLocal<EntityManagerContext>();

    @Inject
    private PersistenceTools tools;

    @Inject
    private Metadata metadata;

    @Inject
    private FetchPlanManager fetchPlanMgr;

    @Inject
    private OpenJPAEntityManagerFactory jpaEmf;

    @Inject
    private PlatformTransactionManager transactionManager;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private EntityLifecycleListener entityLifecycleListener;

    @Inject
    public void setFactory(OpenJPAEntityManagerFactory jpaEmf,
                           EntityLifecycleListener entityLifecycleListener,
                           EntityTransactionListener entityTransactionListener)
    {
        this.jpaEmf = jpaEmf;
        ((OpenJPAEntityManagerFactorySPI) jpaEmf).addLifecycleListener(entityLifecycleListener, null);
        ((OpenJPAEntityManagerFactorySPI) jpaEmf).addTransactionListener(entityTransactionListener);
    }

    @Override
    public PersistenceTools getTools() {
        return tools;
    }

    @Override
    public DbDialect getDbDialect() {
        if (dbDialect == null) {
            OpenJPAConfiguration configuration = ((OpenJPAEntityManagerFactorySPI) jpaEmf).getConfiguration();
            if (configuration instanceof JDBCConfiguration) {
                DBDictionary dictionary = ((JDBCConfiguration) configuration).getDBDictionaryInstance();
                if (dictionary instanceof HSQLDictionary) {
                    dbDialect = new HsqlDbDialect();
                } else if (dictionary instanceof PostgresDictionary) {
                    dbDialect = new PostgresDbDialect();
                } else if (dictionary instanceof SQLServerDictionary) {
                    dbDialect = new MssqlDbDialect();
                } else if (dictionary instanceof OracleDictionary) {
                    dbDialect = new OracleDbDialect();
                } else {
                    throw new UnsupportedOperationException("Unsupported DBDictionary class: " + dictionary.getClass());
                }
            } else {
                throw new UnsupportedOperationException("Unsupported OpenJPAConfiguration class: " + configuration.getClass());
            }
        }
        return dbDialect;
    }

    @Override
    public DbTypeConverter getDbTypeConverter() {
        if (dbTypeConverter == null) {
            switch (DbmsType.getCurrent()) {
                case HSQL:
                    dbTypeConverter = new HSQLTypeConverter();
                    break;
                case POSTGRES:
                    dbTypeConverter = new PostgresTypeConverter();
                    break;
                case MSSQL:
                    dbTypeConverter = new MssqlTypeConverter();
                    break;
                case ORACLE:
                    dbTypeConverter = new OracleTypeConverter();
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported DBMS type: " + DbmsType.getCurrent());
            }
        }
        return dbTypeConverter;
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

        OpenJPAEntityManager jpaEm = (OpenJPAEntityManager)
                EntityManagerFactoryUtils.doGetTransactionalEntityManager(jpaEmf, null);

        UserSession userSession = userSessionSource.checkCurrentUserSession() ? userSessionSource.getUserSession() : null;
        EntityManagerImpl impl = new EntityManagerImpl(jpaEm, userSession, metadata, fetchPlanMgr);
        EntityManagerContext ctx = contextHolder.get();
        if (ctx != null) {
            impl.setSoftDeletion(ctx.isSoftDeletion());
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
        return (DataSource) AppBeans.get("dataSource");
    }

    @Override
    @Nonnull
    public EntityManagerContext getEntityManagerContext() {
        EntityManagerContext emCtx = contextHolder.get();
        if (emCtx == null)
            emCtx = new EntityManagerContext();
        return emCtx;
    }

    public TransactionSynchronization createSynchronization() {
        return new EntityManagerContextSynchronization();
    }

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
}
