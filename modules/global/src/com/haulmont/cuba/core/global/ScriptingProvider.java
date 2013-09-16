/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import groovy.lang.Binding;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Map;

/**
 * DEPRECATED - use {@link Scripting} via DI or <code>AppBeans.get(Scripting.class)</code>
 *
 * @author krivopustov
 * @version $Id$
 */
@Deprecated
public abstract class ScriptingProvider {

    private static Scripting getScripting() {
        return AppBeans.get(Scripting.NAME, Scripting.class);
    }

    /**
     * See {@link Scripting#evaluateGroovy(String, groovy.lang.Binding)}
     */
    public static <T> T evaluateGroovy(String text, Binding binding) {
        return (T) getScripting().evaluateGroovy(text, binding);
    }

    /**
     * See {@link Scripting#evaluateGroovy(String, java.util.Map)}
     */
    public static <T> T evaluateGroovy(String text, Map<String, Object> context) {
        return (T) getScripting().evaluateGroovy(text, context);
    }

    /**
     * See {@link Scripting#runGroovyScript(String, groovy.lang.Binding)}
     */
    public static <T> T runGroovyScript(String name, Binding binding) {
        return (T) getScripting().runGroovyScript(name, binding);
    }

    /**
     * See {@link Scripting#runGroovyScript(String, java.util.Map)}
     */
    public static <T> T runGroovyScript(String name, Map<String, Object> context) {
        return (T) getScripting().runGroovyScript(name, context);
    }

    /**
     * See {@link com.haulmont.cuba.core.global.Scripting#getClassLoader()}
     */
    public static ClassLoader getClassLoader() {
        return getScripting().getClassLoader();
    }

    /**
     * See {@link com.haulmont.cuba.core.global.Scripting#loadClass(String)}
     */
    @Nullable
    public static Class loadClass(String name) {
        return getScripting().loadClass(name);
    }

    /**
     * DEPRECATED - use {@link Resources#getResourceAsStream(String)}
     */
    @Deprecated
    @Nullable
    public static InputStream getResourceAsStream(String name) {
        return getScripting().getResourceAsStream(name);
    }

    /**
     * DEPRECATED - use {@link Resources#getResourceAsStream(String)}
     */
    @Deprecated
    @Nullable
    public static String getResourceAsString(String name) {
        return getScripting().getResourceAsString(name);
    }
}
