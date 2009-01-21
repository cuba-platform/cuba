/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 07.11.2008 19:09:04
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

public class UuidStringValueHandler extends AbstractValueHandler
{
    private static final long serialVersionUID = -8302367450468711877L;

    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column col = new Column();
        col.setName(name);
        col.setJavaType(JavaSQLTypes.STRING);
        col.setSize(-1);
        return new Column[]{col};
    }

    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        return val.toString();
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        return val == null ? null : UUID.fromString((String) val);
    }
}
