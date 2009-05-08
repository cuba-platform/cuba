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
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.util.UserException;
import org.apache.openjpa.meta.JavaTypes;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.Reader;
import java.io.InputStream;

public class CubaPostgresDictionary extends PostgresDictionary
{
    public SQLBuffer toTraditionalJoin(Join join) {
        return DBDictionaryUtils.toTraditionalJoin(this, join);
    }

    protected SQLBuffer getWhere(Select sel, boolean forUpdate) {
        return DBDictionaryUtils.getWhere(this, sel, forUpdate);
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
}
