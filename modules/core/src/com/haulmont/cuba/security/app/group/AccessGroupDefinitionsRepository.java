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

package com.haulmont.cuba.security.app.group;

import com.haulmont.cuba.security.group.AccessGroupDefinition;

import java.util.Collection;

public interface AccessGroupDefinitionsRepository {
    String NAME = "cuba_AccessGroupDefinitionsRepository";

    /**
     * @return group definition by specified group name
     */
    AccessGroupDefinition getGroupDefinition(String name);

    /**
     * @return all annotation based group definitions
     */
    Collection<AccessGroupDefinition> getGroupDefinitions();

    /**
     * Allows you to register an access group created using the {@link AccessGroupDefinitionBuilder}.
     * This method should be invoked during application startup.
     *
     * @param groupDefinition group to register
     */
    void registerGroupDefinition(AccessGroupDefinition groupDefinition);
}
