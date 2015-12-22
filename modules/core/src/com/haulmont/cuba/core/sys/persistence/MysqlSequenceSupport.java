/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.util.Preconditions;

/**
 * @author gorbunkov
 * @version $Id$
 */
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
        return "update SYS_SEQUENCE set CURR_VALUE = last_insert_id(CURR_VALUE + INCREMENT) where NAME = '" + sequenceName + "' ^ select last_insert_id()";
    }

    @Override
    public String getCurrentValueSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "select CURR_VALUE from SYS_SEQUENCE where NAME = '" + sequenceName.toLowerCase() + "'";
    }
}