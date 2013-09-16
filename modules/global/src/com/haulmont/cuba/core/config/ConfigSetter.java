/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.type.TypeStringify;

import java.lang.reflect.Method;

/**
 * Implementation of configuration setter methods.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class ConfigSetter extends ConfigAccessorMethod
{
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
