/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.cuba.client.sys.config.ConfigPersisterClientImpl;
import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;

import javax.annotation.ManagedBean;
import java.lang.reflect.Proxy;

/**
 * Client-side implementation of the {@link Configuration} interface.
 * <p>
 * A config implementation instance obtained through this interface caches DB-stored parameter values to minimize
 * server invocations. There is a side effect of this behaviour: if you get a config instance, all subsequent
 * invocations of some method will return the same value, even if the real value of the parameter has been changed.
 * You can see the new value only when you get another instance of the config interface.
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
@ManagedBean(Configuration.NAME)
public class ConfigurationClientImpl implements Configuration {

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = new ConfigHandler(new ConfigPersisterClientImpl(), configInterface);
        Object proxy = Proxy.newProxyInstance(configInterface.getClassLoader(), new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}
