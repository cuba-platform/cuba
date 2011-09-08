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
import com.haulmont.cuba.core.app.DataCacheAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.StoreCache;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityManagerImpl implements EntityManager
{
    public interface CloseListener
    {
        void onClose();
    }

    private OpenJPAEntityManager delegate;

    private UserSession userSession;

    private QueryMacroHandler[] queryMacroHandlers;

    private boolean closed;
    private Set<CloseListener> closeListeners = new HashSet<CloseListener>();

    private boolean softDeletion = true;

    private static Boolean storeCacheEnabled;

    private List<Entity> entitiesToEvict;

    EntityManagerImpl(OpenJPAEntityManager jpaEntityManager, UserSession userSession, QueryMacroHandler[] queryMacroHandlers) {
        this.delegate = jpaEntityManager;
        this.userSession = userSession;
        this.queryMacroHandlers = queryMacroHandlers;
    }

    private static boolean isStoreCacheEnabled() {
        if (storeCacheEnabled == null) {
            DataCacheAPI bean = Locator.lookup(DataCacheAPI.NAME);
            storeCacheEnabled = bean.isStoreCacheEnabled();
        }
        return storeCacheEnabled;
    }

    public OpenJPAEntityManager getDelegate() {
        return delegate;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
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
        if (entity instanceof SoftDelete && softDeletion) {
            ((SoftDelete) entity).setDeleteTs(TimeProvider.currentTimestamp());
            ((SoftDelete) entity).setDeletedBy(userSession != null ? userSession.getUser().getLogin() : "<unknown>");
        }
        else {
            delegate.remove(entity);
        }
    }

    public <T extends Entity> T find(Class<T> clazz, Object key) {
        T entity = delegate.find(clazz, key);
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted() && softDeletion)
            return null;
        else
            return entity;
    }

    public <T extends Entity> T getReference(Class<T> clazz, Object key) {
        return delegate.getReference(clazz, key);
    }

    public Query createQuery() {
        return new QueryImpl(this, false, queryMacroHandlers);
    }

    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(this, false, queryMacroHandlers);
        query.setQueryString(qlStr);
        return query;
    }

    public Query createNativeQuery() {
        return new QueryImpl(this, true, queryMacroHandlers);
    }

    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(this, true, queryMacroHandlers);
        query.setQueryString(sql);
        return query;
    }

    public void setView(View view) {
        ViewHelper.setView(delegate.getFetchPlan(), view);
    }

    public void addView(View view) {
        ViewHelper.addView(delegate.getFetchPlan(), view);
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
}
