/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import com.haulmont.cuba.core.app.ManagementBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ObjectsCacheManager
 * <p>$Id$</p>
 *
 * @author artamonov
 */
@ManagedBean(ObjectsCacheManagerAPI.NAME)
public class ObjectsCacheManager extends ManagementBean implements ObjectsCacheManagerAPI, ObjectsCacheManagerMBean {

    private ConcurrentMap<String, ObjectsCacheInstance> instances =
            new ConcurrentHashMap<String, ObjectsCacheInstance>();

    private Log log = LogFactory.getLog(ObjectsCacheManager.class);

    public void registerCache(ObjectsCacheInstance objectsCache) {
        if ((objectsCache != null) && (StringUtils.isNotEmpty(objectsCache.getName()))) {
            if (!instances.containsKey(objectsCache.getName())) {
                instances.put(objectsCache.getName(), objectsCache);
            } else
                log.error(
                        String.format("Cache instance with name \"%s\" is already registered",
                                objectsCache.getName()));
        } else {
            log.error("Undefined cache instance");
        }
    }

    public Collection<ObjectsCacheInstance> getActiveInstances() {
        return instances.values();
    }

    public ObjectsCacheInstance getCache(String cacheName) {
        return instances.get(cacheName);
    }

    public int getCacheCount() {
        return instances.size();
    }

    public String printActiveCaches() {
        StringBuilder stringBuilder = new StringBuilder();
        Collection<ObjectsCacheInstance> instancesList = instances.values();
        for (ObjectsCacheInstance cache : instancesList) {
            int size = cache.getStatistics().getObjectsCount();
            stringBuilder.append(cache.getName()).append(String.format(" [%s]\n", size));
        }
        return stringBuilder.toString();
    }

    public String printStatsByName(String cacheName) {
        if (StringUtils.isNotEmpty(cacheName)) {
            if (instances.containsKey(cacheName)) {
                return instances.get(cacheName).getStatistics().toString();
            } else
                return String.format("Couldn't found cache %s", cacheName);
        } else
            return "Empty name not permitted";
    }
}