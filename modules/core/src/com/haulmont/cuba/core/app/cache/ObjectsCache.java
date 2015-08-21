/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cache for application objects
 *
 * @author artamonov
 * @version $Id$
 */
public class ObjectsCache implements ObjectsCacheInstance, ObjectsCacheController {

    private String name;
    private CacheSet cacheSet;
    private CacheLoader loader;
    private boolean logUpdateEvent = false;

    private static Log log = LogFactory.getLog(ObjectsCache.class);

    private ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
    private ReentrantLock updateDataLock = new ReentrantLock();

    private Date lastUpdateTime;
    private long lastUpdateDuration;

    private final static int UPDATE_COUNT_FOR_AVERAGE_DURATION = 10;
    private List<Long> updateDurations = new ArrayList<>(UPDATE_COUNT_FOR_AVERAGE_DURATION);
    private int updateDurationsIndex = 0;

    @Inject
    private ObjectsCacheManagerAPI managerAPI;

    public ObjectsCache() {
        cacheSet = new CacheSet(Collections.emptyList());
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

    private boolean isValidState() {
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

                    temporaryCacheSet = (CacheSet) cacheSet.clone();
                } catch (CloneNotSupportedException e) {
                    log.error(String.format("Update data for cache %s failed", name), e);
                    this.cacheSet = new CacheSet(Collections.emptyList());
                    return;
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
                    // Modify cache set
                    this.cacheSet = temporaryCacheSet;
                } finally {
                    cacheLock.writeLock().unlock();
                }
            } finally {
                updateDataLock.unlock();
            }
        }
    }
}