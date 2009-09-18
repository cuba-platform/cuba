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
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * Other methods are intended to invoke from the JMX console.
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface PersistenceConfigMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=PersistenceConfig";
    
    void create();

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    PersistenceConfigAPI getAPI();

    String getDatasourceName();

    void initDbMetadata();

    String printSoftDeleteTables();

    String loadSystemProperties();
}
