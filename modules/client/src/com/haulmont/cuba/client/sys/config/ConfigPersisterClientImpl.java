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
package com.haulmont.cuba.client.sys.config;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.config.ConfigPersister;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigPersisterClientImpl implements ConfigPersister {
    protected static final Logger log = LoggerFactory.getLogger(ConfigPersisterClientImpl.class);

    private Map<String, String> cache = new ConcurrentHashMap<>();

    private volatile boolean cacheLoaded;
    protected boolean caching;

    protected ConfigStorageService configStorageService;

    public ConfigPersisterClientImpl(ConfigStorageService configStorageService, boolean caching) {
        this.caching = caching;
        this.configStorageService = configStorageService;
    }

    @Override
    public String getProperty(SourceType sourceType, String name) {
        log.trace("Getting property '" + name + "', source=" + sourceType.name());
        String value;
        switch (sourceType) {
            case SYSTEM:
                value = System.getProperty(name);
                break;
            case APP:
                value = AppContext.getProperty(name);
                break;
            case DATABASE:
                value = AppContext.getProperty(name);
                if (StringUtils.isEmpty(value)) {
                    if (caching) {
                        loadCache();
                        value = cache.get(name);
                    } else {
                        return getConfigStorage().getDbProperty(name);
                    }
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
        return value;
    }

    private void loadCache() {
        if (!cacheLoaded) {
            synchronized (this) {
                if (!cacheLoaded) {
                    Map<String, String> properties = getConfigStorage().getDbProperties();
                    cache.clear();
                    cache.putAll(properties);
                    cacheLoaded = true;
                }
            }
        }
    }

    @Override
    public void setProperty(SourceType sourceType, String name, String value) {
        log.debug("Setting property '" + name + "' to '" + value + "', source=" + sourceType.name());
        switch (sourceType) {
            case SYSTEM:
                System.setProperty(name, value);
                break;
            case APP:
                AppContext.setProperty(name, value);
                break;
            case DATABASE:
                if (value != null) {
                    cache.put(name, value);
                } else {
                    cache.remove(name);
                }
                getConfigStorage().setDbProperty(name, value);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported config source type: " + sourceType);
        }
    }

    protected ConfigStorageService getConfigStorage() {
        return configStorageService;
    }
}