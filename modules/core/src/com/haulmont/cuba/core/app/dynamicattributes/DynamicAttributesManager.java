/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(DynamicAttributesManagerAPI.NAME)
public class DynamicAttributesManager implements DynamicAttributesManagerAPI {
    Log log = LogFactory.getLog(DynamicAttributesManager.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Persistence persistence;

    protected ClusterManagerAPI clusterManager;

    protected ReentrantLock loadCacheLock = new ReentrantLock();

    protected volatile DynamicAttributesCache dynamicAttributesCache;

    private static class ReloadCacheMsg implements Serializable {
        private static final long serialVersionUID = -3116358584797500962L;
    }

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        clusterManager.addListener(ReloadCacheMsg.class, new ClusterListenerAdapter<ReloadCacheMsg>() {
            @Override
            public void receive(ReloadCacheMsg message) {
                doLoadCache(false, false);
            }
        });
    }

    @Override
    public void loadCache() {
        doLoadCache(true, false);
    }

    protected void doLoadCache(boolean sendClusterMessage, boolean stopIfNotNull) {
        loadCacheLock.lock();
        Transaction tx = persistence.createTransaction();
        try {
            if (stopIfNotNull && dynamicAttributesCache != null) {
                return;
            }

            EntityManager entityManager = persistence.getEntityManager();
            TypedQuery<Category> query = entityManager.createQuery("select c from sys$Category c", Category.class);
            query.setViewName("for.cache");
            List<Category> resultList = query.getResultList();

            Multimap<String, Category> categoriesCache = HashMultimap.create();
            Map<String, Map<String, CategoryAttribute>> attributesCache = new HashMap<>();

            for (Category category : resultList) {
                MetaClass metaClass = resolveTargetMetaClass(metadata.getSession().getClass(category.getEntityType()));
                if (metaClass != null) {
                    categoriesCache.put(metaClass.getName(), category);
                    Map<String, CategoryAttribute> attributes = attributesCache.get(metaClass.getName());
                    if (attributes == null) {
                        attributes = new HashMap<>();
                        attributesCache.put(metaClass.getName(), attributes);
                    }

                    for (CategoryAttribute categoryAttribute : category.getCategoryAttrs()) {
                        attributes.put(categoryAttribute.getCode(), categoryAttribute);
                    }
                } else {
                    log.warn(String.format("Could not resolve meta class name [%s] for the category [%s].",
                            category.getEntityType(), category.getName()));
                }
            }
            tx.commit();

            dynamicAttributesCache = new DynamicAttributesCache(categoriesCache, attributesCache, timeSource.currentTimestamp());
            if (sendClusterMessage) {
                clusterManager.send(new ReloadCacheMsg());
            }
        } finally {
            loadCacheLock.unlock();
            tx.end();
        }
    }

    @Override
    public Collection<Category> getCategoriesForMetaClass(MetaClass metaClass) {
        return cache().getCategoriesForMetaClass(metaClass);
    }

    @Override
    public Collection<CategoryAttribute> getAttributesForMetaClass(MetaClass metaClass) {
        return cache().getAttributesForMetaClass(metaClass);
    }

    @Nullable
    @Override
    public CategoryAttribute getAttributeForMetaClass(MetaClass metaClass, String code) {
        return cache().getAttributeForMetaClass(metaClass, code);
    }

    @Override
    public DynamicAttributesCache getCacheIfNewer(Date clientCacheDate) {
        if (clientCacheDate == null
                || this.dynamicAttributesCache == null
                || this.dynamicAttributesCache.getCreationDate() == null
                || clientCacheDate.before(this.dynamicAttributesCache.getCreationDate())) {
            return cache();
        } else {
            return null;
        }
    }

    protected DynamicAttributesCache cache() {
        if (this.dynamicAttributesCache == null) {
            doLoadCache(true, true);
        }
        return this.dynamicAttributesCache;
    }

    @Nullable
    protected MetaClass resolveTargetMetaClass(MetaClass metaClass) {
        if (metaClass == null) {
            return null;
        }

        MetaClass targetMetaClass = metadata.getExtendedEntities().getOriginalMetaClass(metaClass);
        if (targetMetaClass == null) {
            targetMetaClass = metaClass;
        }
        return targetMetaClass;
    }
}
