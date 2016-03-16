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

package com.haulmont.cuba.core.sys.mybatis;

import com.haulmont.cuba.core.global.UuidProvider;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 */
public class UUIDTypeHandler implements TypeHandler {

    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter, Types.OTHER);
    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        String val = rs.getString(columnName);
        if (val != null) {
            return UuidProvider.fromString(val);
        } else {
            return null;
        }
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        String val = rs.getString(columnIndex);
        if (val != null) {
            return UuidProvider.fromString(val);
        } else {
            return null;
        }
    }

    @Override
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String val = cs.getString(columnIndex);
        if (val != null) {
            return UuidProvider.fromString(val);
        } else {
            return null;
        }
    }
}