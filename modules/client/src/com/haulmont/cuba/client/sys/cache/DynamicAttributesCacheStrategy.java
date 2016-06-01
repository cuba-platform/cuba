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

package com.haulmont.cuba.client.sys.cache;

import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCache;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesCacheService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Each time dynamic attributes cache is used, the strategy initiate check of cache validity
 * The check is performed in separate thread
 * So applied changes in dynamic attributes structure will be visible after 10 seconds
 */
@Component(DynamicAttributesCacheStrategy.NAME)
public class DynamicAttributesCacheStrategy implements CachingStrategy {
    public static final String NAME = "cuba_DynamicAttributesCacheStrategy";

    protected Logger log = LoggerFactory.getLogger(DynamicAttributesCacheStrategy.class);

    @Inject
    protected ClientCacheManager clientCacheManager;
    @Inject
    protected LoginService loginService;
    @Inject
    protected Configuration configuration;
    @Inject
    protected CacheUserSessionProvider cacheUserSessionProvider;

    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected DynamicAttributesCache dynamicAttributesCache;

    //there is no need for atomicity when change needToValidateCache or lastRequestedSessionId
    protected volatile boolean needToValidateCache;

    @Override
    public void init() {
        clientCacheManager.getExecutorService().scheduleWithFixedDelay(() -> {
            if (needToValidateCache) {
                UserSession userSession = cacheUserSessionProvider.getUserSession();
                try {
                    AppContext.setSecurityContext(new SecurityContext(userSession));

                    loadObject();
                } catch (NoUserSessionException e) {
                    log.warn("Cache user session expired", e);
                } catch (Exception e) {
                    log.error("Unable to update dynamic attributes cache", e);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public Object getObject() {
        needToValidateCache = true;
        return dynamicAttributesCache;
    }

    @Override
    public Object loadObject() {
        DynamicAttributesCache cacheFromServer = AppBeans.get(DynamicAttributesCacheService.NAME, DynamicAttributesCacheService.class)
                .getCacheIfNewer(dynamicAttributesCache != null ? dynamicAttributesCache.getCreationDate() : null);
        if (cacheFromServer != null) {
            dynamicAttributesCache = cacheFromServer;
        }

        needToValidateCache = false;
        return dynamicAttributesCache;
    }

    @Override
    public boolean needToReload() {
        return dynamicAttributesCache == null;
    }

    @Override
    public ReadWriteLock lock() {
        return readWriteLock;
    }
}