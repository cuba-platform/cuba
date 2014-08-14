/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.config;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.ConfigHandler;
import com.haulmont.cuba.core.global.Configuration;

import java.lang.reflect.Proxy;

/**
 * @author artamonov
 * @version $Id$
 */
public class DesktopConfigurationImpl implements Configuration {

    protected DesktopConfigStorageCache configStorageCache = new DesktopConfigStorageCache();

    @Override
    public <T extends Config> T getConfig(Class<T> configInterface) {
        ConfigHandler handler = new ConfigHandler(new DesktopConfigPersisterImpl(configStorageCache), configInterface);
        Object proxy = Proxy.newProxyInstance(configInterface.getClassLoader(), new Class[]{configInterface}, handler);
        return configInterface.cast(proxy);
    }
}