/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

/**
 * HSQLDB dialect.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class HsqlDbDialect extends DbDialect implements SequenceSupport
{
    public String sequenceExistsSql(String sequenceName) {
        return "select top 1 SEQUENCE_NAME from INFORMATION_SCHEMA.SYSTEM_SEQUENCES where SEQUENCE_NAME = '"
                + sequenceName.toUpperCase() + "'";
    }

    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName
                + " as bigint start with " + startValue + " increment by " + increment;
    }

    public String modifySequenceSql(String sequenceName, long startWith) {
        return "alter sequence " + sequenceName + " restart with " + startWith;
    }

    public String getNextValueSql(String sequenceName) {
        return "select next value for " + sequenceName + " from dual";
    }

    public String getCurrentValueSql(String sequenceName) {
        return "select START_WITH from INFORMATION_SCHEMA.SYSTEM_SEQUENCES where SEQUENCE_NAME = '"
                + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String getName() {
        return "sql";
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
    public String getUniqueConstraintViolationMarker() {
        return "Violation of unique index";
    }

    @Override
    public String getUniqueConstraintViolationPattern() {
        return "Violation of unique index (.+): duplicate value\\(s\\) for column\\(s\\) (.+) in statement";
    }

    @Override
    public String getScriptSeparator() {
        return ";";
    }
}
