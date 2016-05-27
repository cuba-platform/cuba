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

@SuppressWarnings("UnusedDeclaration")
public class Mssql2012SequenceSupport implements SequenceSupport {

    @Override
    public String sequenceExistsSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "select NAME from SYS.SEQUENCES where NAME = '" + sequenceName.toUpperCase() + "'";
    }

    @Override
    public String createSequenceSql(String sequenceName, long startValue, long increment) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "create sequence " + sequenceName.toUpperCase()
                + " as bigint increment by " + increment + " start with " + startValue + " minvalue 0";
    }

    @Override
    public String modifySequenceSql(String sequenceName, long startWith) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "alter sequence " + sequenceName.toUpperCase()
                + " restart with " + startWith;
    }

    @Override
    public String deleteSequenceSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "drop sequence " + sequenceName.toUpperCase();
    }

    @Override
    public String getNextValueSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "select next value for " + sequenceName.toUpperCase();
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "select cast(CURRENT_VALUE as bigint) from SYS.SEQUENCES where NAME = '" + sequenceName.toUpperCase() + "'";
    }
}