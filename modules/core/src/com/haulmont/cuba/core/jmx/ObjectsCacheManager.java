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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.cache.ObjectsCacheController;
import com.haulmont.cuba.core.app.cache.ObjectsCacheInstance;
import com.haulmont.cuba.core.app.cache.ObjectsCacheManagerAPI;
import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 */
@Component("cuba_ObjectsCacheManagerMBean")
public class ObjectsCacheManager implements ObjectsCacheManagerMBean {

    @Inject
    protected ObjectsCacheManagerAPI manager;

    @Override
    public int getCacheCount() {
        return manager.getActiveInstances().size();
    }

    @Override
    public String printActiveCaches() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ObjectsCacheInstance cache : manager.getActiveInstances()) {
            int size = cache.getStatistics().getObjectsCount();
            stringBuilder.append(cache.getName()).append(String.format(" [%s]\n", size));
        }
        return stringBuilder.toString();
    }

    @Override
    public String printStatsByName(String cacheName) {
        if (StringUtils.isNotEmpty(cacheName)) {
            ObjectsCacheInstance cache = manager.getCache(cacheName);
            if (cache != null) {
                return cache.getStatistics().toString();
            } else
                return String.format("Couldn't find cache %s", cacheName);
        } else
            return "Empty name is not permitted";
    }

    @Override
    public String reloadByName(String cacheName) {
        if (StringUtils.isNotEmpty(cacheName)) {
            ObjectsCacheController controller = manager.getController(cacheName);
            if (controller != null) {
                controller.reloadCache();
                return String.format("Updated %s", cacheName);
            } else
                return String.format("Couldn't find controller for cache %s", cacheName);
        } else
            return "Empty name is not permitted";
    }}
