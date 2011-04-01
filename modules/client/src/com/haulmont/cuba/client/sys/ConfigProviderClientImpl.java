/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.03.11 17:18
 *
 * $Id$
 */
package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.ConfigProvider;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ConfigProviderClientImpl extends ConfigProvider {

    private Map<Class, ConfigHandler> cache = new HashMap<Class, ConfigHandler>();

    @Override
    public <T extends Config> T doGetConfig(Class<T> configInterface) {
        ConfigHandler handler = cache.get(configInterface);
        if (handler == null) {
            ConfigPersisterClientImpl persister = new ConfigPersisterClientImpl();
            handler = new ConfigHandler(persister, configInterface);
            cache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}
