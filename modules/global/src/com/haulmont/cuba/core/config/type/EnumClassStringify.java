/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

/**
 * @author kozlov
 * @version $Id$
 */
public class EnumClassStringify extends TypeStringify {

    private TypeStringify idStringify;

    public EnumClassStringify(TypeStringify idStringify) {
        this.idStringify = idStringify;
    }

    @Override
    public String stringify(Object value) {
        EnumClass enumeration = (EnumClass) value;
        return idStringify.stringify(enumeration.getId());
    }
}
