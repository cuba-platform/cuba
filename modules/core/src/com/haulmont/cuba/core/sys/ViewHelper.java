/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 13:01:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.SoftDelete;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.EntityManager;
import org.apache.openjpa.persistence.FetchPlan;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

public class ViewHelper
{
    public static void setView(FetchPlan fetchPlan, View view) {
        if (fetchPlan == null)
            throw new IllegalArgumentException("FetchPlan is null");

        fetchPlan.clearFetchGroups();

        if (view != null) {
            fetchPlan.removeFetchGroup(FetchPlan.GROUP_DEFAULT);
            processView(view, fetchPlan);
        } else {
            fetchPlan.addFetchGroup(FetchPlan.GROUP_DEFAULT);
        }
    }

    private static void processView(View view, FetchPlan fetchPlan) {
        if (view.isIncludeSystemProperties()) {
            includeSystemProperties(view, fetchPlan);
        }

        MetaClass metaClass = MetadataProvider.getSession().getClass(view.getEntityClass());
        if (metaClass == null)
            throw new RuntimeException("View '" + view + "' definition error: metaClass not found for '"
                    + view.getEntityClass() + "'");


        for (ViewProperty property : view.getProperties()) {
            if (property.isLazy())
                continue;

            MetaProperty metaProperty = metaClass.getProperty(property.getName());
            if (metaProperty == null)
                throw new RuntimeException("View '" + view + "' definition error: property '"
                        + property.getName() + "' not found in entity '" + metaClass + "'");

            Class declaringClass = metaProperty.getDeclaringClass();
            if (declaringClass != null) {
                fetchPlan.addField(declaringClass, property.getName());
                if (property.getView() != null) {
                    processView(property.getView(), fetchPlan);
                }
            }
        }
    }

    private static void includeSystemProperties(View view, FetchPlan fetchPlan) {
        Class<? extends BaseEntity> entityClass = view.getEntityClass();
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityClass);

        Class<?> declaringClass;
        if (BaseEntity.class.isAssignableFrom(entityClass)) {
            declaringClass = metaClass.getProperty("createTs").getDeclaringClass();
            if (declaringClass != null) {
                fetchPlan.addField(declaringClass, "createTs");
                fetchPlan.addField(declaringClass, "createdBy");
            }
        }
        if (Updatable.class.isAssignableFrom(entityClass)) {
            declaringClass = metaClass.getProperty("updateTs").getDeclaringClass();
            if (declaringClass != null) {
                fetchPlan.addField(declaringClass, "updateTs");
                fetchPlan.addField(declaringClass, "updatedBy");
            }
        }
        if (SoftDelete.class.isAssignableFrom(entityClass)) {
            declaringClass = metaClass.getProperty("deleteTs").getDeclaringClass();
            if (declaringClass != null) {
                fetchPlan.addField(declaringClass, "deleteTs");
                fetchPlan.addField(declaringClass, "deletedBy");
            }
        }
    }

    public static void fetchInstance(Instance instance, View view) {
        if (PersistenceHelper.isDetached(instance))
            throw new IllegalArgumentException("Can not fetch detached entity. Merge first.");
        __fetchInstance(instance, view, new HashSet<Instance>());
    }

    private static void __fetchInstance(Instance instance, View view, Set<Instance> visited) {
        if (visited.contains(instance))
            return;
        visited.add(instance);

        for (ViewProperty property : view.getProperties()) {
            Object value = instance.getValue(property.getName());
            View propertyView = property.getView();
            if (value != null && propertyView != null) {
                if (value instanceof Collection) {
                    for (Object item : ((Collection) value)) {
                        if (item instanceof Instance)
                            __fetchInstance((Instance) item, propertyView, visited);
                    }
                } else if (value instanceof Instance) {
                    if (PersistenceHelper.isDetached(value)) {
                        EntityManager em = PersistenceProvider.getEntityManager();
                        value = em.merge((Entity) value);
                        instance.setValue(property.getName(), value);
                    }
                    __fetchInstance((Instance) value, propertyView, visited);
                }
            }
        }
    }

    public static boolean hasLazyProperties(View view) {
        for (ViewProperty property : view.getProperties()) {
            if (property.isLazy())
                return true;
            if (property.getView() != null) {
                if (hasLazyProperties(property.getView()))
                    return true;
            }
        }
        return false;
    }
}
