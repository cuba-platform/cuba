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

import com.haulmont.cuba.security.entity.SecurityScope;

/**
 * Main interface for working with predefined roles. Regardless of how the role is created
 * (using the builder or using the Role annotation), it must implement this interface.
 *
 * <p>Each {@code UserSession} contains an instance of this interface ({@code effectiveRole} field).
 * This object stores all user permissions.
 */
public interface RoleDefinition {

    String getName();

    EntityPermissionsContainer entityPermissions();

    EntityAttributePermissionsContainer entityAttributePermissions();

    SpecificPermissionsContainer specificPermissions();

    ScreenPermissionsContainer screenPermissions();

    ScreenComponentPermissionsContainer screenComponentPermissions();

    default String getDescription() {
        return "";
    }

    default boolean isDefault() {
        return false;
    }

    default boolean isSuper() {
        return false;
    }

    default String getSecurityScope() {
        return SecurityScope.DEFAULT_SCOPE_NAME;
    }

    default String getLocName() {
        return "";
    }
}
