/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.TypedQuery;
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

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityManagerImpl implements EntityManager {

    private OpenJPAEntityManager delegate;

    private UserSession userSession;
    private Metadata metadata;
    private FetchPlanManager fetchPlanMgr;

    private boolean softDeletion = true;

    private List<View> views = new ArrayList<>(1);

    private Log log = LogFactory.getLog(getClass());

    EntityManagerImpl(OpenJPAEntityManager jpaEntityManager, UserSession userSession, Metadata metadata,
                      FetchPlanManager fetchPlanMgr) {
        this.delegate = jpaEntityManager;
        this.userSession = userSession;
        this.metadata = metadata;
        this.fetchPlanMgr = fetchPlanMgr;
    }

    @Override
    public OpenJPAEntityManager getDelegate() {
        return delegate;
    }

    @Override
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    @Override
    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    @Override
    public void persist(Entity entity) {
        delegate.persist(entity);
    }

    @Override
    public <T extends Entity> T merge(T entity) {
        // Don't use PersistenceHelper.isDetached here, as we have to merge not-detached instances too.
        if (entity instanceof PersistenceCapable
                && BooleanUtils.isFalse(((PersistenceCapable) entity).pcIsDetached()))
            return entity;
        else
            return delegate.merge(entity);
    }

    @Override
    public void remove(Entity entity) {
        if (PersistenceHelper.isDetached(entity)) {
            entity = delegate.merge(entity);
        }
        if (entity instanceof SoftDelete && softDeletion) {
            TimeSource timeSource = AppBeans.get(TimeSource.NAME);
            ((SoftDelete) entity).setDeleteTs(timeSource.currentTimestamp());
            ((SoftDelete) entity).setDeletedBy(userSession != null ? userSession.getUser().getLogin() : "<unknown>");
        } else {
            delegate.remove(entity);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T find(Class<T> clazz, Object key) {
        Class<T> effectiveClass = metadata.getExtendedEntities().getEffectiveClass(clazz);

        T entity = delegate.find(effectiveClass, key);
        if (entity instanceof SoftDelete && ((SoftDelete) entity).isDeleted() && softDeletion)
            return null;
        else
            return entity;
    }

    @Nullable
    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object primaryKey, View... views) {
        setFetchPlan(Arrays.asList(views));
        try {
            return find(entityClass, primaryKey);
        } finally {
            setFetchPlan(this.views);
        }
    }

    @Nullable
    @Override
    public <T extends Entity> T find(Class<T> entityClass, Object primaryKey, String... viewNames) {
        View[] viewArray = new View[viewNames.length];
        for (int i = 0; i < viewNames.length; i++) {
            viewArray[i] = metadata.getViewRepository().getView(entityClass, viewNames[i]);
        }
        return find(entityClass, primaryKey, viewArray);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity> T getReference(Class<T> clazz, Object key) {
        Class<T> effectiveClass = metadata.getExtendedEntities().getEffectiveClass(clazz);

        return delegate.getReference(effectiveClass, key);
    }

    @Override
    public Query createQuery() {
        return new QueryImpl(this, false, null, metadata, fetchPlanMgr);
    }

    @Override
    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(this, false, null, metadata, fetchPlanMgr);
        query.setQueryString(qlStr);
        return query;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        QueryImpl<T> query = new QueryImpl<>(this, false, resultClass, metadata, fetchPlanMgr);
        query.setQueryString(qlString);
        return query;
    }

    @Override
    public Query createNativeQuery() {
        return new QueryImpl(this, true, null, metadata, fetchPlanMgr);
    }

    @Override
    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(this, true, null, metadata, fetchPlanMgr);
        query.setQueryString(sql);
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypedQuery<T> createNativeQuery(String sql, Class<T> resultClass) {
        QueryImpl query = new QueryImpl(this, true, resultClass, metadata, fetchPlanMgr);
        query.setQueryString(sql);
        return query;
    }

    @Override
    public void setView(View view) {
        fetchPlanMgr.setView(delegate.getFetchPlan(), view);
        views.clear();
        views.add(view);
    }

    @Override
    public void addView(View view) {
        fetchPlanMgr.addView(delegate.getFetchPlan(), view);
        views.add(view);
    }

    @Override
    public void fetch(Entity entity, View view) {
        Preconditions.checkNotNullArgument(view, "View is null");
        Preconditions.checkNotNullArgument(entity, "Entity instance is null");
        if (!PersistenceHelper.isManaged(entity))
            throw new IllegalArgumentException("Can not fetch detached entity. Merge first.");

        // Set default fetch plan
        fetchPlanMgr.setView(delegate.getFetchPlan(), null);
        try {
            fetchInstance(entity, view, new HashMap<Instance, Set<View>>());
        } finally {
            // Restore fetch plan
            setFetchPlan(this.views);
        }
    }

    @Nullable
    @Override
    public <T extends Entity> T reload(Class<T> entityClass, Object id, String... viewNames) {
        Preconditions.checkNotNullArgument(entityClass, "entityClass is null");
        Preconditions.checkNotNullArgument(id, "id is null");

        T entity = find(entityClass, id, viewNames);
        if (entity != null) {
            for (String viewName : viewNames) {
                View view = metadata.getViewRepository().getView(entityClass, viewName);
                if (view.hasLazyProperties()) {
                    fetch(entity, view);
                }
            }
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends Entity> T reload(T entity, String... viewNames) {
        Preconditions.checkNotNullArgument(entity, "entity is null");

        Entity resultEntity = reload(entity.getClass(), entity.getId(), viewNames);
        return (T) resultEntity;
    }

    private void setFetchPlan(List<View> views) {
        if (views == null || views.isEmpty()) {
            fetchPlanMgr.setView(delegate.getFetchPlan(), null);
        } else {
            fetchPlanMgr.setView(delegate.getFetchPlan(), views.get(0));
            for (int i = 1; i < views.size(); i++) {
                fetchPlanMgr.addView(delegate.getFetchPlan(), views.get(i));
            }
        }
    }

    private void fetchInstance(Instance instance, View view, Map<Instance, Set<View>> visited) {
        Set<View> views = visited.get(instance);
        if (views == null) {
            views = new HashSet<>();
            visited.put(instance, views);
        } else if (views.contains(view)) {
            return;
        }
        views.add(view);

        if (log.isTraceEnabled()) log.trace("Fetching instance " + instance);
        for (ViewProperty property : view.getProperties()) {
            if (log.isTraceEnabled()) log.trace("Fetching property " + property.getName());

            View propertyView = property.getView();

            Object value;
            if (!property.isLazy() || propertyView == null) {
                value = instance.getValue(property.getName());
            } else {
                if (log.isTraceEnabled()) log.trace("Use property view for lazy load " + propertyView.toString());
                fetchPlanMgr.setView(delegate.getFetchPlan(), propertyView);

                try {
                    value = instance.getValue(property.getName());
                } finally {
                    // Restore fetch plan
                    setFetchPlan(this.views);
                }
            }

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

    @Override
    public void flush() {
        delegate.flush();
    }

    public Collection getManagedObjects() {
        return delegate.getManagedObjects();
    }

    @Override
    public Connection getConnection() {
        return (Connection) delegate.getConnection();
    }
}