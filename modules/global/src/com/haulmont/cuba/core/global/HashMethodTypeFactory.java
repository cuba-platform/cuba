/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.type.TypeFactory;
import com.haulmont.cuba.core.entity.HashMethod;

/**
 * @author artamonov
 * @version $Id$
 */
public class HashMethodTypeFactory extends TypeFactory {

    @Override
    public Object build(String id) {
        HashMethod method = HashMethod.fromId(id);
        if (method == null)
            throw new RuntimeException("Unsupported value for cuba.passwordEncryption");
        return method;
    }
}