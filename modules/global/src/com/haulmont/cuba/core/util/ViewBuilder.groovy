/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.util

import com.haulmont.bali.util.ReflectionHelper
import com.haulmont.cuba.core.global.AppBeans
import com.haulmont.cuba.core.global.Metadata
import com.haulmont.cuba.core.global.View
import com.haulmont.cuba.core.global.ViewRepository
import org.apache.commons.lang.StringUtils

/**
 * Simple Groovy builder for view definitions
 *
 * @author artamonov
 * @version $Id$
 */
class ViewBuilder {

    private Metadata metadata = AppBeans.get(Metadata.class)
    private ViewRepository viewRepository = AppBeans.get(ViewRepository.class)

    private View createView(ViewNode viewNode) {
        String entity = viewNode.entity

        com.haulmont.chile.core.model.MetaClass metaClass
        if (StringUtils.isBlank(entity)) {
            String className = viewNode.className
            if (StringUtils.isBlank(className))
                throw new IllegalStateException('Invalid view definition: no \'entity\' or \'class\' attribute')

            Class entityClass = ReflectionHelper.getClass(className)
            metaClass = metadata.getClassNN(entityClass)
        } else {
            metaClass = metadata.getClassNN(entity)
        }

        String viewName = viewNode.name
        String extendsView = viewNode.extendsView

        if (viewNode.systemProperties == null)
            viewNode.systemProperties = false;

        View view
        if (StringUtils.isNotBlank(extendsView)) {
            View ancestorView = viewRepository.getView(metaClass, extendsView)

            boolean includeSystemProperties = viewNode.systemProperties == null ?
                ancestorView.isIncludeSystemProperties() : viewNode.systemProperties

            view = new View(ancestorView, metaClass.getJavaClass(), viewName, includeSystemProperties)
        } else {
            view = new View(metaClass.getJavaClass(), viewName, viewNode.systemProperties)
        }

        for (ViewPropertyNode propertyNode : viewNode.viewProperties) {
            createPropertyView(view, propertyNode)
        }

        return view
    }

    private void createPropertyView(View view, ViewPropertyNode viewPropertyNode) {
        final com.haulmont.chile.core.model.MetaClass metaClass = metadata.getClassNN(view.getEntityClass())

        String propertyName = viewPropertyNode.name
        com.haulmont.chile.core.model.MetaProperty metaProperty = metaClass.getProperty(propertyName)
        if (metaProperty == null)  {
            throw new IllegalStateException(
                    String.format("View ${metaClass.name}/${view.name} definition error: property $propertyName doesn't exists")
            )
        }

        View refView = null
        com.haulmont.chile.core.model.MetaClass refMetaClass
        com.haulmont.chile.core.model.Range range = metaProperty.getRange()

        if (range == null) {
            throw new IllegalStateException("Cannot find range for meta property ${metaProperty}");
        }

        boolean inlineView = !viewPropertyNode.viewProperties.isEmpty()
        if (StringUtils.isNotBlank(viewPropertyNode.view) && !inlineView) {
            if (!range.isClass()) {
                throw new IllegalStateException(
                        String.format("View ${metaClass.name}/${view.name} definition error: property $propertyName is not an entity")
                );
            }

            refMetaClass = range.asClass()

            refView = viewRepository.findView(refMetaClass, viewPropertyNode.name)
        }

        if (range.isClass() && refView == null && inlineView) {
            String extendsView = viewPropertyNode.view
            if (StringUtils.isBlank(extendsView)) {
                refView = new View(range.asClass().getJavaClass())
            } else {
                refMetaClass = range.asClass()
                View ancestorView = viewRepository.getView(refMetaClass, extendsView)
                refView = new View(ancestorView, range.asClass().getJavaClass(), "", true)
            }

            for (ViewPropertyNode childPropertyNode : viewPropertyNode.viewProperties) {
                createPropertyView(refView, childPropertyNode)
            }
        }

        if (viewPropertyNode.lazy == null)
            viewPropertyNode.lazy = false

        view.addProperty(propertyName, refView, viewPropertyNode.lazy)
    }

    View view(Map attributes, Closure definition) {
        ViewNode viewNode = new ViewNodeBuilder().view(attributes, definition)

        return createView(viewNode)
    }
}