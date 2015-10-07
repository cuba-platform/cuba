/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.queryresults.QueryResultsManagerAPI;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(DataManager.NAME)
public class DataManagerBean implements DataManager {

    public static final int MAX_ENTITIES_FOR_ATTRIBUTE_VALUES_BATCH = 100;

    private Logger log = LoggerFactory.getLogger(DataManagerBean.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Configuration configuration;

    @Inject
    protected PersistenceSecurity security;

    @Inject
    protected AttributeSecuritySupport attributeSecurity;

    @Inject
    protected Persistence persistence;

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected QueryResultsManagerAPI queryResultsManager;

    @Inject
    protected EntityLoadInfoBuilder entityLoadInfoBuilder;

    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        if (log.isDebugEnabled())
            log.debug("load: metaClass=" + context.getMetaClass() + ", id=" + context.getId() + ", view=" + context.getView());

        final MetaClass metaClass = metadata.getSession().getClassNN(context.getMetaClass());

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning null");
            return null;
        }

        E result = null;

        Transaction tx = persistence.createTransaction();
        try {
            final EntityManager em = persistence.getEntityManager();

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);
            persistence.getEntityManagerContext().setDbHints(context.getDbHints());

            com.haulmont.cuba.core.Query query = createQuery(em, context);
            //noinspection unchecked
            List<E> resultList = executeQuery(query);
            if (!resultList.isEmpty())
                result = resultList.get(0);

            if (result instanceof BaseGenericIdEntity && context.getLoadDynamicAttributes()) {
                fetchDynamicAttributes(Collections.singletonList((BaseGenericIdEntity) result));
            }

