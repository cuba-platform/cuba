/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import java.text.SimpleDateFormat;

/**
 * Microsoft SQLServer dialect.
 *
 * @author krivopustov
 * @version $Id$
 */
public class MssqlDbDialect extends DbDialect implements SequenceSupport {

    @Override
    public String getDbmsType() {
        return DBMS_MSSQL;
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
        return "with unique index \'(.+)\'";
    }

    @Override
    public String sequenceExistsSql(String sequenceName) {
        return String.format("select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = '%s'",
                sequenceName.toUpperCase());
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return String.format("create table %s (ID bigint identity(%d,%d), CREATE_TS datetime)",
                sequenceName.toUpperCase(), startValue, increment);
    }

    @Override
    public String modifySequenceSql(String sequenceName, long startWith) {
        return String.format("drop table %s ^ create table %s (ID bigint identity(%d,1), CREATE_TS datetime)",
                sequenceName.toUpperCase(), sequenceName.toUpperCase(), startWith);
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "drop table " + (sequenceName != null ? sequenceName.toLowerCase() : sequenceName);
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return String.format("insert into %s (CREATE_TS) values ({ts '%s'}) ^ select ident_current('%s') as NEXT_VALUE",
                sequenceName.toUpperCase(), dateFormat.format(TimeProvider.currentTimestamp()), sequenceName.toUpperCase());
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return String.format("select ident_current('%s') as CURR_VALUE", sequenceName.toUpperCase());
    }
}