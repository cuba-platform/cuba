/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

import java.util.Collection;

/**
 * Facade to OpenJPA data cache functionality.
 */
public interface DataCacheAPI {

    String NAME = "cuba_DataCache";

    boolean isStoreCacheEnabled();

    boolean isQueryCacheEnabled();

    void dataCacheEvict(Class cls, Object id);

    void dataCacheEvictAll(Class cls, Collection ids);

    void dataCacheEvictAll();

    void queryCacheEvictAll(Class cls);

    void queryCacheEvictAll();
}
