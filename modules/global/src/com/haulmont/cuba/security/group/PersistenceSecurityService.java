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

public interface PersistenceSecurityService {

    String NAME = "cuba_PersistenceSecurityService";

    /**
     * Check if the operation type is permitted for the entity
     */
    boolean isPermitted(Entity entity, EntityOp operation);

    /**
     * Check the special constraint permission for the entity
     */
    boolean isPermitted(Entity entity, String customCode);

    /**
     * Execute groovy constraint script
     *
     * @return boolean as script result
     */
    Object evaluateConstraintScript(Entity entity, String groovyScript);

    /**
     * Validate groovy access constraint script
     */
    ConstraintValidationResult validateConstraintScript(String entityType, String groovyScript);
}
