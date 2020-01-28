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

package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.role.*;

public class TestFullAccessRole implements RoleDefinition {

    private EntityPermissionsContainer entityPermissions;
    private EntityAttributePermissionsContainer entityAttributePermissions;
    private SpecificPermissionsContainer specificPermissions;
    private ScreenPermissionsContainer screenPermissions;
    private ScreenComponentPermissionsContainer screenElementsPermissions;

    public TestFullAccessRole() {
        entityPermissions = new EntityPermissionsContainer();
        entityAttributePermissions = new EntityAttributePermissionsContainer();
        specificPermissions = new SpecificPermissionsContainer();
        screenPermissions = new ScreenPermissionsContainer();
        screenElementsPermissions = new ScreenComponentPermissionsContainer();

        entityPermissions.setDefaultEntityCreateAccess(Access.ALLOW);
        entityPermissions.setDefaultEntityReadAccess(Access.ALLOW);
        entityPermissions.setDefaultEntityUpdateAccess(Access.ALLOW);
        entityPermissions.setDefaultEntityDeleteAccess(Access.ALLOW);
        entityAttributePermissions.setDefaultEntityAttributeAccess(EntityAttrAccess.MODIFY);
        specificPermissions.setDefaultSpecificAccess(Access.ALLOW);
        screenPermissions.setDefaultScreenAccess(Access.ALLOW);
    }

    @Override
    public String getName() {
        return "system-test-full-access";
    }

    @Override
    public EntityPermissionsContainer entityPermissions() {
        return entityPermissions;
    }

    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return entityAttributePermissions;
    }

    @Override
    public SpecificPermissionsContainer specificPermissions() {
        return specificPermissions;
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return screenPermissions;
    }

    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return screenElementsPermissions;
    }
}
