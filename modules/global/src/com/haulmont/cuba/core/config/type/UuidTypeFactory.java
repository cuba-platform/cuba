/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.config.type;

import java.util.UUID;

public class UuidTypeFactory extends TypeFactory
{
    public Object build(String string) {
        if (string == null)
            return null;

        return UUID.fromString(string);
    }
}
