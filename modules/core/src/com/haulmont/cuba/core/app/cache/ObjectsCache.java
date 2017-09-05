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

import com.google.common.collect.Lists;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cache for application objects.
 *
 * @deprecated Will be removed in release 7.0.
 */
@Deprecated
public class ObjectsCache implements ObjectsCacheInstance, ObjectsCacheController {

    private final Logger log = LoggerFactory.getLogger(ObjectsCache.class);

    protected String name;
    protected CacheSet cacheSet;
    protected CacheLoader loader;
    protected boolean logUpdateEvent = false;

    protected ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    protected ReentrantLock updateDataLock = new ReentrantLock();

    protected Date lastUpdateTime;
    protected long lastUpdateDuration;

    protected final static int UPDATE_COUNT_FOR_AVERAGE_DURATION = 10;
    protected List<Long> updateDurations = new ArrayList<>(UPDATE_COUNT_FOR_AVERAGE_DURATION);
    protected int updateDurationsIndex = 0;

    @Inject
    protected ObjectsCacheManagerAPI managerAPI;
    @Inject
    protected ClusterManagerAPI clusterManagerAPI;

    public ObjectsCache() {
        cacheSet = new CacheSet(Collections.emptyList());
    }

    protected CacheSet createCacheSet(Collection<Object> items) {
        return new CacheSet(items);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        managerAPI.registerCache(this);
        managerAPI.registerController(this);
    }

    public CacheLoader getLoader() {
        return loader;
    }

    public void setLoader(CacheLoader loader) {
        this.loader = loader;
    }

    public boolean isLogUpdateEvent() {
        return logUpdateEvent;
    }

    public void setLogUpdateEvent(boolean logUpdateEvent) {
        this.logUpdateEvent = logUpdateEvent;
    }

    protected boolean isValidState() {
        if (StringUtils.isEmpty(name)) {
            log.error("Not set name for ObjectsCache instance");
            return false;
        }

        if (!AppContext.isStarted())
            return false;

        if (loader == null) {
            log.error("Not set cache loader for ObjectsCache:" + name);
            return false;
        }

        return true;
    }

    public void refresh() {
        TimeSource timeSource = AppBeans.get(TimeSource.class);
        if (isValidState()) {
            Date updateStart = timeSource.currentTimestamp();

            // Load data
            CacheSet data;
            try {
                data = loader.loadData(this);
            } catch (CacheException e) {
                log.error(String.format("Load data for cache %s failed", name), e);
                this.cacheSet = new CacheSet(Collections.emptyList());
                return;
            }

            Date updateEnd = timeSource.currentTimestamp();

            this.lastUpdateDuration = updateEnd.getTime() - updateStart.getTime();

            if (updateDurations.size() > updateDurationsIndex)
                updateDurations.set(updateDurationsIndex, lastUpdateDuration);
            else
                updateDurations.add(lastUpdateDuration);

            updateDurationsIndex = (updateDurationsIndex + 1) % UPDATE_COUNT_FOR_AVERAGE_DURATION;

            cacheLock.writeLock().lock();

            // Modify cache set
            this.cacheSet = data;

            cacheLock.writeLock().unlock();

            this.lastUpdateTime = timeSource.currentTimestamp();

            if (logUpdateEvent)
                log.debug("Updated cache set in " + name + " " +
                        String.valueOf(lastUpdateDuration) + " millis");
        }
    }

    @Override
    public CacheStatistics getStatistics() {

        cacheLock.readLock().lock();

        CacheStatistics stats = new CacheStatistics(this);
        stats.setObjectsCount(cacheSet.getSize());
        stats.setLastUpdateTime(lastUpdateTime);
        stats.setLastUpdateDuration(lastUpdateDuration);

        double durationSumm = 0;
        int durationsCount = 0;
        long averageDurationTime = 0;
        for (Long updateDuration : updateDurations) {
            if (updateDuration != null) {
                durationSumm += updateDuration;
                durationsCount++;
            }
        }
        if (durationsCount > 0)
            averageDurationTime = Math.round(durationSumm / durationsCount);
        stats.setAverageUpdateDuration(averageDurationTime);

        cacheLock.readLock().unlock();

        return stats;
    }

