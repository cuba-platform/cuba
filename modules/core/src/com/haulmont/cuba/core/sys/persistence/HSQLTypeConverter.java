/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class HSQLTypeConverter implements DbTypeConverter {
    @Override
    public Object getJavaObject(ResultSet resultSet, int columnIndex) throws SQLException {
        Object value;

        ResultSetMetaData metaData = resultSet.getMetaData();

        if ((columnIndex > metaData.getColumnCount()) || (columnIndex <= 0))
            throw new IndexOutOfBoundsException("Column index out of bound");

        value = resultSet.getObject(columnIndex);

        return value;
    }

    @Override
    public Object getSqlObject(Object value) {
        if (value instanceof Date)
            return new Timestamp(((Date) value).getTime());
        if (value instanceof UUID)
            return value.toString();
        return value;
    }

    @Override
    public int getSqlType(Class<?> javaClass) {
        if (javaClass == Date.class)
            return Types.TIMESTAMP;
        else if (javaClass == UUID.class)
            return Types.VARCHAR;
        return Types.OTHER;
    }
}
