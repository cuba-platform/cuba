/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.client.ClientConfiguration;
import com.haulmont.cuba.client.sys.config.ConfigPersisterClientImpl;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;

import javax.annotation.ManagedBean;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean(Configuration.NAME)
public class ConfigurationClientImpl implements ClientConfiguration {

    protected Map<Class, ConfigHandler> handlersCache = new ConcurrentHashMap<>();

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = handlersCache.get(configInterface);
        if (handler == null) {
            handler = new ConfigHandler(new ConfigPersisterClientImpl(false), configInterface);
            handlersCache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }

    @Override
    public <T extends Config> T getConfigCached(Class<T> configInterface) {
        ConfigHandler handler = new ConfigHandler(new ConfigPersisterClientImpl(true), configInterface);
        Object proxy = Proxy.newProxyInstance(configInterface.getClassLoader(), new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}