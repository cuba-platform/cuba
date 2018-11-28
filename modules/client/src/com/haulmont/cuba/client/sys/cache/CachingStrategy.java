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

package com.haulmont.cuba.client.sys.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Describes cache storage and invalidation policy
*/
public interface CachingStrategy {
    /**
     * Method for strategy initialization
     * Invoked at first login, so security context is available
     */
    default void init() {
    }

    /**
     * Return cached object
     */
    Object getObject();

    /**
     * Refresh cached object
     */
    Object loadObject();

    /**
     * Return lock used to provide caching thread safety
     */
    ReadWriteLock lock();

    /**
     * Indicate whether cached object should be refreshed or not
     */
    boolean needToReload();

    /**
     * Method for clearing cache
     */
    default void clearCache() {
    }
}