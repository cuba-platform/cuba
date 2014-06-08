/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

/**
 * Oracle dialect.
 *
 * @author degtyarjov
 * @version $Id$
 */
public class OracleDbDialect extends DbDialect implements SequenceSupport {

    @Override
    public String sequenceExistsSql(String sequenceName) {
        return "select SEQUENCE_NAME from USER_SEQUENCES where SEQUENCE_NAME = '" + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "create sequence " + sequenceName + " increment by " + increment + " start with " + startValue + " nocache minvalue 0";
    }

    @Override
    public String modifySequenceSql(String sequenceName, long newVal) {
        return "{call SET_SEQ_VAL('" + sequenceName + "', " + newVal + ")}";
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "drop sequence " + (sequenceName != null ? sequenceName.toUpperCase() : sequenceName);
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        return "select " + sequenceName + ".NEXTVAL from DUAL";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return "select GET_SEQ_VAL('" + sequenceName.toUpperCase() + "') from DUAL";
    }

    @Override
    public String getDbmsType() {
        return DBMS_ORACLE;
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
        return "unique constraint \\((.+)\\) violated";
    }
}