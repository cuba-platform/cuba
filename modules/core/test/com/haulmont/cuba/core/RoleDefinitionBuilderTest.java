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
import com.haulmont.cuba.security.app.role.RoleDefinitionBuilder;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.PermissionsUtils;
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
        assertEquals(RoleType.STANDARD, role.getRoleType());
        assertEquals(0, PermissionsUtils.getPermissions(role.entityPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.entityAttributePermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.specificPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenElementsPermissions()).size());
    }

    @Test
    public void createRoleWithDuplicatePermissions() {
        RoleDefinition role = RoleDefinitionBuilder.create()
                .withName("testRole")
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.DENY)
                .build();

        assertEquals("testRole", role.getName());
        assertEquals(1, PermissionsUtils.getPermissions(role.screenPermissions()).size());
        assertTrue(role.screenPermissions().isScreenAccessPermitted("sec$Role.browse"));
    }

    @Test
    public void createRoleDefBasedOnRoleEntity() {
        Role roleEntity = createRoleEntityWithPermissions();

        RoleDefinition role = RoleDefinitionBuilder.create()
                .withRoleType(roleEntity.getType())
                .withName(roleEntity.getName())
                .withDescription(roleEntity.getDescription())
                .join(roleEntity)
                .build();

        assertEquals("roleEntity", role.getName());
        assertEquals(RoleType.DENYING, role.getRoleType());
        assertFalse(role.isDefault());
        assertEquals("test description", role.getDescription());
        assertEquals(0, PermissionsUtils.getPermissions(role.entityPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.entityAttributePermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenElementsPermissions()).size());
        assertEquals(2, PermissionsUtils.getPermissions(role.specificPermissions()).size());
        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission1"));
        assertFalse(role.specificPermissions().isSpecificAccessPermitted("specificPermission2"));
    }

    @Test
    public void joinRole() {
        RoleDefinition role1 = RoleDefinitionBuilder.create()
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .build();

        RoleDefinition role2 = RoleDefinitionBuilder.create()
                .withRoleType(RoleType.DENYING)
                .withName("ordinaryRole")
                .withDescription("description")
                .withSpecificPermission("specificPermission3", Access.ALLOW)
                .join(createRoleEntityWithPermissions())
                .join(role1)
                .build();

        assertEquals("ordinaryRole", role2.getName());
        assertEquals(RoleType.DENYING, role2.getRoleType());
        assertFalse(role2.isDefault());
        assertEquals("description", role2.getDescription());

        assertEquals(0, PermissionsUtils.getPermissions(role2.entityPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role2.entityAttributePermissions()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role2.screenPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role2.screenElementsPermissions()).size());
        assertEquals(3, PermissionsUtils.getPermissions(role2.specificPermissions()).size());

        assertTrue(role2.specificPermissions().isSpecificAccessPermitted("specificPermission1"));
        assertFalse(role2.specificPermissions().isSpecificAccessPermitted("specificPermission2"));
        assertTrue(role2.specificPermissions().isSpecificAccessPermitted("specificPermission3"));
        assertTrue(role2.screenPermissions().isScreenAccessPermitted("sec$Role.browse"));
    }

    @Test
    public void createRoleWithMultiplePermissions() {
        RoleDefinition role = RoleDefinitionBuilder.create()
                .withName("role")
                .withRoleType(RoleType.STANDARD)
                .withEntityAccessPermission(metadata.getClassNN(User.class), EntityOp.CREATE, Access.ALLOW)
                .withEntityAttrAccessPermission(metadata.getClassNN(User.class), "login", EntityAttrAccess.MODIFY)
                .withSpecificPermission("specificPermission1", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenElementPermission("sec$Role.browse", "roleGroupBox", Access.ALLOW)
                .withSpecificPermission("specificPermission2", Access.ALLOW)
                .withSpecificPermission("specificPermission3", Access.ALLOW)
                .build();

        assertEquals("role", role.getName());
        assertEquals(RoleType.STANDARD, role.getRoleType());
        assertEquals(1, PermissionsUtils.getPermissions(role.entityPermissions()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role.entityAttributePermissions()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role.screenPermissions()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role.screenElementsPermissions()).size());
        assertEquals(3, PermissionsUtils.getPermissions(role.specificPermissions()).size());

        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission1"));
        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission2"));
        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission3"));
        assertTrue(role.screenPermissions().isScreenAccessPermitted("sec$Role.browse"));
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
}
