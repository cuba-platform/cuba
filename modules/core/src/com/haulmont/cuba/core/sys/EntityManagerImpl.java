/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.core.sys.listener.EntityListenerType;
import com.haulmont.cuba.core.sys.persistence.PersistenceImplSupport;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityManagerImpl implements EntityManager {

    private javax.persistence.EntityManager delegate;

    private UserSession userSession;
    private Metadata metadata;
    private FetchGroupManager fetchGroupMgr;
    private EntityListenerManager entityListenerMgr;
    private PersistenceImplSupport support;

    private boolean softDeletion = true;

    private Logger log = LoggerFactory.getLogger(EntityManagerImpl.class);

    EntityManagerImpl(javax.persistence.EntityManager jpaEntityManager, UserSession userSession, Metadata metadata,
                      FetchGroupManager fetchGroupMgr, EntityListenerManager entityListenerMgr,
                      PersistenceImplSupport support) {
        this.delegate = jpaEntityManager;
        this.userSession = userSession;
        this.metadata = metadata;
        this.fetchGroupMgr = fetchGroupMgr;
        this.entityListenerMgr = entityListenerMgr;
        this.support = support;
    }

    @Override
    public javax.persistence.EntityManager getDelegate() {
        return delegate;
    }

    @Override
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    @Override
    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        delegate.setProperty("disableSoftDelete", softDeletion ? 0 : 1);
    }

    @Override
    public void persist(Entity entity) {
        delegate.persist(entity);
        support.registerInstance(entity, this);
    }

    @Override
    public <T extends Entity> T merge(T entity) {
        if (PersistenceHelper.isManaged(entity))
            return entity;

        if (entity instanceof BaseEntity) {
            entityListenerMgr.fireListener((BaseEntity) entity, EntityListenerType.BEFORE_ATTACH);
        }

        if (PersistenceHelper.isNew(entity)) {
            // if a new instance is passed to merge(), we suppose it is persistent but "not detached"
            Entity destEntity = findOrCreate(entity.getClass(), entity.getId());
            deepCopyIgnoringNulls(entity, destEntity);
            //noinspection unchecked
            return (T) destEntity;
        }

        T merged = delegate.merge(entity);
        support.registerInstance(merged, this);
        return merged;
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
            if (entity instanceof BaseGenericIdEntity)
                ((BaseGenericIdEntity) entity).__removed(true);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity<K>, K> T find(Class<T> entityClass, K key) {
        MetaClass metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(entityClass);
        Class<T> javaClass = metaClass.getJavaClass();

        return delegate.find(javaClass, key);
    }

    @Nullable
    @Override
    public <T extends Entity<K>, K> T find(Class<T> entityClass, K primaryKey, View... views) {
        MetaClass metaClass = metadata.getExtendedEntities().getEffectiveMetaClass(entityClass);
        return findWithViews(metaClass, primaryKey, Arrays.asList(views));
    }

    @Nullable
    @Override
    public <T extends Entity<K>, K> T find(Class<T> entityClass, K primaryKey, String... viewNames) {
        View[] viewArray = new View[viewNames.length];
        for (int i = 0; i < viewNames.length; i++) {
            viewArray[i] = metadata.getViewRepository().getView(entityClass, viewNames[i]);
        }
        return find(entityClass, primaryKey, viewArray);
    }

    private <T extends Entity> T findWithViews(MetaClass metaClass, Object key, List<View> views) {
        Query query = createQuery("select e from " + metaClass.getName() + " e where e.id = ?1");
        query.setParameter(1, key);
        for (View view : views) {
            query.addView(view);
        }
        return (T) query.getFirstResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Entity<K>, K> T getReference(Class<T> clazz, K key) {
        Class<T> effectiveClass = metadata.getExtendedEntities().getEffectiveClass(clazz);

        return delegate.getReference(effectiveClass, key);
    }

    @Override
    public Query createQuery() {
        return new QueryImpl(this, false, null, metadata, fetchGroupMgr);
    }

    @Override
    public Query createQuery(String qlStr) {
        QueryImpl query = new QueryImpl(this, false, null, metadata, fetchGroupMgr);
        query.setQueryString(qlStr);
        return query;
    }

    @Override
    public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
        QueryImpl<T> query = new QueryImpl<>(this, false, resultClass, metadata, fetchGroupMgr);
        query.setQueryString(qlString);
        return query;
    }

    @Override
    public Query createNativeQuery() {
        return new QueryImpl(this, true, null, metadata, fetchGroupMgr);
    }

    @Override
    public Query createNativeQuery(String sql) {
        QueryImpl query = new QueryImpl(this, true, null, metadata, fetchGroupMgr);
        query.setQueryString(sql);
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypedQuery<T> createNativeQuery(String sql, Class<T> resultClass) {
        QueryImpl query = new QueryImpl(this, true, resultClass, metadata, fetchGroupMgr);
        query.setQueryString(sql);
        return query;
    }

    @Override
    @Deprecated
    public void fetch(Entity entity, View view) {
    }

    @Nullable
    @Override
    public <T extends Entity<K>, K> T reload(Class<T> entityClass, K id, String... viewNames) {
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

    @Override
    public void flush() {
        support.fireEntityListeners();
        delegate.flush();
    }

    @Override
    public Connection getConnection() {
        return delegate.unwrap(Connection.class);
    }

    private void deepCopyIgnoringNulls(Entity source, Entity dest) {
        for (MetaProperty srcProperty : source.getMetaClass().getProperties()) {
            String name = srcProperty.getName();
            Object value = source.getValue(name);
            if (value == null)
                continue;

            if (srcProperty.getRange().isClass()) {
                MetadataTools metadataTools = metadata.getTools();

                if (!metadataTools.isOwningSide(srcProperty))
                    continue;

                Class refClass = srcProperty.getRange().asClass().getJavaClass();
                if (!metadataTools.isPersistent(refClass))
                    continue;

                if (srcProperty.getRange().getCardinality().isMany()) {
                    if (!metadataTools.isOwningSide(srcProperty))
                        continue;
                    //noinspection unchecked
                    Collection<Entity> srcCollection = (Collection) value;
                    Collection<Entity> dstCollection = dest.getValue(name);
                    if (dstCollection == null)
                        throw new RuntimeException("Collection is null: " + srcProperty);
                    boolean equal = srcCollection.size() == dstCollection.size();
                    if (equal) {
                        if (srcProperty.getRange().isOrdered()) {
                            equal = Arrays.equals(srcCollection.toArray(), dstCollection.toArray());
                        } else {
                            equal = CollectionUtils.isEqualCollection(srcCollection, dstCollection);
                        }
                    }
                    if (!equal) {
                        dstCollection.clear();
                        for (Entity srcRef : srcCollection) {
                            Entity reloadedRef = findOrCreate(refClass, srcRef.getId());
                            dstCollection.add(reloadedRef);
                            deepCopyIgnoringNulls(srcRef, reloadedRef);
                        }
                    }
                } else {
                    Entity srcRef = (Entity) value;
                    Entity destRef = dest.getValue(name);
                    if (srcRef.equals(destRef)) {
                        deepCopyIgnoringNulls(srcRef, destRef);
                    } else {
                        Entity reloadedRef = findOrCreate(refClass, srcRef.getId());
                        dest.setValue(name, reloadedRef);
                        deepCopyIgnoringNulls(srcRef, reloadedRef);
                    }
                }
            } else {
                dest.setValue(name, value);
            }
        }
    }

    protected <T extends Entity> T findOrCreate(Class<T> entityClass, Object id) {
        Entity reloadedRef = find(entityClass, id);
        if (reloadedRef == null) {
            reloadedRef = metadata.create(entityClass);
            persist(reloadedRef);
        }
        //noinspection unchecked
        return (T) reloadedRef;
    }
}