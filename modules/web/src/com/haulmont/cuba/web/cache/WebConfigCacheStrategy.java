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
 */

package com.haulmont.cuba.web.cache;

import com.haulmont.cuba.client.sys.cache.CachingStrategy;
import com.haulmont.cuba.core.app.ConfigStorageService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component(WebConfigCacheStrategy.NAME)
public class WebConfigCacheStrategy implements CachingStrategy {
    public static final String NAME = "cuba_WebConfigCacheStrategy";

    protected volatile Map<String, String> cachedProperties = Collections.unmodifiableMap(new HashMap<>());

    @Inject
    protected ConfigStorageService configStorageService;

    protected volatile long lastUsedTs = 0;
    protected volatile long updateIntervalMs = 60 * 1000;

    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public void init() {
    }

    @Override
    public Object getObject() {
        return cachedProperties;
    }

    @Override
    public Object loadObject() {
        Map<String, String> cachedPropertiesFromServer = Collections.unmodifiableMap(configStorageService.getDbProperties());

        cachedProperties = cachedPropertiesFromServer;
        lastUsedTs = System.currentTimeMillis();

        return cachedPropertiesFromServer;
    }

    @Override
    public ReadWriteLock lock() {
        return readWriteLock;
    }

    @Override
    public boolean needToReload() {
        return System.currentTimeMillis() - lastUsedTs > updateIntervalMs;
    }

    public long getUpdateIntervalMs() {
        return updateIntervalMs;
    }

    public void setUpdateIntervalMs(long updateIntervalMs) {
        this.updateIntervalMs = updateIntervalMs;
    }
}