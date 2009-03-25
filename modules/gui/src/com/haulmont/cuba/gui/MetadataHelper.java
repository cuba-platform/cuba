/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 29.12.2008 17:18:11
 * $Id$
 */
package com.haulmont.cuba.gui;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.BaseLongIdEntity;

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Arrays;

public class MetadataHelper {
    public static Class getTypeClass(MetaProperty metaProperty) {
        if (metaProperty == null)
            throw new IllegalArgumentException("MetaProperty is null");
        
        final Range range = metaProperty.getRange();
        if (range.isDatatype()) {
            return range.asDatatype().getJavaClass();
        } else if (range.isClass()) {
            return range.asClass().getJavaClass();
        } else if (range.isEnum()) {
            return range.asEnumiration().getJavaClass();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean isCascade(MetaProperty metaProperty) {
        boolean cascadeProperty = false;

        final Field field = metaProperty.getJavaField();
        if (field != null) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                final Collection<CascadeType> cascadeTypes = Arrays.asList(oneToMany.cascade());
                if (cascadeTypes.contains(CascadeType.ALL) ||
                        cascadeTypes.contains(CascadeType.MERGE))
                {
                    cascadeProperty = true;
                }
            }
        }
        return cascadeProperty;
    }

    public static boolean isSystem(MetaProperty metaProperty) {
        final MetaClass metaClass = metaProperty.getDomain();
        final Class javaClass = metaClass.getJavaClass();

        return BaseUuidEntity.class.equals(javaClass) ||
                StandardEntity.class.equals(javaClass) ||
                    BaseLongIdEntity.class.equals(javaClass);
    }
}
