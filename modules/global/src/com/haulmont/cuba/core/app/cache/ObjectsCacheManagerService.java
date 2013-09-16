/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import java.util.Map;

/**
 * Service for manage application caches
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ObjectsCacheManagerService {
    String NAME = "cuba_ObjectsCacheManagerService";

    /**
     * Update cache content
     * @param cacheName Cache instance name
     * @param params Params for update
     */
    void updateCache(String cacheName, Map<String, Object> params);
}
