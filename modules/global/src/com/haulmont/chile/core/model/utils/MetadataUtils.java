/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 15.04.2009 16:20:13
 * $Id: MetadataUtils.java 1911 2010-06-02 14:07:46Z krivopustov $
 */

package com.haulmont.chile.core.model.utils;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.chile.core.model.Session;

public class MetadataUtils {

    private static Session serializationSupportSession;

    public static Class getTypeClass(MetaProperty metaProperty) {
        if (metaProperty == null)
            throw new IllegalArgumentException("MetaProperty is null");

        final Range range = metaProperty.getRange();
        if (range.isDatatype()) {
            return range.asDatatype().getJavaClass();
        } else if (range.isClass()) {
            return range.asClass().getJavaClass();
        } else if (range.isEnum()) {
            return range.asEnumeration().getJavaClass();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static void setSerializationSupportSession(Session session) {
        serializationSupportSession = session;
    }

    public static Session getSerializationSupportSession() {
        return serializationSupportSession;
    }
}
