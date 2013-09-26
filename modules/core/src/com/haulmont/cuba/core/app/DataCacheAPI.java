/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.app;

import java.util.Collection;

/**
 * Facade to OpenJPA data cache functionality.
 *
 * @author krivopustov
 * @version $Id$
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
