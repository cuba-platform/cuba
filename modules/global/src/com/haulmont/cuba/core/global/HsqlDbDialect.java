/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

/**
 * HSQLDB dialect.
 *
 * @author krivopustov
 * @version $Id$
 */
public class HsqlDbDialect extends DbDialect implements SequenceSupport {

    @Override
    public String sequenceExistsSql(String sequenceName) {
        return "select top 1 SEQUENCE_NAME from INFORMATION_SCHEMA.SYSTEM_SEQUENCES where SEQUENCE_NAME = '"
                + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName
                + " as bigint start with " + startValue + " increment by " + increment;
    }

    @Override
    public String modifySequenceSql(String sequenceName, long startWith) {
        return "alter sequence " + sequenceName + " restart with " + startWith;
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "drop sequence " + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName);
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        return "select next value for " + sequenceName + " from dual";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return "select START_WITH from INFORMATION_SCHEMA.SYSTEM_SEQUENCES where SEQUENCE_NAME = '"
                + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String getDbmsType() {
        return DBMS_HSQL;
    }

    @Override
    public String getIdColumn() {
        return "ID";
    }

    @Override
    public String getDeleteTsColumn() {
        return "DELETE_TS";
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "Violation of unique index (.+): duplicate value\\(s\\) for column\\(s\\) (.+) in statement";
    }
}