/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

import com.haulmont.cuba.core.config.type.TypeFactory;

import java.lang.reflect.Method;
import java.util.NoSuchElementException;

/**
 * Implementation of configuration getter methods.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
 */
public class ConfigGetter extends ConfigAccessorMethod
{
    /**
     * The default value.
     */
    private final String defaultValue;

    private final SourceType sourceType;

    private TypeFactory factory;

    /**
     * Create a new ConfigGetter instance.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     */
    public ConfigGetter(Class<?> configInterface, Method method) {
        super(configInterface, method);
        // not needed for defaulted method, but no harm.
        defaultValue = ConfigUtil.getDefaultValue(configInterface, method);
        sourceType = ConfigUtil.getSourceType(configInterface, method);
//        if (!String.class.equals(method.getReturnType()))
            factory = TypeFactory.getInstance(configInterface, method);
    }

    /**
     * {@inheritDoc}
     * This implementation invokes either {@link #getProperty(ConfigPersister)} or
     * {@link #getProperty(ConfigPersister, String)} depending on whether a
     * run-time default value was specified.
     */
    public Object invoke(ConfigHandler handler, Object[] args) {
        ConfigPersister configuration = handler.getPersister();
        String str;
        if ((args == null) || (args.length == 0)) {
            str = getProperty(configuration);
        }
        else {
            String defValue = null;
            if (!(args[0] instanceof String)) {
                Class<?> t = args[0].getClass();
                if (t.isEnum())
                    defValue = ((Enum) args[0]).name();
                else if (Class.class.equals(t))
                    defValue = ((Class) args[0]).getName();
                else
                    defValue = args[0].toString();
            }
            str = getProperty(configuration, defValue);
        }
        if (factory == null)
            return str;
        else
            return factory.build(str);
    }

    /**
     * Get a configuration field value.
     *
     * @param configuration The configuration source.
     * @return The field value.
     */
    public String getProperty(ConfigPersister configuration) {
        String value = getProperty(configuration, defaultValue);
        return (value == ConfigUtil.NO_DEFAULT) ? null : value;
    }

    /**
     * Get a configuration field value with a run-time default.
     *
     * @param persister The configuration source.
     * @param defaultValue  The default value to use if a value is
     *                      not specified by the configuration source.
     * @return The field value.
     */
    public String getProperty(ConfigPersister persister, String defaultValue) {
        try {
            String value = persister.getProperty(sourceType, getPropertyName());
            return value != null ? value : defaultValue;
        } catch (NoSuchElementException ex) { // primitives
            if (defaultValue == ConfigUtil.NO_DEFAULT) {
                throw ex;
            }
            return defaultValue;
        }
    }

    /**
     * The ConfigGetter factory.
     */
    public static final Factory FACTORY = new Factory()
    {
        /**
         * {@inheritDoc}
         * The method has the name get* or is* (if the return type is
         * boolean), has a non-void return type and either no parameters
         * or one parameter with the same type as the return value.
         */
        public boolean canHandle(Method method) {
            String methodName = method.getName();
            Class returnType = method.getReturnType();
            Class[] parameterTypes = method.getParameterTypes();
            return ConfigUtil.GET_RE.matcher(methodName).matches() &&
                    !Void.TYPE.equals(returnType) &&
                    (Boolean.TYPE.equals(returnType) || methodName.startsWith("get")) &&
                    ((parameterTypes.length == 0) ||
                            ((parameterTypes.length == 1) && parameterTypes[0].equals(returnType)));
        }

        /* Inherited. */
        public ConfigMethod newInstance(Class<?> configInterface, Method configMethod) {
            return new ConfigGetter(configInterface, configMethod);
        }
    };
}
