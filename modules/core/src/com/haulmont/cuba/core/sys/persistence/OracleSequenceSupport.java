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

public class OracleSequenceSupport implements SequenceSupport {

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
        return "drop sequence " + (sequenceName != null ? sequenceName.toUpperCase() : null);
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        return "select " + sequenceName + ".NEXTVAL from DUAL";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        return "select GET_SEQ_VAL('" + sequenceName.toUpperCase() + "') from DUAL";
    }
}