            tx.commit();
        } finally {
            tx.end();
        }

        attributeSecurity.afterLoad(result);

        return result;
    }

    @Override
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        if (log.isDebugEnabled())
            log.debug("loadList: metaClass=" + context.getMetaClass() + ", view=" + context.getView()
                    + (context.getPrevQueries().isEmpty() ? "" : ", to selected")
                    + ", query=" + (context.getQuery() == null ? null : DataServiceQueryBuilder.printQuery(context.getQuery().getQueryString()))
                    + (context.getQuery() == null || context.getQuery().getFirstResult() == 0 ? "" : ", first=" + context.getQuery().getFirstResult())
                    + (context.getQuery() == null || context.getQuery().getMaxResults() == 0 ? "" : ", max=" + context.getQuery().getMaxResults()));

        final MetaClass metaClass = metadata.getSession().getClass(context.getMetaClass());

        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of " + metaClass + " not permitted, returning empty list");
            return Collections.emptyList();
        }

        queryResultsManager.savePreviousQueryResults(context);

        List<E> resultList;

        Transaction tx = persistence.createTransaction();
        try {
            final EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(context.isSoftDeletion());
            persistence.getEntityManagerContext().setDbHints(context.getDbHints());

            boolean ensureDistinct = false;
            if (configuration.getConfig(ServerConfig.class).getInMemoryDistinct() && context.getQuery() != null) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                        context.getQuery().getQueryString());
                ensureDistinct = transformer.removeDistinct();
                if (ensureDistinct) {
                    context.getQuery().setQueryString(transformer.getResult());
                }
            }
            Query query = createQuery(em, context);
            resultList = getResultList(context, query, ensureDistinct);

            // Fetch dynamic attributes
            if (context.getView() != null
                    && BaseGenericIdEntity.class.isAssignableFrom(context.getView().getEntityClass())
                    && context.getLoadDynamicAttributes()) {
                fetchDynamicAttributes((List<BaseGenericIdEntity>) resultList);
            }

            tx.commit();
        } finally {
            tx.end();
        }

        attributeSecurity.afterLoad(resultList);

        return resultList;
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        Objects.requireNonNull(viewName, "viewName is null");
        return reload(entity, metadata.getViewRepository().getView(entity.getClass(), viewName));
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return reload(entity, view, null);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass) {
        return reload(entity, view, metaClass, true);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes) {
        if (metaClass == null) {
            metaClass = metadata.getSession().getClass(entity.getClass());
        }
        LoadContext<E> context = new LoadContext<>(metaClass);
        context.setId(entity.getId());
        context.setView(view);
        context.setLoadDynamicAttributes(loadDynamicAttributes);

        E reloaded = load(context);
        if (reloaded == null)
            throw new EntityAccessException();

        return reloaded;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Entity> commit(CommitContext context) {
        if (log.isDebugEnabled())
            log.debug("commit: commitInstances=" + context.getCommitInstances()
                    + ", removeInstances=" + context.getRemoveInstances());

        Set<Entity> res = new HashSet<>();
        List<Entity> persisted = new ArrayList<>();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            checkPermissions(context);

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);

            persistence.getEntityManagerContext().setDbHints(context.getDbHints());

            if (context instanceof NotDetachedCommitContext) {
                persistOrMergeNotDetached((NotDetachedCommitContext) context, em, res);
            } else {
                List<BaseGenericIdEntity> entitiesToStoreDynamicAttributes = new ArrayList<>();

                // persist new
                for (Entity entity : context.getCommitInstances()) {
                    if (PersistenceHelper.isNew(entity)) {
                        attributeSecurity.beforePersist(entity);
                        em.persist(entity);
                        res.add(entity);
                        persisted.add(entity);

                        if (entityHasDynamicAttributes(entity)) {
                            entitiesToStoreDynamicAttributes.add((BaseGenericIdEntity) entity);
                        }
                    }
                }

                // merge detached
                for (Entity entity : context.getCommitInstances()) {
                    if (PersistenceHelper.isDetached(entity)) {
                        attributeSecurity.beforeMerge(entity);
                        View view = context.getViews().get(entity);
                        if (view == null) {
                            view = viewRepository.getView(entity.getClass(), View.LOCAL);
                        }
                        View restrictedView = attributeSecurity.createRestrictedView(view);

                        Entity merged = em.merge(entity, restrictedView);
                        res.add(merged);
                        if (entityHasDynamicAttributes(entity)) {
                            BaseGenericIdEntity originalBaseGenericIdEntity = (BaseGenericIdEntity) entity;
                            BaseGenericIdEntity mergedBaseGenericIdEntity = (BaseGenericIdEntity) merged;
                            mergedBaseGenericIdEntity.setDynamicAttributes(originalBaseGenericIdEntity.getDynamicAttributes());
                            entitiesToStoreDynamicAttributes.add(mergedBaseGenericIdEntity);
                        }
                    }
                }

                for (BaseGenericIdEntity entity : entitiesToStoreDynamicAttributes) {
                    storeDynamicAttributes(entity);
                }
            }

            // remove
            for (Entity entity : context.getRemoveInstances()) {
                Entity e = em.merge(entity);
                em.remove(e);
                res.add(e);

                if (entityHasDynamicAttributes(entity)) {
                    Map<String, CategoryAttributeValue> dynamicAttributes = ((BaseGenericIdEntity) entity).getDynamicAttributes();

                    //dynamicAttributes checked for null in entityHasDynamicAttributes()
                    //noinspection ConstantConditions
                    for (CategoryAttributeValue categoryAttributeValue : dynamicAttributes.values()) {
                        if (!PersistenceHelper.isNew(categoryAttributeValue)) {
                            em.remove(categoryAttributeValue);
                            res.add(categoryAttributeValue);
                        }
                    }
                }
            }

            tx.commit();
        } finally {
            tx.end();
        }

        for (Entity entity : res) {
            if (!persisted.contains(entity)) {
                View view = context.getViews().get(entity);
                if (view == null) {
                    view = viewRepository.getView(entity.getClass(), View.LOCAL);
                }
                attributeSecurity.afterMerge(entity, view);
            }
        }

        updateReferences(persisted, res);

        return res;
    }

    protected boolean entityHasDynamicAttributes(Entity entity) {
        return entity instanceof BaseGenericIdEntity
                && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null;
    }

    @SuppressWarnings("unchecked")
    protected void storeDynamicAttributes(BaseGenericIdEntity entity) {
        final EntityManager em = persistence.getEntityManager();
        Map<String, CategoryAttributeValue> dynamicAttributes = entity.getDynamicAttributes();
        if (dynamicAttributes != null) {
            Map<String, CategoryAttributeValue> mergedDynamicAttributes = new HashMap<>();
            for (Map.Entry<String, CategoryAttributeValue> entry : dynamicAttributes.entrySet()) {
                CategoryAttributeValue categoryAttributeValue = entry.getValue();
                if (categoryAttributeValue.getCategoryAttribute() == null
                        && categoryAttributeValue.getCode() != null) {
                    CategoryAttribute attribute =
                            dynamicAttributesManagerAPI.getAttributeForMetaClass(entity.getMetaClass(), categoryAttributeValue.getCode());
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

    protected <E extends BaseGenericIdEntity> void fetchDynamicAttributes(List<E> entities) {
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

    protected void persistOrMergeNotDetached(NotDetachedCommitContext context, EntityManager em, Set<Entity> result) {
        List<EntityLoadInfo> newInstances = new ArrayList<>(context.getNewInstanceIds().size());
        for (String str : context.getNewInstanceIds()) {
            newInstances.add(entityLoadInfoBuilder.parse(str));
        }

        for (Entity entity : context.getCommitInstances()) {
            MetaClass metaClass = metadata.getSession().getClassNN(entity.getClass());
            for (MetaProperty property : metaClass.getProperties()) {
                if (property.getRange().isClass()
                        && !metadata.getTools().isEmbedded(property)
                        && !property.getRange().getCardinality().isMany()
                        && PersistenceHelper.isLoaded(entity, property.getName())) {

                    Entity refEntity = entity.getValue(property.getName());
                    if (refEntity == null || refEntity.getId() == null)
                        continue;

                    if (entityLoadInfoBuilder.contains(newInstances, refEntity)) {
                        // reference to a new entity
                        Entity e = getEntityById(context.getCommitInstances(), refEntity.getId());
                        ((AbstractInstance) entity).setValue(property.getName(), e, false);
                    } else {
                        // reference to an existing entity
                        refEntity = em.getReference(refEntity.getMetaClass().getJavaClass(), refEntity.getId());
                        ((AbstractInstance) entity).setValue(property.getName(), refEntity, false);
                    }
                }
            }

            if (entity instanceof BaseGenericIdEntity) {
                storeDynamicAttributes((BaseGenericIdEntity) entity);
            }

            if (entityLoadInfoBuilder.contains(newInstances, entity)) {
                attributeSecurity.beforePersist(entity);
                em.persist(entity);
                result.add(entity);
            } else {
                attributeSecurity.beforeMerge(entity);
                View view = context.getViews().get(entity);
                Entity e = em.merge(entity, view);
                result.add(e);
            }
        }
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable View view) {
        Set<Entity> res = commit(new CommitContext().addInstanceToCommit(entity, view));

        for (Entity e : res) {
            if (e.equals(entity)) {
                //noinspection unchecked
                return (E) e;
            }
        }
        return null;
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        if (viewName != null) {
            View view = metadata.getViewRepository().getView(metadata.getClassNN(entity.getClass()), viewName);
            return commit(entity, view);
        } else {
            return commit(entity, (View) null);
        }
    }

    @Override
    public <E extends Entity> E commit(E entity) {
        return commit(entity, (View) null);
    }

    @Override
    public void remove(Entity entity) {
        CommitContext context = new CommitContext(
                Collections.<Entity>emptyList(),
                Collections.singleton(entity));
        commit(context);
    }

    protected Query createQuery(EntityManager em, LoadContext context) {
        LoadContext.Query contextQuery = context.getQuery();
        if ((contextQuery == null || StringUtils.isBlank(contextQuery.getQueryString()))
                && context.getId() == null)
            throw new IllegalArgumentException("Query string or ID needed");

        DataServiceQueryBuilder queryBuilder = AppBeans.get(DataServiceQueryBuilder.NAME);
        queryBuilder.init(
                contextQuery == null ? null : contextQuery.getQueryString(),
                contextQuery == null ? null : contextQuery.getParameters(),
                context.getId(), context.getMetaClass()
        );

        if (!context.getPrevQueries().isEmpty()) {
            log.debug("Restrict query by previous results");
            queryBuilder.restrictByPreviousResults(userSessionSource.getUserSession().getId(), context.getQueryKey());
        }
        Query query = queryBuilder.getQuery(em);

        if (contextQuery != null) {
            if (contextQuery.getFirstResult() != 0)
                query.setFirstResult(contextQuery.getFirstResult());
            if (contextQuery.getMaxResults() != 0)
                query.setMaxResults(contextQuery.getMaxResults());
        }

        View view = context.getView() != null ? context.getView() :
                viewRepository.getView(metadata.getClassNN(context.getMetaClass()), View.LOCAL);
        View restrictedView = attributeSecurity.createRestrictedView(view);
        query.setView(restrictedView);

        return query;
    }

    protected <E extends Entity> List<E> getResultList(LoadContext<E> context, Query query, boolean ensureDistinct) {
        List<E> list = executeQuery(query);
        if (!ensureDistinct || list.size() == 0)
            return list;

        int requestedFirst = context.getQuery().getFirstResult();
        LinkedHashSet<E> set = new LinkedHashSet<>(list);
        if (set.size() == list.size() && requestedFirst == 0) {
            // If this is the first chunk and it has no duplicates, just return it
            return list;
        }
        // In case of not first chunk, even if there where no duplicates, start filling the set from zero
        // to ensure correct paging

        int requestedMax = context.getQuery().getMaxResults();

        if (requestedMax == 0) {
            // set contains all items if query without paging
            return new ArrayList<>(set);
        }

        int setSize = list.size() + requestedFirst;
        int factor = list.size() / set.size() * 2;

        set.clear();

        int firstResult = 0;
        int maxResults = (requestedFirst + requestedMax) * factor;
        int i = 0;
        while (set.size() < setSize) {
            if (i++ > 10) {
                log.warn("In-memory distinct: endless loop detected for " + context);
                break;
            }
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            //noinspection unchecked
            list = query.getResultList();
            if (list.size() == 0)
                break;
            set.addAll(list);

            firstResult = firstResult + maxResults;
        }

        // Copy by iteration because subList() returns non-serializable class
        int max = Math.min(requestedFirst + requestedMax, set.size());
        List<E> result = new ArrayList<>(max - requestedFirst);
        int j = 0;
        for (E item : set) {
            if (j >= max)
                break;
            if (j >= requestedFirst)
                result.add(item);
            j++;
        }
        return result;
    }

    protected <E extends Entity> List<E> executeQuery(Query query) {
        List<E> list;
        try {
            //noinspection unchecked
            list = query.getResultList();
        } catch (javax.persistence.PersistenceException e) {
            if (e.getCause() instanceof org.eclipse.persistence.exceptions.QueryException
                    && e.getMessage() != null
                    && e.getMessage().contains("Fetch group cannot be set on report query")) {
                throw new DevelopmentException("DataManager cannot execute query for single attributes");
            } else {
                throw e;
            }
        }
        return list;
    }

    protected void checkPermissions(CommitContext context) {
        Set<MetaClass> checkedCreateRights = new HashSet<>();
        Set<MetaClass> checkedUpdateRights = new HashSet<>();
        Set<MetaClass> checkedDeleteRights = new HashSet<>();

        if (context instanceof NotDetachedCommitContext) {
            Set newInstanceIdSet = new HashSet<>(((NotDetachedCommitContext) context).getNewInstanceIds());
            for (Entity entity : context.getCommitInstances()) {
                if (entity == null)
                    continue;

                MetaClass metaClass = entity.getMetaClass();
                if (newInstanceIdSet.contains(metaClass.getName() + "-" + entity.getId())) {
                    checkPermission(checkedCreateRights, metaClass, EntityOp.CREATE);
                } else {
                    checkPermission(checkedUpdateRights, metaClass, EntityOp.UPDATE);
                }
            }
        } else {
            for (Entity entity : context.getCommitInstances()) {
                if (entity == null)
                    continue;

                if (PersistenceHelper.isNew(entity)) {
                    checkPermission(checkedCreateRights, entity.getMetaClass(), EntityOp.CREATE);
                } else {
                    checkPermission(checkedUpdateRights, entity.getMetaClass(), EntityOp.UPDATE);
                }
            }
        }

        for (Entity entity : context.getRemoveInstances()) {
            if (entity == null)
                continue;

            checkPermission(checkedDeleteRights, entity.getMetaClass(), EntityOp.DELETE);
        }
    }

    protected void checkPermission(Set<MetaClass> cache, MetaClass metaClass, EntityOp operation) {
        if (cache.contains(metaClass))
            return;
        checkPermission(metaClass, operation);
        cache.add(metaClass);
    }

    protected void checkPermission(MetaClass metaClass, EntityOp operation) {
        if (!security.isEntityOpPermitted(metaClass, operation))
            throw new AccessDeniedException(PermissionType.ENTITY_OP, metaClass.getName());
    }

    /**
     * Update references from newly persisted entities to merged detached entities. Otherwise a new entity can
     * contain a stale instance of merged entity.
     *
     * @param persisted persisted entities
     * @param committed all committed entities
     */
    protected void updateReferences(Collection<Entity> persisted, Collection<Entity> committed) {
        for (Entity persistedEntity : persisted) {
            for (Entity entity : committed) {
                if (entity != persistedEntity) {
                    updateReferences(persistedEntity, entity, new HashSet<>());
                }
            }
        }
    }

    protected void updateReferences(Entity entity, Entity refEntity, Set<Entity> visited) {
        if (entity == null || refEntity == null || visited.contains(entity))
            return;
        visited.add(entity);

        MetaClass refEntityMetaClass = refEntity.getMetaClass();
        for (MetaProperty property : entity.getMetaClass().getProperties()) {
            if (!property.getRange().isClass() || !property.getRange().asClass().equals(refEntityMetaClass))
                continue;
            if (PersistenceHelper.isLoaded(entity, property.getName())) {
                if (property.getRange().getCardinality().isMany()) {
                    Collection collection = entity.getValue(property.getName());
                    if (collection != null) {
                        for (Object obj : collection) {
                            updateReferences((Entity) obj, refEntity, visited);
                        }
                    }
                } else {
                    Entity value = entity.getValue(property.getName());
                    if (value != null) {
                        if (value.getId().equals(refEntity.getId())) {
                            if (entity instanceof AbstractInstance) {
                                ((AbstractInstance) entity).setValue(property.getName(), refEntity, false);
                            }
                        } else {
                            updateReferences(value, refEntity, visited);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    protected Entity getEntityById(Collection<Entity> entities, Object id) {
        if (id == null)
            return null;

        for (Entity entity : entities)
            if (id.equals(entity.getId()))
                return entity;

        return null;
    }
}
