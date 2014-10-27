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
package com.haulmont.cuba.core.sys.persistence;

/**
 * Interface defining methods for managing database sequences.
 *
 * @author krivopustov
 * @version $Id$
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
