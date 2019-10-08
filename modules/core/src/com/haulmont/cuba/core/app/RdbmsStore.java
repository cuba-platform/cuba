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
 */

package com.haulmont.cuba.core.app;

import com.google.common.collect.Lists;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.app.queryresults.QueryResultsManagerAPI;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.EntityFetcher;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.EntityChangedEventManager;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * INTERNAL.
 * Implementation of the {@link DataStore} interface working with a relational database through ORM.
 */
@Component(RdbmsStore.NAME)
@Scope("prototype")
public class RdbmsStore implements DataStore {

    public static final String NAME = "cuba_RdbmsStore";

    private static final Logger log = LoggerFactory.getLogger(RdbmsStore.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected ServerConfig serverConfig;

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
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Inject
    protected QueryTransformerFactory queryTransformerFactory;

    @Inject
    protected EntityFetcher entityFetcher;

    @Inject
    protected EntityStates entityStates;

    @Inject
    protected EntityChangedEventManager entityChangedEventManager;

    protected String storeName;

    public RdbmsStore(String storeName) {
        this.storeName = storeName;
    }

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        if (log.isDebugEnabled()) {
            log.debug("load: metaClass={}, id={}, view={}", context.getMetaClass(), context.getId(), context.getView());
        }

        final MetaClass metaClass = metadata.getSession().getClassNN(context.getMetaClass());

        if (isAuthorizationRequired(context) && !isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning null", metaClass);
            return null;
        }

        E result = null;
        boolean needToApplyInMemoryReadConstraints = needToApplyInMemoryReadConstraints(context);
        try (Transaction tx = getLoadTransaction(context.isJoinTransaction())) {
            final EntityManager em = persistence.getEntityManager(storeName);

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);

            // If maxResults=1 and the query is not by ID we should not use getSingleResult() for backward compatibility
            boolean singleResult = !(context.getQuery() != null
                        && context.getQuery().getMaxResults() == 1
                        && context.getQuery().getQueryString() != null)
                    && context.getId() != null;

            View view = createRestrictedView(context);
            com.haulmont.cuba.core.Query query = createQuery(em, context, singleResult, false);
            query.setView(view);

            //noinspection unchecked
            List<E> resultList = executeQuery(query, singleResult);
            if (!resultList.isEmpty()) {
                result = resultList.get(0);
            }

            if (result != null && needToFilterByInMemoryReadConstraints(context) && security.filterByConstraints(result)) {
                result = null;
            }

            if (result instanceof BaseGenericIdEntity && context.isLoadDynamicAttributes()) {
                dynamicAttributesManagerAPI.fetchDynamicAttributes(Collections.singletonList((BaseGenericIdEntity) result),
                        collectEntityClassesWithDynamicAttributes(context.getView()));
            }

            if (result != null && needToApplyInMemoryReadConstraints) {
                security.calculateFilteredData(result);
            }

            if (result != null) {
                attributeSecurity.onLoad(result, view);
            }

            if (context.isJoinTransaction()) {
                em.flush();
                detachEntity(em, result, view);
            }

            tx.commit();
        }

        if (result != null) {
            if (needToApplyInMemoryReadConstraints) {
                security.applyConstraints(result);
            }
            if (isAuthorizationRequired(context)) {
                attributeSecurity.afterLoad(result);
            }
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        if (log.isDebugEnabled())
            log.debug("loadList: metaClass=" + context.getMetaClass() + ", view=" + context.getView()
                    + (context.getPrevQueries().isEmpty() ? "" : ", from selected")
                    + ", query=" + context.getQuery()
                    + (context.getQuery() == null || context.getQuery().getFirstResult() == 0 ? "" : ", first=" + context.getQuery().getFirstResult())
                    + (context.getQuery() == null || context.getQuery().getMaxResults() == 0 ? "" : ", max=" + context.getQuery().getMaxResults()));

        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());

        if (isAuthorizationRequired(context) && !isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning empty list", metaClass);
            return Collections.emptyList();
        }

        queryResultsManager.savePreviousQueryResults(context);

