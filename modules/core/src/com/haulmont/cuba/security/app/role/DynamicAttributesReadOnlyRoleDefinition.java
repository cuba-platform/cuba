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

import com.haulmont.cuba.core.entity.Category;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.security.app.role.annotation.EntityAccess;
import com.haulmont.cuba.security.app.role.annotation.EntityAttributeAccess;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.app.role.annotation.ScreenAccess;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.ScreenPermissionsContainer;

/**
 * System role that grants read-only permissions to work with dynamic attributes administration screen.
 */
@Role(name = DynamicAttributesReadOnlyRoleDefinition.ROLE_NAME)
public class DynamicAttributesReadOnlyRoleDefinition extends AnnotatedRoleDefinition {

    public static final String ROLE_NAME = "system-dynamic-attributes-read-only";

    @Override
    @ScreenAccess(screenIds = {
            "administration",
            "sys$Category.browse",
            "sys$Category.edit",
            "runtimePropertiesFrame",
            "sys$CategoryAttribute.edit"
    })
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @Override
    @EntityAccess(entityClass = Category.class, operations = {EntityOp.READ})
    @EntityAccess(entityClass = CategoryAttribute.class, operations = {EntityOp.READ})
    @EntityAccess(entityName = "sys$ScreenAndComponent", operations = {EntityOp.READ})
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    @EntityAttributeAccess(entityClass = Category.class, view = "*")
    @EntityAttributeAccess(entityClass = CategoryAttribute.class, view = "*")
    @EntityAttributeAccess(entityName = "sys$ScreenAndComponent", view = "*")
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    public String getLocName() {
        return "Dynamic attributes read-only access";
    }
}
