/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.security.ds;

import com.google.common.base.Predicate;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Permission;

import java.util.UUID;

/**
 */
public class RestorablePermissionDatasource extends CollectionDatasourceImpl<Permission, UUID> {

    public Permission findRemovedEntity(Predicate<Permission> predicate) {
        for (Object item : itemsToDelete) {
            Permission permission = (Permission) item;
            if (predicate.apply(permission))
                return permission;
        }
        return null;
    }

    public void restoreEntity(Permission entity) {
        if (itemsToDelete.contains(entity)) {
            itemsToDelete.remove(entity);
            includeItem(entity);
        }
    }
}