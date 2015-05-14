/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.cache;

import com.haulmont.cuba.core.global.AppBeans;

import javax.annotation.ManagedBean;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.locks.Lock;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(ClientCacheManager.NAME)
public class ClientCacheManager {
    public static final String NAME = "cuba_ClientCacheManager";

    protected final Object initializationLock = new Object();
    protected volatile boolean initialized = false;

    protected ConcurrentHashMap<String, CachingStrategy> cache = new ConcurrentHashMap<>();
    protected ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

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
