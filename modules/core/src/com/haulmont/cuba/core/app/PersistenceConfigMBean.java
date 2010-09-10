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
 * Management interface of the {@link PersistenceConfig} MBean.<br>
 */
public interface PersistenceConfigMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=PersistenceConfig";
    
    String printSoftDeleteTables();

    String updateDatabase();

    String findUpdateDatabaseScripts();

    String jpqlLoadList(String queryString);

    String jpqlExecuteUpdate(String queryString, boolean softDeletion);
}
