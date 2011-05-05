/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.cuba.core.global.TimeProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cache for application objects
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ObjectsCache implements ObjectsCacheInstance {

    private String name;
    private CacheSet cacheSet;
    private CacheLoader loader;
    private boolean logUpdateEvent = false;

    private static Log log = LogFactory.getLog(ObjectsCache.class);

    private ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();

    private Date lastUpdateTime;
    private long lastUpdateDuration;

    private final static int UPDATE_COUNT_FOR_AVERAGE_DURATION = 10;
    private List<Long> updateDurations = new ArrayList<Long>(UPDATE_COUNT_FOR_AVERAGE_DURATION);
    private int updateDurationsIndex = 0;

    @Inject
    private ObjectsCacheManagerAPI managerAPI;

    public ObjectsCache() {
        cacheSet = new CacheSet(Collections.emptyList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        managerAPI.registerCache(this);
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

    public void refresh() {
        if (StringUtils.isEmpty(name)) {
            log.error("Not set name for ObjectsCache instance");
            return;
        }

        if (loader != null) {

            Date updateStart = TimeProvider.currentTimestamp();

            // Load data
            CacheSet data;
            try {
                data = loader.loadData(this);
            } catch (CacheException e) {
                log.error(String.format("Load data for cache %s failed", name));
                this.cacheSet = new CacheSet(Collections.emptyList());
                return;
            }

            Date updateEnd = TimeProvider.currentTimestamp();

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

            this.lastUpdateTime = TimeProvider.currentTimestamp();

            if (logUpdateEvent)
                log.info("Updated cache set in " + name + " " +
                        String.valueOf(lastUpdateDuration) + " millis");
        } else
            log.error("Not set cache loader for ObjectsCache:" + name);
    }

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

    public Collection execute(CacheSelector cacheSelector) {
        Collection result;

        cacheLock.readLock().lock();

        if (cacheSelector != null)
            result = cacheSelector.select(cacheSet);
        else
            result = Collections.emptyList();

        cacheLock.readLock().unlock();

        return result;
    }
}