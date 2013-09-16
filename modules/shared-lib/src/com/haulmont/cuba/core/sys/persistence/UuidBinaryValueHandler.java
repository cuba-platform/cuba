/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.kernel.JDBCStore;
import org.apache.openjpa.jdbc.meta.ValueMapping;
import org.apache.openjpa.jdbc.meta.JavaSQLTypes;
import org.apache.openjpa.jdbc.meta.strats.AbstractValueHandler;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ColumnIO;

import java.sql.Types;
import java.util.UUID;

public class UuidBinaryValueHandler extends AbstractValueHandler
{
    private static final long serialVersionUID = 7373689934203363322L;

    public Column[] map(ValueMapping vm, String name, ColumnIO io, boolean adapt) {
        Column col = new Column();
        col.setName(name);
        col.setType(Types.BINARY);
        col.setJavaType(JavaSQLTypes.ARRAY);
        col.setSize(16);
        return new Column[]{col};
    }

    public Object toDataStoreValue(ValueMapping vm, Object val, JDBCStore store) {
        byte[] bytes = new byte[16];
        long l = ((UUID) val).getMostSignificantBits();
        for (int i = 0; i <= 7; i++) {
            byte b = (byte) (Long.rotateRight(l, i * 8) & 255);
            bytes[7 - i] = b;
        }
        l = ((UUID) val).getLeastSignificantBits();
        for (int i = 0; i <= 7; i++) {
            byte b = (byte) (Long.rotateRight(l, i * 8) & 255);
            bytes[15 - i] = b;
        }
        return bytes;
    }

    public Object toObjectValue(ValueMapping vm, Object val) {
        if (val == null)
            return null;

        byte[] bytes = (byte[]) val;

        long mostBits = 0;
        for (int i = 0; i <= 7; i++) {
            long b = Long.rotateLeft(((long) bytes[i]) & 255, 8 * (7 - i));
            mostBits = mostBits + b;
        }

        long leastBits = 0;
        for (int i = 0; i <= 7; i++) {
            long b = Long.rotateLeft(((long) bytes[8 + i]) & 255, 8 * (7 - i));
            leastBits = leastBits + b;
        }
        return new UUID(mostBits, leastBits);
    }
}
