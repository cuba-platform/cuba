/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.group.ConstraintValidationResult;

import java.util.Collection;

/**
 * Interface providing methods to apply security on persistence layer.
 */
public interface PersistenceSecurity extends Security {

    String NAME = "cuba_PersistenceSecurity";

    String CONSTRAINT_PARAM_SESSION_ATTR = "session$";
    String CONSTRAINT_PARAM_USER_LOGIN = "userLogin";
    String CONSTRAINT_PARAM_USER_ID = "userId";
    String CONSTRAINT_PARAM_USER_GROUP_ID = "userGroupId";

    /**
     * Modifies the query depending on current user's security constraints.
     *
     * @param query query to modify
     * @return true if any constraints have been applied
     */
    boolean applyConstraints(Query query);

    /**
     * Sets the query param to a value provided by user session (see constants above).
     *
     * @param query     Query instance
     * @param paramName parameter to set
     */
    void setQueryParam(Query query, String paramName);

    /**
     * Applies in-memory constraints to the entity by filtered data
     *
     * @param entity -
     */
    void applyConstraints(Entity entity);

    /**
     * Applies in-memory constraints to the entity fields by filtered data
     *
     * @param entities - collection of entities
     */
    void applyConstraints(Collection<Entity> entities);

    /**
     * Filter entities in collection by in-memory constraints
     *
     * @param entities - collection of entities that will be filtered
     * @return true if some items were filtered out
     */
    boolean filterByConstraints(Collection<Entity> entities);

    /**
     * Filter entity by in-memory constraints
     *
     * @param entity - entity that will be filtered
     * @return true, if entity should be filtered out from client output
     */
    boolean filterByConstraints(Entity entity);

    /**
     * Reads security token and restores security state
     *
     * @param entity - entity to restore security state
     */
    void restoreSecurityState(Entity entity);

    /**
     * Restores filtered data from security token
     *
     * @param entity - entity to restore filtered data
     */
    void restoreFilteredData(Entity entity);

    /**
     * Reads security token and restores security state and filtered data
     *
     * @param entity - entity to restore
     */
    default void restoreSecurityStateAndFilteredData(Entity entity) {
        restoreSecurityState(entity);
        restoreFilteredData(entity);
    }

    /**
     * Validate that security token exists for specific cases.
     * For example, security constraints exists
     *
     * @param entity - entity to check security token
     */
    void assertToken(Entity entity);

    /**
     * Validate that security token for REST exists for specific cases.
     * For example, security constraints exists
     *
     * @param entity - entity to check security token
     * @param view   - view for entity
     */
    void assertTokenForREST(Entity entity, View view);

    /**
     * Calculate filtered data
     *
     * @param entity for which will calculate filtered data
     */
    void calculateFilteredData(Entity entity);

    /**
     * Calculate filtered data
     *
     * @param entities - collection of entities for which will calculate filtered data
     */
    void calculateFilteredData(Collection<Entity> entities);

    /**
     * Check if there are registered memory read constraints for the metaClass or it's original metaClass
     */
    boolean hasInMemoryReadConstraints(MetaClass metaClass);

    /**
     * Validate groovy access constraint script
     */
    ConstraintValidationResult validateConstraintScript(String entityType, String groovyScript);
}
