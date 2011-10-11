/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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