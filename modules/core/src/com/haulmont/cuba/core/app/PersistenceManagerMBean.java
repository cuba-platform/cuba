/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 12:54:22
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of {@link PersistenceManager}.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface PersistenceManagerMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=PersistenceManager";
    
    int getDefaultLookupScreenThreshold();
    void setDefaultLookupScreenThreshold(int value);

    int getDefaultLazyCollectionThreshold();
    void setDefaultLazyCollectionThreshold(int value);

    int getDefaultFetchUI();
    void setDefaultFetchUI(int value);

    int getDefaultMaxFetchUI();
    void setDefaultMaxFetchUI(int value);

    /**
     * Show list of tables supporting soft deletion.
     * @return  operation result
     */
    String printSoftDeleteTables();

    /**
     * Start the database update.
     * @return  operation result
     */
    String updateDatabase();

    /**
     * Show database update scripts that will be executed on next update.
     * @return  operation result
     */
    String findUpdateDatabaseScripts();

    /**
     * Execute a JPQL query.
     * <p>The query may contain security-related parameters specified in
     * {@link com.haulmont.cuba.core.PersistenceSecurity}. This is a way to test some JPQL code in the real server
     * environment</p>
     * @param queryString   JPQL query string
     * @return              list of loaded entities as string
     */
    String jpqlLoadList(String queryString);

    /**
     * Execute a JPQL update statement.
     * @param queryString   JPQL update statement
     * @param softDeletion  soft deletion sign
     * @return              number of entity instances affected by update
     */
    String jpqlExecuteUpdate(String queryString, boolean softDeletion);

    /**
     * Calculate and refresh statistics for the specified entity. This method updates statistics in the database table
     * and in cache.
     * @param entityName    entity name
     * @return              operation result
     */
    String refreshStatistics(String entityName);

    /**
     * Show current statistics for the specified entity.
     * @param entityName    entity name
     * @return              operation result
     */
    String showStatistics(String entityName);

    /**
     * Flush statistics cache. It will be loaded on a next request.
     * @return  operation result
     */
    String flushStatisticsCache();
}
