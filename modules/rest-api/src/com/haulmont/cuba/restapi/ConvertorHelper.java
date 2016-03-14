/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.restapi;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.LoadContext;

import java.util.*;

/**
 * @author chevelev
 * @version $Id$
 */
public class ConvertorHelper {
    public static final Comparator<MetaProperty> PROPERTY_COMPARATOR = new Comparator<MetaProperty>() {
        public int compare(MetaProperty p1, MetaProperty p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

    public static List<MetaProperty> getOrderedProperties(MetaClass metaClass) {
        List<MetaProperty> result = new ArrayList<MetaProperty>(metaClass.getProperties());
        Collections.sort(result, PROPERTY_COMPARATOR);
        return result;
    }

    public static List<MetaProperty> getActualMetaProperties(MetaClass metaClass, Entity entity) {
        List<MetaProperty> result = new ArrayList<MetaProperty>(metaClass.getProperties());
        if (entity instanceof BaseGenericIdEntity
                && ((BaseGenericIdEntity) entity).getDynamicAttributes() != null) {
            Collection<CategoryAttribute> dynamicAttributes
                    = AppBeans.get(DynamicAttributes.NAME, DynamicAttributes.class).getAttributesForMetaClass(metaClass);
            for (CategoryAttribute dynamicAttribute : dynamicAttributes) {
                result.add(DynamicAttributesUtils.getMetaPropertyPath(metaClass, dynamicAttribute).getMetaProperty());
            }
        }

        Collections.sort(result, PROPERTY_COMPARATOR);
        return result;
    }

    public static void fetchDynamicAttributes(Entity entity){
        if (entity instanceof BaseGenericIdEntity) {
            LoadContext<BaseGenericIdEntity> loadContext = new LoadContext<>(entity.getMetaClass());
            loadContext.setId(entity.getId()).setLoadDynamicAttributes(true);
            DataService dataService = AppBeans.get(DataService.NAME, DataService.class);
            BaseGenericIdEntity reloaded = dataService.load(loadContext);
            if (reloaded != null) {
                ((BaseGenericIdEntity) entity).setDynamicAttributes(reloaded.getDynamicAttributes());
            } else {
                ((BaseGenericIdEntity) entity).setDynamicAttributes(new HashMap<>());
            }
        }
    }
}
