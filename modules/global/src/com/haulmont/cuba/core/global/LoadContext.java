/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;

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
 *
 * @author krivopustov
 * @version $Id$
 */
public class LoadContext<E extends Entity> implements Serializable {

    private static final long serialVersionUID = -8808320502197308698L;

    protected String metaClass;
    protected Query query;
    protected View view;
    protected Object id;
    protected boolean softDeletion = true;
    protected List<Query> prevQueries = new ArrayList<>();
    protected int queryKey;
    protected Map<String, Object> dbHints = new HashMap<>();
    protected boolean loadDynamicAttributes;

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
        this.id = id;
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
     * @return custom hints which can be used later during query construction
     */
    public Map<String, Object> getDbHints() {
        return dbHints;
    }

    /**
     * @return whether to load dynamic attributes
     */
    public boolean getLoadDynamicAttributes() {
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
        ctx.setQuery(query.copy());
        ctx.view = view;
        ctx.id = id;
        ctx.softDeletion = softDeletion;
        ctx.prevQueries.addAll(prevQueries.stream().map(Query::copy).collect(Collectors.toList()));
        ctx.queryKey = queryKey;
        ctx.dbHints.putAll(dbHints);
        ctx.loadDynamicAttributes = loadDynamicAttributes;
        return ctx;
    }

    @Override
    public String toString() {
        return "LoadContext{" +
                "metaClass='" + metaClass + '\'' +
                ", query=" + query +
                ", view=" + view +
                ", id=" + id +
                ", softDeletion=" + softDeletion +
                '}';
    }

    /**
     * Class that defines a query to be executed for data loading.
     */
    public static class Query implements Serializable {

        private static final long serialVersionUID = 3819951144050635838L;

        private Map<String, Object> parameters = new HashMap<>();
        private String queryString;
        private int firstResult;
        private int maxResults;

        public static class TemporalValue implements Serializable {

            private static final long serialVersionUID = 4972088045550018312L;

            public final Date date;
            public final TemporalType type;

            public TemporalValue(Date date, TemporalType type) {
                this.date = date;
                this.type = type;
            }
        }
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
        public void setQueryString(String queryString) {
            this.queryString = queryString;
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
        public void setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
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

        /**
         * Creates a copy of this Query instance.
         */
        public Query copy() {
            Query query = new Query(queryString);
            query.parameters.putAll(parameters);
            query.firstResult = firstResult;
            query.maxResults = maxResults;
            return query;
        }

        @Override
        public String toString() {
            return "Query{" +
                    "queryString='" + queryString + '\'' +
                    ", firstResult=" + firstResult +
                    ", maxResults=" + maxResults +
                    '}';
        }
    }
}
