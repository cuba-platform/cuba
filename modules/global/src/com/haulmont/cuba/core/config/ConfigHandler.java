/*
 * A High-Level Framework for Application Configuration
 *
 * Copyright 2007 Merlin Hughes / Learning Objects, Inc.
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
 */

package com.haulmont.cuba.core.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Configuration interface method handler.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
public class ConfigHandler implements InvocationHandler {
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
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ConfigMethod configMethod = ConfigMethod.getInstance(configInterface, method);
        return configMethod.invoke(this, args);
  }
}