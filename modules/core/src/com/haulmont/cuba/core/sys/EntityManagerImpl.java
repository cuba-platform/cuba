/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:56:32
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.core.app.DataCacheMBean;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.lang.BooleanUtils;
import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.jdbc.conf.JDBCConfiguration;
import org.apache.openjpa.kernel.Broker;
import org.apache.openjpa.kernel.OpCallbacks;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.persistence.AutoDetachType;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAEntityManagerFactorySPI;
import org.apache.openjpa.persistence.StoreCache;

import java.util.*;
import java.sql.Connection;

public class EntityManagerImpl implements EntityManager
{
    public interface CloseListener
    {
        void onClose();
    }

    private OpenJPAEntityManager delegate;

    private boolean closed;
    private Set<CloseListener> closeListeners = new HashSet<CloseListener>();

    private boolean deleteDeferred = true;

    private OpCallbacksImpl dummyOpCallbacks = new OpCallbacksImpl();

    private static Boolean storeCacheEnabled;

    private List<Entity> entitiesToEvict;

    EntityManagerImpl(OpenJPAEntityManager jpaEntityManager) {
        delegate = jpaEntityManager;
        // Set AutoDetachType to none to prevent automatic detach after transaction rollback 
        delegate.setAutoDetach(EnumSet.noneOf(AutoDetachType.class));
    }

    private static boolean isStoreCacheEnabled() {
        if (storeCacheEnabled == null) {
            DataCacheMBean bean = Locator.lookupMBean(DataCacheMBean.class, DataCacheMBean.OBJECT_NAME);
            storeCacheEnabled = bean.isStoreCacheEnabled();
        }
        return storeCacheEnabled;
    }

    public OpenJPAEntityManager getDelegate() {
        return delegate;
    }

    public boolean isSoftDeletion() {
        return deleteDeferred;
    }

    public void setSoftDeletion(boolean deleteDeferred) {
        if (deleteDeferred != this.deleteDeferred) {
            // clear SQL queries cache
            OpenJPAConfiguration conf = ((OpenJPAEntityManagerFactorySPI) delegate.getEntityManagerFactory()).getConfiguration();
            if (conf instanceof JDBCConfiguration) {
                Map map = ((JDBCConfiguration) conf).getQuerySQLCacheInstance();
                for (Object val : map.values()) {
                    if (val instanceof Map)
                        ((Map) val).clear();
                }
            }
            this.deleteDeferred = deleteDeferred;
        }
    }

    public void persist(Entity entity) {
        delegate.persist(entity);
    }

    public <T extends Entity> T merge(T entity) {
        if (entity instanceof PersistenceCapable
                && BooleanUtils.isFalse(((PersistenceCapable) entity).pcIsDetached()))
            return entity;
        else
            return delegate.merge(entity);
    }

    public void remove(Entity entity) {
        if (entity instanceof SoftDelete && deleteDeferred) {
            ((SoftDelete) entity).setDeleteTs(TimeProvider.currentTimestamp());
            ((SoftDelete) entity).setDeletedBy(SecurityProvider.currentUserSession().getUser().getLogin());
        }
        else {
            delegate.remove(entity);
        }
    }

    public <T extends Entity> T find(Class<T> clazz, Object key) {
        T entity = delegate.find(clazz, key);
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted() && deleteDeferred)
            return null;
        else
            return entity;
    }

    public <T extends Entity> T getReference(Class<T> clazz, Object key) {
        return delegate.getReference(clazz, key);
    }

    public Query createQuery() {
        return new QueryImpl(this, false);
    }

    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(this, false);
        query.setQueryString(qlStr);
        return query;
    }

    public Query createNativeQuery() {
        return new QueryImpl(this, true);
    }

    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(this, true);
        query.setQueryString(sql);
        return query;
    }

    public void setView(View view) {
        ViewHelper.setView(delegate.getFetchPlan(), view);
    }

    public void flush() {
        delegate.flush();
    }

    public void close() {
        if (isStoreCacheEnabled()) {
            // Evict from L2 cache all updated entities
            StoreCache storeCache = delegate.getEntityManagerFactory().getStoreCache();
            if (storeCache != null && entitiesToEvict != null) {
                for (Entity entity : entitiesToEvict) {
                    storeCache.evict(entity.getClass(), entity.getId());
                }
            }
            entitiesToEvict = null;
        }

        delegate.close();
        closed = true;
        for (CloseListener listener : closeListeners) {
            listener.onClose();
        }
    }

    public boolean isClosed() {
        return closed;
    }

    public void addCloseListener(CloseListener listener) {
        closeListeners.add(listener);
    }

    public void removeCloseListener(CloseListener listener) {
        closeListeners.remove(listener);
    }

    public Collection getManagedObjects() {
        return delegate.getManagedObjects();
    }

    public Connection getConnection() {
        return (Connection) delegate.getConnection();
    }

    public void detachAll() {
        Broker broker = ((org.apache.openjpa.persistence.EntityManagerImpl) delegate).getBroker();

        if (isStoreCacheEnabled()) {
            // When using L2 cache we have to evict all updated entities, because they are being put
            // into cache after detaching, with all fields = null.
            // This is because DataCacheStoreManager remembers StateManagers before detaching, but
            // commits states into cache after detaching.

            // For new entities go standard way
            broker.setPopulateDataCache(false);

            // Save updated entities to evict them on close() 
            entitiesToEvict = new ArrayList<Entity>();
            for (Object obj : broker.getManagedObjects()) {
                PersistenceCapable pc = (PersistenceCapable) obj;
                if (!pc.pcIsNew() && pc.pcIsDirty() && pc instanceof Entity) {
                    entitiesToEvict.add((Entity) pc);
                }
            }
        }

        broker.detachAll(dummyOpCallbacks);
    }

    private static class OpCallbacksImpl implements OpCallbacks
    {
        public int processArgument(int op, Object arg, OpenJPAStateManager sm) {
            return ACT_RUN | ACT_CASCADE;
        }
    }
}
