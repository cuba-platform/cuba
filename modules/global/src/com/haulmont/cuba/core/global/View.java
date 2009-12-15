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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * View is used to declare a graph of objects which must be retrieved from the database.<br>
 * A view can be constructed in Java code or defined in XML and deployed
 * to the {@link com.haulmont.cuba.core.global.ViewRepository} for multiple usage.
 */
public class View implements Serializable
{
    public static final String LOCAL = "_local";
    public static final String MINIMAL = "_minimal";

    private static final long serialVersionUID = 4313784222934349594L;

    private Class<? extends BaseEntity> entityClass;

    private String name;

    private Map<String, ViewProperty> properties = new HashMap<String, ViewProperty>();

    private boolean includeSystemProperties;

    public View(Class<? extends BaseEntity> entityClass) {
        this(entityClass, "", true);
    }

    public View(Class<? extends BaseEntity> entityClass, boolean includeSystemProperties) {
        this(entityClass, "", includeSystemProperties);
    }

    public View(Class<? extends BaseEntity> entityClass, String name) {
        this(entityClass, name, true);
    }

    public View(View src, String name) {
        this.entityClass = src.entityClass;
        this.name = name;
        this.includeSystemProperties = src.includeSystemProperties;
        this.properties.putAll(src.properties);
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

    public Collection<ViewProperty> getProperties() {
        return properties.values();
    }

    public boolean isIncludeSystemProperties() {
        return includeSystemProperties;
    }

    public View addProperty(String name, View view, boolean lazy) {
        properties.put(name, new ViewProperty(name, view, lazy));
        return this;
    }

    public View addProperty(String name, View view) {
        properties.put(name, new ViewProperty(name, view));
        return this;
    }

    public View addProperty(String name) {
        properties.put(name, new ViewProperty(name, null));
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        View view = (View) o;

        return entityClass.equals(view.entityClass) && name.equals(view.name);
    }

    public int hashCode() {
        int result = entityClass.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public String toString() {
        return entityClass.getName() + "/" + name;
    }

    public ViewProperty getProperty(String name) {
        return properties.get(name);
    }
}
