/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.config;

import java.lang.reflect.Method;

/**
 * Superclass for all field accessor method implementations.
 *
 * @author Merlin Hughes
 * @version 0.1, 2007/04/15
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
