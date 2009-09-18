/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.08.2009 16:23:19
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link com.haulmont.cuba.core.app.DataCache} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface DataCacheMBean {

    String OBJECT_NAME = "haulmont.cuba:service=DataCache";

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    DataCacheAPI getAPI();

    boolean isStoreCacheEnabled();

    boolean isQueryCacheEnabled();

    int getStoreCacheSize();

    int getStoreCacheMaxSize();

    int getQueryCacheSize();

    int getQueryCacheMaxSize();

    void dataCacheEvictAll();

    void queryCacheEvictAll();
}
