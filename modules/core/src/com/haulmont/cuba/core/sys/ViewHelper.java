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

import com.haulmont.chile.core.model.Instance;
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;

import java.util.Collection;

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

    /**
     * DEPRECATED. Use {@link com.haulmont.cuba.core.EntityManager#fetch(com.haulmont.cuba.core.entity.Entity, com.haulmont.cuba.core.global.View)}
     * directly.
     */
    @Deprecated
    public static void fetchInstance(Instance instance, View view) {
        PersistenceProvider.getEntityManager().fetch((Entity) instance, view);
    }
}
