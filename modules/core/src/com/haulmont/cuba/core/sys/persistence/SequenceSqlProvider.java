/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:25:05
 * 
 * $Id$
 */
package com.haulmont.cuba.core.sys.persistence;

public interface SequenceSqlProvider
{
    String sequenceExistsSql(String sequenceName);

    String createSequenceSql(String sequenceName, long startValue, long increment);

    String modifySequenceSql(String sequenceName, long startWith);

    String getNextValueSql(String sequenceName);

    String getCurrentValueSql(String sequenceName);
}
