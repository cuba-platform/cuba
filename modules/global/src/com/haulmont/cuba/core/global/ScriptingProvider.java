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

import com.haulmont.cuba.core.sys.AppContext;
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

    private static ScriptingProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_ScriptingProvider", ScriptingProvider.class);
    }

    public static void addGroovyClassPath(String path) {
        getInstance().doAddGroovyClassPath(path);
    }

    public static void addGroovyEvaluatorImport(Layer layer, String className) {
        getInstance().doAddGroovyEvaluatorImport(layer, className);
    }

    public static <T> T evaluateGroovy(Layer layer, String text, Binding binding) {
        return (T) getInstance().doEvaluateGroovy(layer, text, binding);
    }

    public static <T> T evaluateGroovy(Layer layer, String text, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) evaluateGroovy(layer, text, binding);
    }

    public static <T> T runGroovyScript(String name, Binding binding) {
        return (T) getInstance().doRunGroovyScript(name, binding);
    }

    public static <T> T runGroovyScript(String name,  Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) getInstance().doRunGroovyScript(name, binding);
    }

    public static Class loadClass(String name) {
        return getInstance().doLoadClass(name);
    }

    public static InputStream getResourceAsStream(String name) {
        return getInstance().doGetResourceAsStream(name);
    }

    public static ClassLoader getGroovyClassLoader() {
        return getInstance().doGetGroovyClassLoader();
    }

    public static void clearCache() {
        getInstance().doGetGroovyClassLoader().clearCache();
    }

    public <T> T doRunGroovyScript(String name, Binding binding) {
        try {
            return (T) doGetGroovyScriptEngine().run(name, binding);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public Class doLoadClass(String name) {
        try {
            return doGetGroovyClassLoader().loadClass(name, true, false);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public InputStream doGetResourceAsStream(String name) {
        String s = name.startsWith("/") ? name.substring(1) : name;
        return doGetGroovyClassLoader().getResourceAsStream(s);
    }

    protected static Binding createBinding(Map<String, Object> map) {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }

        return binding;
    }

    public abstract void doAddGroovyClassPath(String path);

    public abstract void doAddGroovyEvaluatorImport(Layer layer, String str);

    public abstract <T> T doEvaluateGroovy(Layer layer, String text, Binding binding);

    public abstract GroovyScriptEngine doGetGroovyScriptEngine();

    public abstract GroovyClassLoader doGetGroovyClassLoader();
}
