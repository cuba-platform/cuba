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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface defining methods to convert data between Java objects and JDBC params and results depending on the current
 * DBMS type.
 * <p/> The main goal is to convert dates and UUID.
 *
 */
public interface DbTypeConverter {

    /**
     * Convert a JDBC ResultSet column value to a value appropriate for an entity attribute.
     *
     * @param resultSet JDBC ResultSet
     * @param column    ResultSet column number, starting from 1
     * @return          corresponding value for an entity attribute
     */
    Object getJavaObject(ResultSet resultSet, int column);

    /**
     * Convert an entity attribute value to a value appropriate for a JDBC parameter.
     *
     * @param value an entity attribute value
     * @return      corresponding value for a JDBC parameter
     */
    Object getSqlObject(Object value);

    /**
     * Get a JDBC type corresponding to an entity attribute type.
     *
     * @param javaClass entity attribute type
     * @return          corresponding JDBC type
     * @see java.sql.Types
     */
    int getSqlType(Class<?> javaClass);
}
