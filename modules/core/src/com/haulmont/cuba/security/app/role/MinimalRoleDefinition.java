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

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.security.app.role.annotation.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.security.role.EntityAttributePermissionsContainer;
import com.haulmont.cuba.security.role.EntityPermissionsContainer;
import com.haulmont.cuba.security.role.ScreenPermissionsContainer;
import com.haulmont.cuba.security.role.SpecificPermissionsContainer;

import javax.inject.Inject;

/**
 * System role that grants minimal permissions required for all users of generic UI client. This role is marked as a
 * default role. If this role must not be a default, set the value of the {@link ServerConfig#getMinimalRoleIsDefault}
 * property to false (cuba.security.minimalRoleIsDefault app property).
 */
@Role(name = MinimalRoleDefinition.ROLE_NAME,
        isDefault = true)
public class MinimalRoleDefinition extends AnnotatedRoleDefinition {

    public static final String ROLE_NAME = "system-minimal";

    @Inject
    private ServerConfig serverConfig;

    @Override
    @ScreenAccess(screenIds = {
            "addCondition",
            "backgroundWorkProgressWindow",
            "backgroundWorkWindow",
            "customConditionEditor",
            "customConditionFrame",
            "dynamicAttributesConditionEditor",
            "dynamicAttributesConditionFrame",
            "editWindowActions",
            "extendedEditWindowActions",
            "fileUploadDialog",
            "filterEditor",
            "filterSelect",
            "groupConditionFrame",
            "layoutAnalyzer",
            "mainWindow",
            "main",
            "loginWindow",
            "login",
            "notFoundScreen",
            "multiuploadDialog",
            "propertyConditionFrame",
            "runtimePropertiesFrame",
            "saveFilter",
            "saveSetInFolder",
            "inputDialog",
            "thirdpartyLicenseWindow",
            "list-editor-popup",
            "date-interval-editor"
    })
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @Override
    @EntityAccess(entityClass = FilterEntity.class, operations = {EntityOp.READ})
    @EntityAccess(entityClass = KeyValueEntity.class, operations = {EntityOp.READ})
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    @EntityAttributeAccess(entityClass = KeyValueEntity.class, view = "*")
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    @SpecificAccess(permissions = "cuba.gui.loginToClient")
    public SpecificPermissionsContainer specificPermissions() {
        return super.specificPermissions();
    }

    @Override
    public String getLocName() {
        return "Minimal";
    }

    @Override
    public boolean isDefault() {
        return serverConfig.getMinimalRoleIsDefault();
    }
}
