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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.app.role.AnnotatedPermissionsBuilder;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AnnotatedPermissionsBuilderTest {

    protected AnnotatedPermissionsBuilder builder;
    protected Metadata metadata;
    protected TestPredefinedRole role;

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        builder = AppBeans.get(AnnotatedPermissionsBuilder.class);
        metadata = cont.metadata();
        role = new TestPredefinedRole();
    }

    @Test
    public void testGettingInfoFromClassAnnotation() {
        assertEquals("TestPredefinedRole", builder.getNameFromAnnotation(role));
        assertEquals("Test role", builder.getDescriptionFromAnnotation(role));
        assertFalse(builder.getIsDefaultFromAnnotation(role));
    }

    @Test
    public void testPermissionsBuilding() {
        EntityPermissionsContainer entityPermissions = builder.buildEntityAccessPermissions(role);

        MetaClass userMetaClass = metadata.getClassNN(User.class);
        MetaClass roleMetaClass = metadata.getClassNN(com.haulmont.cuba.security.entity.Role.class);

        assertEquals(3, entityPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.CREATE)));
        assertEquals(Access.ALLOW.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.READ)));
        assertEquals(Access.ALLOW.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(roleMetaClass, EntityOp.READ)));

        EntityAttributePermissionsContainer entityAttributePermissions =
                builder.buildEntityAttributeAccessPermissions(role);
        assertEquals(2, entityAttributePermissions.getExplicitPermissions().size());
        assertEquals(EntityAttrAccess.MODIFY.getId(),
                entityAttributePermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityAttributeTarget(userMetaClass, "login")));
        assertEquals(EntityAttrAccess.VIEW.getId(),
                entityAttributePermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityAttributeTarget(roleMetaClass, "name")));


        SpecificPermissionsContainer specificPermissions = builder.buildSpecificPermissions(role);
        assertEquals(2, specificPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(), specificPermissions.getExplicitPermissions().get("specificPermission2"));
        assertEquals(Access.ALLOW.getId(), specificPermissions.getExplicitPermissions().get("specificPermission1"));


        ScreenPermissionsContainer screenPermissions = builder.buildScreenPermissions(role);
        assertEquals(2, screenPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(), screenPermissions.getExplicitPermissions().get("sec$Role.edit"));
        assertEquals(Access.ALLOW.getId(), screenPermissions.getExplicitPermissions().get("sec$User.edit"));


        ScreenComponentPermissionsContainer screenElementsPermissions = builder.buildScreenElementsPermissions(role);
        assertEquals(2, screenElementsPermissions.getExplicitPermissions().size());
        assertEquals(ScreenComponentPermission.MODIFY.getId(),
                screenElementsPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getScreenComponentTarget("sec$Role.edit", "roleGroupBox")));

        assertEquals(ScreenComponentPermission.VIEW.getId(),
                screenElementsPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getScreenComponentTarget("sec$Role.edit", "roleGroupBox_1")));
    }

    @Test
    public void testAttributeWildcards() {
        TestWildcardRole testRole = new TestWildcardRole();
        EntityAttributePermissionsContainer permissionsContainer = builder.buildEntityAttributeAccessPermissions(testRole);

        Map<String, Integer> explicitPermissions = permissionsContainer.getExplicitPermissions();
        assertEquals(EntityAttrAccess.VIEW.getId(), explicitPermissions.get("sec$User:*"));
        assertEquals(EntityAttrAccess.MODIFY.getId(), explicitPermissions.get("sec$Role:*"));
    }

    @Test
    public void testEntityWildcards() {
        TestWildcardRole testRole = new TestWildcardRole();
        EntityPermissionsContainer permissionsContainer = builder.buildEntityAccessPermissions(testRole);

        Map<String, Integer> explicitPermissions = permissionsContainer.getExplicitPermissions();
        assertEquals(Access.ALLOW.getId(), explicitPermissions.get("*:read"));
        assertNull(explicitPermissions.get("*:create"));
    }

    @Role(name = "TestPredefinedRole",
            isDefault = false,
            description = "Test role")
    protected class TestPredefinedRole implements RoleDefinition {

        @Override
        public String getName() {
            return null;
        }

        @EntityAccess(entityClass = User.class,
                operations = {EntityOp.CREATE, EntityOp.READ})
        @EntityAccess(entityClass = com.haulmont.cuba.security.entity.Role.class,
                operations = {EntityOp.READ})
        @Override
        public EntityPermissionsContainer entityPermissions() {
            return null;
        }

        @EntityAttributeAccess(entityClass = User.class, modify = {"login"})
        @EntityAttributeAccess(entityClass = com.haulmont.cuba.security.entity.Role.class,
                view = {"name"})
        @Override
        public EntityAttributePermissionsContainer entityAttributePermissions() {
            return null;
        }

        @SpecificAccess(permissions = "specificPermission2")
        @SpecificAccess(permissions = "specificPermission1")
        @Override
        public SpecificPermissionsContainer specificPermissions() {
            return null;
        }

        @ScreenAccess(screenIds = {"sec$Role.edit", "sec$User.edit"})
        @Override
        public ScreenPermissionsContainer screenPermissions() {
            return null;
        }

        @ScreenComponentAccess(screenId = "sec$Role.edit",  modify = {"roleGroupBox"}, view = {"roleGroupBox_1"})
        @Override
        public ScreenComponentPermissionsContainer screenComponentPermissions() {
            return null;
        }
    }

    @Role(name = "TestWildcardRole")
    protected class TestWildcardRole implements RoleDefinition {

        @Override
        public String getName() {
            return null;
        }

        @Override
        @EntityAccess(entityName = "*", operations = {EntityOp.READ})
        public EntityPermissionsContainer entityPermissions() {
            return null;
        }

        @Override
        @EntityAttributeAccess(entityClass = User.class, view = {"*"})
        @EntityAttributeAccess(entityName = "sec$Role", modify = {"*"})
        public EntityAttributePermissionsContainer entityAttributePermissions() {
            return null;
        }

        @Override
        public SpecificPermissionsContainer specificPermissions() {
            return null;
        }

        @Override
        public ScreenPermissionsContainer screenPermissions() {
            return null;
        }

        @Override
        public ScreenComponentPermissionsContainer screenComponentPermissions() {
            return null;
        }
    }
}
