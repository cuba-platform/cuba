/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config;

import java.lang.reflect.Method;

import org.apache.commons.collections.map.MultiKeyMap;

/**
 * Superclass for all configuration method implementations.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public abstract class ConfigMethod
{
    /**
     * Create a new ConfigMethod instance.
     */
    protected ConfigMethod() {
    }

    /**
     * Handle a configuration interface method invocation.
     *
     * @param handler The handler.
     * @param args    The method arguments.
     * @return The method result.
     */
    public abstract Object invoke(ConfigHandler handler, Object[] args);

    /**
     * A cache of instantiated configuration method implementations.
     */
    private static final MultiKeyMap configMethods = new MultiKeyMap();

    /**
     * Get a ConfigMethod implementation appropriate for handling a
     * configuration interface method. Internally, this uses a cache so
     * that it can operate very efficiently.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return The method implementation.
     */
    public static synchronized ConfigMethod getInstance(Class<?> configInterface, Method method) {
        ConfigMethod configMethod = (ConfigMethod) configMethods.get(configInterface, method);
        if (configMethod == null) {
            configMethod = newInstance(configInterface, method);
            configMethods.put(configInterface, method, configMethod);
        }
        return configMethod;
    }

    /**
     * Create a new ConfigMethod implementation appropriate for handling a
     * configuration interface method.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return The method implementation.
     */
    private static ConfigMethod newInstance(Class<?> configInterface, Method method) {
        for (Factory factory : CONFIG_METHOD_FACTORIES) {
            if (factory.canHandle(method)) {
                return factory.newInstance(configInterface, method);
            }
        }
        throw new RuntimeException("Invalid config interface method: " + method);
    }

    /**
     * Supported configuration method factories.
     */
    private static final Factory[] CONFIG_METHOD_FACTORIES = {
            ConfigGetter.FACTORY, ConfigSetter.FACTORY
    };

    /**
     * Interface describing a configuration method factory.
     */
    public static interface Factory
    {
        /**
         * Test whether this factory can handle a particular configuration interface
         * method.
         *
         * @param method The method.
         * @return Whether this factory can handle the method.
         */
        public boolean canHandle(Method method);


        /**
         * Create a new configuration method implementation to handle a
         * configuration interface method.
         *
         * @param configInterface The configuration interface.
         * @param method          The method.
         * @return The method implementation.
         */
        public ConfigMethod newInstance(Class<?> configInterface, Method method);
    }
}
