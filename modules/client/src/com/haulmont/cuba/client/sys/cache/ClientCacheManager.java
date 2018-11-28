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

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.core.sys.events.AppContextStoppedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;

/**
 * Provides ability to cache any abstract object in client application
 */
@Component(ClientCacheManager.NAME)
public class ClientCacheManager {

    public static final String NAME = "cuba_ClientCacheManager";

    private static final Logger log = LoggerFactory.getLogger(ClientCacheManager.class);

    protected ConcurrentHashMap<String, CachingStrategy> cache = new ConcurrentHashMap<>();
    protected ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
        final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

        @Override
        public Thread newThread(@Nonnull Runnable r) {
            Thread thread = defaultFactory.newThread(r);
            thread.setName("ClientCacheManager-" + thread.getName());
            thread.setUncaughtExceptionHandler((t, e) ->
                    log.error("Unhandled exception", t)
            );
            return thread;
        }
    });

    @EventListener(AppContextInitializedEvent.class)
    @Order(Events.LOWEST_PLATFORM_PRECEDENCE - 120)
    public void initialize(AppContextInitializedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, CachingStrategy> cachingStrategyMap = applicationContext.getBeansOfType(CachingStrategy.class);
        for (Map.Entry<String, CachingStrategy> entry : cachingStrategyMap.entrySet()) {
            addCachedObject(entry.getKey(), entry.getValue());
        }
    }

    @EventListener(AppContextStoppedEvent.class)
    public void destroy() {
        try {
            executorService.shutdownNow();
        } catch (Exception e) {
            //do nothing
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

    public void clearCache() {
        for (CachingStrategy cachingStrategy : cache.values()) {
            cachingStrategy.clearCache();
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