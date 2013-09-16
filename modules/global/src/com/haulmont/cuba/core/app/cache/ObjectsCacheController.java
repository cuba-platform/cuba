/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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