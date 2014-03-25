/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.global.UuidProvider;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class PostgresTypeConverter implements DbTypeConverter {

    @Override
    public Object getJavaObject(ResultSet resultSet, int columnIndex) throws SQLException {
        Object value;

        ResultSetMetaData metaData = resultSet.getMetaData();

        if ((columnIndex > metaData.getColumnCount()) || (columnIndex <= 0))
            throw new IndexOutOfBoundsException("Column index out of bound");

        int sqlType = metaData.getColumnType(columnIndex);
        String typeName = metaData.getColumnTypeName(columnIndex);

        switch (sqlType) {
            case Types.OTHER:
                if (resultSet.getObject(columnIndex) instanceof UUID) {
                    value = resultSet.getObject(columnIndex);
                } else if ("uuid".equals(typeName)) {
                    String stringValue = resultSet.getString(columnIndex);
                    value = stringValue != null ? UuidProvider.fromString(stringValue) : null;
                } else {
                    value = resultSet.getObject(columnIndex);
                }
                break;

            default:
                value = resultSet.getObject(columnIndex);
                break;
        }

        return value;
    }

    @Override
    public Object getSqlObject(Object value) throws SQLException {
        if (value instanceof Date)
            return new Timestamp(((Date) value).getTime());
        if (value instanceof UUID)
            return new PostgresUUID((UUID) value);
        return value;
    }

    @Override
    public int getSqlType(Class<?> javaClass) {
        if (javaClass == Date.class)
            return Types.TIMESTAMP;
        return Types.OTHER;
    }
}
