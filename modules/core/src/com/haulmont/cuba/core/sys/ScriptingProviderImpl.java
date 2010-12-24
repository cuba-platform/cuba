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

import com.haulmont.cuba.core.app.ClusterListener;
import com.haulmont.cuba.core.app.ClusterManagerAPI;
import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.core.sys.javacl.JavaClassLoader;
import groovy.util.GroovyScriptEngine;
import groovy.lang.GroovyClassLoader;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.*;
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

    private volatile GroovyScriptEngine gse;

    private GroovyClassLoader gcl;

    private Map<Layer, GenericKeyedObjectPool> pools = new HashMap<Layer, GenericKeyedObjectPool>();

    private ClusterManagerAPI clusterManager;

    public ScriptingProviderImpl(ConfigProvider configProvider, ClusterManagerAPI clusterManager) {
        this.clusterManager = clusterManager;
        this.clusterManager.addListener(ScriptingSettings.class, new ScriptingSettingsDistributor());

        GlobalConfig config = configProvider.doGetConfig(GlobalConfig.class);
        doAddGroovyClassPath(config.getConfDir());
    }

//    public void setClusterManager(ClusterManagerAPI clusterManager) {
//        this.clusterManager = clusterManager;
//        this.clusterManager.addListener(ScriptingSettings.class, new ScriptingSettingsDistributor());
//    }
//
    public void doAddGroovyClassPath(String path) {
        internalAddGroovyClassPath(path, true);
    }

    private void internalAddGroovyClassPath(String path, boolean distribute) {
        if (!groovyClassPath.contains(File.pathSeparator + path)) {
            groovyClassPath = groovyClassPath + File.pathSeparator + path;
            if (distribute)
                clusterManager.send(new ScriptingSettings(null, null, path));
        }
    }

    public synchronized void doAddGroovyEvaluatorImport(Layer layer, String className) {
        internalAddGroovyEvaluatorImport(layer, className, true);
    }

    private void internalAddGroovyEvaluatorImport(Layer layer, String className, boolean distribute) {
        Set<String> list = imports.get(layer);
        if (list == null) {
            list = new HashSet<String>();
            imports.put(layer, list);
        }
        if (!list.contains(className)) {
            list.add(className);
            if (distribute)
                clusterManager.send(new ScriptingSettings(layer, className, null));
        }
    }

    public GroovyScriptEngine doGetGroovyScriptEngine() {
        if (gse == null) {
            synchronized (this) {
                if (gse == null) {
                    final String[] rootPath =  new String[]{
                            // src-conf directory:
                            ConfigProvider.getConfig(ServerConfig.class).getServerConfDir(),
                            // db scripts directory:
                            ConfigProvider.getConfig(ServerConfig.class).getServerDbDir()
                    };
                    try {
                        gse = new GroovyScriptEngine(rootPath);
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

    private static class ScriptingSettings implements Serializable {

        private Layer importLayer;
        private String importClassName;
        private String classPath;

        private static final long serialVersionUID = -2146330970539251455L;

        private ScriptingSettings(Layer importLayer, String importClassName, String classPath) {
            this.classPath = classPath;
            this.importClassName = importClassName;
            this.importLayer = importLayer;
        }
    }

    private class ScriptingSettingsDistributor implements ClusterListener<ScriptingSettings> {

        public void receive(ScriptingSettings message) {
            if (message.importLayer != null && message.importClassName != null)
                internalAddGroovyEvaluatorImport(message.importLayer, message.importClassName, false);

            if (message.classPath != null)
                internalAddGroovyClassPath(message.classPath, false);
        }

        public byte[] getState() {
            List<ScriptingSettings> list = new ArrayList<ScriptingSettings>();

            String[] strings = groovyClassPath.split(File.pathSeparator);
            for (String path : strings) {
                list.add(new ScriptingSettings(null, null, path));
            }

            for (Map.Entry<Layer, Set<String>> entry : imports.entrySet()) {
                for (String className : entry.getValue()) {
                    list.add(new ScriptingSettings(entry.getKey(), className, null));
                }
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(list);
            } catch (IOException e) {
                log.error("Error serializing ScriptingSettings", e);
                return new byte[0];
            }
            return bos.toByteArray();
        }

        public void setState(byte[] state) {
            if (state == null || state.length == 0)
                return;

            List<ScriptingSettings> list;
            ByteArrayInputStream bis = new ByteArrayInputStream(state);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                list = (List<ScriptingSettings>) ois.readObject();
            } catch (Exception e) {
                log.error("Error deserializing ScriptingSettings", e);
                return;
            }

            for (ScriptingSettings ss : list) {
                receive(ss);
            }
        }
    }
}
