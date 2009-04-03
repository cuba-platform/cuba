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
import com.haulmont.cuba.core.entity.BaseEntity;
import com.haulmont.cuba.core.entity.DeleteDeferred;
import com.haulmont.cuba.core.entity.Updatable;
import com.haulmont.cuba.core.global.MetadataProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import org.apache.openjpa.persistence.FetchPlan;

import java.lang.reflect.Field;

public class ViewHelper
{
    public static void setView(FetchPlan fetchPlan, View view) {
        if (fetchPlan == null)
            throw new IllegalArgumentException("FetchPlan is null");
        if (view == null)
            throw new IllegalArgumentException("View is null");

        fetchPlan.clearFetchGroups();
        fetchPlan.removeFetchGroup(FetchPlan.GROUP_DEFAULT);
        processView(view, fetchPlan);
    }

    private static void processView(View view, FetchPlan fetchPlan) {
        if (view.isIncludeSystemProperties()) {
            includeSystemProperties(view, fetchPlan);
        }

        MetaClass metaClass = MetadataProvider.getSession().getClass(view.getEntityClass());

        for (ViewProperty property : view.getProperties()) {
            MetaProperty metaProperty = metaClass.getProperty(property.getName());
            Field field = metaProperty.getJavaField();
            if (field != null) {
                fetchPlan.addField(field.getDeclaringClass(), property.getName());
                if (property.getView() != null) {
                    processView(property.getView(), fetchPlan);
                }
            }
        }
    }

    private static void includeSystemProperties(View view, FetchPlan fetchPlan) {
        Class<? extends BaseEntity> entityClass = view.getEntityClass();
        MetaClass metaClass = MetadataProvider.getSession().getClass(entityClass);

        Class<?> declaringClass = metaClass.getProperty("createTs").getJavaField().getDeclaringClass();
        fetchPlan.addField(declaringClass, "createTs");
        fetchPlan.addField(declaringClass, "createdBy");
        if (Updatable.class.isAssignableFrom(entityClass)) {
            declaringClass = metaClass.getProperty("updateTs").getJavaField().getDeclaringClass();
            fetchPlan.addField(declaringClass, "updateTs");
            fetchPlan.addField(declaringClass, "updatedBy");
        }
        if (DeleteDeferred.class.isAssignableFrom(entityClass)) {
            declaringClass = metaClass.getProperty("deleteTs").getJavaField().getDeclaringClass();
            fetchPlan.addField(declaringClass, "deleteTs");
            fetchPlan.addField(declaringClass, "deletedBy");
        }
    }
}
