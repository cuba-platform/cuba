/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.meta.JavaTypes;

import java.sql.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class CubaPostgresDictionary extends PostgresDictionary {
    @Override
    public SQLBuffer toTraditionalJoin(Join join) {
        return DBDictionaryUtils.toTraditionalJoin(this, join);
    }

    @Override
    protected SQLBuffer getWhere(Select sel, boolean forUpdate) {
        return DBDictionaryUtils.getWhere(this, sel, forUpdate, true);
    }

    @Override
    public void setUnknown(PreparedStatement stmnt, int idx, Object val, Column col) throws SQLException {
        if (val instanceof PostgresUUID) {
            stmnt.setObject(idx, val);
        } else {
            super.setUnknown(stmnt, idx, val, col);
        }
    }

    @Override
    public void setTyped(PreparedStatement stmnt, int idx, Object val, Column col, int type, JDBCStore store) throws SQLException {
        if ((type == JavaTypes.STRING) && (val instanceof PostgresUUID)) {
            stmnt.setObject(idx, val);
        } else {
            super.setTyped(stmnt, idx, val, col, type, store);
        }
    }
}