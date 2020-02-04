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

package com.haulmont.cuba.security.app.role;

import com.haulmont.cuba.security.role.*;

import javax.inject.Inject;

/**
 * Abstract class that helps to work with {@link com.haulmont.cuba.security.app.role.annotation.Role} annotation.
 *
 * @see com.haulmont.cuba.security.app.role.annotation.Role
 */
public abstract class AnnotatedRoleDefinition implements RoleDefinition {

    @Inject
    protected AnnotatedPermissionsBuilder annotatedPermissionsBuilder;

    @Override
    public String getName() {
        return annotatedPermissionsBuilder.getNameFromAnnotation(this);
    }

    @Override
    public String getSecurityScope() {
        return annotatedPermissionsBuilder.getSecurityScopeFromAnnotation(this);
    }

    @Override
    public EntityPermissionsContainer entityPermissions() {
        return annotatedPermissionsBuilder.buildEntityAccessPermissions(this);
    }

    @Override
    public EntityAttributePermissionsContainer entityAttributePermissions() {
        return annotatedPermissionsBuilder.buildEntityAttributeAccessPermissions(this);
    }

    @Override
    public SpecificPermissionsContainer specificPermissions() {
        return annotatedPermissionsBuilder.buildSpecificPermissions(this);
    }

    @Override
    public ScreenPermissionsContainer screenPermissions() {
        return annotatedPermissionsBuilder.buildScreenPermissions(this);
    }

    @Override
    public ScreenComponentPermissionsContainer screenComponentPermissions() {
        return annotatedPermissionsBuilder.buildScreenElementsPermissions(this);
    }

    @Override
    public boolean isDefault() {
        return annotatedPermissionsBuilder.getIsDefaultFromAnnotation(this);
    }

    public boolean isSuper() {
        return annotatedPermissionsBuilder.getIsSuperFromAnnotation(this);
    }

    @Override
    public String getDescription() {
        return annotatedPermissionsBuilder.getDescriptionFromAnnotation(this);
    }
}
