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

package com.haulmont.cuba.core.sys.entitycache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component(QueryCache.NAME)
public class StandardQueryCache implements QueryCache {

    protected Cache<QueryKey, QueryResult> data;
    protected ConcurrentMap<String, CopyOnWriteArrayList<QueryKey>> typeIndex = new ConcurrentHashMap<>();
    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    protected QueryCacheConfig queryCacheConfig;

    protected static final Logger log = LoggerFactory.getLogger(QueryCache.class);

    @PostConstruct
    protected void init() {
        data = CacheBuilder.newBuilder().maximumSize(queryCacheConfig.getQueryCacheMaxSize()).build();
    }

    @Override
    public QueryResult get(QueryKey queryKey) {
        return data.getIfPresent(queryKey);
    }

    @Override
    public void put(QueryKey queryKey, QueryResult queryResult) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            data.put(queryKey, queryResult);

            for (String type : queryResult.getRelatedTypes()) {
                CopyOnWriteArrayList<QueryKey> keys = typeIndex.get(type);
                if (keys == null) {
                    typeIndex.putIfAbsent(type, new CopyOnWriteArrayList<>());
                    keys = typeIndex.get(type);
                }
                keys.add(queryKey);
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public QueryKey findQueryKeyById(UUID queryId) {
        Set<QueryKey> keys = Sets.newHashSet(data.asMap().keySet());
        for (QueryKey key : keys) {
            if (Objects.equals(queryId, key.getId())) {
                return key;
            }
        }
        return null;
    }

    @Override
    public void invalidate(QueryKey queryKey) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            log.debug("Invalidate query by key {}", queryKey.printDescription());
            data.invalidate(queryKey);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void invalidate(String typeName) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            CopyOnWriteArrayList<QueryKey> keys = typeIndex.get(typeName);
            if (keys == null) return;
            log.debug("Invalidate cache for type {}", typeName);
            Arrays.stream(keys.toArray()).forEach(it -> data.invalidate(it));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void invalidate(Set<String> typeNames) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            typeNames.forEach(typeName -> {
                        CopyOnWriteArrayList<QueryKey> keys = typeIndex.get(typeName);
                        if (keys == null) return;
                        log.debug("Invalidate cache for type {}", typeName);
                        Arrays.stream(keys.toArray()).forEach(it -> data.invalidate(it));
                    }
            );
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public QueryKey invalidate(UUID queryId) {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Set<QueryKey> keys = Sets.newHashSet(data.asMap().keySet());
            for (QueryKey key : keys) {
                if (Objects.equals(queryId, key.getId())) {
                    log.debug("Invalidate query by identifier {}", queryId);
                    data.invalidate(key);
                    return key;
                }
            }
        } finally {
            readLock.unlock();
        }
        return null;
    }

    @Override
    public void invalidateAll() {
        Lock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            log.debug("Invalidate all cache");
            data.invalidateAll();
            typeIndex.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public long size() {
        return data.size();
    }

    @Override
    public long getMaxSize() {
        return queryCacheConfig.getQueryCacheMaxSize();
    }

    @Override
    public Map<QueryKey, QueryResult> asMap() {
        return Maps.newHashMap(data.asMap());
    }
}
