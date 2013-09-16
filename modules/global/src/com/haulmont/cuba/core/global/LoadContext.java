/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;

import java.io.Serializable;
import java.util.*;

/**
 * Class that defines parameters for loading entities from the database.
 * <p/> Used by {@link com.haulmont.cuba.core.app.DataService}
 *
 * @author krivopustov
 * @version $Id$
 */
public class LoadContext implements Serializable {

    private static final long serialVersionUID = -8808320502197308698L;

    protected String metaClass;
    protected Query query;
    protected View view;
    protected Object id;
    protected Collection<Object> ids;
    protected boolean softDeletion = true;
    protected boolean useSecurityConstraints = true;
    protected List<Query> prevQueries = new ArrayList<Query>();
    protected int queryKey;

    /**
     * @param metaClass metaclass of the loaded entities
     */
    public LoadContext(MetaClass metaClass) {
        Objects.requireNonNull(metaClass, "metaClass is null");
        this.metaClass = metaClass.getName();
    }

    /**
     * @param javaClass class of the loaded entities
     */
    public LoadContext(Class javaClass) {
        this.metaClass = AppBeans.get(Metadata.class).getSession().getClassNN(javaClass).getName();
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
     */
    public void setQuery(Query query) {
        this.query = query;
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
    public LoadContext setView(View view) {
        this.view = view;
        return this;
    }

    /**
     * @param viewName view that is used for loading entities
     * @return this instance for chaining
     */
    public LoadContext setView(String viewName) {
        Metadata metadata = AppBeans.get(Metadata.class);
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
    public LoadContext setId(Object id) {
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
    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    /**
     * @return whether to use security constraints (row-level security) when loading entities
     */
    public boolean isUseSecurityConstraints() {
        return useSecurityConstraints;
    }

    /**
     * @param useSecurityConstraints whether to use security constraints (row-level security) when loading entities
     */
    public void setUseSecurityConstraints(boolean useSecurityConstraints) {
        this.useSecurityConstraints = useSecurityConstraints;
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
    public void setQueryKey(int queryKey) {
        this.queryKey = queryKey;
    }

    @Override
    public String toString() {
        return "LoadContext{" +
                "metaClass='" + metaClass + '\'' +
                ", query=" + query +
                ", view=" + view +
                ", id=" + id +
                ", ids=" + ids +
                ", softDeletion=" + softDeletion +
                ", useSecurityConstraints=" + useSecurityConstraints +
                '}';
    }

    /**
     * Class that defines a query to be executed for data loading.
     */
    public static class Query implements Serializable {

        private static final long serialVersionUID = 3819951144050635838L;

        private Map<String, Object> parameters = new HashMap<String, Object>();
        private String queryString;
        private int firstResult;
        private int maxResults;

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
         * DEPRECATED because of bad name. Use {@link #setParameter(String, Object)}
         */
        @Deprecated
        public Query addParameter(String name, Object value) {
            return setParameter(name, value);
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
