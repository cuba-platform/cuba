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

import static com.google.common.base.Preconditions.checkNotNull;

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

    private ConcurrentMap<String, ObjectsCacheController> controllers =
            new ConcurrentHashMap<String, ObjectsCacheController>();

    private Log log = LogFactory.getLog(ObjectsCacheManager.class);

    @Override
    public void registerCache(ObjectsCacheInstance objectsCache) {
        checkNotNull(objectsCache);

        if (StringUtils.isNotEmpty(objectsCache.getName())) {
            if (!instances.containsKey(objectsCache.getName())) {
                instances.put(objectsCache.getName(), objectsCache);
            } else
                log.error(
                        String.format("Cache instance with name \"%s\" is already registered", objectsCache.getName()));
        }
    }

    @Override
    public void registerController(ObjectsCacheController cacheController) {
        checkNotNull(cacheController);
        checkNotNull(cacheController.getCache());

        ObjectsCacheInstance objectsCache = cacheController.getCache();

        if (StringUtils.isNotEmpty(objectsCache.getName())) {
            if (!controllers.containsKey(objectsCache.getName())) {
                controllers.put(objectsCache.getName(), cacheController);
            } else
                log.error(
                        String.format("Cache instance with name \"%s\" is already registered", objectsCache.getName()));
        }
    }

    @Override
    public Collection<ObjectsCacheInstance> getActiveInstances() {
        return instances.values();
    }

    @Override
    public ObjectsCacheInstance getCache(String cacheName) {
        return instances.get(cacheName);
    }

    @Override
    public ObjectsCacheController getController(String cacheName) {
        return controllers.get(cacheName);
    }

    @Override
    public int getCacheCount() {
        return instances.size();
    }

    @Override
    public String printActiveCaches() {
        StringBuilder stringBuilder = new StringBuilder();
        Collection<ObjectsCacheInstance> instancesList = instances.values();
        for (ObjectsCacheInstance cache : instancesList) {
            int size = cache.getStatistics().getObjectsCount();
            stringBuilder.append(cache.getName()).append(String.format(" [%s]\n", size));
        }
        return stringBuilder.toString();
    }

    @Override
    public String printStatsByName(String cacheName) {
        if (StringUtils.isNotEmpty(cacheName)) {
            if (instances.containsKey(cacheName)) {
                return instances.get(cacheName).getStatistics().toString();
            } else
                return String.format("Couldn't found cache %s", cacheName);
        } else
            return "Empty name not permitted";
    }

    @Override
    public String reloadByName(String cacheName) {
        if (StringUtils.isNotEmpty(cacheName)) {
            if (controllers.containsKey(cacheName)) {
                controllers.get(cacheName).reloadCache();
                return String.format("Updated %s", cacheName);
            } else
                return String.format("Couldn't found controller for cache %s", cacheName);
        } else
            return "Empty name not permitted";
    }
}