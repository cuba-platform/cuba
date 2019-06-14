/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.gui.app.security.ds;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.role.RolesService;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;

import java.util.Collection;
import java.util.Map;

/**
 * INTERNAL
 */
public class PermissionsCollectionDatasource extends RestorablePermissionDatasource {

    protected RolesService rolesService = AppBeans.get(RolesService.NAME);

    @Override
    protected void loadData(Map<String, Object> params) {
        Role role = (Role) params.get("role");
        PermissionType permissionType = (PermissionType) params.get("permissionType");
        if (role != null && permissionType != null && role.isPredefined()) {
            Collection<Permission> permissions = rolesService.getPermissions(role.getName(), permissionType);

            data.clear();
            for (Permission entity : permissions) {
                data.put(entity.getId(), entity);
            }
        } else {
            super.loadData(params);
        }
    }
}
