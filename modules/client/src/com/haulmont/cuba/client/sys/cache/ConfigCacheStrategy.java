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
 */

package com.haulmont.cuba.client.sys.cache;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Caching strategy for config values stored with {@link com.haulmont.cuba.core.config.SourceType#DATABASE}.
 */
@Component(ConfigCacheStrategy.NAME)
public class ConfigCacheStrategy implements CachingStrategy {
    public static final String NAME = "cuba_ConfigCacheStrategy";

    protected Logger log = LoggerFactory.getLogger(ConfigCacheStrategy.class);

    protected volatile Map<String, String> cachedProperties = null;

    @Inject
    protected ConfigStorageService configStorageService;
    @Inject
    protected ClientCacheManager clientCacheManager;
    @Inject
    protected CacheUserSessionProvider cacheUserSessionProvider;

    protected volatile long updateIntervalMs = 60 * 1000;
    protected volatile boolean updateSynchronously = false;

    protected volatile long lastUsedTs = 0;

    protected volatile boolean backgroundUpdateTriggered = false;

    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public Object getObject() {
        return cachedProperties;
    }

    @Override
    public Object loadObject() {
        if (cachedProperties == null || updateSynchronously) {
            Map<String, String> cachedPropertiesFromServer = Collections.unmodifiableMap(configStorageService.getDbProperties());

            cachedProperties = cachedPropertiesFromServer;
            lastUsedTs = System.currentTimeMillis();

            cachedProperties = cachedPropertiesFromServer;
        } else {
            if (!backgroundUpdateTriggered) {
                clientCacheManager.getExecutorService().submit(this::updateCacheInBackground);

                backgroundUpdateTriggered = true;
            }
        }

        return cachedProperties;
    }

    protected void updateCacheInBackground() {
        UserSession userSession = cacheUserSessionProvider.getUserSession();
        try {
            AppContext.setSecurityContext(new SecurityContext(userSession));

            Map<String, String> cachedPropertiesFromServer =
                    Collections.unmodifiableMap(configStorageService.getDbProperties());

            readWriteLock.writeLock().lock();
            try {
                cachedProperties = cachedPropertiesFromServer;
                lastUsedTs = System.currentTimeMillis();
            } finally {
                readWriteLock.writeLock().unlock();
            }
        } catch (NoUserSessionException e) {
            log.warn("Cache user session expired", e);
        } catch (Exception e) {
            log.error("Unable to update config storage cache", e);
        } finally {
            AppContext.setSecurityContext(null);

            backgroundUpdateTriggered = false;
        }
    }

    @Override
    public ReadWriteLock lock() {
        return readWriteLock;
    }

    @Override
    public boolean needToReload() {
        return System.currentTimeMillis() - lastUsedTs > updateIntervalMs;
    }

    public long getUpdateIntervalMs() {
        return updateIntervalMs;
    }

    public void setUpdateIntervalMs(long updateIntervalMs) {
        this.updateIntervalMs = updateIntervalMs;
    }

    public boolean isUpdateSynchronously() {
        return updateSynchronously;
    }

    public void setUpdateSynchronously(boolean updateSynchronously) {
        this.updateSynchronously = updateSynchronously;
    }
}