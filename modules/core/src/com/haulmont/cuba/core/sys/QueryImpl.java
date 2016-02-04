/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.google.common.collect.Iterables;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.persistence.DbmsFeatures;
import com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.persistence.config.CascadePolicy;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.jpa.JpaQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.TemporalType;
import java.util.*;

/**
 * Implementation of {@link TypedQuery} interface based on EclipseLink.
 *
 * @author krivopustov
 * @version $Id$
 */
public class QueryImpl<T> implements TypedQuery<T> {

    private Logger log = LoggerFactory.getLogger(QueryImpl.class);

    private Metadata metadata;
    private javax.persistence.EntityManager emDelegate;
    private JpaQuery query;
    private boolean isNative;
    private String queryString;
    private Class<? extends Entity> resultClass;
    private FetchGroupManager fetchGroupMgr;
    private Set<Param> params = new HashSet<>();
    private LockModeType lockMode;
    private List<View> views = new ArrayList<>();
    private Integer maxResults;
    private Integer firstResult;
    private boolean singleResultExpected;

    private Collection<QueryMacroHandler> macroHandlers;

    public QueryImpl(EntityManagerImpl entityManager, boolean isNative, @Nullable Class resultClass,
                     Metadata metadata, FetchGroupManager fetchGroupMgr) {
        this.metadata = metadata;
        this.emDelegate = entityManager.getDelegate();
        this.isNative = isNative;
        this.macroHandlers = AppBeans.getAll(QueryMacroHandler.class).values();
        //noinspection unchecked
        this.resultClass = resultClass;
        this.fetchGroupMgr = fetchGroupMgr;
    }

    private JpaQuery<T> getQuery() {
        if (query == null) {
            if (isNative) {
                log.trace("Creating SQL query: " + queryString);
                if (resultClass == null)
                    query = (JpaQuery) emDelegate.createNativeQuery(queryString);
                else {
                    Class effectiveClass = metadata.getExtendedEntities().getEffectiveClass(resultClass);
                    query = (JpaQuery) emDelegate.createNativeQuery(queryString, effectiveClass);
                }
            } else {
                log.trace("Creating JPQL query: " + queryString);
                String s = transformQueryString();
                log.trace("Transformed JPQL query: " + s);
                if (resultClass != null) {
                    query = (JpaQuery) emDelegate.createQuery(s, resultClass);
                } else {
                    query = (JpaQuery) emDelegate.createQuery(s);
                }
                if (!views.isEmpty()) {
                    query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                    query.setHint(QueryHints.REFRESH_CASCADE, CascadePolicy.CascadeByMapping);
                }
            }
            query.setFlushMode(FlushModeType.COMMIT);

            boolean nullParam = false;
            for (Param param : params) {
                param.apply(query);
                if (param.value == null)
                    nullParam = true;
            }
            // disable SQL caching to support "is null" generation
            if (nullParam)
                query.setHint(QueryHints.PREPARE, HintValues.FALSE);

            if (maxResults != null)
                query.setMaxResults(maxResults);
            if (firstResult != null)
                query.setFirstResult(firstResult);
            if (lockMode != null)
                query.setLockMode(lockMode);

            for (int i = 0; i < views.size(); i++) {
                if (i == 0)
                    fetchGroupMgr.setView(query, queryString, views.get(i), singleResultExpected);
                else
                    fetchGroupMgr.addView(query, queryString, views.get(i), singleResultExpected);
            }

            addMacroParams(query);

        }
        //noinspection unchecked
        return query;
    }

    private void checkState() {
        if (query != null)
            throw new IllegalStateException("Query delegate has already been created");
    }

