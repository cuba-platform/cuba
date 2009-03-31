/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.01.2009 17:35:20
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

import org.apache.openjpa.jdbc.sql.*;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.Table;
import com.haulmont.cuba.core.PersistenceProvider;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

public class DBDictionaryUtils
{
    private static final String DELETE_TS_COL = "DELETE_TS";

    public static SQLBuffer toTraditionalJoin(DBDictionary dbDictionary, Join join) {
        ForeignKey fk = join.getForeignKey();
        if (fk == null)
            return null;

        boolean inverse = join.isForeignKeyInversed();
        Column[] from = (inverse) ? fk.getPrimaryKeyColumns()
            : fk.getColumns();
        Column[] to = (inverse) ? fk.getColumns()
            : fk.getPrimaryKeyColumns();

        // do column joins
        SQLBuffer buf = new SQLBuffer(dbDictionary);
        int count = 0;
        for (int i = 0; i < from.length; i++, count++) {
            if (count > 0)
                buf.append(" AND ");
            buf.append(join.getAlias1()).append(".").append(from[i]);
            buf.append(" = ");
            buf.append(join.getAlias2()).append(".").append(to[i]);

            // KK: support deferred delete for collections
            if (inverse
                    && to[i].getTable().containsColumn(DELETE_TS_COL)
                    && PersistenceProvider.getEntityManager().isDeleteDeferred())
            {
                buf.append(" AND ");
                buf.append(join.getAlias2()).append(".").append(DELETE_TS_COL).append(" IS NULL");
            }
        }

        // do constant joins
        Column[] constCols = fk.getConstantColumns();
        for (int i = 0; i < constCols.length; i++, count++) {
            if (count > 0)
                buf.append(" AND ");
            if (inverse)
                buf.appendValue(fk.getConstant(constCols[i]), constCols[i]);
            else
                buf.append(join.getAlias1()).append(".").
                    append(constCols[i]);
            buf.append(" = ");

            if (inverse)
                buf.append(join.getAlias2()).append(".").
                    append(constCols[i]);
            else
                buf.appendValue(fk.getConstant(constCols[i]), constCols[i]);
        }

        Column[] constColsPK = fk.getConstantPrimaryKeyColumns();
        for (int i = 0; i < constColsPK.length; i++, count++) {
            if (count > 0)
                buf.append(" AND ");
            if (inverse)
                buf.append(join.getAlias1()).append(".").
                    append(constColsPK[i]);
            else
                buf.appendValue(fk.getPrimaryKeyConstant(constColsPK[i]),
                    constColsPK[i]);
            buf.append(" = ");

            if (inverse)
                buf.appendValue(fk.getPrimaryKeyConstant(constColsPK[i]),
                    constColsPK[i]);
            else
                buf.append(join.getAlias2()).append(".").
                    append(constColsPK[i]);
        }
        return buf;
    }

    public static SQLBuffer getWhere(DBDictionary dbDictionary, Select sel, boolean forUpdate) {
        Joins joins = sel.getJoins();
        if (sel.getJoinSyntax() == JoinSyntaxes.SYNTAX_SQL92
            || joins == null || joins.isEmpty())
        {
            SQLBuffer buf = sel.getWhere();
            if (!PersistenceProvider.getEntityManager().isDeleteDeferred())
                return buf;

            Map<Table, String> tables = new HashMap<Table, String>();
            Collection columns;
            if (buf != null) {
                columns = buf.getColumns();
            }
            else {
                columns = sel.getSelects();
            }

            if (columns != null) {
                for (Object item : columns) {
                    if (item instanceof Column) {
                        Column col = (Column) item;
                        for (String s : (Collection<String>) sel.getTableAliases()) {
                            int i = s.indexOf(' ');
                            String tableName = s.substring(0, i);
                            if (col.getTable().getName().equals(tableName)) {
                                if (col.getTable().containsColumn(DELETE_TS_COL))
                                    tables.put(col.getTable(), s.substring(i + 1));
                                break;
                            }
                        }
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            for (String alias : tables.values()) {
                if (sb.length() > 0)
                    sb.append(" AND ");
                sb.append(alias).append(".").append(DELETE_TS_COL).append(" IS NULL");
            }
            sel.where(sb.toString());
            return sel.getWhere();
        }

        SQLBuffer where = new SQLBuffer(dbDictionary);
        if (sel.getWhere() != null)
            where.append(sel.getWhere());
        if (joins != null)
            sel.append(where, joins);
        return where;
    }
}
