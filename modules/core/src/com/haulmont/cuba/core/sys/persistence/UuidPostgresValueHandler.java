/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.05.2009 18:29:44
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.jdbc.kernel.JDBCStore;

import java.util.UUID;
import java.sql.SQLException;
import java.sql.Types;

public class UuidPostgresValueHandler extends AbstractValueHandler
{
    private static final long serialVersionUID = -3661314510767686247L;

    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column col = new Column();
        col.setName(name);
        col.setType(Types.OTHER);
        col.setJavaType(JavaSQLTypes.STRING);
        col.setSize(-1);
        return new Column[]{col};
    }

    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        try {
            PostgresUUID pgUuid = new PostgresUUID(((UUID) val));
            return pgUuid;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        if (val == null)
            return null;

        UUID uuid = UUID.fromString(((String) val));
        return uuid;
    }
}
