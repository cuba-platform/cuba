/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractConfiguration implements Configuration {

    protected Map<Class, ConfigHandler> cache = new HashMap<Class, ConfigHandler>();

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = cache.get(configInterface);
        if (handler == null) {
            handler = getConfigHandler(configInterface);
            cache.put(configInterface, handler);
        }
        ClassLoader classLoader = configInterface.getClassLoader();
        Object proxy = Proxy.newProxyInstance(classLoader, new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }

    protected abstract ConfigHandler getConfigHandler(Class configInterface);
}
