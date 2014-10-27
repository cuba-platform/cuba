/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

/**
 * @author krivopustov
 * @version $Id$
 */
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
}
