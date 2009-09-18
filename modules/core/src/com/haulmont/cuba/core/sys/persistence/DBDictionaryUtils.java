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
import com.haulmont.cuba.core.PersistenceProvider;
import com.haulmont.cuba.core.Locator;
import com.haulmont.cuba.core.app.PersistenceConfigAPI;
import com.haulmont.cuba.core.app.PersistenceConfigMBean;

import java.util.*;

public class DBDictionaryUtils
{
    private static PersistenceConfigAPI persistenceConfig;

    public static SQLBuffer toTraditionalJoin(DBDictionary dbDictionary, Join join) {
        final String deleteTsCol = PersistenceProvider.getDbDialect().getDeleteTsColumn();

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
                    && PersistenceProvider.getEntityManager().isSoftDeletion())
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
        final String deleteTsCol = PersistenceProvider.getDbDialect().getDeleteTsColumn();
        final String idCol = PersistenceProvider.getDbDialect().getIdColumn();

        Joins joins = sel.getJoins();
        if (sel.getJoinSyntax() == JoinSyntaxes.SYNTAX_SQL92
            || joins == null || joins.isEmpty())
        {
            SQLBuffer buf = sel.getWhere();
            if (!PersistenceProvider.getEntityManager().isSoftDeletion())
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
                        if (col != null && col.getTableName().equals(tableName)
                                && !col.getName().equals(idCol)
                                && !whereSql.contains(alias + "." + deleteTsCol + " IS NULL"))
                        {
                            add = true;
                            break;
                        }
                    }
                }
                if (add && getPersistenceConfigAPI().isDeleteDeferredFor(tableName)) {
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
            if (sel instanceof SelectImpl && PersistenceProvider.getEntityManager().isSoftDeletion()) {
                StringBuilder sb = new StringBuilder();
                Map tables = ((SelectImpl) sel).getTables();
                for (Object table : tables.values()) {
                    int p = ((String) table).indexOf(' ');
                    if (p > 0) {
                        String t = ((String) table).substring(0, p);
                        int dot = t.indexOf('.');
                        if (dot > 0)
                            t = t.substring(dot + 1);
                        if (getPersistenceConfigAPI().isDeleteDeferredFor(t)) {
                            String alias = ((String) table).substring(p + 1);
                            if (sb.length() > 0)
                                sb.append(" AND ");
                            sb.append(alias).append(".").append(deleteTsCol).append(" IS NULL");
                        }
                    }
                }
                if (!where.isEmpty())
                    where.append(" AND ");
                where.append(sb.toString());
            }
            return where;
        }
    }

    private static PersistenceConfigAPI getPersistenceConfigAPI() {
        if (persistenceConfig == null) {
            PersistenceConfigMBean mbean = Locator.lookupMBean(PersistenceConfigMBean.class, PersistenceConfigMBean.OBJECT_NAME);
            persistenceConfig = mbean.getAPI();
        }
        return persistenceConfig;
    }
}
