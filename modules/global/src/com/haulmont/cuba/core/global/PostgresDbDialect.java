/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

/**
 * PostgreSQL dialect.
 *
 * @author krivopustov
 * @version $Id$
 */
public class PostgresDbDialect extends DbDialect implements SequenceSupport {

    @Override
    public String sequenceExistsSql(String sequenceName) {
        return "select relname from pg_class where relkind = 'S' and relname = '"
                + sequenceName.toLowerCase() + "'";
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName)
                + " increment by " + increment + " start with " + startValue + " minvalue 0";
    }

    @Override
    public String modifySequenceSql(String sequenceName, long startWith) {
        return "select setval('" + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName) + "', " + startWith + ")";
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "drop sequence " + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName);
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        return "select nextval('" + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName) + "')";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return "select last_value from " + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName);
    }

    @Override
    public String getDbmsType() {
        return DBMS_POSTGRES;
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
    public String getUniqueConstraintViolationPattern() {
        return "ERROR: duplicate key value violates unique constraint \"(.+)\"";
    }
}