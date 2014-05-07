/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

/*
 * Author: Konstantin Krivopustov
 * Created: 15.05.2009 22:25:05
 * 
 * $Id$
 */
package com.haulmont.cuba.core.global;

/**
 * Interface implemented by {@link com.haulmont.cuba.core.global.DbDialect} implementation
 *  if the underlaying database supports sequences 
 */
public interface SequenceSupport {

    String SQL_DELIMITER = "^";

    String sequenceExistsSql(String sequenceName);

    String createSequenceSql(String sequenceName, long startValue, long increment);

    String modifySequenceSql(String sequenceName, long startWith);

    String deleteSequenceSql(String sequenceName);

    String getNextValueSql(String sequenceName);

    String getCurrentValueSql(String sequenceName);
}
