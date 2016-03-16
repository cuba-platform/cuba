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

package com.haulmont.cuba.core.sys.persistence;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Interface defining methods of getting some DBMS-specific values. It must be implemented for each supported DBMS type
 * and version.
 *
 * @see com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory
 *
 */
public interface DbmsFeatures {

    /**
     * @return JPA implementation properties to set in persistence.xml
     */
    Map<String, String> getJpaParameters();

    /**
     * @return  primary key column name
     */
    String getIdColumn();

    /**
     * @return  soft deletion column name
     */
    String getDeleteTsColumn();

    /**
     * @return name of data type storing date and time
     */
    String getTimeStampType();

    /**
     * @return name of class representing UUID in JDBC driver, or null if no special class required
     */
    @Nullable
    String getUuidTypeClassName();

    /**
     * @return statement to issue for setting the current transaction timeout, or null if not required.
     * <p>The statement text should contain %d placeholder that will be replaced by timeout value in milliseconds.
     */
    @Nullable
    String getTransactionTimeoutStatement();

    /**
     * @return  regexp to extract a unique constraint name from an exception message
     */
    String getUniqueConstraintViolationPattern();

    /**
     * @return default sort order of null values
     */
    boolean isNullsLastSorting();
}
