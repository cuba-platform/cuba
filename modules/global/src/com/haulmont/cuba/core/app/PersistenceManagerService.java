/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

/**
 * Exposes some of {@code PersistenceManagerAPI} methods and other DBMS-related information to the client tier.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface PersistenceManagerService {

    String NAME = "cuba_PersistenceManagerService";

    boolean useLazyCollection(String entityName);

    boolean useLookupScreen(String entityName);

    int getFetchUI(String entityName);

    int getMaxFetchUI(String entityName);

    /**
     * @return current DBMS type set by {@code cuba.dbmsType} app property on Middleware
     */
    String getDbmsType();

    /**
     * @return current DBMS version set by {@code cuba.dbmsVersion} app property on Middleware
     */
    String getDbmsVersion();

    /**
     * @return  regexp to extract a unique constraint name from an exception message.
     * <p>See {@code DbmsFeatures.getUniqueConstraintViolationPattern()}
     */
    String getUniqueConstraintViolationPattern();

    /**
     * @return default sort order of null values used by the current DBMS
     */
    boolean isNullsLastSorting();
}
