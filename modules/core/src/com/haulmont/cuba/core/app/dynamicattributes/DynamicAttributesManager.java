/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
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
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.lang.StringUtils;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(DynamicAttributesManagerAPI.NAME)
public class DynamicAttributesManager implements DynamicAttributesManagerAPI {
    @Inject
    protected Metadata metadata;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Persistence persistence;

    private ClusterManagerAPI clusterManager;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @GuardedBy("lock")
    protected Multimap<MetaClass, Category> categoriesCache;

    @GuardedBy("lock")
    protected Map<MetaClass, Map<String, CategoryAttribute>> attributesCache;

    private static class ReloadCacheMsg implements Serializable {
        private static final long serialVersionUID = -3116358584797500962L;
    }

    @Inject
    public void setClusterManager(ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        clusterManager.addListener(ReloadCacheMsg.class, new ClusterListenerAdapter<ReloadCacheMsg>() {
            @Override
            public void receive(ReloadCacheMsg message) {
                loadCache();
            }
        });
    }

    @Override
    public void loadCache() {
        lock.writeLock().lock();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager entityManager = persistence.getEntityManager();
            TypedQuery<Category> query = entityManager.createQuery("select c from sys$Category c", Category.class);
            View categoryView = new View(viewRepository.getView(Category.class, View.LOCAL), null, false)
                    .addProperty("categoryAttrs", viewRepository.getView(CategoryAttribute.class, View.LOCAL));
            query.setView(categoryView);
            List<Category> resultList = query.getResultList();

            Multimap<MetaClass, Category> categoriesCache = HashMultimap.create();
            Map<MetaClass, Map<String, CategoryAttribute>> attributesCache = new HashMap<>();

            for (Category category : resultList) {
                MetaClass metaClass = resolveTargetMetaClass(metadata.getSession().getClass(category.getEntityType()));
                categoriesCache.put(metaClass, category);
                Map<String, CategoryAttribute> attributes = attributesCache.get(metaClass);
                if (attributes == null) {
                    attributes = new HashMap<>();
                    attributesCache.put(metaClass, attributes);
                }

                for (CategoryAttribute categoryAttribute : category.getCategoryAttrs()) {
                    attributes.put(categoryAttribute.getCode(), categoryAttribute);
                }
            }
            tx.commit();

            this.categoriesCache = categoriesCache;
            this.attributesCache = attributesCache;
        } finally {
            lock.writeLock().unlock();
            tx.end();
            clusterManager.send(new ReloadCacheMsg());
        }
    }

    @Override
    public Collection<Category> getCategoriesForMetaClass(@Nullable MetaClass metaClass) {
        MetaClass targetMetaClass = resolveTargetMetaClass(metaClass);
        return new ArrayList<>(categories().get(targetMetaClass));
    }

    @Override
    public Collection<CategoryAttribute> getAttributesForMetaClass(@Nullable MetaClass metaClass) {
        MetaClass targetMetaClass = resolveTargetMetaClass(metaClass);
        Collection<Category> categories = categories().get(targetMetaClass);
        List<CategoryAttribute> categoryAttributes = new ArrayList<>();
        for (Category category : categories) {
            categoryAttributes.addAll(Collections2.filter(category.getCategoryAttrs(), new Predicate<CategoryAttribute>() {
                @Override
                public boolean apply(@Nullable CategoryAttribute input) {
                    return input != null && StringUtils.isNotBlank(input.getCode());
                }
            }));
        }
        return categoryAttributes;
    }

    @Nullable
    @Override
    public CategoryAttribute getAttributeForMetaClass(@Nullable MetaClass metaClass, String code) {
        MetaClass targetMetaClass = resolveTargetMetaClass(metaClass);
        Map<String, CategoryAttribute> attributes = attributes().get(targetMetaClass);
        if (attributes != null) {
            return attributes.get(code);
        }

        return null;
    }

    protected Multimap<MetaClass, Category> categories() {
        this.lock.readLock().lock();
        try {
            if (this.categoriesCache == null) {
                try {
                    this.lock.readLock().unlock();
                    loadCache();
                } finally {
                    this.lock.readLock().lock();
                }
            }
            return this.categoriesCache;
        } finally {
            lock.readLock().unlock();
        }
    }

    protected Map<MetaClass, Map<String, CategoryAttribute>> attributes() {
        this.lock.readLock().lock();
        try {
            if (this.attributesCache == null) {
                try {
                    this.lock.readLock().unlock();
                    loadCache();
                } finally {
                    this.lock.readLock().lock();
                }
            }
            return this.attributesCache;
        } finally {
            lock.readLock().unlock();
        }
    }

    protected MetaClass resolveTargetMetaClass(@Nullable MetaClass metaClass) {
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
