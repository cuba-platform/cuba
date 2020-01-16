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

package com.haulmont.cuba.security.role;

import com.haulmont.cuba.security.entity.User;

/**
 * The service is used for getting effective permissions containers. Effective permissions container evaluates all
 * implicit permissions defined by roles default values and undefined permission policies and holds explicit permission
 * value for all permission targets.
 */
public interface EffectivePermissionsService {

    String NAME = "cuba_EffectivePermissionsService";

    /**
     * Method joins all roles assigned to the user and returns an {@link EntityPermissionsContainer} that contains
     * explicit permission values for all application entities.
     */
    EntityPermissionsContainer getEffectiveEntityPermissions(User user);

    /**
     * Method joins all roles assigned to the user and returns an {@link EntityAttributePermissionsContainer} that
     * contains explicit permission values for all attributes of all entities.
     */
    EntityAttributePermissionsContainer getEffectiveEntityAttributePermissions(User user);
}
