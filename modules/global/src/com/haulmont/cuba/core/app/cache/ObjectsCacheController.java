/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import java.util.Map;

/**
 * Interface defining methods for manage cache content and modify cache properties in runtime
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ObjectsCacheController {

    /**
     * Get a controllable cache
     * @return Cache instance
     */
    ObjectsCacheInstance getCache();

    /**
     * Reload cache content
     */
    void reloadCache();

    /**
     * Update cache content
     * @param params Update params
     */
    void updateCache(Map<String, Object> params);
}