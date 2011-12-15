/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.security;

import com.google.common.base.Predicate;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.data.DataService;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;

import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class RestorablePermissionDatasource extends CollectionDatasourceImpl<Permission, UUID> {
    public RestorablePermissionDatasource(DsContext context, DataService dataservice, String id, MetaClass metaClass, String viewName) {
        super(context, dataservice, id, metaClass, viewName);
    }

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