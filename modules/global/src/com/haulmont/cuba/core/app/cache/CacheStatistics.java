/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import java.util.Date;

/**
 * Statistics cache life cycle
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class CacheStatistics {

    private String cacheName;
    private int objectsCount = 0;

    private Date lastUpdateTime;
    private long lastUpdateDuration;
    private long averageUpdateDuration;

    public CacheStatistics(ObjectsCacheInstance cacheInstance) {
        this.cacheName = cacheInstance.getName();
    }

    public String getCacheName() {
        return cacheName;
    }

    public int getObjectsCount() {
        return objectsCount;
    }

    public void setObjectsCount(int objectsCount) {
        this.objectsCount = objectsCount;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getLastUpdateDuration() {
        return lastUpdateDuration;
    }

    public void setLastUpdateDuration(long lastUpdateDuration) {
        this.lastUpdateDuration = lastUpdateDuration;
    }

    public long getAverageUpdateDuration() {
        return averageUpdateDuration;
    }

    public void setAverageUpdateDuration(long averageUpdateDuration) {
        this.averageUpdateDuration = averageUpdateDuration;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Cache: ").append(cacheName).append("\n");
        sb.append("Count: ").append(objectsCount).append("\n");

        if (lastUpdateTime != null)
            sb.append("Last update: ").append(lastUpdateTime).append("\n");

        sb.append("Last update duration: ").append(lastUpdateDuration).append(" millis \n");

        sb.append("Average update duration: ").append(averageUpdateDuration).append(" millis \n");

        return sb.toString();
    }
}