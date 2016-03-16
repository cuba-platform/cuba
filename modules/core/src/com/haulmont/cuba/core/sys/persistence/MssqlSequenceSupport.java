/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;

import java.text.SimpleDateFormat;

/**
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
        return String.format("insert into %s(CREATE_TS) output inserted.id values(CURRENT_TIMESTAMP);", sequenceName.toUpperCase());
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return String.format("select ident_current('%s') as CURR_VALUE", sequenceName.toUpperCase());
    }
}
