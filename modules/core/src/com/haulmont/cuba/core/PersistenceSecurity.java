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

import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Security;

import java.util.Collection;

/**
 * Interface providing methods to apply security on persistence layer.
 *
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
     * Applies in-memory constraints to the entity
     * @param entity -
     * @return true, if entity should be filtered from client output
     */
    boolean applyConstraints(Entity entity);

    /**
     * Applies in-memory constraints to the collection of entities and filter the collection
     * @param entities -
     */
    void applyConstraints(Collection<Entity> entities);

    /**
     * Filter entities in collection by in-memory constraints
     * @param entities - collection of entities that will be filtered
     */
    boolean filterByConstraints(Collection<Entity> entities);

    /**
     * Reads security token and restores filtered data
     * @param resultEntity -
     */
    void restoreFilteredData(BaseGenericIdEntity<?> resultEntity);
}
