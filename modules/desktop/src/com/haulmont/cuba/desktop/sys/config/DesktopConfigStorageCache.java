/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.config;

import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.global.AppBeans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopConfigStorageCache implements ConfigStorageService {

    protected static final int SEQUENTIAL_INVALIDATE_THRESHOLD = 10000; // 10 seconds

    protected volatile long lastInvalidateTs = 0;
    protected final Map<String, String> properties = new ConcurrentHashMap<>();

    @Override
    public Map<String, String> getDbProperties() {
        synchronized (properties) {
            invalidateIfNeeded();
        }
        return properties;
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