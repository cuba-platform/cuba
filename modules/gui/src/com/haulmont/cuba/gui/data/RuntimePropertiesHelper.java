/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data;

import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.sys.SetValueEntity;
import org.apache.commons.lang.BooleanUtils;

import java.util.Date;

/**
 * <p>$Id$</p>
 *
 * @author devyatkin
 */
public class RuntimePropertiesHelper {

    public static Class getAttributeClass(CategoryAttribute attribute) {

        if (BooleanUtils.isTrue(attribute.getIsEntity())) {
            try {
                return Class.forName(attribute.getDataType());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class " + attribute.getDataType(), e);
            }

        } else {
            String dataType = attribute.getDataType();
            switch (RuntimePropsDatasource.PropertyType.valueOf(dataType)) {
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
