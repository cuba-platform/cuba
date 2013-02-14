/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 31.10.2008 16:56:32
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.DataCacheAPI;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.StoreCache;

import java.sql.Connection;
import java.util.*;

public class EntityManagerImpl implements EntityManager {

    public interface CloseListener {
        void onClose();
    }

    private OpenJPAEntityManager delegate;

    private UserSession userSession;
    private Metadata metadata;
    private FetchPlanManager fetchPlanMgr;

    private boolean closed;
    private Set<CloseListener> closeListeners = new HashSet<CloseListener>();

    private boolean softDeletion = true;

    private static Boolean storeCacheEnabled;

    private List<Entity> entitiesToEvict;

    private List<View> views = new ArrayList<>(1);

    private Log log = LogFactory.getLog(getClass());

    EntityManagerImpl(OpenJPAEntityManager jpaEntityManager, UserSession userSession, Metadata metadata,
                      FetchPlanManager fetchPlanMgr) {
        this.delegate = jpaEntityManager;
        this.userSession = userSession;
        this.metadata = metadata;
        this.fetchPlanMgr = fetchPlanMgr;
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
        // Don't use PersistenceHelper.isDetached here, as we have to merge not-detached instances too.
        if (entity instanceof PersistenceCapable
                && BooleanUtils.isFalse(((PersistenceCapable) entity).pcIsDetached()))
            return entity;
        else
            return delegate.merge(entity);
    }

    public void remove(Entity entity) {
        if (entity instanceof SoftDelete && softDeletion) {
            if (PersistenceHelper.isDetached(entity)) {
                entity = delegate.merge(entity);
            }
            ((SoftDelete) entity).setDeleteTs(TimeProvider.currentTimestamp());
            ((SoftDelete) entity).setDeletedBy(userSession != null ? userSession.getUser().getLogin() : "<unknown>");
        } else {
            delegate.remove(entity);
        }
    }

    public <T extends Entity> T find(Class<T> clazz, Object key) {
        Class<T> effectiveClass = metadata.getExtendedEntities().getEffectiveClass(clazz);

        T entity = delegate.find(effectiveClass, key);
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted() && softDeletion)
            return null;
        else
            return entity;
    }

    public <T extends Entity> T getReference(Class<T> clazz, Object key) {
        return delegate.getReference(clazz, key);
    }

    public Query createQuery() {
        return new QueryImpl(this, false, null, metadata, fetchPlanMgr);
    }

    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(this, false, null, metadata, fetchPlanMgr);
        query.setQueryString(qlStr);
        return query;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        QueryImpl<T> query = new QueryImpl<T>(this, false, resultClass, metadata, fetchPlanMgr);
        query.setQueryString(qlString);
        return query;
    }

    public Query createNativeQuery() {
        return new QueryImpl(this, true, null, metadata, fetchPlanMgr);
    }

    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(this, true, null, metadata, fetchPlanMgr);
        query.setQueryString(sql);
        return query;
    }

    @Override
    public <T> TypedQuery<T> createNativeQuery(String sql, Class<T> resultClass) {
        QueryImpl query = new QueryImpl(this, true, resultClass, metadata, fetchPlanMgr);
        query.setQueryString(sql);
        return query;
    }

    public void setView(View view) {
        fetchPlanMgr.setView(delegate.getFetchPlan(), view);
        views.clear();
        views.add(view);
    }

    public void addView(View view) {
        fetchPlanMgr.addView(delegate.getFetchPlan(), view);
        views.add(view);
    }

    public void fetch(Entity entity, View view) {
        Objects.requireNonNull(view, "View is null");
        if (PersistenceHelper.isDetached(entity))
            throw new IllegalArgumentException("Can not fetch detached entity. Merge first.");

        // Set default fetch plan
        fetchPlanMgr.setView(delegate.getFetchPlan(), null);
        try {
            fetchInstance(entity, view, new HashMap<Instance, Set<View>>());
        } finally {
            // Restore fetch plan
            for (int i = 0; i < views.size(); i++) {
                if (i == 0)
                    fetchPlanMgr.setView(delegate.getFetchPlan(), views.get(i));
                else
                    fetchPlanMgr.addView(delegate.getFetchPlan(), views.get(i));
            }
        }
    }

    private void fetchInstance(Instance instance, View view, Map<Instance, Set<View>> visited) {
        Set<View> views = visited.get(instance);
        if (views == null) {
            views = new HashSet<View>();
            visited.put(instance, views);
        } else if (views.contains(view)) {
            return;
        }
        views.add(view);

        if (log.isTraceEnabled()) log.trace("Fetching instance " + instance);
        for (ViewProperty property : view.getProperties()) {
            if (log.isTraceEnabled()) log.trace("Fetching property " + property.getName());
            Object value = instance.getValue(property.getName());
            View propertyView = property.getView();
            if (value != null && propertyView != null) {
                if (value instanceof Collection) {
                    for (Object item : ((Collection) value)) {
                        if (item instanceof Instance)
                            fetchInstance((Instance) item, propertyView, visited);
                    }
                } else if (value instanceof Instance) {
                    if (PersistenceHelper.isDetached(value) && !(value instanceof EmbeddableEntity)) {
                        log.trace("Object " + value + " is detached, loading it");
                        Entity entity = (Entity) value;
                        value = find(entity.getClass(), entity.getId());
                        if (value == null) {
                            // the instance is most probably deleted
                            continue;
                        }
                        instance.setValue(property.getName(), value);
                    }
                    fetchInstance((Instance) value, propertyView, visited);
                }
            }
        }
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
