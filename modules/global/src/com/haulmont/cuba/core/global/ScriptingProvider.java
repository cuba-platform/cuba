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
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class ScriptingProvider {

    protected String confPath;

    public ScriptingProvider(ConfigProvider configProvider) {
        confPath = configProvider.doGetConfig(GlobalConfig.class).getConfDir() + File.pathSeparator;
    }

    public enum Layer {
        CORE,
        GUI
    }

    private static ScriptingProvider getInstance() {
        return AppContext.getApplicationContext().getBean("cuba_ScriptingProvider", ScriptingProvider.class);
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

    public static <T> T runGroovyScript(String name, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) getInstance().doRunGroovyScript(name, binding);
    }

    public static Class loadClass(String name) {
        return getInstance().doLoadClass(name);
    }

    @Nullable
    public static InputStream getResourceAsStream(String name) {
        return getInstance().doGetResourceAsStream(name);
    }

    @Nullable
    public static String getResourceAsString(String name) {
        return getInstance().doGetResourceAsString(name);
    }

    public static ClassLoader getGroovyClassLoader() {
        return getInstance().doGetGroovyClassLoader();
    }

    public static void clearCache() {
        getInstance().doGetGroovyClassLoader().clearCache();
        JavaClassLoader.getInstance().clearCache();
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
            File file = new File(confPath, name.replace(".", "/") + ".java");
            if (file.exists())
                return JavaClassLoader.getInstance().loadClass(name);
            else
                return doGetGroovyClassLoader().loadClass(name, true, false);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Nullable
    public InputStream doGetResourceAsStream(String name) {
        String s = name.startsWith("/") ? name.substring(1) : name;
        File file = new File(confPath, s);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return doGetGroovyClassLoader().getResourceAsStream(s);
        }
    }

    @Nullable
    public String doGetResourceAsString(String name) {
        InputStream stream = doGetResourceAsStream(name);
        if (stream == null)
            return null;

        try {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                //
            }
        }
    }

    protected static Binding createBinding(Map<String, Object> map) {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }

        return binding;
    }

    public abstract <T> T doEvaluateGroovy(Layer layer, String text, Binding binding);

    public abstract GroovyScriptEngine doGetGroovyScriptEngine();

    public abstract GroovyClassLoader doGetGroovyClassLoader();
}
