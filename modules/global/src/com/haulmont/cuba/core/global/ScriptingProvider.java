/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.11.2009 9:50:15
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;

import java.io.InputStream;
import java.util.Map;

public abstract class ScriptingProvider {

    public enum Layer {
        CORE,
        GUI
    }

    public static final String IMPL_PROP = "cuba.ScriptingProvider.impl";

    private static final String DEFAULT_IMPL = "com.haulmont.cuba.core.sys.ScriptingProviderImpl";

    private static ScriptingProvider instance;

    private static ScriptingProvider getInstance() {
        if (instance == null) {
            String implClassName = System.getProperty(IMPL_PROP);
            if (implClassName == null)
                implClassName = DEFAULT_IMPL;
            try {
                Class implClass = Thread.currentThread().getContextClassLoader().loadClass(implClassName);
                instance = (ScriptingProvider) implClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public static void addGroovyClassPath(String path) {
        getInstance().__addGroovyClassPath(path);
    }

    public static void addGroovyEvaluatorImport(Layer layer, String className) {
        getInstance().__addGroovyEvaluatorImport(layer, className);
    }

    public static <T> T evaluateGroovy(Layer layer, String text, Binding binding) {
        return (T) getInstance().__evaluateGroovy(layer, text, binding);
    }

    public static <T> T evaluateGroovy(Layer layer, String text, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) evaluateGroovy(layer, text, binding);
    }

    public static <T> T runGroovyScript(String name, Binding binding) {
        return (T) getInstance().__runGroovyScript(name, binding);
    }

    public static Class loadClass(String name) {
        return getInstance().__loadClass(name);
    }

    public static InputStream getResourceAsStream(String name) {
        return getInstance().__getResourceAsStream(name);
    }

    public static ClassLoader getGroovyClassLoader() {
        return getInstance().__getGroovyClassLoader();
    }

    public static void clearCache() {
        getInstance().__getGroovyClassLoader().clearCache();
    }

    protected <T> T __runGroovyScript(String name, Binding binding) {
        try {
            return (T) __getGroovyScriptEngine().run(name, binding);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    private Class __loadClass(String name) {
        try {
            return __getGroovyClassLoader().loadClass(name, true, false);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private InputStream __getResourceAsStream(String name) {
        return __getGroovyClassLoader().getResourceAsStream(name);
    }

    protected static Binding createBinding(Map<String, Object> map) {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }

        return binding;
    }

    protected abstract void __addGroovyClassPath(String path);

    protected abstract void __addGroovyEvaluatorImport(Layer layer, String str);

    protected abstract <T> T __evaluateGroovy(Layer layer, String text, Binding binding);

    protected abstract GroovyScriptEngine __getGroovyScriptEngine();

    protected abstract GroovyClassLoader __getGroovyClassLoader();
}
