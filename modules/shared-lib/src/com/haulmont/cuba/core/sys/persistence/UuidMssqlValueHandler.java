/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;

import java.sql.Types;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class UuidMssqlValueHandler extends AbstractValueHandler {

    @Override
    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column col = new Column();
        col.setName(name);
        col.setType(Types.BINARY);
        col.setJavaType(JavaSQLTypes.STRING);
        col.setSize(16);
        return new Column[]{col};
    }

    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        return val == null ? null : val.toString();
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        return val == null ? null : UUID.fromString((String) val);
    }
}
