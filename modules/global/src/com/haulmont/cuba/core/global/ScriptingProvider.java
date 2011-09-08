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

    public static <T> T evaluateGroovy(Scripting.Layer layer, String text, Binding binding) {
        return (T) getScripting().evaluateGroovy(layer, text, binding);
    }

    public static <T> T evaluateGroovy(Scripting.Layer layer, String text, Map<String, Object> context) {
        return (T) getScripting().evaluateGroovy(layer, text, context);
    }

    public static <T> T runGroovyScript(String name, Binding binding) {
        return (T) getScripting().runGroovyScript(name, binding);
    }

    public static <T> T runGroovyScript(String name, Map<String, Object> context) {
        return (T) getScripting().runGroovyScript(name, context);
    }

    public static Class loadClass(String name) {
        return getScripting().loadClass(name);
    }

    @Nullable
    public static InputStream getResourceAsStream(String name) {
        return getScripting().getResourceAsStream(name);
    }

    @Nullable
    public static String getResourceAsString(String name) {
        return getScripting().getResourceAsString(name);
    }
}
