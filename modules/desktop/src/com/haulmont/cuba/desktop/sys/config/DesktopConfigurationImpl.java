/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.config;

import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Desktop specific implementation of Configuration that uses application scope cache for db properties.
 *
 * @see com.haulmont.cuba.desktop.sys.config.DesktopConfigStorageCache
 *
 * @author artamonov
 * @version $Id$
 */
public class DesktopConfigurationImpl implements ClientConfiguration {

    protected Map<Class, ConfigHandler> handlersCache = new ConcurrentHashMap<>();

    protected DesktopConfigStorageCache configStorageCache = new DesktopConfigStorageCache();

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = handlersCache.get(configInterface);
        if (handler == null) {
            handler = new ConfigHandler(new DesktopConfigPersisterImpl(configStorageCache, false), configInterface);
            handlersCache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }

    @Override
    public <T extends Config> T getConfigCached(Class<T> configInterface) {
        ConfigHandler handler = new ConfigHandler(new DesktopConfigPersisterImpl(configStorageCache, true), configInterface);
        Object proxy = Proxy.newProxyInstance(configInterface.getClassLoader(), new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}