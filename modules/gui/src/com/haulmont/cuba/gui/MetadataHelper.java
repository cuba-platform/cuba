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

import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Arrays;

public class MetadataHelper {
    public static Class getPropertyTypeClass(MetaProperty metaProperty) {
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
}
