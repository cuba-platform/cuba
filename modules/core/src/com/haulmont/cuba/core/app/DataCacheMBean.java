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

public interface DataCacheMBean {

    String OBJECT_NAME = "haulmont.cuba:service=DataCache";

    boolean isStoreCacheEnabled();

    boolean isQueryCacheEnabled();

    int getStoreCacheSize();

    int getStoreCacheMaxSize();

    int getQueryCacheSize();

    int getQueryCacheMaxSize();

    void dataCacheEvictAll();

    void queryCacheEvictAll();
}
