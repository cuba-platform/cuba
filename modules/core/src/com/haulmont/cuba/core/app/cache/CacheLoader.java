/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import java.util.Map;

/**
 * Data loader interface for ObjectsCache
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface CacheLoader {
    CacheSet loadData(ObjectsCache cache) throws CacheException;

    void updateData(CacheSet cacheSet, Map<String, Object> params) throws CacheException;
}