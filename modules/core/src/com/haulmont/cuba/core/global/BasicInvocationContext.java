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

import java.io.Serializable;

public class BasicInvocationContext implements Serializable
{
    private Class<? extends BaseEntity> entityClass;

    private Object id;

    private String queryString;

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }

    public BasicInvocationContext setEntityClass(Class<? extends BaseEntity> entityClass) {
        this.entityClass = entityClass;
        return this;
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
}
