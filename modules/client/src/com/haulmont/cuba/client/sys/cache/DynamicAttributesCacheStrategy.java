/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Each time dynamic attributes cache is used, the strategy initiate check of cache validity
 * The check is performed in separate thread
 * So applied changes in dynamic attributes structure will be visible after 10 seconds
 *
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean(DynamicAttributesCacheStrategy.NAME)
public class DynamicAttributesCacheStrategy implements CachingStrategy {
    public static final String NAME = "cuba_DynamicAttributesCacheStrategy";

    @Inject
    protected ClientCacheManager clientCacheManager;
    @Inject
    protected LoginService loginService;
    @Inject
    protected Configuration configuration;

    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected DynamicAttributesCache dynamicAttributesCache;

    //there is no need for atomicity when change needToValidateCache or lastRequestedSessionId
    protected volatile boolean needToValidateCache;
    protected volatile UUID lastRequestedSessionId;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public void init() {
        clientCacheManager.getExecutorService().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (needToValidateCache && lastRequestedSessionId != null) {
                    try{
                        AppContext.setSecurityContext(new SecurityContext(lastRequestedSessionId));
                        loadObject();
                    } catch (NoUserSessionException e) {
                        //do nothing
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
        if (AppContext.getSecurityContext() != null) {
            lastRequestedSessionId = AppContext.getSecurityContext().getSessionId();
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
