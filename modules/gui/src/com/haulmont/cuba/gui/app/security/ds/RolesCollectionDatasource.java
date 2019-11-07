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
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.role.RolesService;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * INTERNAL
 */
public class RolesCollectionDatasource extends CollectionDatasourceImpl<Role, UUID> {

    protected RolesService rolesService = AppBeans.get(RolesService.NAME);

    public RolesCollectionDatasource() {
        super();
        query = "select r from sec$Role r order by r.name";
    }

    @Override
    protected void loadData(Map<String, Object> params) {

        if (rolesService.isPredefinedRolesModeAvailable()
                && rolesService.applicationHasPredefinedRoles()) {

            Collection<Role> entities = rolesService.getAllRoles();
            if (entities != null) {
                detachListener(data.values());
                data.clear();

                for (Role entity : entities) {
                    data.put(entity.getId(), entity);
                    attachListener(entity);
                }
                return;
            }
        }

        super.loadData(params);
    }
}
