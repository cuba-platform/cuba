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
import com.haulmont.cuba.security.app.role.RoleDefBuilder;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.PermissionsUtils;
import com.haulmont.cuba.security.role.RoleDef;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class RoleDefBuilderTest {

    protected Metadata metadata;

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
    }

    @Test
    public void createNewRole() {
        RoleDef role = RoleDefBuilder.createRole().build();

        assertNotNull(role);
        assertEquals(RoleType.STANDARD, role.getRoleType());
        assertEquals(0, PermissionsUtils.getPermissions(role.entityAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.attributeAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.specificPermissions()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenElementsAccess()).size());
    }

    @Test
    public void createRoleWithDuplicatePermissions() {
        RoleDef role = RoleDefBuilder.createRole()
                .withName("testRole")
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .withScreenPermission("sec$Role.browse", Access.DENY)
                .build();

        assertEquals("testRole", role.getName());
        assertEquals(1, PermissionsUtils.getPermissions(role.screenAccess()).size());
        assertTrue(role.screenAccess().isScreenAccessPermitted("sec$Role.browse"));
    }

    @Test
    public void createRoleDefBasedOnRoleEntity() {
        Role roleEntity = createRoleEntityWithPermissions();

        RoleDef role = RoleDefBuilder.createRole(roleEntity).build();

        assertEquals("roleEntity", role.getName());
        assertEquals(RoleType.DENYING, role.getRoleType());
        assertFalse(role.isDefault());
        assertEquals("test description", role.getDescription());
        assertEquals(0, PermissionsUtils.getPermissions(role.entityAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.attributeAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role.screenElementsAccess()).size());
        assertEquals(2, PermissionsUtils.getPermissions(role.specificPermissions()).size());
        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission1"));
        assertFalse(role.specificPermissions().isSpecificAccessPermitted("specificPermission2"));
    }

    @Test
    public void joinRole() {
        RoleDef role1 = RoleDefBuilder.createRole()
                .withScreenPermission("sec$Role.browse", Access.ALLOW)
                .build();

        RoleDef role2 = RoleDefBuilder.createRole(RoleType.DENYING)
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

        assertEquals(0, PermissionsUtils.getPermissions(role2.entityAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role2.attributeAccess()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role2.screenAccess()).size());
        assertEquals(0, PermissionsUtils.getPermissions(role2.screenElementsAccess()).size());
        assertEquals(3, PermissionsUtils.getPermissions(role2.specificPermissions()).size());

        assertTrue(role2.specificPermissions().isSpecificAccessPermitted("specificPermission1"));
        assertFalse(role2.specificPermissions().isSpecificAccessPermitted("specificPermission2"));
        assertTrue(role2.specificPermissions().isSpecificAccessPermitted("specificPermission3"));
        assertTrue(role2.screenAccess().isScreenAccessPermitted("sec$Role.browse"));
    }

    @Test
    public void createRoleWithMultiplePermissions() {
        RoleDef role = RoleDefBuilder.createRole()
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
        assertEquals(1, PermissionsUtils.getPermissions(role.entityAccess()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role.attributeAccess()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role.screenAccess()).size());
        assertEquals(1, PermissionsUtils.getPermissions(role.screenElementsAccess()).size());
        assertEquals(3, PermissionsUtils.getPermissions(role.specificPermissions()).size());

        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission1"));
        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission2"));
        assertTrue(role.specificPermissions().isSpecificAccessPermitted("specificPermission3"));
        assertTrue(role.screenAccess().isScreenAccessPermitted("sec$Role.browse"));
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
