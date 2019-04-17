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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.ClusterListenerAdapter;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component(DynamicAttributesManagerAPI.NAME)
public class DynamicAttributesManager implements DynamicAttributesManagerAPI {
    public static final int MAX_ENTITIES_FOR_ATTRIBUTE_VALUES_BATCH = 100;

    private static final Logger log = LoggerFactory.getLogger(DynamicAttributesManager.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Persistence persistence;

    @Inject
    protected ReferenceToEntitySupport referenceToEntitySupport;

    @Inject
    protected PersistentAttributesLoadChecker persistentAttributesLoadChecker;

    protected ClusterManagerAPI clusterManager;

    protected ReentrantLock loadCacheLock = new ReentrantLock();

    protected volatile DynamicAttributesCache dynamicAttributesCache;

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
        try {
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
                        log.warn(format("Could not resolve meta class name [%s] for the category [%s].",
                                category.getEntityType(), category.getName()));
                    }
                }
                tx.commit();

                dynamicAttributesCache = new DynamicAttributesCache(categoriesCache, attributesCache, timeSource.currentTimestamp());
                if (sendClusterMessage) {
                    clusterManager.send(new ReloadCacheMsg());
                }
            } finally {
                tx.end();
            }
        } finally {
            loadCacheLock.unlock();
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

    @Override
    public void storeDynamicAttributes(BaseGenericIdEntity entity) {
        try (Transaction tx = persistence.getTransaction()) {
            doStoreDynamicAttributes(entity);
            tx.commit();
        }
    }

    @Override
    public <E extends BaseGenericIdEntity> void fetchDynamicAttributes(List<E> entities, @Nonnull Set<Class> dependentClasses) {
        Set<BaseGenericIdEntity> toProcess = new HashSet<>();
        entities.forEach(entity -> {
            toProcess.add(entity);
            if (!dependentClasses.isEmpty()) {
                metadata.getTools().traverseAttributes(entity, new EntityAttributeVisitor() {
                    @Override
                    public void visit(Entity visitedEntity, MetaProperty property) {
                        if (dependentClasses.contains(property.getRange().asClass().getJavaClass()) &&
                                persistentAttributesLoadChecker.isLoaded(visitedEntity, property.getName())) {
                            Object value = visitedEntity.getValue(property.getName());
                            if (value != null) {
                                if (value instanceof Collection) {
                                    for (Object item : ((Collection) value)) {
                                        if (item instanceof BaseGenericIdEntity) {
                                            toProcess.add((BaseGenericIdEntity) item);
                                        }
                                    }
                                } else if (value instanceof BaseGenericIdEntity) {
                                    toProcess.add((BaseGenericIdEntity) value);
                                }
                            }
                        }
                    }

                    @Override
                    public boolean skip(MetaProperty property) {
                        return metadata.getTools().isNotPersistent(property) || !property.getRange().isClass();
                    }
                });
            }
        });
        if (toProcess.isEmpty())
            return;

        try (Transaction tx = persistence.getTransaction()) {
            Multimap<String, BaseGenericIdEntity> entitiesByType = HashMultimap.create();
            toProcess.forEach(e -> entitiesByType.put(e.getMetaClass().getName(), e));
            entitiesByType.keySet().forEach(entityType -> {
                MetaClass metaClass = metadata.getExtendedEntities().getOriginalOrThisMetaClass(metadata.getClass(entityType));
                doFetchDynamicAttributes(metaClass, entitiesByType.get(entityType));
            });
            tx.commit();
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
                    if (entity instanceof BaseDbGeneratedIdEntity && categoryAttributeValue.getObjectEntityId() == null) {
                        categoryAttributeValue.setObjectEntityId(referenceToEntitySupport.getReferenceId(entity));
                    }
                    CategoryAttributeValue mergedCategoryAttributeValue = em.merge(categoryAttributeValue);
                    mergedCategoryAttributeValue.setCategoryAttribute(categoryAttributeValue.getCategoryAttribute());

                    //copy transient fields (for nested CAVs as well)
                    mergedCategoryAttributeValue.setTransientEntityValue(categoryAttributeValue.getTransientEntityValue());
                    mergedCategoryAttributeValue.setTransientCollectionValue(categoryAttributeValue.getTransientCollectionValue());
                    if (BooleanUtils.isTrue(categoryAttributeValue.getCategoryAttribute().getIsCollection()) && categoryAttributeValue.getChildValues() != null) {
                        for (CategoryAttributeValue childCAV : categoryAttributeValue.getChildValues()) {
                            for (CategoryAttributeValue mergedChildCAV : mergedCategoryAttributeValue.getChildValues()) {
                                if (mergedChildCAV.getId().equals(childCAV.getId())) {
                                    mergedChildCAV.setTransientEntityValue(childCAV.getTransientEntityValue());
                                    break;
                                }
                            }

                        }
                    }

                    if (BooleanUtils.isTrue(mergedCategoryAttributeValue.getCategoryAttribute().getIsCollection())) {
                        storeCategoryAttributeValueWithCollectionType(mergedCategoryAttributeValue);
                    }

                    mergedDynamicAttributes.put(entry.getKey(), mergedCategoryAttributeValue);
                } else {
                    em.remove(categoryAttributeValue);
                }
            }

            entity.setDynamicAttributes(mergedDynamicAttributes);
        }
    }

    /**
     * Removes nested {@code CategoryAttributeValue} entities for items that were removed from the collection value
     * and creates new child {@code CategoryAttributeValue} instances for just added collection value items.
     *
     * @param categoryAttributeValue
     */
    protected void storeCategoryAttributeValueWithCollectionType(CategoryAttributeValue categoryAttributeValue) {
        EntityManager em = persistence.getEntityManager();

        List<Object> collectionValue = categoryAttributeValue.getTransientCollectionValue();
        List<Object> newCollectionValue = new ArrayList<>(collectionValue);

        //remove existing child CategoryAttributeValues that are not in the CategoryAttributeValue.collectionValue property
        if (categoryAttributeValue.getChildValues() != null) {
            for (CategoryAttributeValue existingChildCategoryAttributeValue : categoryAttributeValue.getChildValues()) {
                boolean isChildDeleted = existingChildCategoryAttributeValue.getDeleteTs() != null;
                if (!isChildDeleted) {
                    Object value = existingChildCategoryAttributeValue.getValue();
                    if (!collectionValue.contains(value)) {
                        em.remove(existingChildCategoryAttributeValue);
                    }
                    newCollectionValue.remove(value);
                }
            }
        }

        //newCollectionValue now contains only the values that were added but not persisted yet
        newCollectionValue.forEach(value -> {
            CategoryAttributeValue childCAV = metadata.create(CategoryAttributeValue.class);
            childCAV.setParent(categoryAttributeValue);
            childCAV.setValue(value);
            if (categoryAttributeValue.getObjectEntityId() != null) {
                childCAV.setObjectEntityId(categoryAttributeValue.getObjectEntityId());
            }
            childCAV.setCode(categoryAttributeValue.getCode());
            childCAV.setCategoryAttribute(categoryAttributeValue.getCategoryAttribute());
            em.persist(childCAV);
        });
    }

    protected void doFetchDynamicAttributes(MetaClass metaClass, Collection<BaseGenericIdEntity> entities) {
        Collection<CategoryAttribute> categoryAttributes = getAttributesForMetaClass(metaClass);
        if (categoryAttributes == null || categoryAttributes.isEmpty()) {
            for (BaseGenericIdEntity entity : entities) {
                Map<String, CategoryAttributeValue> map = new HashMap<>();
                entity.setDynamicAttributes(map);
            }
            return;
        }
        if (metadata.getTools().hasCompositePrimaryKey(metaClass)
                && !HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
            for (BaseGenericIdEntity entity : entities) {
                Map<String, CategoryAttributeValue> map = new HashMap<>();
                entity.setDynamicAttributes(map);
            }
            return;
        }

        List<Object> ids = entities.stream()
                .map(e -> referenceToEntitySupport.getReferenceId(e))
                .collect(Collectors.toList());

        Multimap<Object, CategoryAttributeValue> allAttributeValues = HashMultimap.create();

        List<Object> currentIds = new ArrayList<>();
        for (Object id : ids) {
            currentIds.add(id);
            if (currentIds.size() >= MAX_ENTITIES_FOR_ATTRIBUTE_VALUES_BATCH) {
                handleAttributeValuesForIds(metaClass, currentIds, allAttributeValues);
                currentIds = new ArrayList<>();
            }
        }
        handleAttributeValuesForIds(metaClass, currentIds, allAttributeValues);

        for (BaseGenericIdEntity entity : entities) {
            Collection<CategoryAttributeValue> entityAttributeValues = allAttributeValues.get(referenceToEntitySupport.getReferenceId(entity));
            Map<String, CategoryAttributeValue> map = new HashMap<>();
            entity.setDynamicAttributes(map);
            if (CollectionUtils.isNotEmpty(entityAttributeValues)) {
                for (CategoryAttributeValue categoryAttributeValue : entityAttributeValues) {
                    CategoryAttribute attribute = categoryAttributeValue.getCategoryAttribute();
                    if (attribute != null) {
                        map.put(attribute.getCode(), categoryAttributeValue);
                    }
                }
            }
        }
    }

    protected void handleAttributeValuesForIds(MetaClass metaClass, List<Object> currentIds, Multimap<Object, CategoryAttributeValue> attributeValuesForEntity) {
        if (CollectionUtils.isNotEmpty(currentIds)) {
            for (CategoryAttributeValue attributeValue : loadAttributeValues(metaClass, currentIds)) {
                attributeValuesForEntity.put(attributeValue.getObjectEntityId(), attributeValue);
            }
        }
    }

    protected List<CategoryAttributeValue> loadAttributeValues(MetaClass metaClass, List<Object> entityIds) {
        List<CategoryAttributeValue> attributeValues = new ArrayList<>();
        try (Transaction tx = persistence.getTransaction()) {
            EntityManager em = persistence.getEntityManager();

            View view = new View(viewRepository.getView(CategoryAttributeValue.class, View.LOCAL), null, false)
                    .addProperty("categoryAttribute", new View(viewRepository.getView(CategoryAttribute.class, View.LOCAL), null, false)
                            .addProperty("category")
                            .addProperty("defaultEntity", viewRepository.getView(ReferenceToEntity.class, View.LOCAL)));

            TypedQuery<CategoryAttributeValue> query;
            if (HasUuid.class.isAssignableFrom(metaClass.getJavaClass())) {
                query = em.createQuery(format("select cav from sys$CategoryAttributeValue cav where cav.entity.%s in :ids and cav.parent is null",
                        referenceToEntitySupport.getReferenceIdPropertyName(metaClass)),
                        CategoryAttributeValue.class);
            } else {
                query = em.createQuery(format("select cav from sys$CategoryAttributeValue cav where cav.entity.%s in :ids " +
                                "and cav.categoryAttribute.categoryEntityType = :entityType and cav.parent is null",
                        referenceToEntitySupport.getReferenceIdPropertyName(metaClass)),
                        CategoryAttributeValue.class);
                query.setParameter("entityType", metaClass.getName());
            }
            query.setParameter("ids", entityIds);
            query.setView(view);
            List<CategoryAttributeValue> resultList = query.getResultList();

            List<CategoryAttributeValue> cavsOfEntityType = resultList.stream()
                    .filter(cav -> cav.getObjectEntityValueId() != null)
                    .collect(Collectors.toList());

            List<CategoryAttributeValue> cavsOfCollectionType = resultList.stream()
                    .filter(cav -> cav.getCategoryAttribute().getIsCollection())
                    .collect(Collectors.toList());

            if (cavsOfCollectionType.isEmpty()) {
                loadEntityValues(cavsOfEntityType);
                attributeValues.addAll(resultList);
            } else {
                List<CategoryAttributeValue> cavsOfCollectionTypeWithChildren = reloadCategoryAttributeValuesWithChildren(cavsOfCollectionType);

                //add nested collection values to the cavsOfEntityType collection, because this collection will later be
                //used for loading entity values
                cavsOfCollectionTypeWithChildren.stream()
                        .filter(cav -> cav.getCategoryAttribute().getDataType() == PropertyType.ENTITY && cav.getChildValues() != null)
                        .forEach(cav -> cavsOfEntityType.addAll(cav.getChildValues()));

                loadEntityValues(cavsOfEntityType);

                cavsOfCollectionTypeWithChildren.stream()
                        .filter(cav -> cav.getChildValues() != null)
                        .forEach(cav -> {
                            List<Object> value = cav.getChildValues().stream()
                                    .filter(c -> c.getDeleteTs() == null)
                                    .map(CategoryAttributeValue::getValue)
                                    .collect(Collectors.toList());
                            cav.setTransientCollectionValue(value);
                        });

                attributeValues.addAll(resultList.stream()
                        .filter(cav -> !cavsOfCollectionTypeWithChildren.contains(cav))
                        .collect(Collectors.toList()));

                attributeValues.addAll(cavsOfCollectionTypeWithChildren);
            }

            tx.commit();
        }
        return attributeValues;
    }

    /**
     * Method loads entity values for CategoryAttributeValues of entity type and sets entity values to the corresponding
     * property of the {@code CategoryAttributeValue} entity.
     */
    @SuppressWarnings("unchecked")
    protected void loadEntityValues(List<CategoryAttributeValue> cavsOfEntityType) {
        HashMultimap<MetaClass, Object> entitiesIdsToBeLoaded = HashMultimap.create();
        HashMultimap<MetaClass, CategoryAttributeValue> cavByType = HashMultimap.create();

        cavsOfEntityType.forEach(cav -> {
            String className = cav.getCategoryAttribute().getEntityClass();
            try {
                Class<?> aClass = Class.forName(className);
                MetaClass metaClass = metadata.getClass(aClass);
                entitiesIdsToBeLoaded.put(metaClass, cav.getObjectEntityValueId());
                cavByType.put(metaClass, cav);
            } catch (ClassNotFoundException e) {
                log.error("Class {} not found", className);
            }
        });

        EntityManager em = persistence.getEntityManager();

        for (Map.Entry<MetaClass, Collection<Object>> entry : entitiesIdsToBeLoaded.asMap().entrySet()) {
            Map<Object, BaseGenericIdEntity> idToEntityMap = new HashMap<>();
            MetaClass metaClass = entry.getKey();
            Collection<Object> ids = entry.getValue();

            if (!ids.isEmpty()) {
                String pkName = referenceToEntitySupport.getPrimaryKeyForLoadingEntity(metaClass);
                List<BaseGenericIdEntity> entitiesValues = em.createQuery(format("select e from %s e where e.%s in :ids", metaClass.getName(), pkName))
                        .setParameter("ids", ids)
                        .setView(metaClass.getJavaClass(), View.MINIMAL)
                        .getResultList();

                for (BaseGenericIdEntity entity : entitiesValues) {
                    idToEntityMap.put(entity.getId(), entity);
                }
            }

            for (CategoryAttributeValue cav : cavByType.get(metaClass)) {
                cav.setTransientEntityValue(idToEntityMap.get(cav.getObjectEntityValueId()));
            }
        }
    }

    protected List<CategoryAttributeValue> reloadCategoryAttributeValuesWithChildren(List<CategoryAttributeValue> categoryAttributeValues) {
        EntityManager em = persistence.getEntityManager();

        View categoryAttributeValueLocalView = viewRepository.getView(CategoryAttributeValue.class, View.LOCAL);
        View categoryAttributeLocalView = viewRepository.getView(CategoryAttribute.class, View.LOCAL);

        View view = new View(categoryAttributeValueLocalView, null, false)
                .addProperty("categoryAttribute", new View(categoryAttributeLocalView, null, false).addProperty("category"))
                .addProperty("childValues", categoryAttributeValueLocalView);

        List<UUID> ids = categoryAttributeValues.stream()
                .map(BaseUuidEntity::getId)
                .collect(Collectors.toList());

        return em.createQuery("select cav from sys$CategoryAttributeValue cav where cav.id in :ids", CategoryAttributeValue.class)
                .setParameter("ids", ids)
                .setView(view)
                .getResultList();
    }

    private static class ReloadCacheMsg implements Serializable {
        private static final long serialVersionUID = -3116358584797500962L;
    }
}