/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides access to all managed beans of the application.
 *
 * @author krivopustov
 * @version $Id$
 */
public class AppBeans {

    private static Map<Class, Optional<String>> names = new ConcurrentHashMap<>();

    /**
     * Return the bean instance that matches the given object type.
     * If the provided bean class contains a public static field <code>NAME</code>, this name is used to look up the
     * bean to improve performance.
     * @param beanType type the bean must match; can be an interface or superclass. {@code null} is disallowed.
     * @return an instance of the single bean matching the required type
     */
    public static <T> T get(Class<T> beanType) {
        String name = null;
        Optional<String> optName = names.get(beanType);
        if (optName == null) {
            // Try to find a bean name defined in its NAME static field
            try {
                Field nameField = beanType.getField("NAME");
                name = (String) nameField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException ignore) {
            }
            names.put(beanType, Optional.ofNullable(name));
        } else {
            name = optName.orElse(null);
        }
        // If the name is found, look up the bean by name because it is much faster
        if (name == null)
            return AppContext.getApplicationContext().getBean(beanType);
        else
            return AppContext.getApplicationContext().getBean(name, beanType);
    }

    /**
     * Return an instance of the specified bean.
     * @param name  the name of the bean to retrieve
     * @return      bean instance
     * @see         org.springframework.beans.factory.BeanFactory#getBean(java.lang.String)
     */
    public static <T> T get(String name) {
        return (T) AppContext.getApplicationContext().getBean(name);
    }

    /**
     * Return an instance of the specified bean.
     * @param name      the name of the bean to retrieve
     * @param beanType  type the bean must match. Can be an interface or superclass of the actual class, or null
     * for any match. For example, if the value is Object.class, this method will succeed whatever the class of the
     * returned instance.
     * @return          bean instance
     */
    public static <T> T get(String name, @Nullable Class<T> beanType) {
        return AppContext.getApplicationContext().getBean(name, beanType);
    }

    /**
     * Return an instance of prototype bean, specifying explicit constructor arguments.
     * @param name  the name of the bean to retrieve
     * @param args  constructor arguments
     * @return      bean instance
     */
    public static <T> T getPrototype(String name, Object... args) {
        return (T) AppContext.getApplicationContext().getBean(name, args);
    }

    /**
     * Return the bean instances that match the given object type (including
     * subclasses).
     * <p>The Map returned by this method should always return bean names and
     * corresponding bean instances <i>in the order of definition</i> in the
     * backend configuration, as far as possible.
     * @param beanType the class or interface to match, or <code>null</code> for all concrete beans
     * @return a Map with the matching beans, containing the bean names as
     * keys and the corresponding bean instances as values
     */
    public static <T> Map<String, T> getAll(Class<T> beanType) {
        return AppContext.getApplicationContext().getBeansOfType(beanType);
    }

    /**
     * @return whether a bean with the given name is present
     */
    public static boolean containsBean(String name) {
        return AppContext.getApplicationContext().containsBean(name);
    }
}
