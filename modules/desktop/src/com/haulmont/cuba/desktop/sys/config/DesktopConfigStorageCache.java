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

package com.haulmont.cuba.desktop.sys.config;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.AppPropertyEntity;
import com.haulmont.cuba.core.global.AppBeans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Config storage service proxy with caching for desktop client. <br/>
 * Reloads db properties only if last loading was at least 10 seconds ago.
 */
public class DesktopConfigStorageCache implements ConfigStorageService {

    protected static final int SEQUENTIAL_INVALIDATE_THRESHOLD = 10000; // 10 seconds

    protected long lastInvalidateTs = 0;
    protected final Map<String, String> properties = new HashMap<>();

    @Override
    public Map<String, String> getDbProperties() {
        Map<String, String> props;
        synchronized (properties) {
            invalidateIfNeeded();
            props = ImmutableMap.copyOf(properties);
        }
        return props;
    }

    @Override
    public String getDbProperty(String name) {
        String value;
        synchronized (properties) {
            invalidateIfNeeded();
            value = properties.get(name);
        }
        return value;
    }

    @Override
    public void setDbProperty(String name, String value) {
        synchronized (properties) {
            getService().setDbProperty(name, value);
            lastInvalidateTs = 0;
        }
    }

    @Override
    public List<AppPropertyEntity> getAppProperties() {
        return getService().getAppProperties();
    }

    /**
     * invoke only with synchronization by this.properties
     */
    protected void invalidateIfNeeded() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastInvalidateTs > SEQUENTIAL_INVALIDATE_THRESHOLD) {
            properties.clear();
            properties.putAll(getService().getDbProperties());
            lastInvalidateTs = System.currentTimeMillis();
        }
    }

    protected ConfigStorageService getService() {
        return AppBeans.get(ConfigStorageService.NAME);
    }
}