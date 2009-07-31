/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.05.2009 10:20:46
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

public class PostgresDbDialect extends DbDialect implements SequenceSupport
{
    public String sequenceExistsSql(String sequenceName) {
        return "select relname from pg_class where relkind = 'S' and relname = '"
                + sequenceName.toLowerCase() + "'";
    }

    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName
                + " increment by " + increment + " start with " + startValue;
    }

    public String modifySequenceSql(String sequenceName, long startWith) {
        return "select setval('" + sequenceName + "', " + startWith + ")";
    }

    public String getNextValueSql(String sequenceName) {
        return "select nextval('" + sequenceName + "')";
    }

    public String getCurrentValueSql(String sequenceName) {
        return "select currval('" + sequenceName + "')";
    }

    @Override
    public String getIdColumn() {
        return "id";
    }

    @Override
    public String getDeleteTsColumn() {
        return "delete_ts";
    }

    @Override
    public String getUniqueConstraintViolationMarker() {
        return "ERROR: duplicate key value violates unique constraint";
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "ERROR: duplicate key value violates unique constraint \"(.+)\"";
    }
}
