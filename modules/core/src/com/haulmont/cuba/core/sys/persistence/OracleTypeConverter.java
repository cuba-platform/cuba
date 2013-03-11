/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class OracleTypeConverter implements DbTypeConverter {
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
        if (value instanceof Boolean) {
            return ((Boolean) value) ? "1" : "0";
        }
        if (value instanceof UUID)
            return value.toString();
        return value;
    }
}
