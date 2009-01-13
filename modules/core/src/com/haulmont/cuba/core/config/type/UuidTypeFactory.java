/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 15:29:19
 *
 * $Id$
 */
package com.haulmont.cuba.core.config.type;

import java.util.UUID;

public class UuidTypeFactory extends TypeFactory
{
    public Object build(String string) {
        return UUID.fromString(string);
    }
}
