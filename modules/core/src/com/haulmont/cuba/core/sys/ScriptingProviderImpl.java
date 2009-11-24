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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.groovy.control.CompilerConfiguration;

public class ScriptingProviderImpl extends ScriptingProvider {

    private GroovyScriptEngine gse;

    private GroovyClassLoader gcl;

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

    protected GroovyScriptEngine __getGroovyScriptEngine() {
        return gse;
    }

    protected GroovyClassLoader __getGroovyClassLoader() {
        return gcl;
    }
}
