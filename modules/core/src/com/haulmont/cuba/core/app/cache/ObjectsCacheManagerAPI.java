/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Interface to provide "Data in memory" caches and controllers.
 *
 * @author artamonov
 * @version $Id$
 */
public interface ObjectsCacheManagerAPI {

    String NAME = "cuba_ObjectsCacheManager";

    /**
     * Register cache instance in
     *
     * @param objectsCache Cache instance
     */
    void registerCache(ObjectsCacheInstance objectsCache);

    /**
     * Register cache controller in
     *
     * @param cacheController Cache controller
     */
    void registerController(ObjectsCacheController cacheController);

    /**
     * Get collection of registered instances
     *
     * @return Active cache instances
     */
    Collection<ObjectsCacheInstance> getActiveInstances();

    /**
     * Get cache instance by name
     *
     * @param cacheName Unique cache name
     * @return Cache instance
     */
    @Nullable
    ObjectsCacheInstance getCache(String cacheName);

    /**
     * Get cache instance by name
     *
     * @param cacheName Unique cache name
     * @return Cache instance
     * @throws java.lang.IllegalArgumentException if cache with given name not found
     */
    ObjectsCacheInstance getCacheNN(String cacheName);

    /**
     * Get cache controller instance by cache name
     *
     * @param cacheName Unique cache name
     * @return Cache controller
     */
    @Nullable
    ObjectsCacheController getController(String cacheName);
}