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

package com.haulmont.cuba.security.group;

import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.SessionAttribute;

import javax.annotation.Nullable;
import java.util.Collection;

public interface AccessGroupsService {
    String NAME = "cuba_AccessGroupsService";

    /**
     * Load all access groups including design time groups
     */
    Collection<Group> getAllGroups();

    /**
     * Load group constraints for specified access group
     */
    Collection<Constraint> getGroupConstraints(Group group);

    /**
     * Load session attributes for specified access group
     */
    Collection<SessionAttribute> getGroupAttributes(Group group);

    /**
     * Find predefined group by its name
     */
    @Nullable
    Group findPredefinedGroupByName(String name);

    /**
     * Get default group for new user
     */
    @Nullable
    Group getUserDefaultGroup();
}
