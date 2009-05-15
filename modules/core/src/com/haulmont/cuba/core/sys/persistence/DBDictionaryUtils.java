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

import java.util.*;

public class DBDictionaryUtils
{
    public static SQLBuffer toTraditionalJoin(DBDictionary dbDictionary, Join join, boolean lowerCase) {
        String deleteTsCol = getDeleteTsCol(lowerCase);

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
                    && to[i].getTable().containsColumn(deleteTsCol)
                    && PersistenceProvider.getEntityManager().isDeleteDeferred())
            {
                buf.append(" AND ");
                buf.append(join.getAlias2()).append(".").append(deleteTsCol).append(" IS NULL");
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

    public static SQLBuffer getWhere(DBDictionary dbDictionary, Select sel, boolean forUpdate,
                                     boolean lowerCase, boolean useSchema) {
        String deleteTsCol = getDeleteTsCol(lowerCase);

        Joins joins = sel.getJoins();
        if (sel.getJoinSyntax() == JoinSyntaxes.SYNTAX_SQL92
            || joins == null || joins.isEmpty())
        {
            SQLBuffer buf = sel.getWhere();
            if (!PersistenceProvider.getEntityManager().isDeleteDeferred())
                return buf;

            Set<String> aliases = new HashSet<String>();
            Collection columns = null;
            if (buf != null) {
                columns = buf.getColumns();
            }
            if (columns == null) {
                columns = sel.getSelects();
            }

            if (columns != null) {
                for (Object item : columns) {
                    if (item instanceof Column) {
                        Column col = (Column) item;
                        for (String s : (Collection<String>) sel.getTableAliases()) {
                            int i = s.indexOf(' ');
                            String tableName = s.substring(0, i);
                            String t = useSchema ? col.getTable().getFullName() : col.getTable().getName();
                            if (t.equals(tableName)) {
                                if (col.getTable().containsColumn(deleteTsCol))
                                    aliases.add(s.substring(i + 1));
                                break;
                            }
                        }
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            for (String alias : aliases) {
                if (sb.length() > 0)
                    sb.append(" AND ");
                sb.append(alias).append(".").append(deleteTsCol).append(" IS NULL");
            }
            sel.where(sb.toString());
            return sel.getWhere();
        }

        SQLBuffer where = new SQLBuffer(dbDictionary);
        if (sel.getWhere() != null)
            where.append(sel.getWhere());
        if (joins != null)
            sel.append(where, joins);
        if (sel instanceof SelectImpl) {
            StringBuilder sb = new StringBuilder();
            Map tables = ((SelectImpl) sel).getTables();
            for (Object table : tables.values()) {
                int p = ((String) table).indexOf(' ');
                if (p > 0) {
                    String alias = ((String) table).substring(p + 1);
                    if (sb.length() > 0)
                        sb.append(" AND ");
                    sb.append(alias).append(".").append(deleteTsCol).append(" IS NULL");
                }
            }
            if (!where.isEmpty())
                where.append(" AND ");
            where.append(sb.toString());
        }
        return where;
    }

    private static String getDeleteTsCol(boolean lowerCase) {
        return lowerCase ? "delete_ts" : "DELETE_TS";
    }
}
