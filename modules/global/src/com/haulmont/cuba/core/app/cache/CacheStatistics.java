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

import java.util.Date;

/**
 * Statistics cache life cycle
 *
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