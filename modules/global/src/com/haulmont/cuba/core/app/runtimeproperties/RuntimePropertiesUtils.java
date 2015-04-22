/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.runtimeproperties;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;

/**
 * @author degtyarjov
 * @version $Id$
 */
public final class RuntimePropertiesUtils {
    private RuntimePropertiesUtils() {
    }

    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, CategoryAttribute attribute) {
        MetaProperty metaProperty = new RuntimePropertiesMetaProperty(metaClass, attribute);
        return new MetaPropertyPath(metaClass, metaProperty);
    }

    @Nullable
    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, String attributeCode) {
        attributeCode = decodeAttributeCode(attributeCode);
        CategoryAttribute attribute =
                AppBeans.get(RuntimePropertiesService.class).getAttributeForMetaClass(metaClass, attributeCode);

        if (attribute != null) {
            MetaProperty metaProperty = new RuntimePropertiesMetaProperty(metaClass, attribute);
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

    public static boolean isRuntimeProperty(String name) {
        return name.startsWith("+");
    }
}
