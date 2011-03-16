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
 * Management interface of the {@link PersistenceManager} MBean.<br>
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

    String printSoftDeleteTables();

    String updateDatabase();

    String findUpdateDatabaseScripts();

    String jpqlLoadList(String queryString);

    String jpqlExecuteUpdate(String queryString, boolean softDeletion);

    String refreshStatistics(String entityName);

    String showStatistics(String entityName);

    String loadStatisticsCache();
}
