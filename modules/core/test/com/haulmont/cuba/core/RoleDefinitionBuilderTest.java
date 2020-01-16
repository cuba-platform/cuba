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

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.app.RoleDefinitionBuilder;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.PermissionsContainer;
import com.haulmont.cuba.security.role.RoleDefinition;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class RoleDefinitionBuilderTest {

    protected Metadata metadata;

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
    }

    @Test
    public void createNewRole() {
        RoleDefinition role = RoleDefinitionBuilder.create().build();

        assertNotNull(role);
        assertEquals(0, role.entityPermissions().getExplicitPermissions().size());
        assertEquals(0, role.entityAttributePermissions().getExplicitPermissions().size());
        assertEquals(0, role.specificPermissions().getExplicitPermissions().size());
        assertEquals(0, role.screenPermissions().getExplicitPermissions().size());
        assertEquals(0, role.screenElementsPermissions().getExplicitPermissions().size());
    }

    @Test
    public void createRoleWithDuplicatePermissions() {
        RoleDefinition role = RoleDefinitionBuilder.create()
                .withName("testRole")
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.DENY)
                .build();

        assertEquals("testRole", role.getName());
        assertEquals("testRole", role.getName());
        assertEquals(Integer.valueOf(0), role.screenPermissions().getExplicitPermissions().get("sec$Role.browse"));
    }

    @Test
    public void createRoleWithMultiplePermissions() {
        RoleDefinition role = RoleDefinitionBuilder.create()
                .withName("role")
                .withEntityAccessPermission(metadata.getClassNN(User.class), EntityOp.CREATE, Access.ALLOW)
                .withEntityAttrAccessPermission(metadata.getClassNN(User.class), "login", EntityAttrAccess.MODIFY)
                .withSpecificPermission("specificPermission1", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenElementPermission("sec$Role.browse", "roleGroupBox", Access.ALLOW)
                .withSpecificPermission("specificPermission2", Access.ALLOW)
                .withSpecificPermission("specificPermission3", Access.ALLOW)
                .build();

        assertEquals("role", role.getName());
        assertEquals(1, role.entityPermissions().getExplicitPermissions().size());
        assertEquals(1, role.entityAttributePermissions().getExplicitPermissions().size());
        assertEquals(1, role.screenPermissions().getExplicitPermissions().size());
        assertEquals(1, role.screenElementsPermissions().getExplicitPermissions().size());
        assertEquals(3, role.specificPermissions().getExplicitPermissions().size());

        assertTrue(isPermitted(role.specificPermissions(), "specificPermission1"));
        assertTrue(isPermitted(role.specificPermissions(), "specificPermission2"));
        assertTrue(isPermitted(role.specificPermissions(), "specificPermission3"));
        assertTrue(isPermitted(role.screenPermissions(), "sec$Role.browse"));
    }

    protected Role createRoleEntityWithPermissions() {
        Role roleEntity = metadata.create(Role.class);
        roleEntity.setName("roleEntity");
        roleEntity.setType(RoleType.DENYING);
        roleEntity.setDefaultRole(true);
        roleEntity.setDescription("test description");

        Set<Permission> permissionSet = new HashSet<>();
        permissionSet.add(createPermission(roleEntity, PermissionType.SPECIFIC, "specificPermission1", 1));
        permissionSet.add(createPermission(roleEntity, PermissionType.SPECIFIC, "specificPermission2", 0));
        roleEntity.setPermissions(permissionSet);

        return roleEntity;
    }

    protected Permission createPermission(Role role, PermissionType type, String target, Integer value) {
        Permission permission = metadata.create(Permission.class);
        permission.setRole(role);
        permission.setType(type);
        permission.setTarget(target);
        permission.setValue(value);

        return permission;
    }

    protected boolean isPermitted(PermissionsContainer screenPermissions, String target) {
        Integer v = screenPermissions.getExplicitPermissions().get(target);
        return v != null && v > 0;
    }
}
