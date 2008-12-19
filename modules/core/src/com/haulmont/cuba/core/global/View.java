/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 10:40:35
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class View implements Serializable
{
    private static final long serialVersionUID = 4313784222934349594L;

    private Class<? extends BaseEntity> entityClass;

    private String name;

    private List<ViewProperty> properties = new ArrayList<ViewProperty>();

    private boolean includeSystemProperties;

    public View(Class<? extends BaseEntity> entityClass, String name) {
        this(entityClass, name, true);
    }

    public View(Class<? extends BaseEntity> entityClass, String name, boolean includeSystemProperties) {
        this.entityClass = entityClass;
        this.name = name;
        this.includeSystemProperties = includeSystemProperties;
    }

    public Class<? extends BaseEntity> getEntityClass() {
        return entityClass;
    }

    public String getName() {
        return name;
    }

    public List<ViewProperty> getProperties() {
        return properties;
    }

    public boolean isIncludeSystemProperties() {
        return includeSystemProperties;
    }

    public View addProperty(String name, View view) {
        properties.add(new ViewProperty(name, view));
        return this;
    }

    public View addProperty(String name) {
        properties.add(new ViewProperty(name, null));
        return this;
    }

    public String toString() {
        return "View{" +
                "entityClass=" + entityClass.getName() +
                ", name='" + name + '\'' +
                '}';
    }
}
