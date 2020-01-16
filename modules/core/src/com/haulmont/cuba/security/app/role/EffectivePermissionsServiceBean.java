/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.security.app.role;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewBuilder;
import com.haulmont.cuba.security.app.RoleDefinitionsJoiner;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.role.*;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

@Service(EffectivePermissionsService.NAME)
public class EffectivePermissionsServiceBean implements EffectivePermissionsService {

    @Inject
    protected EffectiveEntityPermissionsBuilder effectiveEntityPermissionsBuilder;

    @Inject
    protected EffectiveEntityAttributePermissionsBuilder effectiveEntityAttributePermissionsBuilder;

    @Inject
    protected DataManager dataManager;

    @Inject
    protected RolesService rolesService;

    @Override
    public EntityPermissionsContainer getEffectiveEntityPermissions(User user) {
        RoleDefinition effectiveRole = evaluateEffectiveRole(user);
        return effectiveEntityPermissionsBuilder.buildEffectivePermissionContainer(effectiveRole.entityPermissions());
    }

    @Override
    public EntityAttributePermissionsContainer getEffectiveEntityAttributePermissions(User user) {
        RoleDefinition effectiveRole = evaluateEffectiveRole(user);
        return effectiveEntityAttributePermissionsBuilder.buildEffectivePermissionContainer(effectiveRole.entityAttributePermissions());
    }

    protected RoleDefinition evaluateEffectiveRole(User user) {
        View userRoleView = ViewBuilder.of(UserRole.class)
                .addView(View.LOCAL)
                .add("role", builder ->
                        builder
                                .addView(View.LOCAL)
                                .add("permissions", View.LOCAL))
                .build();
        List<UserRole> userRoles = dataManager.load(UserRole.class)
                .query("select ur from sec$UserRole ur where ur.user.id = :userId")
                .parameter("userId", user.getId())
                .view(userRoleView)
                .list();

        Collection<RoleDefinition> roleDefinitions = rolesService.getRoleDefinitions(userRoles);
        return RoleDefinitionsJoiner.join(roleDefinitions);
    }
}
