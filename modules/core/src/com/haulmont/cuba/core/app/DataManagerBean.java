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

package com.haulmont.cuba.core.app;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Session;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesManagerAPI;
import com.haulmont.cuba.core.app.queryresults.QueryResultsManagerAPI;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.ConstraintOperationType;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.PermissionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isBlank;

@Component(DataManager.NAME)
public class DataManagerBean implements DataManager {

    private Logger log = LoggerFactory.getLogger(DataManagerBean.class);

    @Inject
    protected Metadata metadata;

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
    protected EntityLoadInfoBuilder entityLoadInfoBuilder;

    @Inject
    protected DynamicAttributesManagerAPI dynamicAttributesManagerAPI;

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        if (log.isDebugEnabled()) {
            log.debug("load: metaClass={}, id={}, view={}", context.getMetaClass(), context.getId(), context.getView());
        }

        final MetaClass metaClass = metadata.getSession().getClassNN(context.getMetaClass());

        if (!isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning null", metaClass);
            return null;
        }

        E result = null;
        boolean needToApplyInMemoryConstraints = needToApplyInMemoryConstraints(context);
        try (Transaction tx = persistence.createTransaction()) {
            final EntityManager em = persistence.getEntityManager();

            if (!context.isSoftDeletion())
                em.setSoftDeletion(false);
            persistence.getEntityManagerContext().setDbHints(context.getDbHints());

            com.haulmont.cuba.core.Query query = createQuery(em, context);
            query.setView(createRestrictedView(context));

            // If maxResults=1 and the query is not by ID we should not use getSingleResult() for backward compatibility
            boolean singleResult = !(context.getQuery() != null
                    && context.getQuery().getMaxResults() == 1
                    && context.getQuery().getQueryString() != null);

            //noinspection unchecked
            List<E> resultList = executeQuery(query, singleResult);
            if (!resultList.isEmpty()) {
                result = resultList.get(0);
            }

            if (result != null && needToApplyInMemoryConstraints && security.filterByConstraints(result)) {
                result = null;
            }

            if (result instanceof BaseGenericIdEntity && context.isLoadDynamicAttributes()) {
                dynamicAttributesManagerAPI.fetchDynamicAttributes(Collections.singletonList((BaseGenericIdEntity) result));
            }

            tx.commit();
        }

        if (result != null) {
            if (needToApplyInMemoryConstraints) {
                security.applyConstraints(result);
            }
            attributeSecurity.afterLoad(result);
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        if (log.isDebugEnabled())
            log.debug("loadList: metaClass=" + context.getMetaClass() + ", view=" + context.getView()
                    + (context.getPrevQueries().isEmpty() ? "" : ", from selected")
                    + ", query=" + (context.getQuery() == null ? null : DataServiceQueryBuilder.printQuery(context.getQuery().getQueryString()))
                    + (context.getQuery() == null || context.getQuery().getFirstResult() == 0 ? "" : ", first=" + context.getQuery().getFirstResult())
                    + (context.getQuery() == null || context.getQuery().getMaxResults() == 0 ? "" : ", max=" + context.getQuery().getMaxResults()));

        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());

        if (!isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning empty list", metaClass);
            return Collections.emptyList();
        }

        queryResultsManager.savePreviousQueryResults(context);

        List<E> resultList;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            em.setSoftDeletion(context.isSoftDeletion());
            persistence.getEntityManagerContext().setDbHints(context.getDbHints());

