/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.DatasourceImplementation;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * @author degtyarjov
 * @version $Id$
 */
@Component(DynamicAttributesGuiTools.NAME)
public class DynamicAttributesGuiTools {
    public static final String NAME = "cuba_DynamicAttributesGuiTools";

    @Inject
    protected DynamicAttributes dynamicAttributes;

    @Inject
    protected MetadataTools metadataTools;

    /**
     * Enforce the datasource to change modified status if dynamic attribute is changed
     */
    @SuppressWarnings("unchecked")
    public void listenDynamicAttributesChanges(final Datasource datasource) {
        if (datasource != null && datasource.getLoadDynamicAttributes()) {
            datasource.addItemPropertyChangeListener(e -> {
                if (DynamicAttributesUtils.isDynamicAttribute(e.getProperty())) {
                    ((DatasourceImplementation) datasource).modified(e.getItem());
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
                dynamicAttributes.getAttributesForMetaClass(metaClass);
        Set<CategoryAttribute> categoryAttributes = new LinkedHashSet<>();

        for (CategoryAttribute attribute : attributesForMetaClass) {
            if (attributeShouldBeShownOnTheScreen(screen, component, attribute)) {
                categoryAttributes.add(attribute);
            }
        }

        return categoryAttributes;
    }

    public void initDefaultAttributeValues(BaseGenericIdEntity item) {
        Collection<CategoryAttribute> attributes =
                dynamicAttributes.getAttributesForMetaClass(item.getMetaClass());
        item.setDynamicAttributes(new HashMap<>());
        Date currentTimestamp = AppBeans.get(TimeSource.NAME, TimeSource.class).currentTimestamp();

        for (CategoryAttribute categoryAttribute : attributes) {
            String code = DynamicAttributesUtils.encodeAttributeCode(categoryAttribute.getCode());
            if (categoryAttribute.getDefaultValue() != null) {
                item.setValue(code, categoryAttribute.getDefaultValue());
            } else if (Boolean.TRUE.equals(categoryAttribute.getDefaultDateIsCurrent())) {
                item.setValue(code, currentTimestamp);
            }
        }
    }

    protected boolean attributeShouldBeShownOnTheScreen(String screen, String component, CategoryAttribute attribute) {
        Set<String> targetScreensSet = attribute.targetScreensSet();
        return targetScreensSet.contains(screen) || targetScreensSet.contains(screen + "#" + component);
    }
}