/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.dynamicattributes;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.SetValueEntity;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * @author degtyarjov
 * @version $Id$
 */
public final class DynamicAttributesUtils {
    private DynamicAttributesUtils() {
    }

    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, CategoryAttribute attribute) {
        MetaProperty metaProperty = new DynamicAttributesMetaProperty(metaClass, attribute);
        return new MetaPropertyPath(metaClass, metaProperty);
    }

    @Nullable
    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, String attributeCode) {
        attributeCode = decodeAttributeCode(attributeCode);
        CategoryAttribute attribute =
                AppBeans.get(DynamicAttributesService.class).getAttributeForMetaClass(metaClass, attributeCode);

        if (attribute != null) {
            MetaProperty metaProperty = new DynamicAttributesMetaProperty(metaClass, attribute);
            return new MetaPropertyPath(metaClass, metaProperty);
        } else {
            return null;
        }
    }

    public static String decodeAttributeCode(String attributeCode) {
        return attributeCode.startsWith("+") ? attributeCode.substring(1) : attributeCode;
    }

    public static String encodeAttributeCode(String attributeCode) {
        return attributeCode.startsWith("+") ? attributeCode : "+" + attributeCode;
    }

    public static boolean isDynamicAttribute(String name) {
        return name.startsWith("+");
    }

    public static Class getAttributeClass(CategoryAttribute attribute) {

        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            return ReflectionHelper.getClass(attribute.getDataType());
        } else {
            String dataType = attribute.getDataType();
            switch (PropertyType.valueOf(dataType)) {
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
                    return SetValueEntity.class;
            }
        }
        return String.class;
    }
}
