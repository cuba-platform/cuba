/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;
import org.apache.openjpa.jdbc.kernel.JDBCStore;

import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UuidStringValueHandler extends AbstractValueHandler {

    private static final long serialVersionUID = -8302367450468711877L;

    public boolean compact = false;

    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column col = new Column();
        col.setName(name);
        col.setJavaType(JavaSQLTypes.STRING);
        col.setSize(-1);
        return new Column[]{col};
    }

    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        if (val == null)
            return null;
        else {
            return compact ? val.toString().replace("-", "") : val.toString();
        }
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        if (val == null)
            return null;
        else {
            String str = (String) val;
            if (compact) {
                StringBuilder sb = new StringBuilder((String) val);
                sb.insert(8, '-');
                sb.insert(13, '-');
                sb.insert(18, '-');
                sb.insert(23, '-');
                str = sb.toString();
            }
            return UuidHelper.fromString(str);
        }
    }
}
