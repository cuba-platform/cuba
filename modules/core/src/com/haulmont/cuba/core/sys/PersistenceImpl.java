/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.persistence.EntityLifecycleListener;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.jdbc.sql.DBDictionary;
import org.apache.openjpa.jdbc.sql.HSQLDictionary;
import org.apache.openjpa.jdbc.sql.PostgresDictionary;
import org.apache.openjpa.jdbc.sql.SQLServerDictionary;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.StateManagerImpl;
import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.FieldMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactory;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.util.ObjectId;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.ManagedBean;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Persistence.NAME)
public class PersistenceImpl implements Persistence {

    private DbDialect dbDialect;

    private volatile boolean softDeletion = true;

    private ThreadLocal<EntityManagerContext> contextHolder = new ThreadLocal<EntityManagerContext>();

    @Inject
    private OpenJPAEntityManagerFactory jpaEmf;

    @Inject
    private PlatformTransactionManager transactionManager;

    @Inject
    private UserSessionSource userSessionSource;

    @Inject
    private EntityLifecycleListener entityLifecycleListener;

    @Inject
    public void setFactory(OpenJPAEntityManagerFactory jpaEmf, EntityLifecycleListener entityLifecycleListener) {
        this.jpaEmf = jpaEmf;
        ((OpenJPAEntityManagerFactorySPI) jpaEmf).addLifecycleListener(entityLifecycleListener, null);
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
    public Transaction createTransaction() {
        return new TransactionImpl(transactionManager, this, false);
    }

    @Override
    public Transaction getTransaction() {
        return new TransactionImpl(transactionManager, this, true);
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
        EntityManagerImpl impl = new EntityManagerImpl(jpaEm, userSession);
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
    public Set<String> getDirtyFields(BaseEntity entity) {
        if (!(entity instanceof PersistenceCapable))
            return Collections.emptySet();

        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (stateManager == null)
            return Collections.emptySet();

        Set<String> set = new HashSet<String>();
        BitSet dirtySet = stateManager.getDirty();
        for (int i = 0; i < dirtySet.size()-1; i++) {
            if (dirtySet.get(i)) {
                FieldMetaData field = stateManager.getMetaData().getField(i);
                set.add(field.getName());
            }
        }
        return set;
    }

    @Override
    public UUID getReferenceId(Object entity, String property) {
        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (!(stateManager instanceof StateManagerImpl))
            throw new IllegalStateException("Entity must be in managed state");

        ClassMetaData metaData = stateManager.getMetaData();
        int index = metaData.getField(property).getIndex();

        UUID id;
        BitSet loaded = stateManager.getLoaded();
        if (loaded.get(index)) {
            Object reference = ((Instance) entity).getValue(property);
            if (!(reference instanceof Instance))
                throw new IllegalArgumentException("Property " + property + " is not a reference");
            id = ((Instance) reference).getUuid();
        } else {
            Object implData = stateManager.getIntermediate(index);
            if (implData == null)
                return null;
            if (!(implData instanceof ObjectId))
                throw new IllegalArgumentException("Property " + property + " is not a reference");
            ObjectId objectId = (ObjectId) implData;
            id = (UUID) objectId.getId();
        }
        return id;
    }

    @Override
    public boolean isLoaded(Object entity, String property) {
        if (entity instanceof PersistenceCapable) {
            final PersistenceCapable persistenceCapable = (PersistenceCapable) entity;
            final OpenJPAStateManager stateManager = (OpenJPAStateManager) persistenceCapable.pcGetStateManager();

            if (!(stateManager instanceof StateManagerImpl))
                throw new IllegalStateException("Entity must be in managed state");

            final BitSet loaded = stateManager.getLoaded();
            final ClassMetaData metaData = stateManager.getMetaData();

            final FieldMetaData fieldMetaData = metaData.getField(property);
            if (fieldMetaData == null) throw new IllegalStateException();

            final int index = fieldMetaData.getIndex();

            return loaded.get(index);
        } else {
            return true;
        }
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
