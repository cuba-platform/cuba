/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
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
@ManagedBean(DynamicAttributesGuiTools.NAME)
public class DynamicAttributesGuiTools {
    public static final String NAME = "cuba_DynamicAttributesGuiTools";

    @Inject
    protected DynamicAttributesService dynamicAttributesService;

    @Inject
    protected MetadataTools metadataTools;

    /**
     * Enforce the datasource to change modified status if dynamic attribute is changed
     */
    @SuppressWarnings("unchecked")
    public void listenDynamicAttributesChanges(final Datasource datasource) {
        if (datasource != null && datasource.getLoadDynamicAttributes()) {
            datasource.addListener(new DsListenerAdapter() {
                @Override
                public void valueChanged(Object source, String property, @Nullable Object prevValue, @Nullable Object value) {
                    if (DynamicAttributesUtils.isDynamicAttribute(property)) {
                        ((DatasourceImplementation) datasource).modified((Entity) source);
                    }
                }
            });
        }
    }

    /**
     * Get attributes which should be added automatically to the screen and component.
     * Based on visibility settings from category attribute editor.
     */
    public Set<CategoryAttribute> getAttributesToShowOnTheScreen(MetaClass metaClass, String screen, @Nullable String component) {
        Collection<CategoryAttribute> attributesForMetaClass =
                dynamicAttributesService.getAttributesForMetaClass(metaClass);
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
