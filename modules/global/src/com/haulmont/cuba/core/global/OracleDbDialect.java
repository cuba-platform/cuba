/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

/**
 * Oracle dialect.
 *
 * @author degtyarjov
 * @version $Id$
 */
public class OracleDbDialect extends DbDialect implements SequenceSupport {

    public String sequenceExistsSql(String sequenceName) {
        return "select SEQUENCE_NAME from USER_SEQUENCES where SEQUENCE_NAME = '" + sequenceName.toUpperCase() + "'";
    }

    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName + " increment by " + increment + " start with " + startValue + " nocache";
    }

    public String modifySequenceSql(String sequenceName, long newVal) {
        return "{call SET_SEQ_VAL('" + sequenceName + "', " + newVal + ")}";
    }

    public String getNextValueSql(String sequenceName) {
        return "select " + sequenceName + ".NEXTVAL from DUAL";
    }

    public String getCurrentValueSql(String sequenceName) {
        return "select LAST_NUMBER from USER_SEQUENCES where SEQUENCE_NAME = '" + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String getName() {
        return "oracle";
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

    @Override
    public String getScriptSeparator() {
        return "^";
    }
}
