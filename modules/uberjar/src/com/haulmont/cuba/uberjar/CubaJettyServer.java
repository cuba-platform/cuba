/*
 * Copyright (c) 2008-2017 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.uberjar;

import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static com.haulmont.cuba.uberjar.CubaJettyUtils.*;

public class CubaJettyServer {
    protected int port;
    protected String contextPath;
    protected String portalContextPath;
    protected String frontContextPath;
    protected URL jettyEnvPathUrl;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getFrontContextPath() {
        return frontContextPath;
    }

    public void setFrontContextPath(String frontContextPath) {
        this.frontContextPath = frontContextPath;
    }

    public String getPortalContextPath() {
        return portalContextPath;
    }

    public void setPortalContextPath(String portalContextPath) {
        this.portalContextPath = portalContextPath;
    }

    public URL getJettyEnvPathUrl() {
        return jettyEnvPathUrl;
    }

    public void setJettyEnvPathUrl(URL jettyEnvPathUrl) {
        this.jettyEnvPathUrl = jettyEnvPathUrl;
    }

    public void start() {
        String appHome = System.getProperty("app.home");
        if (appHome == null || appHome.length() == 0) {
            System.setProperty("app.home", System.getProperty("user.dir"));
        }
        try {
            Server server = createServer();
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    protected Server createServer() throws Exception {
        ClassLoader serverClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader sharedClassLoader = new URLClassLoader(pathsToURLs(serverClassLoader, SHARED_CLASS_PATH_IN_JAR), serverClassLoader);

        Server server = new Server(port);
        List<Handler> handlers = new ArrayList<>();
        if (CubaJettyUtils.hasCoreApp(serverClassLoader)) {
            String coreContextPath = contextPath;
            if (isSingleJar(serverClassLoader)) {
                if (PATH_DELIMITER.equals(contextPath)) {
                    coreContextPath = PATH_DELIMITER + "app-core";
                } else {
                    coreContextPath = contextPath + "-core";
                }
            }
            handlers.add(createAppContext(serverClassLoader, sharedClassLoader, CORE_PATH_IN_JAR, coreContextPath));
        }
        if (hasWebApp(serverClassLoader)) {
            handlers.add(createAppContext(serverClassLoader, sharedClassLoader, WEB_PATH_IN_JAR, contextPath));
        }
        if (hasPortalApp(serverClassLoader)) {
            String portalContextPath = contextPath;
            if (isSingleJar(serverClassLoader)) {
                portalContextPath = this.portalContextPath;
            }
            handlers.add(createAppContext(serverClassLoader, sharedClassLoader, PORTAL_PATH_IN_JAR, portalContextPath));
        }
        if (hasFrontApp(serverClassLoader)) {
            handlers.add(createFrontAppContext(serverClassLoader));
        }

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(handlers.toArray(new Handler[handlers.size()]));
        server.setHandler(handlerCollection);

        return server;
    }


    protected WebAppContext createAppContext(ClassLoader serverClassLoader, ClassLoader sharedClassLoader,
                                             String appPathInJar, String contextPath) throws URISyntaxException {
        ClassLoader appClassLoader = new URLClassLoader(pathsToURLs(serverClassLoader, getAppClassesPath(appPathInJar)), sharedClassLoader);

        WebAppContext appContext = new WebAppContext();
        appContext.setConfigurations(new Configuration[]{new WebXmlConfiguration(), createEnvConfiguration()});
        appContext.setContextPath(contextPath);
        appContext.setClassLoader(appClassLoader);

        setResourceBase(serverClassLoader, appContext, appPathInJar);

        return appContext;
    }


    protected WebAppContext createFrontAppContext(ClassLoader serverClassLoader) throws URISyntaxException {
        URL frontContentUrl = serverClassLoader.getResource(FRONT_PATH_IN_JAR);
        WebAppContext frontContext = new WebAppContext();
        frontContext.setContextPath(frontContextPath);
        frontContext.setClassLoader(serverClassLoader);
        frontContext.setResourceBase(frontContentUrl.toURI().toString());
        return frontContext;
    }

    protected void setResourceBase(ClassLoader serverClassLoader, WebAppContext appContext, String appPath) throws URISyntaxException {
        URL resourceBaseUrl = serverClassLoader.getResource(appPath);
        if (resourceBaseUrl != null) {
            appContext.setResourceBase(resourceBaseUrl.toURI().toString());
        }
    }

    protected EnvConfiguration createEnvConfiguration() {
        EnvConfiguration envConfiguration = new EnvConfiguration();
        if (jettyEnvPathUrl != null) {
            envConfiguration.setJettyEnvXml(jettyEnvPathUrl);
        }
        return envConfiguration;
    }
}
