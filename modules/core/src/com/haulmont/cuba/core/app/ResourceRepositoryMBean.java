/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 10.12.2008 15:19:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link ResourceRepository} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * Other methods are intended to invoke from the JMX console.
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface ResourceRepositoryMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ResourceRepository";

    void create();

    void start();

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    ResourceRepositoryAPI getAPI();

    String getContent();

    void evict(String name);

    void evictAll();

    String getResAsString(String name);
}
