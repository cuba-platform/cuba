/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AppContext;
import groovy.lang.Binding;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Map;

/**
 * Utility class to provide scripting functionality in static context.<br>
 * <p>Injected {@link Scripting} interface should be used instead of this class wherever possible.</p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class ScriptingProvider {

    private static Scripting getScripting() {
        return AppContext.getBean(Scripting.NAME, Scripting.class);
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
     * See {@link com.haulmont.cuba.core.global.Scripting#getResourceAsStream(String)}
     */
    @Nullable
    public static InputStream getResourceAsStream(String name) {
        return getScripting().getResourceAsStream(name);
    }

    /**
     * See {@link com.haulmont.cuba.core.global.Scripting#getResourceAsString(String)}
     */
    @Nullable
    public static String getResourceAsString(String name) {
        return getScripting().getResourceAsString(name);
    }
}
