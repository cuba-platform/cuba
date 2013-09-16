/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.app.PersistenceManagerAPI;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.openjpa.jdbc.identifier.DBIdentifier;
import org.apache.openjpa.jdbc.schema.Column;
import org.apache.openjpa.jdbc.schema.ForeignKey;
import org.apache.openjpa.jdbc.sql.*;

import java.util.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DBDictionaryUtils {

    public static SQLBuffer toTraditionalJoin(DBDictionary dbDictionary, Join join) {
        Persistence persistence = AppBeans.get(Persistence.NAME);
        PersistenceManagerAPI persistenceManager = AppBeans.get(PersistenceManagerAPI.NAME);

        String deleteTsCol = persistence.getDbDialect().getDeleteTsColumn();

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
            if ((inverse || persistenceManager.isManyToManyLinkTable(from[i].getTable().getIdentifier().getName()))
                    && to[i].getTable().containsColumn(DBIdentifier.newColumn(deleteTsCol), null)
                    && persistence.getEntityManagerContext().isSoftDeletion())
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

    public static SQLBuffer getWhere(DBDictionary dbDictionary, Select sel, boolean forUpdate, boolean useSchema) {
        Persistence persistence = AppBeans.get(Persistence.NAME);
        PersistenceManagerAPI persistenceManager = AppBeans.get(PersistenceManagerAPI.NAME);

        final String deleteTsCol = persistence.getDbDialect().getDeleteTsColumn();
        final String idCol = persistence.getDbDialect().getIdColumn();

        Joins joins = sel.getJoins();
        if (sel.getJoinSyntax() == JoinSyntaxes.SYNTAX_SQL92
            || joins == null || joins.isEmpty())
        {
            SQLBuffer buf = sel.getWhere();
            if (!persistence.getEntityManagerContext().isSoftDeletion())
                return buf;

            Set<String> aliases = new HashSet<String>();

            for (String tableAlias : (Collection<String>) sel.getTableAliases()) {
                int i = tableAlias.indexOf(' ');
                String alias = tableAlias.substring(i + 1);

                String tableName = tableAlias.substring(0, i);
                if (useSchema) {
                    tableName = tableName.substring(tableName.indexOf('.') + 1);
                }
                boolean add = false;
                if (buf != null && buf.getColumns() != null) {
                    String whereSql = buf.getSQL();
                    for (Column col : (Collection<Column>) buf.getColumns()) {
                        if (col != null && col.getTableIdentifier().getName().equals(tableName)
                                && !col.getIdentifier().getName().equals(idCol)
                                && !whereSql.contains(alias + "." + deleteTsCol + " IS NULL")
                                && !aliasForOuterJoin(alias, (SelectImpl) sel))
                        {
                            add = true;
                            break;
                        }
                    }
                }
                if (add && persistenceManager.isSoftDeleteFor(tableName)) {
                    aliases.add(alias);
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

        } else {
            SQLBuffer where = new SQLBuffer(dbDictionary);
            if (sel.getWhere() != null)
                where.append(sel.getWhere());
            if (joins != null)
                sel.append(where, joins);
            if (sel instanceof SelectImpl
                    && persistence.getEntityManagerContext().isSoftDeletion())
            {
                StringBuilder sb = new StringBuilder();
                Map tables = ((SelectImpl) sel).getTables();
                for (Object table : tables.values()) {
                    int p = ((String) table).indexOf(' ');
                    if (p > 0) {
                        String t = ((String) table).substring(0, p);
                        int dot = t.indexOf('.');
                        if (dot > 0)
                            t = t.substring(dot + 1);
                        if (persistenceManager.isSoftDeleteFor(t)) {
                            String alias = ((String) table).substring(p + 1);
                            if (sb.length() > 0)
                                sb.append(" AND ");
                            sb.append(alias).append(".").append(deleteTsCol).append(" IS NULL");
                        }
                    }
                }
                if (!where.isEmpty() && sb.length() > 0)
                    where.append(" AND ");
                where.append(sb.toString());
            }
            return where;
        }
    }

    private static boolean aliasForOuterJoin(String alias, SelectImpl sel) {
        Iterator it = sel.getJoinIterator();
        while (it.hasNext()) {
            Join join = (Join) it.next();
            if (alias.equals(join.getAlias2()) && join.getType() == Join.TYPE_OUTER)
                return true;
        }
        return false;
    }
}
