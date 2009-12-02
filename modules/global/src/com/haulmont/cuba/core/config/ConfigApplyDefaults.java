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
 * $Id$
 */
package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.type.TypeStringify;

import java.lang.reflect.Method;

/**
 * Implementation of configuration defaults application method.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class ConfigApplyDefaults extends ConfigMethod
{
    /**
     * The configuration interface.
     */
    private final Class<?> configInterface;

    private final SourceType sourceType;

    /**
     * Create a new ConfigApplyDefaults instance.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     */
    public ConfigApplyDefaults(Class<?> configInterface, Method method) {
        this.configInterface = configInterface;
        sourceType = ConfigUtil.getSourceType(configInterface, method);
    }

    /**
     * {@inheritDoc}
     * This implementation invokes {@link #applyDefaults}.
     */
    public Object invoke(ConfigHandler handler, Object[] args) {
        applyDefaults(handler.getPersister());
        return null;
    }

    /**
     * Apply the defaults for all configuration fields that have a default.
     *
     * @param persister The configuration source.
     */
    public void applyDefaults(ConfigPersister persister) {
        // Set on the config directly rather than invoke a setter in case there
        // is no setter.
        for (Method method : configInterface.getMethods()) {
            String methodName = method.getName();
            if (ConfigUtil.GET_RE.matcher(methodName).matches() && // is a getter
                    (method.getParameterTypes().length == 0))
            {
                Object defaultValue = ConfigUtil.getDefaultValue(configInterface, method);
                if (defaultValue != ConfigUtil.NO_DEFAULT) { // is defaulted
                    String str;
                    if (String.class.equals(defaultValue)) {
                        str = (String) defaultValue;
                    }
                    else {
                        TypeStringify stringifier = TypeStringify.getInstance(configInterface, method);
                        str = stringifier.stringify(defaultValue);
                    }
                    String propertyName = ConfigUtil.getPropertyName(configInterface, method);
                    persister.setProperty(sourceType, propertyName, str);
                }
            }
        }
    }

    /**
     * The ConfigApplyDefaults factory.
     */
    public static final Factory FACTORY = new Factory()
    {
        /**
         * {@inheritDoc}
         * The method has the name applyDefaults, void return type
         * and no parameters.
         */
        public boolean canHandle(Method method) {
            String methodName = method.getName();
            Class returnType = method.getReturnType();
            Class[] parameterTypes = method.getParameterTypes();
            return "applyDefaults".equals(methodName) &&
                    Void.TYPE.equals(returnType) &&
                    (parameterTypes.length == 0);
        }

        /* Inherited. */
        public ConfigMethod newInstance(Class<?> configInterface, Method method) {
            return new ConfigApplyDefaults(configInterface, method);
        }
    };
}
