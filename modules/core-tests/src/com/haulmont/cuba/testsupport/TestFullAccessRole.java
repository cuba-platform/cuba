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
    @Override
    public String getName() {
        return "system-test-full-access";
    }

    @Override
    public EntityPermissionsContainer entityPermissions() {
        EntityPermissionsContainer container = new EntityPermissionsContainer();
        container.setDefaultEntityCreateAccess(Access.ALLOW);
        container.setDefaultEntityReadAccess(Access.ALLOW);
        container.setDefaultEntityUpdateAccess(Access.ALLOW);
        container.setDefaultEntityDeleteAccess(Access.ALLOW);
        return container;
    }

    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        EntityAttributePermissionsContainer container = new EntityAttributePermissionsContainer();
        container.setDefaultEntityAttributeAccess(EntityAttrAccess.MODIFY);
        return container;
    }

    @Override
    public SpecificPermissionsContainer specificPermissions() {
        SpecificPermissionsContainer container = new SpecificPermissionsContainer();
        container.setDefaultSpecificAccess(Access.ALLOW);
        return container;
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        ScreenPermissionsContainer container = new ScreenPermissionsContainer();
        container.setDefaultScreenAccess(Access.ALLOW);
        return container;
    }

    @Override
    public ScreenElementsPermissionsContainer screenElementsPermissions() {
        return new ScreenElementsPermissionsContainer();
    }
}
