/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 *
 * @author artamonov
 * @version $Id$
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