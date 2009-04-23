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
import com.haulmont.cuba.core.entity.BaseEntity;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.meta.FieldMetaData;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.BitSet;

public class PersistenceHelper {
    public static boolean isNew(Entity entity) {
        if (entity instanceof PersistenceCapable)
            return ((PersistenceCapable) entity).pcIsDetached() == null;
        else
            return entity.getId() != null;
    }

     public static Set<String> getDirtyFields(BaseEntity entity) {
        if (!(entity instanceof PersistenceCapable))
            return Collections.emptySet();

        OpenJPAStateManager stateManager = (OpenJPAStateManager) ((PersistenceCapable) entity).pcGetStateManager();
        if (stateManager == null)
            return Collections.emptySet();

        Set<String> set = new HashSet<String>();
        BitSet dirtySet = stateManager.getDirty();
        for (int i = 0; i < dirtySet.size()-1; i++) {
            if (dirtySet.get(i)) {
                FieldMetaData field = stateManager.getMetaData().getField(i);
                set.add(field.getName());
            }
        }
        return set;
    }
}
