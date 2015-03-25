/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ObjectsCacheManagerAPI.NAME)
public class ObjectsCacheManager implements ObjectsCacheManagerAPI {

    protected ConcurrentMap<String, ObjectsCacheInstance> instances =
            new ConcurrentHashMap<String, ObjectsCacheInstance>();

    protected ConcurrentMap<String, ObjectsCacheController> controllers =
            new ConcurrentHashMap<String, ObjectsCacheController>();

    protected Log log = LogFactory.getLog(getClass());

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
    public ObjectsCacheInstance getCacheNN(String cacheName) {
        ObjectsCacheInstance objectsCacheInstance = instances.get(cacheName);
        if (objectsCacheInstance == null) {
            throw new IllegalArgumentException("ObjectsCacheInstance with name '" + cacheName + "' not found");
        }
        return objectsCacheInstance;
    }

    @Override
    public ObjectsCacheController getController(String cacheName) {
        return controllers.get(cacheName);
    }
}