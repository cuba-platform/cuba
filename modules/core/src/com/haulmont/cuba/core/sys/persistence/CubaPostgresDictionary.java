/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.05.2009 15:58:27
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.meta.JavaTypes;

import java.sql.*;

public class CubaPostgresDictionary extends PostgresDictionary implements SequenceSqlProvider
{
    public SQLBuffer toTraditionalJoin(Join join) {
        return DBDictionaryUtils.toTraditionalJoin(this, join, true);
    }

    protected SQLBuffer getWhere(Select sel, boolean forUpdate) {
        return DBDictionaryUtils.getWhere(this, sel, forUpdate, true, true);
    }

    public void setUnknown(PreparedStatement stmnt, int idx, Object val, Column col) throws SQLException {
        if (val instanceof PostgresUUID) {
            stmnt.setObject(idx, val);
        } else {
            super.setUnknown(stmnt, idx, val, col);
        }
    }

    public void setTyped(PreparedStatement stmnt, int idx, Object val, Column col, int type, JDBCStore store) throws SQLException {
        if ((type == JavaTypes.STRING) && (val instanceof PostgresUUID)) {
            stmnt.setObject(idx, val);
        } else {
            super.setTyped(stmnt, idx, val, col, type, store);
        }
    }

    public String sequenceExistsSql(String sequenceName) {
        return "select relname from pg_class where relkind = 'S' and relname = '"
                + sequenceName.toLowerCase() + "'";
    }

    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName
                + " increment by " + increment + " start with " + startValue;
    }

    public String modifySequenceSql(String sequenceName, long startWith) {
        return "select setval('" + sequenceName + "', " + startWith + ")";
    }

    public String getNextValueSql(String sequenceName) {
        return "select nextval('" + sequenceName + "')";
    }

    public String getCurrentValueSql(String sequenceName) {
        return "select currval('" + sequenceName + "')";
    }
}
