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
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotatedPermissionsBuilderTest {

    protected AnnotatedPermissionsBuilder builder;
    protected Metadata metadata;
    protected TestPredefinedRole role;
    protected TestDefaultsRole defaultsRole;

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        builder = AppBeans.get(AnnotatedPermissionsBuilder.class);
        metadata = cont.metadata();
        role = new TestPredefinedRole();
        defaultsRole = new TestDefaultsRole();
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

        assertEquals(4, entityPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.CREATE)));
        assertEquals(Access.ALLOW.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.READ)));
        assertEquals(Access.DENY.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(userMetaClass, EntityOp.UPDATE)));
        assertEquals(Access.ALLOW.getId(),
                entityPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityOperationTarget(roleMetaClass, EntityOp.READ)));

        EntityAttributePermissionsContainer entityAttributePermissions =
                builder.buildEntityAttributeAccessPermissions(role);
        assertEquals(3, entityAttributePermissions.getExplicitPermissions().size());
        assertEquals(EntityAttrAccess.MODIFY.getId(),
                entityAttributePermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityAttributeTarget(userMetaClass, "login")));
        assertEquals(EntityAttrAccess.VIEW.getId(),
                entityAttributePermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityAttributeTarget(roleMetaClass, "name")));
        assertEquals(EntityAttrAccess.DENY.getId(),
                entityAttributePermissions.getExplicitPermissions().get(
                        PermissionsUtils.getEntityAttributeTarget(roleMetaClass, "description")));



        SpecificPermissionsContainer specificPermissions = builder.buildSpecificPermissions(role);
        assertEquals(2, specificPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(), specificPermissions.getExplicitPermissions().get("specificPermission2"));
        assertEquals(Access.DENY.getId(), specificPermissions.getExplicitPermissions().get("specificPermission1"));


        ScreenPermissionsContainer screenPermissions = builder.buildScreenPermissions(role);
        assertEquals(3, screenPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(), screenPermissions.getExplicitPermissions().get("sec$Role.edit"));
        assertEquals(Access.ALLOW.getId(), screenPermissions.getExplicitPermissions().get("sec$User.edit"));
        assertEquals(Access.DENY.getId(), screenPermissions.getExplicitPermissions().get("sec$Role.browse"));


        ScreenElementsPermissionsContainer screenElementsPermissions = builder.buildScreenElementsPermissions(role);
        assertEquals(1, screenElementsPermissions.getExplicitPermissions().size());
        assertEquals(Access.ALLOW.getId(),
                screenElementsPermissions.getExplicitPermissions().get(
                        PermissionsUtils.getScreenElementTarget("sec$Role.edit", "roleGroupBox")));
    }

    @Test
    public void testDefaultPermissionsBuilding() {
        EntityPermissionsContainer entityPermissions = builder.buildEntityAccessPermissions(defaultsRole);
        assertEquals(Access.ALLOW, entityPermissions.getDefaultEntityCreateAccess());
        assertEquals(Access.ALLOW, entityPermissions.getDefaultEntityReadAccess());
        assertEquals(Access.DENY, entityPermissions.getDefaultEntityUpdateAccess());
        assertNull(entityPermissions.getDefaultEntityDeleteAccess());

        ScreenPermissionsContainer screenPermissions = builder.buildScreenPermissions(defaultsRole);
        assertEquals(Access.ALLOW, screenPermissions.getDefaultScreenAccess());

        EntityAttributePermissionsContainer entityAttributeAccessPermissions = builder.buildEntityAttributeAccessPermissions(defaultsRole);
        assertEquals(EntityAttrAccess.VIEW, entityAttributeAccessPermissions.getDefaultEntityAttributeAccess());

        SpecificPermissionsContainer specificPermissions = builder.buildSpecificPermissions(defaultsRole);
        assertEquals(Access.ALLOW, specificPermissions.getDefaultSpecificAccess());
    }

    @Role(name = "TestPredefinedRole",
            isDefault = false,
            description = "Test role")
    protected class TestPredefinedRole implements RoleDefinition {

        @Override
        public String getName() {
            return null;
        }

        @EntityAccess(target = User.class,
                allow = {EntityOp.CREATE, EntityOp.READ}, deny = {EntityOp.UPDATE})
        @EntityAccess(target = com.haulmont.cuba.security.entity.Role.class,
                allow = {EntityOp.READ})
        @Override
        public EntityPermissionsContainer entityPermissions() {
            return null;
        }

        @EntityAttributeAccess(target = User.class, modify = {"login"})
        @EntityAttributeAccess(target = com.haulmont.cuba.security.entity.Role.class,
                view = {"name"},
                deny = {"description"})
        @Override
        public EntityAttributePermissionsContainer entityAttributePermissions() {
            return null;
        }

        @SpecificAccess(target = "specificPermission2", access = Access.ALLOW)
        @SpecificAccess(target = "specificPermission1", access = Access.DENY)
        @Override
        public SpecificPermissionsContainer specificPermissions() {
            return null;
        }

        @ScreenAccess(allow = {"sec$Role.edit", "sec$User.edit"}, deny = {"sec$Role.browse"})
        @Override
        public ScreenPermissionsContainer screenPermissions() {
            return null;
        }

        @ScreenElementAccess(screen = "sec$Role.edit", allow = {"roleGroupBox"})
        @Override
        public ScreenElementsPermissionsContainer screenElementsPermissions() {
            return null;
        }
    }

    @Role(name = "TestDefaultsRole",
            isDefault = false,
            description = "Test defaults role")
    protected class TestDefaultsRole implements RoleDefinition {

        @Override
        public String getName() {
            return null;
        }

        @Override
        @DefaultEntityAccess(allow = {EntityOp.CREATE, EntityOp.READ}, deny = {EntityOp.UPDATE})
        public EntityPermissionsContainer entityPermissions() {
            return null;
        }

        @Override
        @DefaultEntityAttributeAccess(EntityAttrAccess.VIEW)
        public EntityAttributePermissionsContainer entityAttributePermissions() {
            return null;
        }

        @Override
        @DefaultSpecificAccess(Access.ALLOW)
        public SpecificPermissionsContainer specificPermissions() {
            return null;
        }

        @Override
        @DefaultScreenAccess(Access.ALLOW)
        public ScreenPermissionsContainer screenPermissions() {
            return null;
        }

        @Override
        public ScreenElementsPermissionsContainer screenElementsPermissions() {
            return null;
        }
    }
}
