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

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.security.entity.EntityOp;

import java.util.function.Predicate;

/**
 * Represents access constraint
 */
public interface AccessConstraint {

    /**
     * @return entity type for access constraint
     */
    String getEntityType();

    /**
     * @return CRUD operation
     */
    EntityOp getOperation();

    /**
     * @return in-memory predicate, that returns true if entity is allowed by access constraint
     */
    Predicate<? extends Entity> getPredicate();

    /**
     * @return if constraint works without database access
     */
    boolean isInMemory();

    /**
     * @return true if constrain works for custom operations defined by {@link AccessConstraint#getCode()}\
     */
    boolean isCustom();

    /**
     * @return custom operation code
     */
    String getCode();
}
