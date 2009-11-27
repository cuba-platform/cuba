/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 17:59:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link com.haulmont.cuba.core.app.ConfigStorage} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * Other methods are intended to invoke from the JMX console.
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface ConfigStorageMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ConfigStorage";

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    ConfigStorageAPI getAPI();

    String printProperties();

    String printProperties(String prefix);

    String getProperty(String name);

    String setProperty(String name, String value);

    String removeProperty(String name);

    void clearCache();

    String loadSystemProperties();
}
