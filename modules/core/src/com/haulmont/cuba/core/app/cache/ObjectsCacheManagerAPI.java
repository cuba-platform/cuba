/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.app.cache;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Interface to provide "Data in memory" caches and controllers.
 *
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