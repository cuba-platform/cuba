/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ViewHelper {

    public static View intersectViews(View first, View second) {
        if (first == null)
            throw new IllegalArgumentException("View is null");
        if (second == null)
            throw new IllegalArgumentException("View is null");

        View resultView = new View(first.getEntityClass());

        Collection<ViewProperty> firstProps = first.getProperties();

        for (ViewProperty firstProperty : firstProps) {
            if (second.containsProperty(firstProperty.getName())) {
                View resultPropView = null;
                ViewProperty secondProperty = second.getProperty(firstProperty.getName());
                if ((firstProperty.getView() != null) && (secondProperty.getView() != null)) {
                    resultPropView = intersectViews(firstProperty.getView(), secondProperty.getView());
                }
                resultView.addProperty(firstProperty.getName(), resultPropView);
            }
        }

        return resultView;
    }

    /**
     * DEPRECATED. Use {@link com.haulmont.cuba.core.global.View#hasLazyProperties()}.
     */
    @Deprecated
    public static boolean hasLazyProperties(View view) {
        return view.hasLazyProperties();
    }

    /**
     * DEPRECATED. Use {@link com.haulmont.cuba.core.EntityManager#fetch(com.haulmont.cuba.core.entity.Entity, com.haulmont.cuba.core.global.View)}
     * directly.
     */
    @Deprecated
    public static void fetchInstance(Instance instance, View view) {
        AppBeans.get(Persistence.class).getEntityManager().fetch((Entity) instance, view);
    }
}
