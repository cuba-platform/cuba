/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 17:10:33
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;

/**
 * Entry point to configuration parameters functionality.<br>
 * Use static methods. 
 */
public abstract class ConfigProvider
{
    public static final String IMPL_PROP = "cuba.ConfigProvider.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.ConfigProviderImpl";

    private static ConfigProvider instance;

    private static ConfigProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (ConfigProvider) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    /**
     * Get reference to a configuration interface implementation.
     * @param configInterface   class of configuration interface
     * @return  the interface implementation which can be used to get/set parameters
     */
    public static <T extends Config> T getConfig(Class<T> configInterface) {
        return getInstance().__getConfig(configInterface);
    }

    protected abstract <T extends Config> T __getConfig(Class<T> configInterface);
}
