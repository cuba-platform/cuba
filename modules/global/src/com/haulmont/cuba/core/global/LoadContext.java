/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.09.2009 11:20:17
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.chile.core.model.MetaClass;

import java.io.Serializable;
import java.util.*;

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

    public LoadContext(MetaClass metaClass) {
        this.metaClass = metaClass.getName();
    }

    public LoadContext(Class javaClass) {
        this.metaClass = MetadataProvider.getSession().getClass(javaClass).getName();
    }

    public String getMetaClass() {
        return metaClass;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public View getView() {
        return view;
    }

    public LoadContext setView(View view) {
        this.view = view;
        return this;
    }

    public LoadContext setView(String viewName) {
        this.view = MetadataProvider.getViewRepository().getView(
                MetadataProvider.getSession().getClass(metaClass), viewName);
        return this;
    }

    public Object getId() {
        return id;
    }

    public LoadContext setId(Object id) {
        this.id = id;
        return this;
    }

    public Collection<Object> getIds() {
        return ids;
    }

    public void setIds(Collection<Object> ids) {
        this.ids = ids;
    }

    public Query setQueryString(String queryString) {
        final Query query = new Query(queryString);
        setQuery(query);
        return query;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    public boolean isUseSecurityConstraints() {
        return useSecurityConstraints;
    }

    public void setUseSecurityConstraints(boolean useSecurityConstraints) {
        this.useSecurityConstraints = useSecurityConstraints;
    }

    public List<Query> getPrevQueries() {
        return prevQueries;
    }

    public int getQueryKey() {
        return queryKey;
    }

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

    public static class Query implements Serializable {

        private static final long serialVersionUID = 3819951144050635838L;

        private Map<String, Object> parameters = new HashMap<String, Object>();
        private String queryString;
        private int firstResult;
        private int maxResults;

        public Query(String queryString) {
            this.queryString = queryString;
        }

        public Query addParameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public String getQueryString() {
            return queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
        }

        public Query setFirstResult(int firstResult) {
            this.firstResult = firstResult;
            return this;
        }

        public Query setMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public int getFirstResult() {
            return firstResult;
        }

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
