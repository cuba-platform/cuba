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
package com.haulmont.cuba.core.sys;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.IdProxy;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.entity.contracts.Ids;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.entitycache.QueryCacheManager;
import com.haulmont.cuba.core.sys.entitycache.QueryKey;
import com.haulmont.cuba.core.sys.persistence.DbmsFeatures;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import com.haulmont.cuba.core.sys.persistence.PersistenceImplSupport;
import org.eclipse.persistence.config.CascadePolicy;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.internal.helper.ClassConstants;
import org.eclipse.persistence.internal.helper.ConversionManager;
import org.eclipse.persistence.internal.helper.CubaUtil;
import org.eclipse.persistence.internal.jpa.EJBQueryImpl;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.ObjectLevelReadQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.*;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TypedQuery} interface based on EclipseLink.
 */
@Component(com.haulmont.cuba.core.Query.NAME)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class QueryImpl<T> implements TypedQuery<T> {

    private final Logger log = LoggerFactory.getLogger(QueryImpl.class);

    @Inject
    protected Metadata metadata;
    @Inject
    protected PersistenceImplSupport support;
    @Inject
    protected FetchGroupManager fetchGroupMgr;
    @Inject
    protected EntityFetcher entityFetcher;
    @Inject
    protected QueryCacheManager queryCacheMgr;
    @Inject
    protected QueryTransformerFactory queryTransformerFactory;
    @Inject
    protected ServerConfig serverConfig;
    @Inject
    protected QueryHintsProcessor hintsProcessor;

    protected javax.persistence.EntityManager emDelegate;
    protected JpaQuery query;
    protected EntityManagerImpl entityManager;
    protected boolean isNative;
    protected String queryString;
    protected String transformedQueryString;
    protected Class resultClass;
    protected Set<Param> params = new HashSet<>();
    protected Map<String, Object> hints;
    protected LockModeType lockMode;
    protected List<View> views = new ArrayList<>();
    protected Integer maxResults;
    protected Integer firstResult;
    protected boolean singleResultExpected;
    protected boolean cacheable;
    protected FlushModeType flushMode;

    protected Collection<QueryMacroHandler> macroHandlers;

    public QueryImpl(EntityManagerImpl entityManager, boolean isNative, @Nullable Class resultClass) {
        this.entityManager = entityManager;
        this.emDelegate = entityManager.getDelegate();
        this.isNative = isNative;
        this.macroHandlers = AppBeans.getAll(QueryMacroHandler.class).values();
        //noinspection unchecked
        this.resultClass = resultClass;
    }

    protected JpaQuery<T> getQuery() {
        if (query == null) {
            View view = views.isEmpty() ? null : views.get(0);

            if (isNative) {
                log.trace("Creating SQL query: {}", queryString);
                if (resultClass == null)
                    query = (JpaQuery) emDelegate.createNativeQuery(queryString);
                else {
                    if (!Entity.class.isAssignableFrom(resultClass)) {
                        throw new IllegalArgumentException("Non-entity result class for native query is not supported" +
                                " by EclipseLink: " + resultClass);
                    }
                    Class effectiveClass = metadata.getExtendedEntities().getEffectiveClass(resultClass);
                    query = (JpaQuery) emDelegate.createNativeQuery(queryString, effectiveClass);
                }
            } else {
                log.trace("Creating JPQL query: {}", queryString);
                transformedQueryString = transformQueryString();
                log.trace("Transformed JPQL query: {}", transformedQueryString);

                Class effectiveClass = getEffectiveResultClass();
                query = buildJPAQuery(transformedQueryString, effectiveClass);
                if (view != null) {
                    MetaClass metaClass = metadata.getClassNN(view.getEntityClass());
                    if (!metadata.getTools().isCacheable(metaClass) || !singleResultExpected) {
                        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                        query.setHint(QueryHints.REFRESH_CASCADE, CascadePolicy.CascadeByMapping);
                    }
                }
            }

            if (flushMode == null) {
                if (view != null && !view.loadPartialEntities()) {
                    query.setFlushMode(FlushModeType.AUTO);
                } else {
                    query.setFlushMode(FlushModeType.COMMIT);
                }
            } else {
                query.setFlushMode(flushMode);
            }

            boolean nullParam = false;
            for (Param param : params) {
                param.apply(query);
                if (param.value == null)
                    nullParam = true;
            }

            addMacroParams(query);

            // disable SQL caching to support "is null" generation
            if (nullParam)
                query.setHint(QueryHints.PREPARE, HintValues.FALSE);

            // Set maxResults and firstResult only if the query is not by ID, otherwise EclipseLink does not select
            // nested collections in some cases
            if (maxResults != null && !singleResultExpected)
                query.setMaxResults(maxResults);
            if (firstResult != null && !singleResultExpected)
                query.setFirstResult(firstResult);

            if (lockMode != null)
                query.setLockMode(lockMode);

            if (hints != null) {
                for (Map.Entry<String, Object> hint : hints.entrySet()) {
                    hintsProcessor.applyQueryHint(query, hint.getKey(), hint.getValue());
                }
            }

            for (int i = 0; i < views.size(); i++) {
                if (i == 0)
                    fetchGroupMgr.setView(query, queryString, views.get(i), singleResultExpected);
                else
                    fetchGroupMgr.addView(query, queryString, views.get(i), singleResultExpected);
            }
        }
        //noinspection unchecked
        return query;
    }

    @Nullable
    protected Class getEffectiveResultClass() {
        if (resultClass == null) {
            return null;
        }
        if (Entity.class.isAssignableFrom(resultClass)) {
            return metadata.getExtendedEntities().getEffectiveClass(resultClass);
        }
        return resultClass;
    }

    protected JpaQuery buildJPAQuery(String queryString, Class<T> resultClass) {
        boolean useJPQLCache = true;
        View view = views.isEmpty() ? null : views.get(0);
        if (view != null) {
            boolean useFetchGroup = view.loadPartialEntities();
            for (View it : views) {
                FetchGroupDescription description = fetchGroupMgr.calculateFetchGroup(queryString, it, singleResultExpected, useFetchGroup);
                if (description.hasBatches()) {
                    useJPQLCache = false;
                    break;
                }
            }
        }
        if (!useJPQLCache) {
            CubaUtil.setEnabledJPQLParseCache(false);
        }
        try {
            if (resultClass != null) {
                return (JpaQuery) emDelegate.createQuery(queryString, resultClass);
            } else {
                return (JpaQuery) emDelegate.createQuery(queryString);
            }
        } finally {
            CubaUtil.setEnabledJPQLParseCache(true);
        }
    }

    protected void checkState() {
        if (query != null)
            throw new IllegalStateException("Query delegate has already been created");
    }

    protected String transformQueryString() {
        String result = expandMacros(queryString);

        boolean rebuildParser = false;
        QueryParser parser = queryTransformerFactory.parser(result);

        String entityName = parser.getEntityName();
        Class effectiveClass = metadata.getExtendedEntities().getEffectiveClass(entityName);
        MetaClass effectiveMetaClass = metadata.getClassNN(effectiveClass);
        String effectiveEntityName = effectiveMetaClass.getName();
        if (!effectiveEntityName.equals(entityName)) {
            QueryTransformer transformer = queryTransformerFactory.transformer(result);
            transformer.replaceEntityName(effectiveEntityName);
            result = transformer.getResult();
            rebuildParser = true;
        }

        if (firstResult != null && firstResult > 0) {
            String storeName = metadata.getTools().getStoreName(effectiveMetaClass);
            DbmsFeatures dbmsFeatures = DbmsSpecificFactory.getDbmsFeatures(storeName);
            if (dbmsFeatures.useOrderByForPaging()) {
                QueryTransformer transformer = queryTransformerFactory.transformer(result);
                transformer.addOrderByIdIfNotExists(metadata.getTools().getPrimaryKeyName(effectiveMetaClass));
                result = transformer.getResult();
                rebuildParser = true;
            }
        }

        result = replaceParams(result, parser);

        if (rebuildParser) {
            parser = queryTransformerFactory.parser(result);
        }
        String nestedEntityName = parser.getOriginalEntityName();
        String nestedEntityPath = parser.getOriginalEntityPath();
        if (nestedEntityName != null) {
            if (parser.isCollectionOriginalEntitySelect()) {
                throw new IllegalStateException(String.format("Collection attributes are not supported in select clause: %s", nestedEntityPath));
            }
            QueryTransformer transformer = queryTransformerFactory.transformer(result);
            transformer.replaceWithSelectEntityVariable("tempEntityAlias");
            transformer.addFirstSelectionSource(String.format("%s tempEntityAlias", nestedEntityName));
            transformer.addWhereAsIs(String.format("tempEntityAlias.id = %s.id", nestedEntityPath));
            transformer.addEntityInGroupBy("tempEntityAlias");
            result = transformer.getResult();
        }

        result = replaceIsNullAndIsNotNullStatements(result);

        return result;
    }

    protected String expandMacros(String queryStr) {
        String result = queryStr;
        if (macroHandlers != null) {
            for (QueryMacroHandler handler : macroHandlers) {
                result = handler.expandMacro(result);
            }
        }
        return result;
    }

    protected String replaceParams(String query, QueryParser parser) {
        String result = query;
        Set<String> paramNames = Sets.newHashSet(parser.getParamNames());
        for (Iterator<Param> iterator = params.iterator(); iterator.hasNext(); ) {
            Param param = iterator.next();
            String paramName = param.name.toString();
            if (param.value instanceof String) {
                String strValue = (String) param.value;
                if (strValue.startsWith("(?i)")) {
                    result = replaceCaseInsensitiveParam(result, paramName);
                    param.value = strValue.substring(4).toLowerCase();
                }
            }
            if (param.isNamedParam()) {
                paramNames.remove(paramName);
                if (param.value instanceof Collection) {
                    Collection collectionValue = (Collection) param.value;
                    if (collectionValue.isEmpty()) {
                        result = replaceInCollectionParam(result, paramName);
                        iterator.remove();
                    }
                }
                if (param.value == null) {
                    if (parser.isParameterInCondition(paramName)) {
                        result = replaceInCollectionParam(result, paramName);
                        iterator.remove();
                    }
                }
            }
        }
        for (String paramName : paramNames) {
            result = replaceInCollectionParam(result, paramName);
        }
        return result;
    }

    protected String replaceCaseInsensitiveParam(String query, String paramName) {
        QueryTransformer transformer = queryTransformerFactory.transformer(query);
        transformer.handleCaseInsensitiveParam(paramName);
        return transformer.getResult();
    }

    protected String replaceInCollectionParam(String query, String paramName) {
        QueryTransformer transformer = queryTransformerFactory.transformer(query);
        transformer.replaceInCondition(paramName);
        return transformer.getResult();
    }

    protected String replaceIsNullAndIsNotNullStatements(String query) {
        Set<Param> replacedParams = new HashSet<>();

        QueryTransformer transformer = queryTransformerFactory.transformer(query);
        params.stream()
                .filter(Param::isNamedParam)
                .map(param -> Maps.immutableEntry(param, transformer.replaceIsNullStatements(
                        param.name.toString(), param.value == null)))
                .filter(Map.Entry::getValue)
                .forEach(entry -> replacedParams.add(entry.getKey()));

        if (replacedParams.isEmpty()) {
            return query;
        }

        String resultQuery = transformer.getResult();

        QueryParser parser = queryTransformerFactory.parser(resultQuery);
        params.removeAll(replacedParams.stream()
                .filter(param -> !parser.isParameterUsedInAnyCondition(param.name.toString()))
                .collect(Collectors.toSet()));

        return resultQuery;
    }

    protected void addMacroParams(javax.persistence.TypedQuery jpaQuery) {
        if (macroHandlers != null) {
            for (QueryMacroHandler handler : macroHandlers) {

                Map<String, Object> namedParams = new HashMap<>();
                for (Param param : params) {
                    if (param.name instanceof String)
                        namedParams.put((String) param.name, param.value);
                }

                Map<String, Class> paramsTypes = new HashMap<>();
                for (Parameter<?> parameter : jpaQuery.getParameters()) {
                    if (parameter.getName() != null) {
                        paramsTypes.put(parameter.getName(), parameter.getParameterType());
                    }
                }
                handler.setQueryParams(namedParams);
                handler.setExpandedParamTypes(paramsTypes);

                for (Map.Entry<String, Object> entry : handler.getParams().entrySet()) {
                    jpaQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    @Override
    public List<T> getResultList() {
        if (log.isDebugEnabled())
            log.debug(queryString.replaceAll("[\\t\\n\\x0B\\f\\r]", " "));

        singleResultExpected = false;

        JpaQuery<T> query = getQuery();
        preExecute(query);
        @SuppressWarnings("unchecked")
        List<T> resultList = (List<T>) getResultFromCache(query, false, obj -> {
            ((List) obj).stream().filter(item -> item instanceof Entity).forEach(item -> {
                for (View view : views) {
                    entityFetcher.fetch((Entity) item, view);
                }
            });
        });
        return resultList;
    }

    @Override
    public T getSingleResult() {
        if (log.isDebugEnabled())
            log.debug(queryString.replaceAll("[\\t\\n\\x0B\\f\\r]", " "));

        singleResultExpected = true;

        JpaQuery<T> jpaQuery = getQuery();
        preExecute(jpaQuery);
        @SuppressWarnings("unchecked")
        T result = (T) getResultFromCache(jpaQuery, true, obj -> {
            if (obj instanceof Entity) {
                for (View view : views) {
                    entityFetcher.fetch((Entity) obj, view);
                }
            }
        });
        return result;
    }

    @Override
    @Nullable
    public T getFirstResult() {
        if (log.isDebugEnabled())
            log.debug(queryString.replaceAll("[\\t\\n\\x0B\\f\\r]", " "));

        Integer saveMaxResults = maxResults;
        maxResults = 1;
        try {
            JpaQuery<T> query = getQuery();
            preExecute(query);
            @SuppressWarnings("unchecked")
            List<T> resultList = (List<T>) getResultFromCache(query, false, obj -> {
                List list = (List) obj;
                if (!list.isEmpty()) {
                    Object item = list.get(0);
                    if (item instanceof Entity) {
                        for (View view : views) {
                            entityFetcher.fetch((Entity) item, view);
                        }
                    }
                }
            });
            if (resultList.isEmpty()) {
                return null;
            } else {
                return resultList.get(0);
            }
        } finally {
            maxResults = saveMaxResults;
        }
    }

    @Override
    public int executeUpdate() {
        JpaQuery<T> jpaQuery = getQuery();
        DatabaseQuery databaseQuery = jpaQuery.getDatabaseQuery();
        Class referenceClass = databaseQuery.getReferenceClass();
        boolean isDeleteQuery = databaseQuery.isDeleteObjectQuery() || databaseQuery.isDeleteAllQuery();
        boolean enableDeleteInSoftDeleteMode =
                Boolean.parseBoolean(AppContext.getProperty("cuba.enableDeleteStatementInSoftDeleteMode"));
        if (!enableDeleteInSoftDeleteMode && entityManager.isSoftDeletion() && isDeleteQuery) {
            if (SoftDelete.class.isAssignableFrom(referenceClass)) {
                throw new UnsupportedOperationException("Delete queries are not supported with enabled soft deletion. " +
                        "Use 'cuba.enableDeleteStatementInSoftDeleteMode' application property to roll back to legacy behavior.");
            }
        }
        // In some cache configurations (in particular, when shared cache is on, but for some entities cache is set to ISOLATED),
        // EclipseLink does not evict updated entities from cache automatically.
        Cache cache = jpaQuery.getEntityManager().getEntityManagerFactory().getCache();
        if (referenceClass != null) {
            cache.evict(referenceClass);
            queryCacheMgr.invalidate(referenceClass, true);
        } else {
            cache.evictAll();
            queryCacheMgr.invalidateAll(true);
        }
        preExecute(jpaQuery);
        return jpaQuery.executeUpdate();
    }

    @Override
    public TypedQuery<T> setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        if (query != null)
            query.setMaxResults(maxResults);
        return this;
    }

    @Override
    public TypedQuery<T> setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        if (query != null)
            query.setFirstResult(firstResult);
        return this;
    }

    @Override
    public TypedQuery<T> setParameter(String name, Object value) {
        return internalSetParameter(name, value, serverConfig.getImplicitConversionOfJpqlParams());
    }

    @Override
    public TypedQuery<T> setParameter(String name, Object value, boolean implicitConversions) {
        return internalSetParameter(name, value, implicitConversions);
    }

    @SuppressWarnings("unchecked")
    protected TypedQuery<T> internalSetParameter(String name, Object value, boolean implicitConversions) {
        checkState();

        if (value instanceof IdProxy) {
            value = ((IdProxy) value).get();

        } else if (value instanceof Id) {
            value = ((Id) value).getValue();

        } else if (value instanceof Ids) {
            value = ((Ids) value).getValues();

        } else if (value instanceof EnumClass) {
            value = ((EnumClass) value).getId();

        } else if (isCollectionOfEntitiesOrEnums(value)) {
            value = convertToCollectionOfIds(value);

        } else if (implicitConversions) {
            value = handleImplicitConversions(value);
        }
        params.add(new Param(name, value));
        return this;
    }

    protected Object handleImplicitConversions(Object value) {
        if (value instanceof Entity)
            value = ((Entity) value).getId();
        else if (value instanceof Collection) {
            List<Object> list = new ArrayList<>(((Collection) value).size());
            for (Object obj : ((Collection) value)) {
                list.add(obj instanceof Entity ? ((Entity) obj).getId() : obj);
            }
            value = list;
        } else if (value instanceof EnumClass) {
            value = ((EnumClass) value).getId();
        }
        return value;
    }

    @Override
    public TypedQuery<T> setParameter(String name, Date value, TemporalType temporalType) {
        checkState();
        params.add(new Param(name, value, temporalType));
        return this;
    }

    @Override
    public TypedQuery<T> setParameter(int position, Object value) {
        return internalSetParameter(position, value, serverConfig.getImplicitConversionOfJpqlParams());
    }

    @Override
    public TypedQuery<T> setParameter(int position, Object value, boolean implicitConversions) {
        return internalSetParameter(position, value, implicitConversions);
    }

    @SuppressWarnings("unchecked")
    protected TypedQuery<T> internalSetParameter(int position, Object value, boolean implicitConversions) {
        checkState();
        DbmsFeatures dbmsFeatures = DbmsSpecificFactory.getDbmsFeatures();
        if (isNative && (value instanceof UUID) && (dbmsFeatures.getUuidTypeClassName() != null)) {
            Class c = ReflectionHelper.getClass(dbmsFeatures.getUuidTypeClassName());
            try {
                value = ReflectionHelper.newInstance(c, value);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error setting parameter value", e);
            }

        } else if (value instanceof IdProxy) {
            value = ((IdProxy) value).get();

        } else if (value instanceof EnumClass) {
            value = ((EnumClass) value).getId();

        } else if (isCollectionOfEntitiesOrEnums(value)) {
            value = convertToCollectionOfIds(value);

        } else if (implicitConversions) {
            value = handleImplicitConversions(value);
        }

        params.add(new Param(position, value));
        return this;
    }

    @Override
    public TypedQuery<T> setParameter(int position, Date value, TemporalType temporalType) {
        checkState();
        params.add(new Param(position, value, temporalType));
        return this;
    }

    @Override
    public TypedQuery<T> setLockMode(LockModeType lockMode) {
        checkState();
        this.lockMode = lockMode;
        return this;
    }

    @Override
    public TypedQuery<T> setView(View view) {
        if (isNative)
            throw new UnsupportedOperationException("Views are not supported for native queries");
        checkState();
        views.clear();
        views.add(view);
        return this;
    }

    @Override
    public TypedQuery<T> setViewName(String viewName) {
        if (resultClass == null)
            throw new IllegalStateException("resultClass is null");

        setView(metadata.getViewRepository().getView(resultClass, viewName));
        return this;
    }

    @Override
    public TypedQuery<T> setView(Class<? extends Entity> entityClass, String viewName) {
        setView(metadata.getViewRepository().getView(entityClass, viewName));
        return this;
    }

    @Override
    public TypedQuery<T> addView(View view) {
        if (isNative)
            throw new UnsupportedOperationException("Views are not supported for native queries");
        checkState();
        views.add(view);
        return this;
    }

    @Override
    public TypedQuery<T> addViewName(String viewName) {
        if (resultClass == null)
            throw new IllegalStateException("resultClass is null");

        addView(metadata.getViewRepository().getView(resultClass, viewName));
        return this;
    }

    @Override
    public TypedQuery<T> addView(Class<? extends Entity> entityClass, String viewName) {
        addView(metadata.getViewRepository().getView(entityClass, viewName));
        return this;
    }

    @Override
    public javax.persistence.Query getDelegate() {
        return getQuery();
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public TypedQuery<T> setQueryString(String queryString) {
        checkState();
        this.queryString = queryString;
        return this;
    }

    @Override
    public TypedQuery<T> setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    @Override
    public TypedQuery<T> setFlushMode(FlushModeType flushMode) {
        this.flushMode = flushMode;
        return this;
    }

    @Override
    public TypedQuery<T> setHint(String hintName, Object value) {
        if (hints == null) {
            hints = new HashMap<>();
        }
        hints.put(hintName, value);
        return this;
    }

    public void setSingleResultExpected(boolean singleResultExpected) {
        this.singleResultExpected = singleResultExpected;
    }

    protected void preExecute(JpaQuery jpaQuery) {
        // copying behaviour of org.eclipse.persistence.internal.jpa.QueryImpl.executeReadQuery()
        DatabaseQuery elDbQuery = ((EJBQueryImpl) jpaQuery).getDatabaseQueryInternal();
        boolean isObjectLevelReadQuery = elDbQuery.isObjectLevelReadQuery();
        if (jpaQuery.getFlushMode() == FlushModeType.AUTO
                && (!isObjectLevelReadQuery || !((ObjectLevelReadQuery) elDbQuery).isReadOnly())) {
            // flush is expected
            support.processFlush(entityManager, true);
        }
    }

    protected Object getResultFromCache(JpaQuery jpaQuery, boolean singleResult, Consumer<Object> fetcher) {
        Preconditions.checkNotNull(fetcher);
        boolean useQueryCache = cacheable && !isNative && queryCacheMgr.isEnabled() && lockMode == null;
        Object result;
        if (useQueryCache) {
            QueryParser parser = QueryTransformerFactory.createParser(transformedQueryString);
            String entityName = parser.getEntityName();
            useQueryCache = parser.isEntitySelect(entityName);
            QueryKey queryKey = null;
            if (useQueryCache) {
                queryKey = QueryKey.create(transformedQueryString, entityManager.isSoftDeletion(), singleResult, jpaQuery);
                result = singleResult ? queryCacheMgr.getSingleResultFromCache(queryKey, views) :
                        queryCacheMgr.getResultListFromCache(queryKey, views);
                if (result != null) {
                    return result;
                }
            }
            try {
                result = singleResult ? jpaQuery.getSingleResult() : jpaQuery.getResultList();
            } catch (NoResultException | NonUniqueResultException ex) {
                if (useQueryCache && singleResult) {
                    queryCacheMgr.putResultToCache(queryKey, null, entityName, parser.getAllEntityNames(), ex);
                }
                throw ex;
            }
            fetcher.accept(result);
            if (useQueryCache) {
                queryCacheMgr.putResultToCache(queryKey,
                        singleResult ? Collections.singletonList(result) : (List) result,
                        entityName, parser.getAllEntityNames());
            }
        } else {
            result = singleResult ? jpaQuery.getSingleResult() : jpaQuery.getResultList();
            fetcher.accept(result);
        }
        return result;
    }

    protected boolean isCollectionOfEntitiesOrEnums(Object value) {
        return value instanceof Collection
                && ((Collection<?>) value).stream().allMatch(it -> it instanceof Entity || it instanceof EnumClass);
    }

    protected Object convertToCollectionOfIds(Object value) {
        return ((Collection<?>) value).stream()
                .map(it -> it instanceof Entity ? ((Entity) it).getId() : ((EnumClass) it).getId())
                .collect(Collectors.toList());
    }

    protected static class Param {
        protected Object name;
        protected Object value;
        protected TemporalType temporalType;

        private Class<?> actualParamType;

        public Param(Object name, Object value) {
            this.name = name;
            this.value = value;
        }

        public Param(Object name, Date value, TemporalType temporalType) {
            this.name = name;
            this.value = value;
            this.temporalType = temporalType;
        }

        public void apply(JpaQuery query) {
            if (temporalType != null) {
                if (name instanceof Integer)
                    query.setParameter((int) name, (Date) value, temporalType);
                else
                    query.setParameter((String) name, (Date) value, temporalType);
            } else {
                if (value instanceof Date && !isValidParamType(query))
                    convertValue();

                if (name instanceof Integer)
                    query.setParameter((int) name, value);
                else
                    query.setParameter((String) name, value);
            }
        }

        public boolean isNamedParam() {
            return name instanceof String;
        }

        private boolean isValidParamType(JpaQuery query) {
            if (value == null || query.getDatabaseQuery() == null)
                return true;

            int index = query.getDatabaseQuery().getArguments().indexOf(String.valueOf(name));
            if (index == -1)
                return true;
            actualParamType = query.getDatabaseQuery().getArgumentTypes().get(index);

            if (actualParamType == null)
                return true;

            return actualParamType.isAssignableFrom(value.getClass());
        }

        private void convertValue() {
            if (value == null || actualParamType == null || actualParamType.isAssignableFrom(value.getClass()))
                return;

            // Since ConversionManager incorrectly converts Date into LocalDate or LocalDateTime
            if (value instanceof java.util.Date) {
                if (actualParamType == ClassConstants.TIME_LDATE) {
                    java.util.Date date = (java.util.Date) value;
                    value = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return;
                } else if (actualParamType == ClassConstants.TIME_LDATETIME) {
                    java.util.Date date = (java.util.Date) value;
                    value = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return;
                }
            }

            ConversionManager conversionManager = ConversionManager.getDefaultManager();
            try {
                value = conversionManager.convertObject(value, actualParamType);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Param param = (Param) o;
            return name.equals(param.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
