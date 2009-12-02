/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 10:52:31
 *
 * $Id$
 */
package com.haulmont.cuba.core.config.type;

import java.lang.reflect.Method;

public class PrimitiveTypeStringify extends TypeStringify
{
    public String stringify(Object value) {
        return String.valueOf(value);
    }
}
