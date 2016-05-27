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

import com.haulmont.bali.util.Preconditions;

public class MysqlSequenceSupport implements SequenceSupport {
    @Override
    public String sequenceExistsSql(String sequenceName) {
        return "select NAME from SYS_SEQUENCE where NAME = '" + sequenceName + "'";
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        return "insert into SYS_SEQUENCE (NAME, CURR_VALUE, INCREMENT) values ('" + sequenceName + "', " + startValue
                + ", " + increment + ")";
    }

    @Override
    public String modifySequenceSql(String sequenceName, long startWith) {
        return "update SYS_SEQUENCE set CURR_VALUE = " + startWith + " where " +
                "NAME = '" + sequenceName + "'";
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        return "delete from SYS_SEQUENCE where name = '" + sequenceName + "'";
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "update SYS_SEQUENCE set CURR_VALUE = last_insert_id(CURR_VALUE + INCREMENT) where NAME = '" + sequenceName + "' ^ select last_insert_id() - INCREMENT from SYS_SEQUENCE where NAME = '" + sequenceName +"'";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "select CURR_VALUE from SYS_SEQUENCE where NAME = '" + sequenceName.toLowerCase() + "'";
    }
}