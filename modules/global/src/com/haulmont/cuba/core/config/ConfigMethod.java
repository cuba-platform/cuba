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
 *
 */

package com.haulmont.cuba.core.config;

import java.lang.reflect.Method;

/**
 * Superclass for all configuration method implementations.
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
     * @param proxy   The dynamic proxy created for the configuration interface.
     * @return The method result.
     */
    public abstract Object invoke(ConfigHandler handler, Object[] args, Object proxy);

    /**
     * Interface describing a configuration method factory.
     */
    public interface Factory {
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