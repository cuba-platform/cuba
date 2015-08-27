/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface defining methods to convert data between Java objects and JDBC params and results depending on the current
 * DBMS type.
 * <p/> The main goal is to convert dates and UUID.
 *
 * @author artamonov
 * @version $Id$
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
