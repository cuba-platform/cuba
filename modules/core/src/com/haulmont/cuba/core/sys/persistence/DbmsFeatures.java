/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.persistence;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Interface defining methods of getting some DBMS-specific values. It must be implemented for each supported DBMS type
 * and version.
 *
 * @see com.haulmont.cuba.core.sys.persistence.DbmsSpecificFactory
 *
 * @author krivopustov
 * @version $Id$
 */
public interface DbmsFeatures {

    /**
     * @return JPA implementation properties to set in persistence.xml
     */
    Map<String, String> getJpaParameters();

    /**
     * @return  primary key column name
     */
    String getIdColumn();

    /**
     * @return  soft deletion column name
     */
    String getDeleteTsColumn();

    /**
     * @return name of data type storing date and time
     */
    String getTimeStampType();

    /**
     * @return name of class representing UUID in JDBC driver, or null if no special class required
     */
    @Nullable
    String getUuidTypeClassName();

    /**
     * @return statement to issue for setting the current transaction timeout, or null if not required.
     * <p>The statement text should contain %d placeholder that will be replaced by timeout value in milliseconds.
     */
    @Nullable
    String getTransactionTimeoutStatement();

    /**
     * @return  regexp to extract a unique constraint name from an exception message
     */
    String getUniqueConstraintViolationPattern();

    /**
     * @return default sort order of null values
     */
    boolean isNullsLastSorting();
}
