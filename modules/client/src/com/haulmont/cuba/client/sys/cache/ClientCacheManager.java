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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;

import org.springframework.stereotype.Component;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;

/**
 * Provides ability to cache any abstract object in client application
 *
 */

@Component(ClientCacheManager.NAME)
public class ClientCacheManager {
    public static final String NAME = "cuba_ClientCacheManager";

    protected final Object initializationLock = new Object();
    protected volatile boolean initialized = false;

    protected ConcurrentHashMap<String, CachingStrategy> cache = new ConcurrentHashMap<>();
    protected ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public ClientCacheManager() {
        AppContext.addListener(new AppContext.Listener() {
            @Override
            public void applicationStarted() {

            }

            @Override
            public void applicationStopped() {
                try {
                    executorService.shutdownNow();
                } catch (Exception e) {
                    //do nothing
                }
            }
        });
    }

    public void initialize() {
        if (!initialized) {
            synchronized (initializationLock) {
                Map<String, CachingStrategy> cachingStrategyMap = AppBeans.getAll(CachingStrategy.class);
                for (Map.Entry<String, CachingStrategy> entry : cachingStrategyMap.entrySet()) {
                    addCachedObject(entry.getKey(), entry.getValue());
                }
            }
            initialized = true;
        }
    }

    /**
     * Get cached object by its name
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getCached(String name) {
        CachingStrategy cachingStrategy = cache.get(name);
        if (cachingStrategy != null) {
            Lock readLock = cachingStrategy.lock().readLock();
            Lock writeLock = cachingStrategy.lock().writeLock();
            try {
                readLock.lock();
                if (!cachingStrategy.needToReload()) {
                    return (T) cachingStrategy.getObject();
                } else {
                    try {
                        readLock.unlock();
                        writeLock.lock();
                        if (cachingStrategy.needToReload()) {//re-check condition
                            return (T) cachingStrategy.loadObject();
                        } else {
                            return (T) cachingStrategy.getObject();
                        }
                    } finally {
                        readLock.lock();//downgrade lock to read-only
                        writeLock.unlock();
                    }
                }
            } finally {
                readLock.unlock();
            }
        }

        return null;
    }

    public void refreshCached(String name) {
        CachingStrategy cachingStrategy = cache.get(name);
        if (cachingStrategy != null) {
            Lock writeLock = cachingStrategy.lock().writeLock();
            try {
                writeLock.lock();
                cachingStrategy.loadObject();
            } finally {
                writeLock.unlock();
            }
        }
    }

    /**
     * Add new cached object (described in cachingStrategy)
     */
    public void addCachedObject(String key, CachingStrategy cachingStrategy) {
        Lock writeLock = cachingStrategy.lock().writeLock();
        try {
            writeLock.lock();
            cachingStrategy.init();
        } finally {
            writeLock.unlock();
        }

        cache.put(key, cachingStrategy);
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
