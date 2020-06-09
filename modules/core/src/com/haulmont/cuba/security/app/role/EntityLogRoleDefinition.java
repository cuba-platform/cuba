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

import com.haulmont.cuba.security.app.role.annotation.EntityAccess;
import com.haulmont.cuba.security.app.role.annotation.EntityAttributeAccess;
import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.app.role.annotation.ScreenAccess;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.ScreenPermissionsContainer;

/**
 * System role that grants permissions to work with entity log.
 */
@Role(name = EntityLogRoleDefinition.ROLE_NAME)
public class EntityLogRoleDefinition extends AnnotatedRoleDefinition {

    public static final String ROLE_NAME = "system-entity-log";

    @Override
    @ScreenAccess(screenIds = {
            "administration",
            "entityLog",
            "commonLookup"
    })
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @Override
    @EntityAccess(entityClass = LoggedEntity.class, operations = {EntityOp.READ, EntityOp.CREATE, EntityOp.DELETE, EntityOp.UPDATE})
    @EntityAccess(entityClass = EntityLogItem.class, operations = {EntityOp.READ, EntityOp.CREATE, EntityOp.DELETE, EntityOp.UPDATE})
    @EntityAccess(entityClass = EntityLogAttr.class, operations = {EntityOp.READ, EntityOp.CREATE, EntityOp.DELETE, EntityOp.UPDATE})
    @EntityAccess(entityClass = LoggedAttribute.class, operations = {EntityOp.READ, EntityOp.CREATE, EntityOp.DELETE, EntityOp.UPDATE})
    @EntityAccess(entityClass = User.class, operations = {EntityOp.READ})
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    @EntityAttributeAccess(entityClass = LoggedEntity.class, modify = "*")
    @EntityAttributeAccess(entityClass = EntityLogItem.class, modify = "*")
    @EntityAttributeAccess(entityClass = EntityLogAttr.class, modify = "*")
    @EntityAttributeAccess(entityClass = LoggedAttribute.class, modify = "*")
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    public String getLocName() {
        return "Entity log access";
    }
}
