/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.google.common.base.Predicate;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;

import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class RestorablePermissionDatasource extends CollectionDatasourceImpl<Permission, UUID> {

    public Permission findRemovedEntity(Predicate<Permission> predicate) {
        for (Object item : itemToDelete) {
            Permission permission = (Permission) item;
            if (predicate.apply(permission))
                return permission;
        }
        return null;
    }

    public void restoreEntity(Permission entity) {
        if (itemToDelete.contains(entity)) {
            itemToDelete.remove(entity);
            includeItem(entity);
        }
    }
}