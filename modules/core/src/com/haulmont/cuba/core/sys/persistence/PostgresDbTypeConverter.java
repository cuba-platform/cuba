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

import com.haulmont.cuba.core.global.UuidProvider;

import java.sql.*;
import java.util.Date;
import java.util.UUID;

/**
 */
public class PostgresDbTypeConverter implements DbTypeConverter {

    @Override
    public Object getJavaObject(ResultSet resultSet, int columnIndex) {
        Object value;

        try {
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
        } catch (SQLException e) {
            throw new RuntimeException("Error converting database value", e);
        }
    }

    @Override
    public Object getSqlObject(Object value) {
        try {
            if (value instanceof Date)
                return new Timestamp(((Date) value).getTime());
            if (value instanceof UUID)
                return new PostgresUUID((UUID) value);
            return value;
        } catch (SQLException e) {
            throw new RuntimeException("Error converting application value", e);
        }
    }

    @Override
    public int getSqlType(Class<?> javaClass) {
        if (javaClass == Date.class)
            return Types.TIMESTAMP;
        return Types.OTHER;
    }
}
