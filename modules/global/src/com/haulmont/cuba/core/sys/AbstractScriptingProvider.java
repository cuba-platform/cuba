/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractScriptingProvider extends ScriptingProvider {

    private Log log = LogFactory.getLog(getClass());

    protected String groovyClassPath = "";

    protected Map<Layer, Set<String>> imports = new HashMap<Layer, Set<String>>();

    protected volatile GroovyScriptEngine gse;

    protected GroovyClassLoader gcl;

    protected Map<Layer, GenericKeyedObjectPool> pools = new HashMap<Layer, GenericKeyedObjectPool>();

    public AbstractScriptingProvider(ConfigProvider configProvider) {
        GlobalConfig config = configProvider.doGetConfig(GlobalConfig.class);
        groovyClassPath = config.getConfDir() + File.pathSeparator;

        String classPathProp = AppContext.getProperty("cuba.groovyClassPath");
        if (StringUtils.isNotBlank(classPathProp)) {
            String[] strings = classPathProp.split(";");
            for (String string : strings) {
                if (!groovyClassPath.contains(string.trim() + File.pathSeparator))
                    groovyClassPath = groovyClassPath + string.trim() + File.pathSeparator;
            }

        }

        for (Layer layer : Layer.values()) {
            String importProp = AppContext.getProperty("cuba.groovyEvaluatorImport." + layer);
            if (StringUtils.isNotBlank(importProp)) {
                String[] strings = importProp.split(";");
                for (String string : strings) {
                    Set<String> set = imports.get(layer);
                    if (set == null) {
                        set = new HashSet<String>();
                        imports.put(layer, set);
                    }
                    if (!set.contains(string.trim())) {
                        set.add(string.trim());
                    }
                }
            }
        }
    }

    protected abstract String[] getScriptEngineRootPath();

    public GroovyScriptEngine doGetGroovyScriptEngine() {
        if (gse == null) {
            synchronized (this) {
                if (gse == null) {
                    final String[] rootPath = getScriptEngineRootPath();
                    try {
                        gse = new GroovyScriptEngine(rootPath, JavaClassLoader.getInstance());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return gse;
    }

    public GroovyClassLoader doGetGroovyClassLoader() {
        if (gcl == null) {
            synchronized (this) {
                if (gcl == null) {
                    CompilerConfiguration cc = new CompilerConfiguration();
                    cc.setClasspath(groovyClassPath);
                    cc.setRecompileGroovySource(true);
                    gcl = new GroovyClassLoader(JavaClassLoader.getInstance(), cc);
                }
            }
        }
        return gcl;
    }

    public <T> T doEvaluateGroovy(Layer layer, String text, Binding binding) {
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
                    script.setBinding(null); // free memory
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

                            CompilerConfiguration cc = new CompilerConfiguration();
                            cc.setClasspath(groovyClassPath);
                            cc.setRecompileGroovySource(true);
                            GroovyShell shell = new GroovyShell(JavaClassLoader.getInstance(), new Binding(), cc);
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
