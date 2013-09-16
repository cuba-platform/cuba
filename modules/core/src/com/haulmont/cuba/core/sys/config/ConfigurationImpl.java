/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.sys.ConfigPersisterImpl;

import javax.annotation.ManagedBean;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Server-side implementation of the {@link Configuration} interface.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Configuration.NAME)
public class ConfigurationImpl implements Configuration {

    protected Map<Class, ConfigHandler> cache = new ConcurrentHashMap<>();

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = cache.get(configInterface);
        if (handler == null) {
            handler = new ConfigHandler(new ConfigPersisterImpl(), configInterface);
            cache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}
