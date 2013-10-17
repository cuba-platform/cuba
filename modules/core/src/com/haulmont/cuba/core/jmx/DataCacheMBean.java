/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

/**
 * @author krivopustov
 * @version $Id$
 */
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