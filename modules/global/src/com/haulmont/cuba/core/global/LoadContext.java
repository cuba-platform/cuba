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
package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.bali.util.StringHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.queryconditions.Condition;

import javax.annotation.Nullable;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that defines parameters for loading entities from the database via {@link DataManager}.
 * <p>Typical usage:
 * <pre>
    LoadContext&lt;User&gt; context = LoadContext.create(User.class).setQuery(
            LoadContext.createQuery("select u from sec$User u where u.login like :login")
                    .setParameter("login", "a%")
                    .setMaxResults(10))
            .setView("user.browse");
    List&lt;User&gt; users = dataManager.loadList(context);
 * </pre>
 * <p>
 * Instead of using this class directly, consider fluent interface with the entry point in {@link DataManager#load(Class)}.
 */
public class LoadContext<E extends Entity> implements DataLoadContext, Serializable {

    private static final long serialVersionUID = -8808320502197308698L;

    protected String metaClass;
    protected Query query;
    protected View view;
    protected Object id;
    protected List<Object> idList = new ArrayList<>(0);
    protected boolean softDeletion = true;
    protected List<Query> prevQueries = new ArrayList<>(0);
    protected int queryKey;

    protected boolean loadDynamicAttributes;
    protected boolean loadPartialEntities = true;
    protected boolean authorizationRequired;
    protected boolean joinTransaction;

    protected Map<String, Object> hints; // lazy initialized map

    /**
     * Factory method to create a LoadContext instance.
     *
     * @param entityClass   class of the loaded entities
     */
    public static <E extends Entity> LoadContext<E> create(Class<E> entityClass) {
        return new LoadContext<>(entityClass);
    }

    /**
     * Factory method to create a LoadContext.Query instance for passing into {@link #setQuery(Query)} method.
     *
     * @param queryString   JPQL query string. Only named parameters are supported.
     */
    public static LoadContext.Query createQuery(String queryString) {
        return new LoadContext.Query(queryString);
    }

    /**
     * @param metaClass metaclass of the loaded entities
     */
    public LoadContext(MetaClass metaClass) {
        Preconditions.checkNotNullArgument(metaClass, "metaClass is null");
        this.metaClass = AppBeans.get(Metadata.class).getExtendedEntities().getEffectiveMetaClass(metaClass).getName();
    }

    /**
     * @param javaClass class of the loaded entities
     */
    public LoadContext(Class<E> javaClass) {
        Preconditions.checkNotNullArgument(javaClass, "javaClass is null");
        this.metaClass = AppBeans.get(Metadata.class).getExtendedEntities().getEffectiveMetaClass(javaClass).getName();
    }

    protected LoadContext() {
    }

    /**
     * @return name of metaclass of the loaded entities
     */
    public String getMetaClass() {
        return metaClass;
    }

    /**
     * @return name of metaclass of the loaded entities
     */
    public String getEntityMetaClass() {
        return metaClass;
    }

    /**
     * @return query definition
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @param query query definition
     * @return this instance for chaining
     */
    public LoadContext<E> setQuery(Query query) {
        this.query = query;
        return this;
    }

    /**
     * @param queryString JPQL query string. Only named parameters are supported.
     * @return  query definition object
     */
    @Override
    public Query setQueryString(String queryString) {
        final Query query = new Query(queryString);
        setQuery(query);
        return query;
    }

    /**
     * @return view that is used for loading entities
     */
    public View getView() {
        return view;
    }

    /**
     * @param view view that is used for loading entities
     * @return this instance for chaining
     */
    public LoadContext<E> setView(View view) {
        this.view = view;
        return this;
    }

    /**
     * @param viewName view that is used for loading entities
     * @return this instance for chaining
     */
    public LoadContext<E> setView(String viewName) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        this.view = metadata.getViewRepository().getView(metadata.getSession().getClass(metaClass), viewName);
        return this;
    }

    /**
     * @return id of an entity to be loaded
     */
    public Object getId() {
        return id;
    }

    /**
     * @param id id of an entity to be loaded
     * @return this instance for chaining
     */
    public LoadContext<E> setId(Object id) {
        this.id = id instanceof Entity ? ((Entity) id).getId() : id; // for compatibility with legacy code relying on implicit conversions
        return this;
    }

    /**
     * @return identifiers of entities to be loaded
     */
    public List<?> getIds() {
        return idList;
    }

    /**
     *
     * @param ids identifiers of entities to be loaded
     * @return this instance for chaining
     */
    public LoadContext<E> setIds(Collection<?> ids) {
        this.idList.clear();
        this.idList.addAll(ids);
        return this;
    }

    /**
     * @return whether to use soft deletion when loading entities
     */
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    /**
     * @param softDeletion whether to use soft deletion when loading entities
     */
    public LoadContext<E> setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
        return this;
    }

    /**
     * Allows to execute query on a previous query result.
     * @return editable list of previous queries
     */
    public List<Query> getPrevQueries() {
        return prevQueries;
    }

    /**
     * @return key of the current stack of sequential queries, which is unique for the current user session
     */
    public int getQueryKey() {
        return queryKey;
    }

    /**
     * @param queryKey key of the current stack of sequential queries, which is unique for the current user session
     */
    public LoadContext<E> setQueryKey(int queryKey) {
        this.queryKey = queryKey;
        return this;
    }

    /**
     * @return custom hints which are used by the query
     */
    public Map<String, Object> getHints() {
        return hints == null ? Collections.emptyMap() : Collections.unmodifiableMap(hints);
    }

    /**
     * Sets custom hint that should be used by the query.
     * @see com.haulmont.cuba.core.global.QueryHints
     */
    public LoadContext<E> setHint(String hintName, Object value) {
        if (hints == null) {
            hints = new HashMap<>();
        }
        hints.put(hintName, value);
        return this;
    }

    /**
     * @return whether to load dynamic attributes
     */
    public boolean isLoadDynamicAttributes() {
        return loadDynamicAttributes;
    }

    /**
     * @param loadDynamicAttributes whether to load dynamic attributes
     */
    public LoadContext<E> setLoadDynamicAttributes(boolean loadDynamicAttributes) {
        this.loadDynamicAttributes = loadDynamicAttributes;
        return this;
    }

    /**
     * @return whether to load partial entities. When true (which is by default), some local attributes can be unfetched
     * according to {@link #setView(View)}.
     * <p>The state of {@link View#loadPartialEntities()} is ignored when the view is passed to {@link DataManager}.
     */
    public boolean isLoadPartialEntities() {
        return loadPartialEntities;
    }

    /**
     * Whether to load partial entities. When true (which is by default), some local attributes can be unfetched
     * according to {@link #setView(View)}.
     * <p>The state of {@link View#loadPartialEntities()} is ignored when the view is passed to {@link DataManager}.
     */
    public LoadContext<E> setLoadPartialEntities(boolean loadPartialEntities) {
        this.loadPartialEntities = loadPartialEntities;
        return this;
    }

    public boolean isAuthorizationRequired() {
        return authorizationRequired;
    }

    public LoadContext<E> setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
        return this;
    }

    public boolean isJoinTransaction() {
        return joinTransaction;
    }

    public LoadContext<E> setJoinTransaction(boolean joinTransaction) {
        this.joinTransaction = joinTransaction;
        return this;
    }

    /**
     * Creates a copy of this LoadContext instance.
     */
    public LoadContext<?> copy() {
        LoadContext<?> ctx;
        try {
            ctx = getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error copying LoadContext", e);
        }
        ctx.metaClass = metaClass;
        ctx.setQuery(query != null ? query.copy() : null);
        ctx.view = view;
        ctx.id = id;
        ctx.idList.addAll(idList);
        ctx.softDeletion = softDeletion;
        ctx.prevQueries.addAll(prevQueries.stream().map(Query::copy).collect(Collectors.toList()));
        ctx.queryKey = queryKey;
        if (hints != null) {
            ctx.getHints().putAll(hints);
        }
        ctx.loadDynamicAttributes = loadDynamicAttributes;
        ctx.authorizationRequired = authorizationRequired;
        ctx.joinTransaction = joinTransaction;
        return ctx;
    }

    @Override
    public String toString() {
        return String.format(
                "LoadContext{metaClass=%s, query=%s, view=%s, id=%s, softDeletion=%s, partialEntities=%s, dynamicAttributes=%s}",
                metaClass, query, view, id, softDeletion, loadPartialEntities, loadDynamicAttributes
        );
    }

    /**
     * Class that defines a query to be executed for data loading.
     */
    public static class Query implements DataLoadContextQuery, Serializable {

        private static final long serialVersionUID = 3819951144050635838L;

        private Map<String, Object> parameters = new HashMap<>();
        private String[] noConversionParams;
        private String queryString;
        private int firstResult;
        private int maxResults;
        private boolean cacheable;
        private Condition condition;
        private Sort sort;

        /**
         * @param queryString JPQL query string. Only named parameters are supported.
         */
        public Query(String queryString) {
            this.queryString = queryString;
        }

        /**
         * @return JPQL query string
         */
        public String getQueryString() {
            return queryString;
        }

        /**
         * @param queryString JPQL query string. Only named parameters are supported.
         */
        public Query setQueryString(String queryString) {
            this.queryString = queryString;
            return this;
        }

        /**
         * Set value for a query parameter.
         * @param name  parameter name
         * @param value parameter value
         * @return  this query instance for chaining
         */
        public Query setParameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        /**
         * Set value for a query parameter.
         * @deprecated implicit conversions are deprecated, do not use this feature
         * @param name  parameter name
         * @param value parameter value
         * @param implicitConversions whether to do parameter value conversions, e.g. convert an entity to its ID
         * @return  this query instance for chaining
         */
        @Deprecated
        public Query setParameter(String name, Object value, boolean implicitConversions) {
            parameters.put(name, value);
            if (!implicitConversions) {
                // this is a rare case, so let's save some memory by using an array instead of a list
                if (noConversionParams == null)
                    noConversionParams = new String[0];
                noConversionParams = Arrays.copyOfRange(noConversionParams, 0, noConversionParams.length + 1);
                noConversionParams[noConversionParams.length - 1] = name;
            }
            return this;
        }

        /**
         * Set value for a parameter of java.util.Date type.
         * @param name          parameter name
         * @param value         date value
         * @param temporalType  temporal type
         * @return  this query instance for chaining
         */
        public Query setParameter(String name, Date value, TemporalType temporalType) {
            parameters.put(name, new TemporalValue(value, temporalType));
            return this;
        }

        /**
         * @return editable map of the query parameters
         */
        public Map<String, Object> getParameters() {
            return parameters;
        }

        /**
         * @param parameters map of the query parameters
         */
        public Query setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
            return this;
        }

        /**
         * @param firstResult results offset
         * @return this query instance for chaining
         */
        public Query setFirstResult(int firstResult) {
            this.firstResult = firstResult;
            return this;
        }

        /**
         * @param maxResults results limit
         * @return this query instance for chaining
         */
        public Query setMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        /**
         * @return root query condition
         */
        public Condition getCondition() {
            return condition;
        }

        /**
         * @param condition root query condition
         * @return this query instance for chaining
         */
        public Query setCondition(Condition condition) {
            this.condition = condition;
            return this;
        }

        /**
         * @return query sort
         */
        public Sort getSort() {
            return sort;
        }

        /**
         * @param sort query sort
         * @return this query instance for chaining
         */
        public Query setSort(Sort sort) {
            this.sort = sort;
            return this;
        }

        /**
         * Indicates that the query results should be cached.
         * @return the same query instance
         */
        public Query setCacheable(boolean cacheable) {
            this.cacheable = cacheable;
            return this;
        }

        /**
         * @return results offset
         */
        public int getFirstResult() {
            return firstResult;
        }

        /**
         * @return results limit
         */
        public int getMaxResults() {
            return maxResults;
        }


        public boolean isCacheable() {
            return cacheable;
        }

        @Nullable
        public String[] getNoConversionParams() {
            return noConversionParams;
        }

        /**
         * Creates a copy of this Query instance.
         */
        public Query copy() {
            Query query = new Query(queryString);
            query.parameters.putAll(parameters);
            query.firstResult = firstResult;
            query.maxResults = maxResults;
            query.cacheable = cacheable;
            query.condition = condition == null ? null : condition.copy();
            query.sort = sort;
            return query;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Query query = (Query) o;

            if (firstResult != query.firstResult) return false;
            if (maxResults != query.maxResults) return false;
            if (!parameters.equals(query.parameters)) return false;
            return queryString.equals(query.queryString);
        }

        @Override
        public int hashCode() {
            int result = parameters.hashCode();
            result = 31 * result + queryString.hashCode();
            result = 31 * result + firstResult;
            result = 31 * result + maxResults;
            return result;
        }

        @Override
        public String toString() {
            String stringResult = "Query{" +
                    "queryString='" + queryString + '\'' +
                    ", condition=" + condition +
                    ", sort=" + sort +
                    ", firstResult=" + firstResult +
                    ", maxResults=" + maxResults +
                    "}";
            return StringHelper.removeExtraSpaces(stringResult.replace('\n', ' '));
        }
    }
}
