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
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * INTERNAL
 */
public class RolesDatasource extends CollectionDatasourceImpl<Role, UUID> {

    protected RolesService rolesService = AppBeans.get(RolesService.NAME);

    @Override
    protected void loadData(Map<String, Object> params) {
        Collection<Role> entities = rolesService.getAllRoles();
        if (entities != null) {
            detachListener(data.values());
            data.clear();

            List<Role> filteredEntities = applyFilter(entities, params);

            if (sortInfos != null) {
                sortDelegate.sort(filteredEntities, sortInfos);
            }

            for (Role entity : filteredEntities) {
                data.put(entity.getId(), entity);
                attachListener(entity);
            }
        }
    }

    protected List<Role> applyFilter(Collection<Role> entities, Map<String, Object> params) {
        Stream<Role> stream = entities.stream();

        if (params.get("name") != null) {
            stream = stream.filter(e -> StringUtils.containsIgnoreCase(e.getName(), (String) params.get("name")));
        }

        if (params.get("locName") != null) {
            stream = stream.filter(e -> StringUtils.containsIgnoreCase(e.getLocName(), (String) params.get("locName")));
        }

        if (params.get("description") != null) {
            stream = stream.filter(e -> StringUtils.containsIgnoreCase(e.getDescription(), (String) params.get("description")));
        }

        return stream.collect(Collectors.toList());
    }
}
