/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;

import java.text.SimpleDateFormat;

/**
 * @author krivopustov
 * @version $Id$
 */
public class MssqlSequenceSupport implements SequenceSupport {

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return String.format("drop table %1$s ^ " +
                "create table %1$s (ID bigint identity(%2$d,1), CREATE_TS datetime) ^ " +
                "insert into %1$s (CREATE_TS) values ({ts '%3$s'})",
                sequenceName.toUpperCase(), startWith, dateFormat.format(AppBeans.get(TimeSource.class).currentTimestamp()));
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "drop table " + (sequenceName != null ? sequenceName.toUpperCase() : null);
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return String.format("insert into %s (CREATE_TS) values ({ts '%s'}) ^ select ident_current('%s') as NEXT_VALUE",
                sequenceName.toUpperCase(), dateFormat.format(AppBeans.get(TimeSource.class).currentTimestamp()), sequenceName.toUpperCase());
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return String.format("select ident_current('%s') as CURR_VALUE", sequenceName.toUpperCase());
    }}
