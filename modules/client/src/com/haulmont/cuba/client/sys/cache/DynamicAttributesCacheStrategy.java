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
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.SecurityContext;
import com.haulmont.cuba.security.app.LoginService;
import com.haulmont.cuba.security.global.NoUserSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Each time dynamic attributes cache is used, the strategy initiate check of cache validity
 * The check is performed in separate thread
 * So applied changes in dynamic attributes structure will be visible after 10 seconds
 *
 */
@Component(DynamicAttributesCacheStrategy.NAME)
public class DynamicAttributesCacheStrategy implements CachingStrategy {
    public static final String NAME = "cuba_DynamicAttributesCacheStrategy";

    @Inject
    protected ClientCacheManager clientCacheManager;
    @Inject
    protected LoginService loginService;
    @Inject
    protected Configuration configuration;
    @Inject
    protected UserSessionSource userSessionSource;

    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected DynamicAttributesCache dynamicAttributesCache;

    //there is no need for atomicity when change needToValidateCache or lastRequestedSessionId
    protected volatile boolean needToValidateCache;
    protected volatile AtomicReference<UUID> lastRequestedSessionId = new AtomicReference<>();

    protected Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init() {
        clientCacheManager.getExecutorService().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                UUID lastSessionId = lastRequestedSessionId.get();
                if (needToValidateCache && lastSessionId != null) {
                    try {
                        AppContext.setSecurityContext(new SecurityContext(lastSessionId));
                        if (userSessionSource.checkCurrentUserSession()) {
                            loadObject();
                        } else {
                            lastRequestedSessionId.compareAndSet(lastSessionId, null);
                        }
                    } catch (NoUserSessionException e) {//in case of session death between check and service call
                        lastRequestedSessionId.compareAndSet(lastSessionId, null);
                    } catch (Exception e) {
                        log.error("Unable to update dynamic attributes cache", e);
                    }
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public Object getObject() {
        needToValidateCache = true;
        SecurityContext securityContext = AppContext.getSecurityContext();
        if (securityContext != null) {
            lastRequestedSessionId.set(securityContext.getSessionId());
        }
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
