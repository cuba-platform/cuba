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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

@Component(ObjectsCacheManagerAPI.NAME)
public class ObjectsCacheManager implements ObjectsCacheManagerAPI {

    protected ConcurrentMap<String, ObjectsCacheInstance> instances = new ConcurrentHashMap<>();

    protected ConcurrentMap<String, ObjectsCacheController> controllers = new ConcurrentHashMap<>();

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void registerCache(ObjectsCacheInstance objectsCache) {
        checkNotNull(objectsCache);

        if (StringUtils.isNotEmpty(objectsCache.getName())) {
            if (!instances.containsKey(objectsCache.getName())) {
                instances.put(objectsCache.getName(), objectsCache);
            } else
                log.error(
                        String.format("Cache instance with name \"%s\" is already registered", objectsCache.getName()));
        }
    }

    @Override
    public void registerController(ObjectsCacheController cacheController) {
        checkNotNull(cacheController);
        checkNotNull(cacheController.getCache());

        ObjectsCacheInstance objectsCache = cacheController.getCache();

        if (StringUtils.isNotEmpty(objectsCache.getName())) {
            if (!controllers.containsKey(objectsCache.getName())) {
                controllers.put(objectsCache.getName(), cacheController);
            } else
                log.error(
                        String.format("Cache instance with name \"%s\" is already registered", objectsCache.getName()));
        }
    }

    @Override
    public Collection<ObjectsCacheInstance> getActiveInstances() {
        return instances.values();
    }

    @Override
    public ObjectsCacheInstance getCache(String cacheName) {
        return instances.get(cacheName);
    }

    @Override
    public ObjectsCacheInstance getCacheNN(String cacheName) {
        ObjectsCacheInstance objectsCacheInstance = instances.get(cacheName);
        if (objectsCacheInstance == null) {
            throw new IllegalArgumentException("ObjectsCacheInstance with name '" + cacheName + "' not found");
        }
        return objectsCacheInstance;
    }

    @Override
    public ObjectsCacheController getController(String cacheName) {
        return controllers.get(cacheName);
    }
}