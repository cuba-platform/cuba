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

import com.haulmont.cuba.core.config.type.TypeStringify;

import java.lang.reflect.Method;

/**
 * Implementation of configuration setter methods.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
public class ConfigSetter extends ConfigAccessorMethod {

    private final SourceType sourceType;

    private TypeStringify stringifier;

    /**
     * Create a new ConfigSetter instance.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     */
    public ConfigSetter(Class<?> configInterface, Method method) {
        super(configInterface, method);
        sourceType = ConfigUtil.getSourceType(configInterface, method);
        if (!String.class.equals(ConfigUtil.getMethodType(method)))
            stringifier = TypeStringify.getInstance(configInterface, method);
    }

    /**
     * {@inheritDoc}
     * This implementation invokes {@link #setProperty} with the
     * filed value.
     */
    @Override
    public Object invoke(ConfigHandler handler, Object[] args) {
        setProperty(handler.getPersister(), args[0]);
        return null;
    }

    /**
     * Set a configuration field value.
     *
     * @param persister The configuration source.
     * @param value         The field value.
     */
    public void setProperty(ConfigPersister persister, Object value) {
        String str;
        if (stringifier == null || value == null)
            str = (String) value;
        else
            str = stringifier.stringify(value);
        persister.setProperty(sourceType, getPropertyName(), str);
    }

    /**
     * The ConfigSetter factory.
     */
    public static final Factory FACTORY = new Factory()
    {
        /**
         * {@inheritDoc}
         * The method has the name set*, has a void return type and
         * one parameter.
         */
        public boolean canHandle(Method method) {
            String methodName = method.getName();
            Class returnType = method.getReturnType();
            Class[] parameterTypes = method.getParameterTypes();
            return ConfigUtil.SET_RE.matcher(methodName).matches() &&
                    Void.TYPE.equals(returnType) &&
                    (parameterTypes.length == 1);
        }

        /* Inherited. */
        public ConfigMethod newInstance(Class<?> configInterface, Method method) {
            return new ConfigSetter(configInterface, method);
        }
    };
}