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

public class HsqlSequenceSupport implements SequenceSupport {

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
        return "drop sequence " + (sequenceName != null ? sequenceName.toUpperCase() : null);
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
}