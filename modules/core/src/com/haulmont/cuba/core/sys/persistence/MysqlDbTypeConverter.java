/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author gorbunkov
 * @version $Id$
 */
public class MysqlDbTypeConverter implements DbTypeConverter {

    @Override
    public Object getJavaObject(ResultSet resultSet, int columnIndex) {
        Object value;

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();

            if ((columnIndex > metaData.getColumnCount()) || (columnIndex <= 0))
                throw new IndexOutOfBoundsException("Column index out of bound");

            value = resultSet.getObject(columnIndex);

            return value;
        } catch (SQLException e) {
            throw new RuntimeException("Error converting database value", e);
        }
    }

    @Override
    public Object getSqlObject(Object value) {
        if (value instanceof Date)
            return new Timestamp(((Date) value).getTime());
        if (value instanceof UUID)
            return value.toString().replace("-", "");
        return value;
    }

    @Override
    public int getSqlType(Class<?> javaClass) {
        if (javaClass == Date.class)
            return Types.TIMESTAMP;
        else if (javaClass == UUID.class)
            return Types.VARCHAR;
        else if (javaClass == Boolean.class)
            return Types.BIT;
        return Types.OTHER;
    }
}
