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

package com.haulmont.cuba.core.sys.persistence;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Provides the additional criteria for the query.
 */
public interface AdditionalCriteriaProvider {

    /**
     * Checks if the class needs additional criteria.
     *
     * @param entityClass a class.
     * @return true if a query to this class requires additional criteria.
     */
    boolean requiresAdditionalCriteria(Class entityClass);

    /**
     * Returns the additional criteria.
     *
     * @return The string of this additional criteria
     */
    String getAdditionalCriteria(Class entityClass);

    /**
     * Returns parameters of the additional criteria.
     *
     * @return The map of parameters, entered in the additional criteria.
     */
    @Nullable
    Map<String, Object> getCriteriaParameters();
}