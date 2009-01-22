/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.11.2008 11:13:41
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.chile.core.model.MetaClass;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class BasicInvocationContext implements Serializable
{
    private Object id;

    private String metaClass;
    private View view;
    private Query query;

    public class Query implements Serializable {
        private Map<String, Object> parameters = new HashMap<String, Object>();
        private String queryString;

        public Query(String queryString) {
            this.queryString = queryString;
        }

        public void addParameter(String name, Object value) {
            parameters.put(name, value);
        }

        public String getQueryString() {
            return queryString;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters.putAll(parameters);
        }
    }

    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(metaClass);
    }

    public BasicInvocationContext setEntityClass(Class<? extends BaseEntity> entityClass) {
        this.metaClass = MetadataProvider.getSession().getClass(entityClass).getName();
        return this;
    }

    public BasicInvocationContext setEntityClass(MetaClass entityClass) {
        this.metaClass = entityClass.getName();
        return this;
    }

    public Object getId() {
        return id;
    }

    public BasicInvocationContext setId(Object id) {
        this.id = id;
        return this;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query setQueryString(String queryString) {
        final Query query = new Query(queryString);
        setQuery(query);
        return query;
    }

    public View getView() {
        return view;
    }

    public BasicInvocationContext setView(View view) {
        this.view = view;
        return this;
    }
}
