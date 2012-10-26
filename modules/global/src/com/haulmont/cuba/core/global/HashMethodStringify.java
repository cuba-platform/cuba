/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.type.TypeStringify;
import com.haulmont.cuba.core.entity.HashMethod;

/**
 * @author artamonov
 * @version $Id$
 */
public class HashMethodStringify extends TypeStringify {

    @Override
    public String stringify(Object value) {
        return value != null ? ((HashMethod) value).getId() : null;
    }
}