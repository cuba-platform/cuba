/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 17:14:13
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.HashMap;

public class ConfigProviderImpl extends ConfigProvider
{
    private Map<Class, ConfigHandler> cache = new HashMap<Class, ConfigHandler>();

    protected synchronized <T extends Config> T __getConfig(Class<T> configInterface) {
        ConfigHandler handler = cache.get(configInterface);
        if (handler == null) {
            ConfigPersisterImpl persister = new ConfigPersisterImpl();
            handler = new ConfigHandler(persister, configInterface);
            cache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}
