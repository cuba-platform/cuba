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

import java.lang.reflect.Method;

/**
 * Superclass for all field accessor method implementations.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
public abstract class ConfigAccessorMethod extends ConfigMethod
{
    /**
     * The property name.
     */
    private final String property;

    /**
     * Create a new ConfigAccessorMethod instance.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     */
    protected ConfigAccessorMethod(Class<?> configInterface, Method method) {
        property = ConfigUtil.getPropertyName(configInterface, method);
    }

    /**
     * Get the name of the configuration property associated with this
     * accessor method.
     *
     * @return The property name.
     */
    public String getPropertyName() {
        return property;
    }
}
