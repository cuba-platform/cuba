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

import java.util.Set;
import java.util.stream.Stream;

/**
 * Represents set of constraints
 */
public interface SetOfAccessConstraints {

    /**
     * @return entity names for the corresponding access constraints set
     */
    Set<String> getEntityTypes();

    /**
     * @return all access constraints for the specified entity type from constraints set
     */
    Stream<AccessConstraint> findConstraintsByEntity(String entityName);

    /**
     * @return true is set contains access constraints
     */
    boolean exists();
}

