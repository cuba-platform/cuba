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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.app.role.AnnotationPermissionsBuilder;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationPermissionsBuilderTest {

    protected AnnotationPermissionsBuilder builder;
    protected Metadata metadata;
    protected TestPredefinedRole role;

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        builder = AppBeans.get(AnnotationPermissionsBuilder.class);
        metadata = cont.metadata();
        role = new TestPredefinedRole();
    }

    @Test
    public void testGettingInfoFromClassAnnotation() {
        assertEquals("TestPredefinedRole", builder.getNameFromAnnotation(role));
        assertEquals(RoleType.STANDARD, builder.getTypeFromAnnotation(role));
        assertEquals("Test role", builder.getDescriptionFromAnnotation(role));
        assertFalse(builder.getIsDefaultFromAnnotation(role));
    }

    @Test
    public void testPermissionsBuilding() {
        EntityAccessPermissions entityAccessPermissions = builder.buildEntityAccessPermissions(role);

        assertEquals(4, PermissionsUtils.getPermissions(entityAccessPermissions).size());
        assertTrue(entityAccessPermissions.isCreateOperationPermitted(metadata.getClassNN(User.class)));
        assertTrue(entityAccessPermissions.isReadOperationPermitted(metadata.getClassNN(User.class)));
        assertFalse(entityAccessPermissions.isUpdateOperationPermitted(metadata.getClassNN(User.class)));
        assertTrue(entityAccessPermissions.isReadOperationPermitted(metadata.getClassNN(com.haulmont.cuba.security.entity.Role.class)));

        EntityAttributeAccessPermissions entityAttributeAccessPermissions = builder.buildEntityAttributeAccessPermissions(role);

        assertEquals(3, PermissionsUtils.getPermissions(entityAttributeAccessPermissions).size());
        assertTrue(entityAttributeAccessPermissions.isModifyOperationPermitted(metadata.getClassNN(User.class), "login"));
        assertTrue(entityAttributeAccessPermissions.isReadOperationPermitted(metadata.getClassNN(com.haulmont.cuba.security.entity.Role.class), "name"));
        assertFalse(entityAttributeAccessPermissions.isModifyOperationPermitted(metadata.getClassNN(com.haulmont.cuba.security.entity.Role.class), "description"));

        SpecificPermissions specificPermissions = builder.buildSpecificPermissions(role);

        assertEquals(2, PermissionsUtils.getPermissions(specificPermissions).size());
        assertTrue(specificPermissions.isSpecificAccessPermitted("specificPermission2"));
        assertFalse(specificPermissions.isSpecificAccessPermitted("specificPermission1"));

        ScreenPermissions screenPermissions = builder.buildScreenPermissions(role);

        assertEquals(3, PermissionsUtils.getPermissions(screenPermissions).size());
        assertTrue(screenPermissions.isScreenAccessPermitted("sec$Role.edit"));
        assertTrue(screenPermissions.isScreenAccessPermitted("sec$User.edit"));
        assertFalse(screenPermissions.isScreenAccessPermitted("sec$Role.browse"));

        ScreenElementsPermissions screenElementsPermissions = builder.buildScreenElementsPermissions(role);

        assertEquals(1, PermissionsUtils.getPermissions(screenElementsPermissions).size());
        assertTrue(screenElementsPermissions.isScreenElementPermitted("sec$Role.edit", "roleGroupBox"));

    }

    @Role(name = "TestPredefinedRole", type = RoleType.STANDARD,
            isDefault = false, description = "Test role")
    protected class TestPredefinedRole implements RoleDef {

        @Override
        public RoleType getRoleType() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @EntityAccess(target = User.class,
                allow = {EntityOp.CREATE, EntityOp.READ}, deny = {EntityOp.UPDATE})
        @EntityAccess(target = com.haulmont.cuba.security.entity.Role.class,
                allow = {EntityOp.READ})
        @Override
        public EntityAccessPermissions entityAccess() {
            return null;
        }

        @EntityAttributeAccess(target = User.class, allow = {"login"})
        @EntityAttributeAccess(target = com.haulmont.cuba.security.entity.Role.class, readOnly = {"name"}, deny = {"description"})
        @Override
        public EntityAttributeAccessPermissions attributeAccess() {
            return null;
        }

        @SpecificPermission(target = "specificPermission2", access = Access.ALLOW)
        @SpecificPermission(target = "specificPermission1", access = Access.DENY)
        @Override
        public SpecificPermissions specificPermissions() {
            return null;
        }

        @ScreenAccess(allow = {"sec$Role.edit", "sec$User.edit"}, deny = {"sec$Role.browse"})
        @Override
        public ScreenPermissions screenAccess() {
            return null;
        }

        @ScreenElementAccess(screen = "sec$Role.edit", allow = {"roleGroupBox"})
        @Override
        public ScreenElementsPermissions screenElementsAccess() {
            return null;
        }
    }
}
