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


import com.haulmont.cuba.security.app.role.annotation.Role;
import com.haulmont.cuba.security.role.*;

/**
 * System role that grants permissions on everything.
 */
@Role(name = FullAccessRoleDefinition.ROLE_NAME, isSuper = true)
public class FullAccessRoleDefinition extends AnnotatedRoleDefinition {

    public static final String ROLE_NAME = "system-full-access";

    @Override
    public EntityPermissionsContainer entityPermissions() {
        return super.entityPermissions();
    }

    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return super.entityAttributePermissions();
    }

    @Override
    public SpecificPermissionsContainer specificPermissions() {
        return super.specificPermissions();
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return super.screenPermissions();
    }

    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return super.screenComponentPermissions();
    }

    @Override
    public String getLocName() {
        return "Full access";
    }
}
