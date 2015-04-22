/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.runtimeprops;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.runtimeproperties.RuntimePropertiesService;
import com.haulmont.cuba.core.app.runtimeproperties.RuntimePropertiesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(RuntimePropertiesGuiTools.NAME)
public class RuntimePropertiesGuiTools {
    public static final String NAME = "cuba_RuntimePropertiesGuiTools";

    @Inject
    protected RuntimePropertiesService runtimePropertiesService;

    @SuppressWarnings("unchecked")
    public void listenRuntimePropertiesChanges(final Datasource datasource) {
        if (datasource != null && datasource.needToLoadRuntimeProperties()) {
            datasource.addListener(new DsListenerAdapter() {
                @Override
                public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                    if (RuntimePropertiesUtils.isRuntimeProperty(property)) {
                        ((DatasourceImplementation) datasource).modified((Entity) source);
                    }
                }
            });
        }
    }

    public MetaPropertyPath resolveMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = metaClass.getPropertyPath(property);
        if (metaPropertyPath == null && RuntimePropertiesUtils.isRuntimeProperty(property)) {
            metaPropertyPath = RuntimePropertiesUtils.getMetaPropertyPath(metaClass, property);
        }
        return metaPropertyPath;
    }

    public Set<CategoryAttribute> getAttributesToShowOnTheScreen(MetaClass metaClass, String screen, String component) {
        Collection<CategoryAttribute> attributesForMetaClass =
                runtimePropertiesService.getAttributesForMetaClass(metaClass);
        Set<CategoryAttribute> categoryAttributes = new HashSet<>();

        for (CategoryAttribute attribute : attributesForMetaClass) {
            if (attributeShouldBeShownOnTheScreen(screen, component, attribute)) {
                categoryAttributes.add(attribute);
            }
        }

        return categoryAttributes;
    }

    protected boolean attributeShouldBeShownOnTheScreen(String screen, String component, CategoryAttribute attribute) {
        Set<String> targetScreensSet = attribute.targetScreensSet();
        return targetScreensSet.contains(screen) || targetScreensSet.contains(screen + "#" + component);
    }
}
