/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config.type;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.OriginalEntityLoadInfo;

/**
 * @author krivopustov
 * @version $Id$
 */
public class EntityStringify extends TypeStringify {

    @Override
    public String stringify(Object value) {
        OriginalEntityLoadInfo entityLoadInfo = OriginalEntityLoadInfo.create((Entity) value);
        return entityLoadInfo.toString();
    }
}