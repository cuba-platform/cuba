/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Configuration interface method handler.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class ConfigHandler implements InvocationHandler
{
    /**
     * The configuration source.
     */
    private final ConfigPersister persister;
    /**
     * The configuration interface.
     */
    private final Class<?> configInterface;

    /**
     * Create a new ConfigHandler instance.
     *
     * @param persister       The configuration source.
     * @param configInterface The configuration interface.
     */
    public ConfigHandler(ConfigPersister persister, Class<?> configInterface) {
        this.persister = persister;
        this.configInterface = configInterface;
    }

    /**
     * Get the configuration source.
     *
     * @return The configuration source.
     */
    public ConfigPersister getPersister() {
        return persister;
    }

    /**
     * Get the configuration interface.
     *
     * @return The configuration interface.
     */
    public Class<?> getConfigInterface() {
        return configInterface;
    }

    /**
     * Handle a configuration interface method invocation.
     *
     * @param proxy  The proxy implementation.
     * @param method The method being invoked.
     * @param args   The method arguments.
     * @return The method result.
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ConfigMethod configMethod = ConfigMethod.getInstance(configInterface, method);
        return configMethod.invoke(this, args);
  }
}
