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

import java.io.Serializable;

public class BasicRoleDefinition implements RoleDefinition, Serializable {

    private EntityPermissionsContainer entityPermissions;
    private EntityAttributePermissionsContainer entityAttributePermissions;
    private SpecificPermissionsContainer specificPermissions;
    private ScreenPermissionsContainer screenPermissions;
    private ScreenElementsPermissionsContainer screenElementsPermissions;
    private String name;
    private String description;
    private String securityScope;

    private BasicRoleDefinition() {
        entityPermissions = new EntityPermissionsContainer();
        entityAttributePermissions = new EntityAttributePermissionsContainer();
        specificPermissions = new SpecificPermissionsContainer();
        screenPermissions = new ScreenPermissionsContainer();
        screenElementsPermissions = new ScreenElementsPermissionsContainer();
    }

    private BasicRoleDefinition(BasicRoleDefinitionBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.screenPermissions = builder.screenPermissions;
        this.entityPermissions = builder.entityPermissions;
        this.entityAttributePermissions = builder.entityAttributePermissions;
        this.specificPermissions = builder.specificPermissions;
        this.screenElementsPermissions = builder.screenElementsPermissions;
        this.securityScope = builder.securityScope;
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
    public ScreenElementsPermissionsContainer screenElementsPermissions() {
        return screenElementsPermissions;
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

    @Override
    public String getSecurityScope() {
        return securityScope;
    }

    public void setSecurityScope(String securityScope) {
        this.securityScope = securityScope;
    }

    public static BasicRoleDefinitionBuilder builder() {
        return new BasicRoleDefinitionBuilder();
    }

    public static class BasicRoleDefinitionBuilder {

        private String name;
        private String description;
        private String securityScope;
        private EntityPermissionsContainer entityPermissions = new EntityPermissionsContainer();
        private EntityAttributePermissionsContainer entityAttributePermissions = new EntityAttributePermissionsContainer();
        private SpecificPermissionsContainer specificPermissions = new SpecificPermissionsContainer();
        private ScreenPermissionsContainer screenPermissions = new ScreenPermissionsContainer();
        private ScreenElementsPermissionsContainer screenElementsPermissions = new ScreenElementsPermissionsContainer();

        private BasicRoleDefinitionBuilder() {
        }

        public BasicRoleDefinitionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public BasicRoleDefinitionBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public BasicRoleDefinitionBuilder withSecurityScope(String securityScope) {
            this.securityScope = securityScope;
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityPermissions(EntityPermissionsContainer entityPermissions) {
            this.entityPermissions = entityPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withEntityAttributePermissions(EntityAttributePermissionsContainer entityAttributePermissions) {
            this.entityAttributePermissions = entityAttributePermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withSpecificPermissions(SpecificPermissionsContainer specificPermissions) {
            this.specificPermissions = specificPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withScreenPermissions(ScreenPermissionsContainer screenPermissions) {
            this.screenPermissions = screenPermissions;
            return this;
        }

        public BasicRoleDefinitionBuilder withScreenElementsPermissions(ScreenElementsPermissionsContainer screenElementsPermissions) {
            this.screenElementsPermissions = screenElementsPermissions;
            return this;
        }

        public BasicRoleDefinition build() {
            return new BasicRoleDefinition(this);
        }
    }
}
