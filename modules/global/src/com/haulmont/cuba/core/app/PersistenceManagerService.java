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
package com.haulmont.cuba.core.app;

/**
 * Exposes some of {@code PersistenceManagerAPI} methods and other DBMS-related information to the client tier.
 *
 */
public interface PersistenceManagerService {

    String NAME = "cuba_PersistenceManagerService";

    boolean useLazyCollection(String entityName);

    boolean useLookupScreen(String entityName);

    int getFetchUI(String entityName);

    int getMaxFetchUI(String entityName);

    /**
     * @return current DBMS type set by {@code cuba.dbmsType} app property on Middleware
     */
    String getDbmsType();

    /**
     * @return current DBMS version set by {@code cuba.dbmsVersion} app property on Middleware
     */
    String getDbmsVersion();

    /**
     * @return  regexp to extract a unique constraint name from an exception message.
     * <p>See {@code DbmsFeatures.getUniqueConstraintViolationPattern()}
     */
    String getUniqueConstraintViolationPattern();

    /**
     * @return default sort order of null values used by the current DBMS
     */
    boolean isNullsLastSorting();
}