            boolean ensureDistinct = false;
            if (serverConfig.getInMemoryDistinct() && context.getQuery() != null) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(
                        context.getQuery().getQueryString());
                ensureDistinct = transformer.removeDistinct();
                if (ensureDistinct) {
                    context.getQuery().setQueryString(transformer.getResult());
                }
            }
            Query query = createQuery(em, context);
            query.setView(createRestrictedView(context));

            resultList = getResultList(context, query, ensureDistinct);

            // Fetch dynamic attributes
            if (context.getView() != null
                    && BaseGenericIdEntity.class.isAssignableFrom(context.getView().getEntityClass())
                    && context.isLoadDynamicAttributes()) {
                dynamicAttributesManagerAPI.fetchDynamicAttributes((List<BaseGenericIdEntity>) resultList);
            }

            tx.commit();
        }

        if (needToApplyInMemoryConstraints(context)) {
            security.applyConstraints((Collection<Entity>) resultList);
        }

        attributeSecurity.afterLoad(resultList);

        return resultList;
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        if (log.isDebugEnabled())
            log.debug("getCount: metaClass=" + context.getMetaClass()
                    + (context.getPrevQueries().isEmpty() ? "" : ", from selected")
                    + ", query=" + (context.getQuery() == null ? null : DataServiceQueryBuilder.printQuery(context.getQuery().getQueryString())));

        MetaClass metaClass = metadata.getClassNN(context.getMetaClass());

        if (!isEntityOpPermitted(metaClass, EntityOp.READ)) {
            log.debug("reading of {} not permitted, returning 0", metaClass);
            return 0;
        }

        queryResultsManager.savePreviousQueryResults(context);

        if (security.hasMemoryConstraints(metaClass, ConstraintOperationType.READ, ConstraintOperationType.ALL)) {
            context = context.copy();
            List resultList;
            try (Transaction tx = persistence.createTransaction()) {
                EntityManager em = persistence.getEntityManager();
                em.setSoftDeletion(context.isSoftDeletion());
                persistence.getEntityManagerContext().setDbHints(context.getDbHints());

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

                Query query = createQuery(em, context);
                query.setView(createRestrictedView(context));

                resultList = getResultList(context, query, ensureDistinct);
                tx.commit();
            }
            return resultList.size();
        } else {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(context.getQuery().getQueryString());
            transformer.replaceWithCount();
            context = context.copy();
            context.getQuery().setQueryString(transformer.getResult());

            Number result;
            try (Transaction tx = persistence.createTransaction()) {
                EntityManager em = persistence.getEntityManager();
                em.setSoftDeletion(context.isSoftDeletion());
                persistence.getEntityManagerContext().setDbHints(context.getDbHints());

                Query query = createQuery(em, context);
                result = (Number) query.getSingleResult();

                tx.commit();
            }

            return result.longValue();
        }
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
        return reload(entity, view, metaClass, entityHasDynamicAttributes(entity));
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

        try (Transaction tx = persistence.createTransaction()) {
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
                        checkOperationPermitted(entity, ConstraintOperationType.CREATE);
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
                        security.restoreFilteredData((BaseGenericIdEntity) entity);
                        checkOperationPermitted(entity, ConstraintOperationType.UPDATE);
                        attributeSecurity.beforeMerge(entity);
                        View view = getViewFromContext(context, entity);

                        Entity merged = em.merge(entity, view);
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
                    dynamicAttributesManagerAPI.storeDynamicAttributes(entity);
                }
            }

            // remove
            for (Entity entity : context.getRemoveInstances()) {
                security.restoreFilteredData((BaseGenericIdEntity) entity);
                checkOperationPermitted(entity, ConstraintOperationType.DELETE);

                Entity e;
                if (entity instanceof SoftDelete) {
                    attributeSecurity.beforeMerge(entity);
                    View view = getViewFromContext(context, entity);
                    e = em.merge(entity, view);
                } else {
                    e = em.merge(entity);
                }
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

                if (isAuthorizationRequired() && userSessionSource.getUserSession().hasConstraints()) {
                    security.filterByConstraints(res);
                }
            }

            tx.commit();
        }

        if (isAuthorizationRequired() && userSessionSource.getUserSession().hasConstraints()) {
            security.applyConstraints(res);
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

    protected View getViewFromContext(CommitContext context, Entity entity) {
        View view = context.getViews().get(entity);
        if (view == null) {
            view = viewRepository.getView(entity.getClass(), View.LOCAL);
        }
        return attributeSecurity.createRestrictedView(view);
    }

    protected void checkOperationPermitted(Entity entity, ConstraintOperationType operationType) {
        if (isAuthorizationRequired()
                && userSessionSource.getUserSession().hasConstraints()
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
                        && !property.isReadOnly()
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
                dynamicAttributesManagerAPI.storeDynamicAttributes((BaseGenericIdEntity) entity);
            }

            if (entityLoadInfoBuilder.contains(newInstances, entity)) {
                checkOperationPermitted(entity, ConstraintOperationType.CREATE);
                attributeSecurity.beforePersist(entity);
                em.persist(entity);
                result.add(entity);
            } else {
                security.restoreFilteredData((BaseGenericIdEntity) entity);
                checkOperationPermitted(entity, ConstraintOperationType.UPDATE);
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

    @Override
    public DataManager secure() {
        if (serverConfig.getDataManagerChecksSecurityOnMiddleware()) {
            return this;
        } else {
            return (DataManager) Proxy.newProxyInstance(
                    getClass().getClassLoader(),
                    new Class[]{DataManager.class},
                    new SecureDataManagerInvocationHandler(this)
            );
        }
    }

    protected Query createQuery(EntityManager em, LoadContext context) {
        LoadContext.Query contextQuery = context.getQuery();
        if ((contextQuery == null || isBlank(contextQuery.getQueryString()))
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

        return query;
    }

    protected View createRestrictedView(LoadContext context) {
        View view = context.getView() != null ? context.getView() :
                viewRepository.getView(metadata.getClassNN(context.getMetaClass()), View.LOCAL);
        View copy = View.copy(attributeSecurity.createRestrictedView(view));
        if (context.isLoadPartialEntities() && !needToApplyInMemoryConstraints(context)) {
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
        boolean needApplyConstraints = needToApplyInMemoryConstraints(context);
        boolean filteredByConstraints = false;
        if (needApplyConstraints) {
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
        return getResultListIteratively(context, query, set, initialSize, needApplyConstraints);
    }

    @SuppressWarnings("unchecked")
    protected <E extends Entity> List<E> getResultListIteratively(LoadContext<E> context, Query query,
                                                                  Collection<E> filteredCollection,
                                                                  int initialSize, boolean needApplyConstraints) {
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
            if (needApplyConstraints) {
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
        if (!isEntityOpPermitted(metaClass, operation))
            throw new AccessDeniedException(PermissionType.ENTITY_OP, metaClass.getName());
    }

    private boolean isEntityOpPermitted(MetaClass metaClass, EntityOp operation) {
        return !isAuthorizationRequired() || security.isEntityOpPermitted(metaClass, operation);
    }

    protected boolean isAuthorizationRequired() {
        return serverConfig.getDataManagerChecksSecurityOnMiddleware()
                || AppContext.getSecurityContextNN().isAuthorizationRequired();
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

    protected boolean needToApplyInMemoryConstraints(LoadContext context) {
        if (!isAuthorizationRequired() || !userSessionSource.getUserSession().hasConstraints()) {
            return false;
        }

        if (context.getView() == null) {
            MetaClass metaClass = metadata.getSession().getClassNN(context.getMetaClass());
            return security.hasMemoryConstraints(metaClass, ConstraintOperationType.READ, ConstraintOperationType.ALL);
        }

        Session session = metadata.getSession();
        for (Class aClass : collectEntityClasses(context.getView(), new HashSet<>())) {
            if (security.hasMemoryConstraints(session.getClassNN(aClass), ConstraintOperationType.READ, ConstraintOperationType.ALL)) {
                return true;
            }
        }
        return false;
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

    private class SecureDataManagerInvocationHandler implements InvocationHandler {

        private final DataManager impl;

        private SecureDataManagerInvocationHandler(DataManager impl) {
            this.impl = impl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean authorizationRequired = AppContext.getSecurityContextNN().isAuthorizationRequired();
            AppContext.getSecurityContextNN().setAuthorizationRequired(true);
            try {
                return method.invoke(impl, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } finally {
                AppContext.getSecurityContextNN().setAuthorizationRequired(authorizationRequired);
            }
        }
    }
}