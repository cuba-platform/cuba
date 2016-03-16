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

package com.haulmont.cuba.core.app.dynamicattributes;

import com.google.common.base.Function;
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
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 */
@Component(DynamicAttributesManagerAPI.NAME)
public class DynamicAttributesManager implements DynamicAttributesManagerAPI {
    public static final int MAX_ENTITIES_FOR_ATTRIBUTE_VALUES_BATCH = 100;

    protected Logger log = LoggerFactory.getLogger(DynamicAttributesManager.class);

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
            Map<String, Map<String, CategoryAttribute>> attributesCache = new LinkedHashMap<>();

            for (Category category : resultList) {
                MetaClass metaClass = resolveTargetMetaClass(metadata.getSession().getClass(category.getEntityType()));
                if (metaClass != null) {
                    categoriesCache.put(metaClass.getName(), category);
                    Map<String, CategoryAttribute> attributes = attributesCache.get(metaClass.getName());
                    if (attributes == null) {
                        attributes = new LinkedHashMap<>();
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

    public void storeDynamicAttributes(BaseGenericIdEntity entity) {
        if (persistence.isInTransaction()) {
            doStoreDynamicAttributes(entity);
        } else {
            Transaction tx = persistence.createTransaction();
            try {
                doStoreDynamicAttributes(entity);
                tx.commit();
            } finally {
                tx.end();
            }
        }
    }

    public <E extends BaseGenericIdEntity> void fetchDynamicAttributes(List<E> entities) {
        if (persistence.isInTransaction()) {
            doFetchDynamicAttributes(entities);
        } else {
            Transaction tx = persistence.createTransaction();
            try {
                doFetchDynamicAttributes(entities);
                tx.commit();
            } finally {
                tx.end();
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void doStoreDynamicAttributes(BaseGenericIdEntity entity) {
        final EntityManager em = persistence.getEntityManager();
        Map<String, CategoryAttributeValue> dynamicAttributes = entity.getDynamicAttributes();
        if (dynamicAttributes != null) {
            Map<String, CategoryAttributeValue> mergedDynamicAttributes = new HashMap<>();
            for (Map.Entry<String, CategoryAttributeValue> entry : dynamicAttributes.entrySet()) {
                CategoryAttributeValue categoryAttributeValue = entry.getValue();
                if (categoryAttributeValue.getCategoryAttribute() == null
                        && categoryAttributeValue.getCode() != null) {
                    CategoryAttribute attribute =
                            getAttributeForMetaClass(entity.getMetaClass(), categoryAttributeValue.getCode());
                    categoryAttributeValue.setCategoryAttribute(attribute);
                }

                //remove deleted and empty attributes
                if (categoryAttributeValue.getDeleteTs() == null && categoryAttributeValue.getValue() != null) {
                    CategoryAttributeValue mergedCategoryAttributeValue = em.merge(categoryAttributeValue);
                    mergedCategoryAttributeValue.setCategoryAttribute(categoryAttributeValue.getCategoryAttribute());
                    mergedDynamicAttributes.put(entry.getKey(), mergedCategoryAttributeValue);
                } else {
                    em.remove(categoryAttributeValue);
                }
            }

            entity.setDynamicAttributes(mergedDynamicAttributes);
        }
    }

    protected <E extends BaseGenericIdEntity> void doFetchDynamicAttributes(List<E> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            Collection<UUID> ids = Collections2.transform(entities, new Function<Entity, UUID>() {
                @Nullable
                @Override
                public UUID apply(@Nullable Entity input) {
                    return input != null ? input.getUuid() : null;
                }
            });

            Multimap<UUID, CategoryAttributeValue> attributeValuesForEntity = HashMultimap.create();

            List<UUID> currentIds = new ArrayList<>();
            for (UUID id : ids) {
                currentIds.add(id);
                if (currentIds.size() >= MAX_ENTITIES_FOR_ATTRIBUTE_VALUES_BATCH) {
                    handleAttributeValuesForIds(currentIds, attributeValuesForEntity);
                    currentIds = new ArrayList<>();
                }
            }
            handleAttributeValuesForIds(currentIds, attributeValuesForEntity);

            for (BaseGenericIdEntity entity : entities) {
                Collection<CategoryAttributeValue> theEntityAttributeValues = attributeValuesForEntity.get(entity.getUuid());
                Map<String, CategoryAttributeValue> map = new HashMap<>();
                entity.setDynamicAttributes(map);
                if (CollectionUtils.isNotEmpty(theEntityAttributeValues)) {
                    for (CategoryAttributeValue categoryAttributeValue : theEntityAttributeValues) {
                        CategoryAttribute attribute = categoryAttributeValue.getCategoryAttribute();
                        if (attribute != null) {
                            map.put(attribute.getCode(), categoryAttributeValue);
                        }
                    }
                }
            }
        }
    }

    protected void handleAttributeValuesForIds(List<UUID> currentIds, Multimap<UUID, CategoryAttributeValue> attributeValuesForEntity) {
        if (CollectionUtils.isNotEmpty(currentIds)) {
            List<CategoryAttributeValue> allAttributeValues = loadAttributeValues(currentIds);
            for (CategoryAttributeValue categoryAttributeValue : allAttributeValues) {
                attributeValuesForEntity.put(categoryAttributeValue.getEntityId(), categoryAttributeValue);
            }
        }
    }

    protected List<CategoryAttributeValue> loadAttributeValues(List<UUID> entityIds) {
        final EntityManager em = persistence.getEntityManager();
        View baseAttributeValueView = viewRepository.getView(CategoryAttributeValue.class, View.LOCAL);
        View baseAttributeView = viewRepository.getView(CategoryAttribute.class, View.LOCAL);

        View view = new View(baseAttributeValueView, null, false)
                .addProperty("categoryAttribute", new View(baseAttributeView, null, false).addProperty("category"));

        return em.createQuery("select cav from sys$CategoryAttributeValue cav where cav.entityId in :ids", CategoryAttributeValue.class)
                .setParameter("ids", entityIds)
                .setView(view)
                .getResultList();
    }
}
