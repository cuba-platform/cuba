/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 13.02.2009 16:35:29
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;
import org.apache.openjpa.enhance.PersistenceCapable;

public class PersistenceHelper {
    public static boolean isNew(Entity entity) {
        if (entity instanceof PersistenceCapable)
            return ((PersistenceCapable) entity).pcIsDetached() == null;
        else
            return entity.getId() != null;
    }
}