        List<E> resultList;
        boolean needToApplyInMemoryReadConstraints = needToApplyInMemoryReadConstraints(context);
        try (Transaction tx = getLoadTransaction(context.isJoinTransaction())) {
            EntityManager em = persistence.getEntityManager(storeName);
            em.setSoftDeletion(context.isSoftDeletion());

            boolean ensureDistinct = false;
            if (serverConfig.getInMemoryDistinct() && context.getQuery() != null) {
                QueryTransformer transformer = queryTransformerFactory.transformer(
                        context.getQuery().getQueryString());
                ensureDistinct = transformer.removeDistinct();
                if (ensureDistinct) {
                    context.getQuery().setQueryString(transformer.getResult());
                }
            }
            View view = createRestrictedView(context);

            List<E> entities;
            Integer maxIdsBatchSize = DbmsSpecificFactory.getDbmsFeatures(storeName).getMaxIdsBatchSize();
            if (!context.getIds().isEmpty() && entityHasEmbeddedId(metaClass)) {
                entities = loadListBySingleIds(context, em, view);
            } else if (!context.getIds().isEmpty() && maxIdsBatchSize != null && context.getIds().size() > maxIdsBatchSize) {
                entities = loadListByBatchesOfIds(context, em, view, maxIdsBatchSize);
            } else {
                Query query = createQuery(em, context, false, false);
                query.setView(view);
                entities = getResultList(context, query, ensureDistinct);
            }
            if (context.getIds().isEmpty()) {
                resultList = entities;
            } else {
                resultList = checkAndReorderLoadedEntities(context.getIds(), entities, metaClass);
            }

            // Fetch dynamic attributes
            if (!resultList.isEmpty() && resultList.get(0) instanceof BaseGenericIdEntity && context.isLoadDynamicAttributes()) {
                dynamicAttributesManagerAPI.fetchDynamicAttributes((List<BaseGenericIdEntity>) resultList,
                        collectEntityClassesWithDynamicAttributes(context.getView()));
            }

            if (needToApplyInMemoryReadConstraints) {
                security.calculateFilteredData((Collection<Entity>) resultList);
            }

            attributeSecurity.onLoad(resultList, view);

            if (context.isJoinTransaction()) {
                em.flush();
                for (E entity : resultList) {
                    detachEntity(em, entity, view);
                }
            }

            tx.commit();
        }

        if (needToApplyInMemoryReadConstraints) {
            security.applyConstraints((Collection<Entity>) resultList);
        }

        if (context.isAuthorizationRequired()) {
            attributeSecurity.afterLoad(resultList);
        }

