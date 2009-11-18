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

import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import groovy.lang.Binding;

public abstract class ScriptingProvider {

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

    public static GroovyScriptEngine getGroovyScriptEngine() {
        return getInstance().__getGroovyScriptEngine();
    }

    public static void runGroovyScript(String name, Binding binding) {
        getInstance().__runGroovyScript(name, binding);
    }

    protected abstract GroovyScriptEngine __getGroovyScriptEngine();

    protected void __runGroovyScript(String name, Binding binding) {
        try {
            __getGroovyScriptEngine().run(name, binding);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }
}
