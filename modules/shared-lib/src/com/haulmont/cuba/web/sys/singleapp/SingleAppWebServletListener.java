/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.sys.singleapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * This class and its twin com.haulmont.cuba.core.sys.singleapp.SingleAppCoreServletListener separate "web" and "core" classes
 * to different classloaders when we pack application to single WAR.
 * <p>
 * We create 2 URLClassLoaders (1 for core and 1 for web), with predefined (during single WAR build) list of jars (/WEB-INF/lib-web/).
 * <p>
 * As a result, core classloader contains core classes, web classloader contains web classes and WebAppClassLoader contains "shared" classes.
 * <p>
 * To make sure the Spring context uses the specific classloader we load {@code AppWebContextLoader} reflectively, create new instance
 * and call its initialization methods reflectively as well.
 * <p>
 * As each classloader has its own AppContext version, we can scan jars only from defined folders (/WEB-INF/lib-web/ for "web" jars).
 */
public class SingleAppWebServletListener implements ServletContextListener {
    protected Object appContextLoader;

    private static final Logger log = LoggerFactory.getLogger(SingleAppWebServletListener.class);

    private URLClassLoader webClassLoader;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            //need to put the following class to WebAppClassLoader, to share it between for web and core
            contextClassLoader.loadClass("com.haulmont.cuba.core.sys.remoting.LocalServiceDirectory");

            ServletContext servletContext = sce.getServletContext();

            URL[] urls = servletContext.getResourcePaths("/WEB-INF/lib-web/").stream()
                    .map((String name) -> {
                        try {
                            return servletContext.getResource(name);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException("An error occurred while loading dependency " + name, e);
                        }
                    })
                    .toArray(URL[]::new);

            webClassLoader = new URLClassLoader(urls, contextClassLoader);

            Thread.currentThread().setContextClassLoader(webClassLoader);
            Class<?> appContextLoaderClass = webClassLoader.loadClass(getAppContextLoaderClassName());
            appContextLoader = appContextLoaderClass.newInstance();


            Method contextInitializedMethod = appContextLoaderClass.getMethod("contextInitialized", ServletContextEvent.class);

            contextInitializedMethod.invoke(appContextLoader, sce);

            Thread.currentThread().setContextClassLoader(contextClassLoader);
        } catch (Exception e) {
            log.error("An error occurred while starting single WAR - web application", e);

            throw new RuntimeException("An error occurred while starting single WAR application", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Method contextDestroyed = appContextLoader.getClass().getMethod("contextDestroyed", ServletContextEvent.class);
            contextDestroyed.invoke(appContextLoader, sce);

            if (webClassLoader != null) {
                webClassLoader.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while destroying context of single WAR application", e);
        }
    }

    protected String getAppContextLoaderClassName() {
        return "com.haulmont.cuba.web.sys.singleapp.SingleAppWebContextLoader";
    }
}
