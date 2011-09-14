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
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    private JavaClassLoader javaClassLoader;

    protected String groovyClassPath;

    protected Set<String> imports = new HashSet<String>();

    protected volatile GroovyScriptEngine gse;

    protected GroovyClassLoader gcl;

    protected GenericKeyedObjectPool pool;

    public AbstractScripting(JavaClassLoader javaClassLoader, Configuration configuration) {
        this.javaClassLoader = javaClassLoader;

        groovyClassPath = configuration.getConfig(GlobalConfig.class).getConfDir() + File.pathSeparator;

        String classPathProp = AppContext.getProperty("cuba.groovyClassPath");
        if (StringUtils.isNotBlank(classPathProp)) {
            String[] strings = classPathProp.split(";");
            for (String string : strings) {
                if (!groovyClassPath.contains(string.trim() + File.pathSeparator))
                    groovyClassPath = groovyClassPath + string.trim() + File.pathSeparator;
            }

        }

        String importProp = AppContext.getProperty("cuba.groovyEvaluatorImport");
        if (StringUtils.isNotBlank(importProp)) {
            String[] strings = importProp.split("[,;]");
            for (String string : strings) {
                imports.add(string.trim());
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
                        gse = new GroovyScriptEngine(rootPath, javaClassLoader);
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
                    gcl = new GroovyClassLoader(javaClassLoader, cc) {
                        // This overridden method is almost identical to super, but prefers Groovy source over parent classloader class
                        @Override
                        public Class loadClass(String name, boolean lookupScriptFiles, boolean preferClassOverScript, boolean resolve) throws ClassNotFoundException, CompilationFailedException {
                            // look into cache
                            Class cls = getClassCacheEntry(name);

                            // enable recompilation?
                            boolean recompile = isRecompilable(cls);
                            if (!recompile) return cls;

                            ClassNotFoundException last = null;

                            // prefer class if no recompilation
                            if (cls != null && preferClassOverScript) return cls;

                            // we want to recompile if needed
                            if (lookupScriptFiles) {
                                // try groovy file
                                try {
                                    // check if recompilation already happened.
                                    final Class classCacheEntry = getClassCacheEntry(name);
                                    if (classCacheEntry != cls) return classCacheEntry;
                                    URL source = getResourceLoader().loadGroovySource(name);
                                    // if recompilation fails, we want cls==null
                                    Class oldClass = cls;
                                    cls = null;
                                    cls = recompile(source, name, oldClass);
                                } catch (IOException ioe) {
                                    last = new ClassNotFoundException("IOException while opening groovy source: " + name, ioe);
                                } finally {
                                    if (cls == null) {
                                        removeClassCacheEntry(name);
                                    } else {
                                        setClassCacheEntry(cls);
                                    }
                                }
                            }

                            if (cls == null) {
                                // try parent loader
                                try {
                                    Class parentClassLoaderClass = super.loadClass(name, false, true, resolve);
                                    // return if the parent loader was successful
                                    if (cls != parentClassLoaderClass) return parentClassLoaderClass;
                                } catch (ClassNotFoundException cnfe) {
                                    last = cnfe;
                                } catch (NoClassDefFoundError ncdfe) {
                                    if (ncdfe.getMessage().indexOf("wrong name") > 0) {
                                        last = new ClassNotFoundException(name);
                                    } else {
                                        throw ncdfe;
                                    }
                                }
                                // no class found, there should have been an exception before now
                                if (last == null) throw new AssertionError(true);
                                throw last;
                            }
                            return cls;
                        }
                    };
                }
            }
        }
        return gcl;
    }

    private synchronized GenericKeyedObjectPool getPool() {
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
                            for (String importItem : imports) {
                                sb.append("import ").append(importItem).append("\n");
                            }
                            sb.append(text);

                            CompilerConfiguration cc = new CompilerConfiguration();
                            cc.setClasspath(groovyClassPath);
                            cc.setRecompileGroovySource(true);
                            GroovyShell shell = new GroovyShell(javaClassLoader, new Binding(), cc);
                            Script script = shell.parse(sb.toString());
                            return script;
                        }
                    },
                    poolConfig
            );
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
    public <T> T evaluateGroovy(String text, Binding binding) {
        Script script = null;
        try {
            script = (Script) getPool().borrowObject(text);
            script.setBinding(binding);
            return (T) script.run();
        } catch (Exception e) {
            try {
                getPool().invalidateObject(text, script);
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
                    getPool().returnObject(text, script);
                } catch (Exception e) {
                    log.warn("Error returning object into the pool", e);
                }
        }
    }

    @Override
    public <T> T evaluateGroovy(String text, Map<String, Object> context) {
        Binding binding = createBinding(context);
        return (T) evaluateGroovy(text, binding);
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
            return getGroovyClassLoader().loadClass(name, true, false);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        String s = name.startsWith("/") ? name.substring(1) : name;
        return getGroovyClassLoader().getResourceAsStream(s);
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
        javaClassLoader.clearCache();
    }
}
