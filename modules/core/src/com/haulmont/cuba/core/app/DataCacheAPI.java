/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 06.08.2009 16:24:11
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import java.util.Collection;

public interface DataCacheAPI {

    boolean isStoreCacheEnabled();

    boolean isQueryCacheEnabled();

    void dataCacheEvict(Class cls, Object id);

    void dataCacheEvictAll(Class cls, Collection ids);

    void dataCacheEvictAll();

    void queryCacheEvictAll(Class cls);

    void queryCacheEvictAll();
}
