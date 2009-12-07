/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.11.2009 10:20:32
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.ScriptingProvider;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;

public class ScriptingProviderImpl extends ScriptingProvider {

    private Log log = LogFactory.getLog(ScriptingProviderImpl.class);

    private String groovyClassPath = "";

    private Map<Layer, Set<String>> imports = new HashMap<Layer, Set<String>>();

    private GroovyScriptEngine gse;

    private GroovyClassLoader gcl;

    private Map<Layer, GenericKeyedObjectPool> pools = new HashMap<Layer, GenericKeyedObjectPool>();

    public ScriptingProviderImpl() {
        final String rootPath = ConfigProvider.getConfig(ServerConfig.class).getServerConfDir() + "/";

        gse = new GroovyScriptEngine(new ResourceConnector() {
            public URLConnection getResourceConnection(String resourceName) throws ResourceException {
                try {
                    final URL resource = getClass().getResource(resourceName);
                    if (resource != null) return resource.openConnection();

                    final URL fileURL = new File(rootPath + resourceName).toURI().toURL();
                    return fileURL.openConnection();
                } catch (IOException e) {
                    throw new ResourceException(e);
                }
            }
        });

        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setClasspath(groovyClassPath);
        cc.setRecompileGroovySource(true);
        gcl = new GroovyClassLoader(Thread.currentThread().getContextClassLoader(), cc);
    }

    protected void __addGroovyClassPath(String path) {
        groovyClassPath = groovyClassPath + File.pathSeparator + path;
    }

    protected synchronized void __addGroovyEvaluatorImport(Layer layer, String className) {
        Set<String> list = imports.get(layer);
        if (list == null) {
            list = new HashSet<String>();
            imports.put(layer, list);
        }
        list.add(className);
    }

    protected GroovyScriptEngine __getGroovyScriptEngine() {
        return gse;
    }

    protected GroovyClassLoader __getGroovyClassLoader() {
        return gcl;
    }

    protected <T> T __evaluateGroovy(Layer layer, String text, Binding binding) {
        Script script = null;
        try {
            script = (Script) getPool(layer).borrowObject(text);
            script.setBinding(binding);
            return (T) script.run();
        } catch (Exception e) {
            try {
                getPool(layer).invalidateObject(text, script);
            } catch (Exception e1) {
                log.warn("Error invalidating object in the pool", e1);
            }
            if (e instanceof RuntimeException)
                throw ((RuntimeException) e);
            else
                throw new RuntimeException(e);
        } finally {
            if (script != null)
                try {
                    getPool(layer).returnObject(text, script);
                } catch (Exception e) {
                    log.warn("Error returning object into the pool", e);
                }
        }
    }

    private synchronized GenericKeyedObjectPool getPool(final Layer layer) {
        GenericKeyedObjectPool pool = pools.get(layer);
        if (pool == null) {
            GenericKeyedObjectPool.Config poolConfig = new GenericKeyedObjectPool.Config();
            poolConfig.maxActive = -1;
            pool = new GenericKeyedObjectPool(
                    new BaseKeyedPoolableObjectFactory() {
                        public Object makeObject(Object key) throws Exception {
                            if (!(key instanceof String))
                                throw new IllegalArgumentException();
                            String text = ((String) key);

                            StringBuilder sb = new StringBuilder();
                            Set<String> strings = imports.get(layer);
                            if (strings != null) {
                                for (String importItem : strings) {
                                    sb.append("import ").append(importItem).append("\n");
                                }
                            }
                            sb.append(text);

                            GroovyShell shell = new GroovyShell();
                            Script script = shell.parse(sb.toString());
                            return script;
                        }
                    },
                    poolConfig
            );
            pools.put(layer, pool);
        }
        return pool;
    }
}
