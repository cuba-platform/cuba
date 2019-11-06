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

package com.haulmont.cuba.security.role;

import com.haulmont.cuba.security.entity.RoleType;

import java.io.Serializable;

public class BasicRoleDefinition implements RoleDefinition, Serializable {

    private EntityPermissions entityPermissions;
    private EntityAttributePermissions entityAttributePermissions;
    private SpecificPermissions specificPermissions;
    private ScreenPermissions screenPermissions;
    private ScreenElementsPermissions screenElementsPermissions;
    private RoleType roleType;
    private String name;
    private String description;

    public BasicRoleDefinition(String name,
                               String description,
                               RoleType roleType,
                               EntityPermissions entityPermissions,
                               EntityAttributePermissions entityAttributePermissions,
                               SpecificPermissions specificPermissions,
                               ScreenPermissions screenPermissions,
                               ScreenElementsPermissions screenElementsPermissions) {
        this.name = name;
        this.description = description;
        this.entityPermissions = entityPermissions;
        this.entityAttributePermissions = entityAttributePermissions;
        this.specificPermissions = specificPermissions;
        this.screenPermissions = screenPermissions;
        this.screenElementsPermissions = screenElementsPermissions;
        this.roleType = roleType;
    }

    public BasicRoleDefinition() {
        initApplicationRoleFields();
        initGenericUiRoleFields();
        roleType = RoleType.STANDARD;
    }

    private void initApplicationRoleFields() {
        entityPermissions = new EntityPermissions();
        entityAttributePermissions = new EntityAttributePermissions();
        specificPermissions = new SpecificPermissions();
    }

    private void initGenericUiRoleFields() {
        screenPermissions = new ScreenPermissions();
        screenElementsPermissions = new ScreenElementsPermissions();
    }

    @Override
    public EntityPermissions entityPermissions() {
        return entityPermissions;
    }

    @Override
    public EntityAttributePermissions entityAttributePermissions() {
        return entityAttributePermissions;
    }

    @Override
    public SpecificPermissions specificPermissions() {
        return specificPermissions;
    }

    @Override
    public ScreenPermissions screenPermissions() {
        return screenPermissions;
    }

    @Override
    public ScreenElementsPermissions screenElementsPermissions() {
        return screenElementsPermissions;
    }

    @Override
    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
