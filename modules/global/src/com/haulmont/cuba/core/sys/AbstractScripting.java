/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractScripting implements Scripting {

    private Log log = LogFactory.getLog(getClass());

    protected String confPath;

    protected String groovyClassPath;

    protected Map<Layer, Set<String>> imports = new HashMap<Layer, Set<String>>();

    protected volatile GroovyScriptEngine gse;

    protected GroovyClassLoader gcl;

    protected Map<Layer, GenericKeyedObjectPool> pools = new HashMap<Layer, GenericKeyedObjectPool>();

    public AbstractScripting(Configuration configuration) {
        confPath = configuration.getConfig(GlobalConfig.class).getConfDir() + File.pathSeparator;
        groovyClassPath = confPath;

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

    protected GroovyScriptEngine getGroovyScriptEngine() {
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

    protected GroovyClassLoader getGroovyClassLoader() {
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

    protected Binding createBinding(Map<String, Object> map) {
        Binding binding = new Binding();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            binding.setVariable(entry.getKey(), entry.getValue());
        }

        return binding;
    }

    @Override
    public <T> T evaluateGroovy(Layer layer, String text, Binding binding) {
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

    @Override
    public <T> T evaluateGroovy(Layer layer, String text, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) evaluateGroovy(layer, text, binding);
    }

    @Override
    public <T> T runGroovyScript(String name, Binding binding) {
        try {
            return (T) getGroovyScriptEngine().run(name, binding);
        } catch (ResourceException e) {
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T runGroovyScript(String name, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) runGroovyScript(name, binding);
    }

    @Override
    public ClassLoader getClassLoader() {
        return getGroovyClassLoader();
    }

    @Override
    public Class loadClass(String name) {
        try {
            File file = new File(confPath, name.replace(".", "/") + ".java");
            if (file.exists())
                return JavaClassLoader.getInstance().loadClass(name);
            else
                return getGroovyClassLoader().loadClass(name, true, false);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        String s = name.startsWith("/") ? name.substring(1) : name;
        File file = new File(confPath, s);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return getGroovyClassLoader().getResourceAsStream(s);
        }
    }

    @Override
    public String getResourceAsString(String name) {
        InputStream stream = getResourceAsStream(name);
        if (stream == null)
            return null;

        try {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    @Override
    public void clearCache() {
        getGroovyClassLoader().clearCache();
        JavaClassLoader.getInstance().clearCache();
    }
}
