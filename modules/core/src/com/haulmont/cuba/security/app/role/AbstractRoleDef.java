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

import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.role.*;

import javax.inject.Inject;

/**
 * Abstract class that helps to work with {@link com.haulmont.cuba.security.app.role.annotation.Role} annotation.
 *
 * @see com.haulmont.cuba.security.app.role.annotation.Role
 */
public abstract class AbstractRoleDef implements RoleDef {

    @Inject
    protected AnnotationPermissionsBuilder annotationPermissionsBuilder;

    @Override
    public RoleType getRoleType() {
        return annotationPermissionsBuilder.getTypeFromAnnotation(this);
    }

    @Override
    public String getName() {
        return annotationPermissionsBuilder.getNameFromAnnotation(this);
    }

    @Override
    public EntityAccessPermissions entityAccess() {
        return annotationPermissionsBuilder.buildEntityAccessPermissions(this);
    }

    @Override
    public EntityAttributeAccessPermissions attributeAccess() {
        return annotationPermissionsBuilder.buildEntityAttributeAccessPermissions(this);
    }

    @Override
    public SpecificPermissions specificPermissions() {
        return annotationPermissionsBuilder.buildSpecificPermissions(this);
    }

    @Override
    public ScreenPermissions screenAccess() {
        return annotationPermissionsBuilder.buildScreenPermissions(this);
    }

    @Override
    public ScreenElementsPermissions screenElementsAccess() {
        return annotationPermissionsBuilder.buildScreenElementsPermissions(this);
    }

    @Override
    public boolean isDefault() {
        return annotationPermissionsBuilder.getIsDefaultFromAnnotation(this);
    }

    @Override
    public String getDescription() {
        return annotationPermissionsBuilder.getDescriptionFromAnnotation(this);
    }
}
