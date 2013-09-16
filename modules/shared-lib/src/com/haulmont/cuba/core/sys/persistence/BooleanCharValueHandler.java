/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author degtyarjov
 * @version $Id$
 */
public class BooleanCharValueHandler extends AbstractValueHandler {

    private static final long serialVersionUID = -8302367450468711877L;

    public static final String TRUE_STRING = "1";
    public static final String FALSE_STRING = "0";

    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column col = new Column();
        col.setName(name);
        col.setJavaType(JavaSQLTypes.STRING);
        col.setType(Types.CHAR);
        col.setSize(1);
        return new Column[]{col};
    }

    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        return val == null ? null : (Boolean) val ? TRUE_STRING : FALSE_STRING;
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        return val == null ? null : TRUE_STRING.equalsIgnoreCase(val.toString());
    }
}
