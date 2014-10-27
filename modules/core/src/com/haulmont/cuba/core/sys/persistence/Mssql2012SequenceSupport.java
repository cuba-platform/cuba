/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.bali.util.Preconditions;

/**
 * @author krivopustov
 * @version $Id$
 */
@SuppressWarnings("UnusedDeclaration")
public class Mssql2012SequenceSupport implements SequenceSupport {

    @Override
    public String sequenceExistsSql(String sequenceName) {
        Preconditions.checkNotNullArgument(sequenceName, "sequenceName is null");
        return "select * from SYS.SEQUENCES where NAME = '" + sequenceName.toUpperCase() + "'";
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
        return "select CURRENT_VALUE from SYS.SEQUENCES where NAME = '" + sequenceName.toUpperCase() + "'";
    }
}