        return resultList;
    }

    protected boolean entityHasEmbeddedId(MetaClass metaClass) {
        MetaProperty pkProperty = metadataTools.getPrimaryKeyProperty(metaClass);
        return pkProperty == null || pkProperty.getRange().isClass();
    }

    protected  <E extends Entity> List<E> loadListBySingleIds(LoadContext<E> context, EntityManager em, View view) {
        LoadContext<?> contextCopy = context.copy();
        contextCopy.setIds(Collections.emptyList());

        List<E> entities = new ArrayList<>(context.getIds().size());
        for (Object id : context.getIds()) {
            contextCopy.setId(id);
            Query query = createQuery(em, contextCopy, true, false);
            query.setView(view);
            List<E> list = executeQuery(query, true);
            entities.addAll(list);
        }
        return entities;
    }

    protected <E extends Entity> List<E> checkAndReorderLoadedEntities(List<?> ids, List<E> entities, MetaClass metaClass) {
        List<E> result = new ArrayList<>(ids.size());
        Map<Object, E> idToEntityMap = entities.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));
        for (Object id : ids) {
            E entity = idToEntityMap.get(id);
            if (entity == null) {
                throw new EntityAccessException(metaClass, id);
            }
            result.add(entity);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> loadListByBatchesOfIds(LoadContext<E> context, EntityManager em, View view, int batchSize) {
        List<List<Object>> partitions = Lists.partition((List<Object>) context.getIds(), batchSize);

        List<E> entities = new ArrayList<>(context.getIds().size());
        for (List partition : partitions) {
            LoadContext<E> contextCopy = (LoadContext<E>) context.copy();
            contextCopy.setIds(partition);

            Query query = createQuery(em, contextCopy, false, false);
            query.setView(view);
            List<E> list = executeQuery(query, false);
            entities.addAll(list);
        }

        return entities;
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        if (log.isDebugEnabled())
            log.debug("getCount: metaClass=" + context.getMetaClass()
                    + (context.getPrevQueries().isEmpty() ? "" : ", from selected")
                    + ", query=" + context.getQuery());

        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());

        if (isAuthorizationRequired(context) && !isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning 0", metaClass);
            return 0;
        }

        queryResultsManager.savePreviousQueryResults(context);

        context = context.copy();
        if (context.getQuery() == null) {
            context.setQuery(LoadContext.createQuery(null));
        }
        if (StringUtils.isBlank(context.getQuery().getQueryString())) {
            context.getQuery().setQueryString("select e from " + metaClass.getName() + " e");
        }

        if (security.hasInMemoryConstraints(metaClass, ConstraintOperationType.READ, ConstraintOperationType.ALL)) {
            List resultList;
            try (Transaction tx = getLoadTransaction(context.isJoinTransaction())) {
                EntityManager em = persistence.getEntityManager(storeName);
                em.setSoftDeletion(context.isSoftDeletion());

                boolean ensureDistinct = false;
                if (serverConfig.getInMemoryDistinct() && context.getQuery() != null) {
                    QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                            context.getQuery().getQueryString());
                    ensureDistinct = transformer.removeDistinct();
                    if (ensureDistinct) {
                        context.getQuery().setQueryString(transformer.getResult());
                    }
                }
                context.getQuery().setFirstResult(0);
                context.getQuery().setMaxResults(0);

                Query query = createQuery(em, context, false, false);
                query.setView(createRestrictedView(context));

                resultList = getResultList(context, query, ensureDistinct);

                tx.commit();
            }
            return resultList.size();
        } else {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(context.getQuery().getQueryString());
            transformer.replaceWithCount();
            context.getQuery().setQueryString(transformer.getResult());

            Number result;
            try (Transaction tx = getLoadTransaction(context.isJoinTransaction())) {
                EntityManager em = persistence.getEntityManager(storeName);
                em.setSoftDeletion(context.isSoftDeletion());

                Query query = createQuery(em, context, false, true);
                result = (Number) query.getSingleResult();

                tx.commit();
            }

            return result.longValue();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Entity> commit(CommitContext context) {
        if (log.isDebugEnabled())
            log.debug("commit: commitInstances=" + context.getCommitInstances()
                    + ", removeInstances=" + context.getRemoveInstances());

        Set<Entity> saved = new HashSet<>();
        List<Entity> persisted = new ArrayList<>();
        List<BaseGenericIdEntity> identityEntitiesToStoreDynamicAttributes = new ArrayList<>();
        List<CategoryAttributeValue> attributeValuesToRemove = new ArrayList<>();

        SavedEntitiesHolder savedEntitiesHolder;

        try (Transaction tx = getSaveTransaction(storeName, context.isJoinTransaction())) {
            EntityManager em = persistence.getEntityManager(storeName);
            checkPermissions(context);

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);

            List<BaseGenericIdEntity> entitiesToStoreDynamicAttributes = new ArrayList<>();

            // persist new
            for (Entity entity : context.getCommitInstances()) {
                if (entityStates.isNew(entity)) {
                    if (isAuthorizationRequired(context)) {
                        attributeSecurity.beforePersist(entity);
                    }
                    em.persist(entity);
                    saved.add(entity);
                    persisted.add(entity);

                    if (isAuthorizationRequired(context))
                        checkOperationPermitted(entity, ConstraintOperationType.CREATE);

                    if (!context.isDiscardCommitted()) {
                        View view = getViewFromContextOrNull(context, entity);
                        entityFetcher.fetch(entity, view, true);
                        attributeSecurity.afterPersist(entity, view);
                    }

                    if (entityHasDynamicAttributes(entity)) {
                        if (entity instanceof BaseDbGeneratedIdEntity) {
                            identityEntitiesToStoreDynamicAttributes.add((BaseGenericIdEntity) entity);
                        } else {
                            entitiesToStoreDynamicAttributes.add((BaseGenericIdEntity) entity);
                        }
                    }
                }
            }

            // merge the rest - instances can be detached or not
            for (Entity entity : context.getCommitInstances()) {
                if (!entityStates.isNew(entity)) {
                    if (isAuthorizationRequired(context)) {
                        security.assertToken(entity);
                    }
                    security.restoreSecurityStateAndFilteredData(entity);
                    if (isAuthorizationRequired(context)) {
                        attributeSecurity.beforeMerge(entity);
                    }

                    Entity merged = em.merge(entity);
                    saved.add(merged);

                    entityFetcher.fetch(merged, getViewFromContext(context, entity));
                    attributeSecurity.afterMerge(merged);

                    if (isAuthorizationRequired(context))
                        checkOperationPermitted(merged, ConstraintOperationType.UPDATE);

                    if (entityHasDynamicAttributes(entity)) {
                        BaseGenericIdEntity originalBaseGenericIdEntity = (BaseGenericIdEntity) entity;
                        BaseGenericIdEntity mergedBaseGenericIdEntity = (BaseGenericIdEntity) merged;
                        mergeDynamicAttributes(originalBaseGenericIdEntity, mergedBaseGenericIdEntity);
                        entitiesToStoreDynamicAttributes.add(mergedBaseGenericIdEntity);
                    }
                }
            }

            for (BaseGenericIdEntity entity : entitiesToStoreDynamicAttributes) {
                dynamicAttributesManagerAPI.storeDynamicAttributes(entity);
            }

            // remove
            for (Entity entity : context.getRemoveInstances()) {
                if (isAuthorizationRequired(context)) {
                    security.assertToken(entity);
                }
                security.restoreSecurityStateAndFilteredData(entity);

                Entity e;
                if (entity instanceof SoftDelete) {
                    attributeSecurity.beforeMerge(entity);
                    e = em.merge(entity);
                    entityFetcher.fetch(e, getViewFromContext(context, entity));
                    attributeSecurity.afterMerge(e);
                } else {
                    e = em.merge(entity);
                }

                if (isAuthorizationRequired(context))
                    checkOperationPermitted(e, ConstraintOperationType.DELETE);

                em.remove(e);
                saved.add(e);

                if (entityHasDynamicAttributes(entity)) {
                    Map<String, CategoryAttributeValue> dynamicAttributes = ((BaseGenericIdEntity) entity).getDynamicAttributes();

                    // old values of dynamic attributes on deleted entity are used in EntityChangedEvent
                    ((BaseGenericIdEntity) e).setDynamicAttributes(dynamicAttributes);

                    //dynamicAttributes checked for null in entityHasDynamicAttributes()
                    //noinspection ConstantConditions
                    for (CategoryAttributeValue categoryAttributeValue : dynamicAttributes.values()) {
                        if (!entityStates.isNew(categoryAttributeValue)) {
                            if (Stores.isMain(storeName)) {
                                em.remove(categoryAttributeValue);
                            } else {
                                attributeValuesToRemove.add(categoryAttributeValue);
                            }
                            saved.add(categoryAttributeValue);
                        }
                    }
                }
            }

            if (!context.isDiscardCommitted() && isAuthorizationRequired(context) && userSessionSource.getUserSession().hasConstraints()) {
                security.calculateFilteredData(saved);
            }

            savedEntitiesHolder = SavedEntitiesHolder.setEntities(saved);

            if (context.isJoinTransaction()) {
                List<EntityChangedEvent> events = entityChangedEventManager.collect(saved);
                em.flush();
                for (Entity entity : saved) {
                    em.detach(entity);
                }
                entityChangedEventManager.publish(events);
            }

            tx.commit();
        }

        Set<Entity> resultEntities = savedEntitiesHolder.getEntities(saved);

        if (!attributeValuesToRemove.isEmpty()) {
            try (Transaction tx = getSaveTransaction(Stores.MAIN, context.isJoinTransaction())) {
                EntityManager em = persistence.getEntityManager();
                for (CategoryAttributeValue entity : attributeValuesToRemove) {
                    em.remove(entity);
                }
                tx.commit();
            }
        }

        if (!identityEntitiesToStoreDynamicAttributes.isEmpty()) {
            try (Transaction tx = getSaveTransaction(storeName, context.isJoinTransaction())) {
                for (BaseGenericIdEntity entity : identityEntitiesToStoreDynamicAttributes) {
                    dynamicAttributesManagerAPI.storeDynamicAttributes(entity);
                }
                tx.commit();
            }
        }

        if (!context.isDiscardCommitted() && isAuthorizationRequired(context) && userSessionSource.getUserSession().hasConstraints()) {
            security.applyConstraints(resultEntities);
        }

        if (!context.isDiscardCommitted()) {
            if (isAuthorizationRequired(context)) {
                for (Entity entity : resultEntities) {
                    if (!persisted.contains(entity)) {
                        attributeSecurity.afterCommit(entity);
                    }
                }
            }
            updateReferences(persisted, resultEntities);
        }

        return context.isDiscardCommitted() ? Collections.emptySet() : resultEntities;
    }

    @Override
    public List<KeyValueEntity> loadValues(ValueLoadContext context) {
        Preconditions.checkNotNullArgument(context, "context is null");
        Preconditions.checkNotNullArgument(context.getQuery(), "query is null");

        ValueLoadContext.Query contextQuery = context.getQuery();

        if (log.isDebugEnabled())
            log.debug("query: " + contextQuery.toString()
                    + (contextQuery.getFirstResult() == 0 ? "" : ", first=" + contextQuery.getFirstResult())
                    + (contextQuery.getMaxResults() == 0 ? "" : ", max=" + contextQuery.getMaxResults()));

        QueryParser queryParser = queryTransformerFactory.parser(contextQuery.getQueryString());
        if (isAuthorizationRequired(context) && !checkValueQueryPermissions(queryParser)) {
            return Collections.emptyList();
        }

        List<KeyValueEntity> entities = new ArrayList<>();

        try (Transaction tx = getLoadTransaction(context.isJoinTransaction())) {
            EntityManager em = persistence.getEntityManager(storeName);
            em.setSoftDeletion(context.isSoftDeletion());

            List<String> keys = context.getProperties();

            JpqlQueryBuilder queryBuilder = AppBeans.get(JpqlQueryBuilder.NAME);

            queryBuilder.setValueProperties(context.getProperties())
                    .setQueryString(contextQuery.getQueryString())
                    .setCondition(contextQuery.getCondition())
                    .setSort(contextQuery.getSort())
                    .setQueryParameters(contextQuery.getParameters())
                    .setNoConversionParams(contextQuery.getNoConversionParams());

            Query query = queryBuilder.getQuery(em);

            if (contextQuery.getFirstResult() != 0)
                query.setFirstResult(contextQuery.getFirstResult());
            if (contextQuery.getMaxResults() != 0)
                query.setMaxResults(contextQuery.getMaxResults());

            List resultList = query.getResultList();
            List<Integer> notPermittedSelectIndexes = isAuthorizationRequired(context) ?
                    getNotPermittedSelectIndexes(queryParser) : Collections.emptyList();
            for (Object item : resultList) {
                KeyValueEntity entity = new KeyValueEntity();
                entity.setIdName(context.getIdName());
                entities.add(entity);

                if (item instanceof Object[]) {
                    Object[] row = (Object[]) item;
                    for (int i = 0; i < keys.size(); i++) {
                        String key = keys.get(i);
                        if (row.length > i) {
                            if (notPermittedSelectIndexes.contains(i)) {
                                entity.setValue(key, null);
                            } else {
                                entity.setValue(key, row[i]);
                            }
                        }
                    }
                } else if (!keys.isEmpty()) {
                    if (!notPermittedSelectIndexes.isEmpty()) {
                        entity.setValue(keys.get(0), null);
                    } else {
                        entity.setValue(keys.get(0), item);
                    }
                }
            }

            tx.commit();
        }

        return entities;
    }

    protected View getViewFromContext(CommitContext context, Entity entity) {
        View view = context.getViews().get(entity);
        if (view == null) {
            view = viewRepository.getView(entity.getClass(), View.LOCAL);
        }
        return isAuthorizationRequired(context) ? attributeSecurity.createRestrictedView(view) : view;
    }

    @Nullable
    protected View getViewFromContextOrNull(CommitContext context, Entity entity) {
        View view = context.getViews().get(entity);
        if (view == null) {
            return null;
        }
        return isAuthorizationRequired(context) ? attributeSecurity.createRestrictedView(view) : view;
    }

    protected void checkOperationPermitted(Entity entity, ConstraintOperationType operationType) {
        if (userSessionSource.getUserSession().hasConstraints()
                && security.hasConstraints(entity.getMetaClass())
                && !security.isPermitted(entity, operationType)) {
            throw new RowLevelSecurityException(
                    operationType + " is not permitted for entity " + entity, entity.getMetaClass().getName(), operationType);
        }
    }

    protected boolean entityHasDynamicAttributes(Entity entity) {
        return entity instanceof BaseGenericIdEntity
                && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null;
    }

    protected Query createQuery(EntityManager em, LoadContext<?> context, boolean singleResult, boolean countQuery) {
        LoadContext.Query contextQuery = context.getQuery();

        JpqlQueryBuilder queryBuilder = AppBeans.get(JpqlQueryBuilder.NAME);

        queryBuilder.setId(context.getId())
                .setIds(context.getIds())
                .setEntityName(context.getMetaClass())
                .setSingleResult(singleResult);

        if (contextQuery != null) {
            queryBuilder.setQueryString(contextQuery.getQueryString())
                    .setCondition(contextQuery.getCondition())
                    .setQueryParameters(contextQuery.getParameters())
                    .setNoConversionParams(contextQuery.getNoConversionParams());
            if (!countQuery) {
                queryBuilder.setSort(contextQuery.getSort());
            }
        }

        if (!context.getPrevQueries().isEmpty()) {
            log.debug("Restrict query by previous results");
            queryBuilder.setPreviousResults(userSessionSource.getUserSession().getId(), context.getQueryKey());
        }

        Query query = queryBuilder.getQuery(em);

        if (contextQuery != null) {
            if (contextQuery.getFirstResult() != 0)
                query.setFirstResult(contextQuery.getFirstResult());
            if (contextQuery.getMaxResults() != 0)
                query.setMaxResults(contextQuery.getMaxResults());
            if (contextQuery.isCacheable()) {
                query.setCacheable(contextQuery.isCacheable());
            }
        }

        if (context.getHints() != null) {
            for (Map.Entry<String, Object> hint : context.getHints().entrySet()) {
                query.setHint(hint.getKey(), hint.getValue());
            }
        }

        return query;
    }

    protected View createRestrictedView(LoadContext<?> context) {
        View view = context.getView() != null ? context.getView() :
                viewRepository.getView(metadata.getClassNN(context.getMetaClass()), View.BASE);
        View copy = View.copy(isAuthorizationRequired(context) ? attributeSecurity.createRestrictedView(view) : view);
        if (context.isLoadPartialEntities()
                && !needToApplyInMemoryReadConstraints(context)
                && !needToFilterByInMemoryReadConstraints(context)
                && !needToApplyAttributeAccess(context)) {
            copy.setLoadPartialEntities(true);
        }
        return copy;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> getResultList(LoadContext<E> context, Query query, boolean ensureDistinct) {
        List<E> list = executeQuery(query, false);
        int initialSize = list.size();
        if (initialSize == 0) {
            return list;
        }
        boolean needToFilterByInMemoryReadConstraints = needToFilterByInMemoryReadConstraints(context);
        boolean filteredByConstraints = false;
        if (needToFilterByInMemoryReadConstraints) {
            filteredByConstraints = security.filterByConstraints((Collection<Entity>) list);
        }
        if (!ensureDistinct) {
            return filteredByConstraints ? getResultListIteratively(context, query, list, initialSize, true) : list;
        }

        int requestedFirst = context.getQuery().getFirstResult();
        LinkedHashSet<E> set = new LinkedHashSet<>(list);
        if (set.size() == list.size() && requestedFirst == 0 && !filteredByConstraints) {
            // If this is the first chunk and it has no duplicates and security constraints are not applied, just return it
            return list;
        }
        // In case of not first chunk, even if there where no duplicates, start filling the set from zero
        // to ensure correct paging
        return getResultListIteratively(context, query, set, initialSize, needToFilterByInMemoryReadConstraints);
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> getResultListIteratively(LoadContext<E> context, Query query,
                                                                  Collection<E> filteredCollection,
                                                                  int initialSize, boolean needToFilterByInMemoryReadConstraints) {
        int requestedFirst = context.getQuery().getFirstResult();
        int requestedMax = context.getQuery().getMaxResults();

        if (requestedMax == 0) {
            // set contains all items if query without paging
            return new ArrayList<>(filteredCollection);
        }

        int setSize = initialSize + requestedFirst;
        int factor = filteredCollection.size() == 0 ? 2 : initialSize / filteredCollection.size() * 2;

        filteredCollection.clear();

        int firstResult = 0;
        int maxResults = (requestedFirst + requestedMax) * factor;
        int i = 0;
        while (filteredCollection.size() < setSize) {
            if (i++ > 10000) {
                log.warn("In-memory distinct: endless loop detected for " + context);
                break;
            }
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            //noinspection unchecked
            List<E> list = query.getResultList();
            if (list.size() == 0) {
                break;
            }
            if (needToFilterByInMemoryReadConstraints) {
                security.filterByConstraints((Collection<Entity>) list);
            }
            filteredCollection.addAll(list);

            firstResult = firstResult + maxResults;
        }

        // Copy by iteration because subList() returns non-serializable class
        int max = Math.min(requestedFirst + requestedMax, filteredCollection.size());
        List<E> result = new ArrayList<>(max - requestedFirst);
        int j = 0;
        for (E item : filteredCollection) {
            if (j >= max)
                break;
            if (j >= requestedFirst)
                result.add(item);
            j++;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> executeQuery(Query query, boolean singleResult) {
        List<E> list;
        try {
            if (singleResult) {
                try {
                    E result = (E) query.getSingleResult();
                    list = new ArrayList<>(1);
                    list.add(result);
                } catch (NoResultException e) {
                    list = Collections.emptyList();
                }
            } else {
                list = query.getResultList();
            }
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
        if (!isAuthorizationRequired(context))
            return;

        Set<MetaClass> checkedCreateRights = new HashSet<>();
        Set<MetaClass> checkedUpdateRights = new HashSet<>();
        Set<MetaClass> checkedDeleteRights = new HashSet<>();

        for (Entity entity : context.getCommitInstances()) {
            if (entity == null)
                continue;

            if (entityStates.isNew(entity)) {
                checkPermission(checkedCreateRights, entity.getMetaClass(), EntityOp.CREATE);
            } else {
                checkPermission(checkedUpdateRights, entity.getMetaClass(), EntityOp.UPDATE);
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
        if (!isEntityOpPermitted(metaClass, operation))
            throw new AccessDeniedException(PermissionType.ENTITY_OP, operation, metaClass.getName());
    }

    protected boolean checkValueQueryPermissions(QueryParser queryParser) {
        queryParser.getQueryPaths().stream()
                .filter(path -> !path.isSelectedPath())
                .forEach(path -> {
                    MetaClass metaClass = metadata.getClassNN(path.getEntityName());
                    MetaPropertyPath propertyPath = metaClass.getPropertyPath(path.getPropertyPath());
                    if (propertyPath == null) {
                        throw new IllegalStateException(String.format("query path '%s' is unresolved", path.getFullPath()));
                    }
                    if (!isEntityAttrViewPermitted(propertyPath)) {
                        throw new AccessDeniedException(PermissionType.ENTITY_ATTR, metaClass + "." + path.getFullPath());
                    }
                });
        MetaClass metaClass = metadata.getClassNN(queryParser.getEntityName());
        if (!isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning empty list", metaClass);
            return false;
        }
        if (security.hasInMemoryConstraints(metaClass, ConstraintOperationType.READ, ConstraintOperationType.ALL)) {
            String msg = String.format("%s is not permitted for %s", ConstraintOperationType.READ, metaClass.getName());
            if (serverConfig.getDisableLoadValuesIfConstraints()) {
                throw new RowLevelSecurityException(msg, metaClass.getName(), ConstraintOperationType.READ);
            } else {
                log.debug(msg);
            }
        }
        Set<String> entityNames = queryParser.getAllEntityNames();
        entityNames.remove(metaClass.getName());
        for (String entityName : entityNames) {
            MetaClass entityMetaClass = metadata.getClassNN(entityName);
            if (!isEntityOpPermitted(entityMetaClass, EntityOp.READ)) {
                log.debug("reading of {} not permitted, returning empty list", entityMetaClass);
                return false;
            }
            if (security.hasConstraints(entityMetaClass)) {
                String msg = String.format("%s is not permitted for %s", ConstraintOperationType.READ, entityName);
                if (serverConfig.getDisableLoadValuesIfConstraints()) {
                    throw new RowLevelSecurityException(msg, entityName, ConstraintOperationType.READ);
                } else {
                    log.debug(msg);
                }
            }
        }
        return true;
    }

    protected boolean isEntityOpPermitted(MetaClass metaClass, EntityOp operation) {
        return security.isEntityOpPermitted(metaClass, operation);
    }

    protected boolean isEntityAttrViewPermitted(MetaPropertyPath metaPropertyPath) {
        for (MetaProperty metaProperty : metaPropertyPath.getMetaProperties()) {
            if (!security.isEntityAttrPermitted(metaProperty.getDomain(), metaProperty.getName(), EntityAttrAccess.VIEW)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isAuthorizationRequired(LoadContext context) {
        return context.isAuthorizationRequired() || serverConfig.getDataManagerChecksSecurityOnMiddleware();
    }

    protected boolean isAuthorizationRequired(ValueLoadContext context) {
        return context.isAuthorizationRequired() || serverConfig.getDataManagerChecksSecurityOnMiddleware();
    }

    protected boolean isAuthorizationRequired(CommitContext context) {
        return context.isAuthorizationRequired() || serverConfig.getDataManagerChecksSecurityOnMiddleware();
    }

    protected List<Integer> getNotPermittedSelectIndexes(QueryParser queryParser) {
        List<Integer> indexes = new ArrayList<>();
        int index = 0;
        for (QueryParser.QueryPath path : queryParser.getQueryPaths()) {
            if (path.isSelectedPath()) {
                MetaClass metaClass = metadata.getClassNN(path.getEntityName());
                if (!Objects.equals(path.getPropertyPath(), path.getVariableName()) && !isEntityAttrViewPermitted(metaClass.getPropertyPath(path.getPropertyPath()))) {
                    indexes.add(index);
                }
                index++;
            }
        }
        return indexes;
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
            if (entityStates.isLoaded(entity, property.getName())) {
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
                                if (property.isReadOnly() && metadataTools.isNotPersistent(property)) {
                                    continue;
                                }
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

    protected boolean needToFilterByInMemoryReadConstraints(LoadContext context) {
        return userSessionSource.getUserSession().hasConstraints()
                && security.hasInMemoryConstraints(metadata.getClassNN(context.getMetaClass()),
                ConstraintOperationType.READ, ConstraintOperationType.ALL);
    }

    protected boolean needToApplyInMemoryReadConstraints(LoadContext context) {
        return isAuthorizationRequired(context) && userSessionSource.getUserSession().hasConstraints()
                && needToApplyByPredicate(context,
                metaClass -> security.hasInMemoryConstraints(metaClass,
                        ConstraintOperationType.READ, ConstraintOperationType.ALL));
    }

    protected boolean needToApplyAttributeAccess(LoadContext context) {
        return needToApplyByPredicate(context, metaClass -> attributeSecurity.isAttributeAccessEnabled(metaClass));
    }

    protected boolean needToApplyByPredicate(LoadContext context, Predicate<MetaClass> hasConstraints) {
        if (context.getView() == null) {
            MetaClass metaClass = metadata.getSession().getClassNN(context.getMetaClass());
            return hasConstraints.test(metaClass);
        }

        Session session = metadata.getSession();
        for (Class aClass : collectEntityClasses(context.getView(), new HashSet<>())) {
            if (hasConstraints.test(session.getClassNN(aClass))) {
                return true;
            }
        }
        return false;
    }

    protected Set<Class> collectEntityClassesWithDynamicAttributes(@Nullable View view) {
        if (view == null) {
            return Collections.emptySet();
        }
        return collectEntityClasses(view, new HashSet<>()).stream()
                .filter(BaseGenericIdEntity.class::isAssignableFrom)
                .filter(aClass -> !dynamicAttributesManagerAPI.getAttributesForMetaClass(metadata.getClassNN(aClass)).isEmpty())
                .collect(Collectors.toSet());
    }

    protected Set<Class> collectEntityClasses(View view, Set<View> visited) {
        if (visited.contains(view)) {
            return Collections.emptySet();
        } else {
            visited.add(view);
        }

        HashSet<Class> classes = new HashSet<>();
        classes.add(view.getEntityClass());
        for (ViewProperty viewProperty : view.getProperties()) {
            if (viewProperty.getView() != null) {
                classes.addAll(collectEntityClasses(viewProperty.getView(), visited));
            }
        }
        return classes;
    }

    protected Transaction getLoadTransaction(boolean useCurrentTransaction) {
        TransactionParams txParams = new TransactionParams();
        if (serverConfig.getUseReadOnlyTransactionForLoad()) {
            txParams.setReadOnly(true);
        }
        return useCurrentTransaction ?
                persistence.getTransaction(storeName) : persistence.createTransaction(storeName, txParams);
    }

    protected Transaction getSaveTransaction(String storeName, boolean useCurrentTransaction) {
        return useCurrentTransaction ?
                persistence.getTransaction(storeName) : persistence.createTransaction(storeName);
    }

    protected <E extends Entity> void detachEntity(EntityManager em, @Nullable E rootEntity, View view) {
        if (rootEntity == null)
            return;
        em.detach(rootEntity);
        metadataTools.traverseAttributesByView(view, rootEntity, (entity, property) -> {
            if (property.getRange().isClass() && !metadataTools.isEmbedded(property)) {
                Object value = entity.getValue(property.getName());
                if (value != null) {
                    if (property.getRange().getCardinality().isMany()) {
                        @SuppressWarnings("unchecked")
                        Collection<Entity> collection = (Collection<Entity>) value;
                        for (Entity element : collection) {
                            em.detach(element);
                        }
                    } else {
                        em.detach((Entity) value);
                    }
                }
            }
        });
    }

    protected void mergeDynamicAttributes(BaseGenericIdEntity entity, BaseGenericIdEntity saved) {
        saved.setDynamicAttributes(entity.getDynamicAttributes());
        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (MetaProperty metaProperty : metaClass.getProperties()) {
            if (metaProperty.getRange().isClass()
                    && entityStates.isLoaded(entity, metaProperty.getName())
                    && entityStates.isLoaded(saved, metaProperty.getName())) {
                Object entityValue = entity.getValue(metaProperty.getName());
                Object savedValue = saved.getValue(metaProperty.getName());
                if (entityValue instanceof BaseGenericIdEntity
                        && savedValue instanceof BaseGenericIdEntity
                        && entityHasDynamicAttributes((BaseGenericIdEntity) entityValue)
                        && !entityHasDynamicAttributes((BaseGenericIdEntity) savedValue)) {
                    mergeDynamicAttributes((BaseGenericIdEntity) entityValue, (BaseGenericIdEntity) savedValue);
                }
                if (savedValue instanceof Collection && entityValue instanceof Collection) {
                    for (Object savedItem: (Collection) savedValue) {
                        if (savedItem instanceof BaseGenericIdEntity
                                && !entityHasDynamicAttributes((BaseGenericIdEntity) savedItem)) {
                            for (Object item : (Collection) entityValue) {
                                if (savedItem.equals(item)
                                        && entityHasDynamicAttributes((BaseGenericIdEntity) item)) {
                                    mergeDynamicAttributes((BaseGenericIdEntity) item, (BaseGenericIdEntity) savedItem);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
