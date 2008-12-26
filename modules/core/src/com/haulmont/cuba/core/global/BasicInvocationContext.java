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
    private static final long serialVersionUID = -6533204272933592530L;

    private Class<? extends BaseEntity> entityClass;
    private Object id;
    private String queryString;
    private Map<String, Object> queryParams = new HashMap<String, Object>();
    private View view;

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }

    public BasicInvocationContext setEntityClass(Class<? extends BaseEntity> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public MetaClass getMetaClass() {
        return MetadataProvider.getSession().getClass(entityClass);
    }

    public Object getId() {
        return id;
    }

    public BasicInvocationContext setId(Object id) {
        this.id = id;
        return this;
    }

    public String getQueryString() {
        return queryString;
    }

    public BasicInvocationContext setQueryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public BasicInvocationContext addQueryParam(String name, Object value) {
        queryParams.put(name, value);
        return this;
    }

    public View getView() {
        return view;
    }

    public BasicInvocationContext setView(View view) {
        this.view = view;
        return this;
    }
}
