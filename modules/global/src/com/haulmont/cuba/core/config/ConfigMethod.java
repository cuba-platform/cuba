/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.config;

import java.lang.reflect.Method;

import org.apache.commons.collections.map.MultiKeyMap;

/**
 * Superclass for all configuration method implementations.
 *
 */
public abstract class ConfigMethod {
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
    public interface Factory
    {
        /**
         * Test whether this factory can handle a particular configuration interface
         * method.
         *
         * @param method The method.
         * @return Whether this factory can handle the method.
         */
        boolean canHandle(Method method);


        /**
         * Create a new configuration method implementation to handle a
         * configuration interface method.
         *
         * @param configInterface The configuration interface.
         * @param method          The method.
         * @return The method implementation.
         */
        ConfigMethod newInstance(Class<?> configInterface, Method method);
    }
}