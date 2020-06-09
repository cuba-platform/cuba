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
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.SearchFolder;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;

/**
 * System role that grants permissions to create and edit search folders
 * that accessible only by the user who created them.
 */
@Role(name = SearchFoldersRoleDefinition.ROLE_NAME)
public class SearchFoldersRoleDefinition extends AnnotatedRoleDefinition {

    public static final String ROLE_NAME = "system-search-folders";

    @Override
    @EntityAccess(entityClass = SearchFolder.class, operations = {EntityOp.READ, EntityOp.CREATE, EntityOp.DELETE, EntityOp.UPDATE})
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    @EntityAttributeAccess(entityClass = SearchFolder.class, modify = "*")
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    public String getLocName() {
        return "Local search folders access";
    }
}