    private String transformQueryString() {
        String result = expandMacros(queryString);

        QueryParser parser = QueryTransformerFactory.createParser(result);

        String entityName = parser.getEntityName();
        Class effectiveClass = metadata.getExtendedEntities().getEffectiveClass(entityName);
        String effectiveEntityName = metadata.getSession().getClassNN(effectiveClass).getName();
        if (!effectiveEntityName.equals(entityName)) {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(result);
            transformer.replaceEntityName(effectiveEntityName);
            result = transformer.getResult();
        }

        for (Iterator<Param> iterator = params.iterator(); iterator.hasNext(); ) {
            Param param = iterator.next();
            if (param.value instanceof String && ((String) param.value).startsWith("(?i)")) {
                result = replaceCaseInsensitiveParam(result, param);
            } else if ((param.value instanceof Collection && CollectionUtils.isEmpty((Collection) param.value))) {
                QueryTransformer transformer = QueryTransformerFactory.createTransformer(result);
                transformer.replaceInCondition((String) param.name);
                result = transformer.getResult();
                iterator.remove();
            }
        }

        String nestedEntityName = parser.getEntityNameIfSecondaryReturnedInsteadOfMain();
        if (nestedEntityName != null) {
            QueryTransformer transformer = QueryTransformerFactory.createTransformer(result);
            transformer.replaceWithSelectId();
            transformer.removeOrderBy();
            result = String.format("select tempEntity from %s tempEntity where tempEntity.id in (%s)",
                    nestedEntityName, transformer.getResult());
        }

        return result;
    }

    private String expandMacros(String queryStr) {
        String result = queryStr;
        if (macroHandlers != null) {
            for (QueryMacroHandler handler : macroHandlers) {
                result = handler.expandMacro(result);
            }
        }
        return result;
    }

    private String replaceCaseInsensitiveParam(String queryStr, Param param) {
        if (!(param.name instanceof String)) // case insensitive search is supported only for named parameters
            return queryStr;

        QueryTransformer transformer = QueryTransformerFactory.createTransformer(queryStr);
        transformer.handleCaseInsensitiveParam((String) param.name);
        String result = transformer.getResult();

        param.value = ((String) param.value).substring(4).toLowerCase();
        return result;
    }

    private void addMacroParams(javax.persistence.TypedQuery jpaQuery) {
        if (macroHandlers != null) {
            for (QueryMacroHandler handler : macroHandlers) {

                Map<String, Object> namedParams = new HashMap<>();
                for (Param param : params) {
                    if (param.name instanceof String)
                        namedParams.put((String) param.name, param.value);
                }
                handler.setQueryParams(namedParams);

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

        return getQuery().getResultList();
    }

    @Override
    public T getSingleResult() {
        if (log.isDebugEnabled())
            log.debug(queryString.replaceAll("[\\t\\n\\x0B\\f\\r]", " "));

        singleResultExpected = true;

        return getQuery().getSingleResult();
    }

    @Override
    @Nullable
    public T getFirstResult() {
        if (log.isDebugEnabled())
            log.debug(queryString.replaceAll("[\\t\\n\\x0B\\f\\r]", " "));

        Integer saveMaxResults = maxResults;
        maxResults = 1;
        try {
            List<T> resultList = getQuery().getResultList();
            return Iterables.getFirst(resultList, null);
        } finally {
            maxResults = saveMaxResults;
        }
    }

    @Override
    public int executeUpdate() {
        return getQuery().executeUpdate();
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
        return setParameter(name, value, true);
    }

    @Override
    public TypedQuery<T> setParameter(String name, Object value, boolean implicitConversions) {
        checkState();

        if (implicitConversions) {
            value = handleImplicitConversions(value);
        }
        params.add(new Param(name, value));
        return this;
    }

    private Object handleImplicitConversions(Object value) {
        if (value instanceof Entity)
            value = ((Entity) value).getId();
        else if (value instanceof Collection) {
            List<Object> list = new ArrayList<>(((Collection) value).size());
            for (Object obj : ((Collection) value)) {
                list.add(obj instanceof Entity ? ((Entity) obj).getId() : obj);
            }
            value = list;
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
        return setParameter(position, value, true);
    }

    @Override
    public TypedQuery<T> setParameter(int position, Object value, boolean implicitConversions) {
        checkState();
        DbmsFeatures dbmsFeatures = DbmsSpecificFactory.getDbmsFeatures();
        if (isNative && (value instanceof UUID) && (dbmsFeatures.getUuidTypeClassName() != null)) {
            Class c = ReflectionHelper.getClass(dbmsFeatures.getUuidTypeClassName());
            try {
                value = ReflectionHelper.newInstance(c, value);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Error setting parameter value", e);
            }
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

    public void setSingleResultExpected(boolean singleResultExpected) {
        this.singleResultExpected = singleResultExpected;
    }

    protected static class Param {
        private Object name;
        private Object value;
        private TemporalType temporalType;

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
                if (name instanceof Integer)
                    query.setParameter((int) name, value);
                else
                    query.setParameter((String) name, value);
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
