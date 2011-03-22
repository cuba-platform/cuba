/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Devyatkin
 * Created: 22.03.11 11:56
 *
 * $Id$
 */
package com.haulmont.cuba.core.config.type;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityLoadInfo;

public class EntityStringify extends TypeStringify {

    @Override
    public String stringify(Object value) {
        EntityLoadInfo entityLoadInfo = EntityLoadInfo.create((Entity) value);
        return entityLoadInfo.toString();
    }
}
