/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Devyatkin
 * Created: 10.03.11 12:23
 *
 * $Id$
 */

package com.haulmont.cuba.core.sys.mybatis;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.UUID;

public class UUIDTypeHandler implements TypeHandler {

    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter, Types.OTHER);
    }

    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        String val = rs.getString(columnName);
        if (val != null) return UUID.fromString(val);
        else return null;
    }

    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String val = cs.getString(columnIndex);
        if (val != null) return UUID.fromString(val);
        else return null;
    }
}
