/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceConnector;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractScripting implements Scripting {

    private Log log = LogFactory.getLog(getClass());

    private static final Pattern IMPORT_PATTERN = Pattern.compile("\\bimport\\b\\s+");

    private JavaClassLoader javaClassLoader;

    protected String groovyClassPath;

    protected Set<String> imports = new HashSet<String>();

    protected volatile GroovyScriptEngine gse;

    protected GroovyClassLoader gcl;

    protected GenericKeyedObjectPool pool;

    protected GlobalConfig globalConfig;

    public AbstractScripting(JavaClassLoader javaClassLoader, Configuration configuration) {
        this.javaClassLoader = javaClassLoader;
        globalConfig = configuration.getConfig(GlobalConfig.class);
        groovyClassPath = globalConfig.getConfDir() + File.pathSeparator;

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
                    gse = new GroovyScriptEngine(new CubaResourceConnector(), javaClassLoader);
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
                    gcl = new CubaGroovyClassLoader(cc);
                }
            }
        }
        return gcl;
    }

    private synchronized GenericKeyedObjectPool getPool() {
        if (pool == null) {
            GenericKeyedObjectPool.Config poolConfig = new GenericKeyedObjectPool.Config();
            poolConfig.maxActive = -1;
            poolConfig.maxIdle = globalConfig.getGroovyEvaluationPoolMaxIdle();
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

                            Matcher matcher = IMPORT_PATTERN.matcher(text);
                            String result;
                            if (matcher.find()) {
                                StringBuffer s = new StringBuffer();
                                matcher.appendReplacement(s, sb + "$0");
                                result = matcher.appendTail(s).toString();
                            } else {
                                result = sb.append(text).toString();
                            }

                            CompilerConfiguration cc = new CompilerConfiguration();
                            cc.setClasspath(groovyClassPath);
                            cc.setRecompileGroovySource(true);
                            GroovyShell shell = new GroovyShell(javaClassLoader, new Binding(), cc);
                            Script script = shell.parse(result);
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
            // Perhaps the Groovy source not found - it is possible when we run tests. Let's try to find a
            // compiled script in the classpath
            if (name.endsWith(".groovy"))
                name = name.substring(0, name.length() - 7);
            if (name.startsWith("/"))
                name = name.substring(1);
            name = name.replace("/", ".");

            Class scriptClass = loadClass(name);
            if (scriptClass != null && groovy.lang.Script.class.isAssignableFrom(scriptClass)) {
                try {
                    Script script = (Script) scriptClass.newInstance();
                    script.setBinding(binding);
                    return (T) script.run();
                } catch (InstantiationException | IllegalAccessException e1) {
                    throw new RuntimeException(e1);
                }
            }
            throw new RuntimeException(e);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T runGroovyScript(String name, Map<String, Object> context) {
        Binding binding = createBinding(context);
        //noinspection unchecked
        return (T) runGroovyScript(name, binding);
    }

    @Override
    public ClassLoader getClassLoader() {
        return getGroovyClassLoader();
    }

    @Override
    public <T> Class<T> loadClass(String name) {
        try {
            //noinspection unchecked
            return getGroovyClassLoader().loadClass(name, true, false);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Deprecated
    @Override
    public InputStream getResourceAsStream(String name) {
        return AppBeans.get(Resources.class).getResourceAsStream(name);
    }

    @Deprecated
    @Override
    public String getResourceAsString(String name) {
        return AppBeans.get(Resources.class).getResourceAsString(name);
    }

    @Override
    public void clearCache() {
        getGroovyClassLoader().clearCache();
        javaClassLoader.clearCache();
    }

    protected class CubaResourceConnector implements ResourceConnector {

        /**
         * This implementation works for sources located in conf directory or packed into JARs.
         * It will throw ResourceException for resources in class directories, which is the case for running tests.
         * @param resourceName          resource to load
         * @return                      connection to the resource
         * @throws ResourceException    if the requested resource can not be loaded
         */
        @Override
        public URLConnection getResourceConnection(String resourceName) throws ResourceException {
            URLConnection groovyScriptConn = null;
            StringBuilder errors = new StringBuilder();
            String[] rootPath = getScriptEngineRootPath();

            // First workaround for invocation from GroovyScriptEngine.isSourceNewer()
            for (String path : rootPath) {
                String substrResourceName = resourceName.substring(1);
                path = path.replace('\\','/');
                if (substrResourceName.startsWith(path))
                    resourceName = substrResourceName;
                if (resourceName.startsWith(path)) {
                    // We came here from GroovyScriptEngine.isSourceNewer() and previously we've loaded the script
                    // from conf
                    File file = new File(resourceName);
                    if (file.exists()) {
                        try {
                            URL url = file.toURI().toURL();
                            groovyScriptConn = url.openConnection();
                            // Make sure we can open it, if we can't it doesn't exist.
                            groovyScriptConn.getInputStream();
                            break;
                        } catch (MalformedURLException e) {
                            groovyScriptConn = null;
                            errors.append(e.toString()).append("\n");
                        } catch (IOException e) {
                            groovyScriptConn = null;
                            errors.append(e.toString()).append("\n");
                        }
                    }
                }
            }
            if (groovyScriptConn != null)
                return groovyScriptConn;

            // Second workaround for invocation from GroovyScriptEngine.isSourceNewer()
            try {
                // Check if the resourceName is a valid URL. If it is and if we can open connection, use it
                if (resourceName.startsWith("file:") && resourceName.contains(".jar!"))
                    resourceName = "jar:" + resourceName;

                URL resourceUrl = new URL(resourceName);
                groovyScriptConn = resourceUrl.openConnection();
                // Make sure we can open it, if we can't it doesn't exist.
                groovyScriptConn.getInputStream();
            } catch (MalformedURLException e) {
                // Not an URL, just continue
            } catch (IOException e) {
                groovyScriptConn = null;
                errors.append(e.toString()).append("\n");
            }
            if (groovyScriptConn != null)
                return groovyScriptConn;

            // Next try to find a source in conf.
            String fileName = resourceName.endsWith(".groovy") ? resourceName : resourceName + ".groovy";
            for (String root : rootPath) {
                File file = new File(root, fileName);
                if (file.exists()) {
                    try {
                        URL url = file.toURI().toURL();
                        groovyScriptConn = url.openConnection();
                        // Make sure we can open it, if we can't it doesn't exist.
                        groovyScriptConn.getInputStream();
                        break;
                    } catch (MalformedURLException e) {
                        groovyScriptConn = null;
                        errors.append(e.toString()).append("\n");
                    } catch (IOException e) {
                        groovyScriptConn = null;
                        errors.append(e.toString()).append("\n");
                    }
                } else {
                    errors.append("File " + file + " doesn't exist").append("\n");
                }
            }
            if (groovyScriptConn != null)
                return groovyScriptConn;

            // Next try to find a source groovy file in the classpath
            URL url = getClass().getResource(fileName);
            if (url != null) {
                try {
                    groovyScriptConn = url.openConnection();
                    // Make sure we can open it, if we can't it doesn't exist.
                    groovyScriptConn.getInputStream();
                } catch (IOException e) {
                    groovyScriptConn = null;
                    errors.append(e.toString()).append("\n");
                }
            } else {
                errors.append("Classpath resource " + fileName + " doesn't exist").append("\n");
            }
            if (groovyScriptConn != null)
                return groovyScriptConn;

            errors.insert(0, "Unable to find resource " + resourceName + ":\n");
            throw new ResourceException(errors.toString());
        }
    }

    protected class CubaGroovyClassLoader extends GroovyClassLoader {

        public CubaGroovyClassLoader(CompilerConfiguration cc) {
            super(AbstractScripting.this.javaClassLoader, cc);
        }

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
    }
}
