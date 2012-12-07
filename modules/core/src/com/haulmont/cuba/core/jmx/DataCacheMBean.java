/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.jmx;

public interface DataCacheMBean {

    boolean isStoreCacheEnabled();

    boolean isQueryCacheEnabled();

    int getStoreCacheSize();

    int getStoreCacheMaxSize();

    int getQueryCacheSize();

    int getQueryCacheMaxSize();

    void dataCacheEvictAll();

    void queryCacheEvictAll();
}
