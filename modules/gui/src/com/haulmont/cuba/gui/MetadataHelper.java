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

public class MetadataHelper {
    public static Class getPropertyTypeClass(MetaProperty metaProperty) {
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
}