    @Override
    public Collection execute(CacheSelector cacheSelector) {
        Collection result;

        cacheLock.readLock().lock();

        if (cacheSelector != null) {
            // Select from cache copy
            CacheSet temporaryCacheSet;
            try {
                temporaryCacheSet = (CacheSet) cacheSet.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            result = cacheSelector.select(temporaryCacheSet);
        } else
            result = Collections.emptyList();

        cacheLock.readLock().unlock();

        return result;
    }

    @Override
    public int count(Predicate... selectors) {
        cacheLock.readLock().lock();

        int count;
        try {
            count = cacheSet.countConjunction(selectors);
        } finally {
            cacheLock.readLock().unlock();
        }

        return count;
    }

    @Override
    public Pair<Integer, Integer> count(Collection<Predicate> selectors, Predicate amplifyingSelector) {
        cacheLock.readLock().lock();
        try {
            return cacheSet.countConjunction(selectors, amplifyingSelector);
        } finally {
            cacheLock.readLock().unlock();
        }
    }

    @Override
    public ObjectsCacheInstance getCache() {
        return this;
    }

    @Override
    public void reloadCache() {
        refresh();
    }

    @Override
    public void updateCache(Map<String, Object> params) {
        if (isValidState()) {
            // do not allow parallel update, but do not block reading
            updateDataLock.lock();

            try {
                // Modify cache copy
                CacheSet temporaryCacheSet;
                try {
                    cacheLock.readLock().lock();

                    temporaryCacheSet = createCacheSet(new ArrayList<>(cacheSet.getItems()));
                    temporaryCacheSet.setForUpdate(true);
                } finally {
                    cacheLock.readLock().unlock();
                }

                try {
                    loader.updateData(temporaryCacheSet, params);
                } catch (CacheException e) {
                    log.error(String.format("Update data for cache %s failed", name), e);
                    this.cacheSet = new CacheSet(Collections.emptyList());
                    return;
                }

                cacheLock.writeLock().lock();

                try {
                    temporaryCacheSet.setForUpdate(false);
                    // Modify cache set
                    this.cacheSet = temporaryCacheSet;
                    sendCacheUpdateMessage(cacheSet.getRemovedItems(),
                            cacheSet.getAddedItems());
                } finally {
                    cacheLock.writeLock().unlock();
                }
            } finally {
                updateDataLock.unlock();
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void sendCacheUpdateMessage(Set<Object> removedItems, Set<Object> addedItems) {
        if (clusterManagerAPI.isStarted())
            clusterManagerAPI.send(new CacheUpdateMessage(name, copyItemsCollection(removedItems), addedItems));
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> copyItemsCollection(Collection<T> items) {
        List<T> result = Lists.newArrayList();
        for (T item : items) {
            if (item instanceof BaseGenericIdEntity
                    && BooleanUtils.isFalse(BaseEntityInternalAccess.isDetached((BaseGenericIdEntity) item)))
                item = (T) copy((BaseGenericIdEntity) item);
            result.add(item);
        }

        return result;
    }

    // Items passed to update method can be managed we send only their copies
    protected Entity copy(BaseGenericIdEntity entity) {
        BaseGenericIdEntity result = (BaseGenericIdEntity) AppBeans.get(Metadata.class).create(entity.getMetaClass());
        result.setId(entity.getId());
        return result;
    }

    public void updateCache(CacheUpdateMessage msg) {
        if (isValidState()) {
            updateDataLock.lock();
            try {
                cacheLock.writeLock().lock();
                try {
                    Collection<Object> itemsToRemove = msg.getItemsToRemove();
                    Collection<Object> itemsToAdd = msg.getItemsToAdd();

                    if (CollectionUtils.isNotEmpty(itemsToRemove))
                        cacheSet.getItems().removeAll(itemsToRemove);

                    if (CollectionUtils.isNotEmpty(itemsToAdd))
                        cacheSet.getItems().addAll(itemsToAdd);

                } finally {
                    cacheLock.writeLock().unlock();
                }
            } finally {
                updateDataLock.unlock();
            }
        }
    }
}