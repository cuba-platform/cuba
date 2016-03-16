/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 */
public final class DynamicAttributesUtils {
    private DynamicAttributesUtils() {
    }

    /**
     * Get special meta property path object for dynamic attribute
     */
    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, CategoryAttribute attribute) {
        MetaProperty metaProperty = new DynamicAttributesMetaProperty(metaClass, attribute);
        return new MetaPropertyPath(metaClass, metaProperty);
    }

    /**
     * Get special meta property path object for dynamic attribute by code
     */
    @Nullable
    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, String attributeCode) {
        attributeCode = decodeAttributeCode(attributeCode);
        CategoryAttribute attribute = AppBeans.get(DynamicAttributes.NAME, DynamicAttributes.class)
                .getAttributeForMetaClass(metaClass, attributeCode);

        if (attribute != null) {
            return getMetaPropertyPath(metaClass, attribute);
        } else {
            return null;
        }
    }

    /**
     * Get special meta property path object for dynamic attribute id
     */
    @Nullable
    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, UUID attributeId) {
        Collection<CategoryAttribute> attributes = AppBeans.get(DynamicAttributes.NAME, DynamicAttributes.class)
                .getAttributesForMetaClass(metaClass);
        CategoryAttribute attribute = null;
        for (CategoryAttribute theAttribute : attributes) {
            if (theAttribute.getId().equals(attributeId)) {
                attribute = theAttribute;
                break;
            }
        }

        if (attribute != null) {
            return getMetaPropertyPath(metaClass, attribute);
        } else {
            return null;
        }
    }

    /**
     * Remove dynamic attribute marker (+) from attribute code (if exists)
     */
    public static String decodeAttributeCode(String attributeCode) {
        return attributeCode.startsWith("+") ? attributeCode.substring(1) : attributeCode;
    }

    /**
     * Add dynamic attribute marker (+) to attribute code (if does not exist)
     */
    public static String encodeAttributeCode(String attributeCode) {
        return attributeCode.startsWith("+") ? attributeCode : "+" + attributeCode;
    }

    /**
     * Check if the name has dynamic attribute marker
     */
    public static boolean isDynamicAttribute(String name) {
        return name.startsWith("+");
    }

    /**
     * Check if the meta property is dynamic attribute property
     */
    public static boolean isDynamicAttribute(MetaProperty metaProperty) {
        return metaProperty instanceof DynamicAttributesMetaProperty;
    }

    public static CategoryAttribute getCategoryAttribute(MetaProperty metaProperty) {
        return ((DynamicAttributesMetaProperty) metaProperty).getAttribute();
    }

    /**
     * Resolve attribute value's Java class
     */
    public static Class getAttributeClass(CategoryAttribute attribute) {
        PropertyType propertyType = attribute.getDataType();
        switch (propertyType) {
            case STRING:
                return String.class;
            case INTEGER:
                return Integer.class;
            case DOUBLE:
                return Double.class;
            case BOOLEAN:
                return Boolean.class;
            case DATE:
                return Date.class;
            case ENUMERATION:
                return String.class;
            case ENTITY:
                return attribute.getJavaClassForEntity();
        }
        return String.class;
    }
}