/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.core.sys.singleapp;

import com.haulmont.cuba.core.sys.CubaSingleAppClassLoader;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

/**
 *
 * This class and its twin com.haulmont.cuba.web.sys.singleapp.SingleAppWebServletListener separate "web" and "core" classes
 * to different classloaders when we pack application to single WAR.
 *
 * We create 2 URLClassLoaders (1 for core and 1 for web), with predefined (during single WAR build) list of jars (core.dependencies).
 * So the classloaders load classes from the jars and only if class is not found they delegate loading to base WebAppClassLoader (their parent).
 *
 * As a result, core classloader contains core classes, web classloder contains web classes and WebAppClassLoader contains "shared" classes.
 *
 * To make sure spring context use necessary classloader we load AppWebContextLoader reflectively, create new instance
 * and call contextInitialized() reflectively as well.
 *
 * As each classloader has its own AppContext version, we can put property with dependencies to AppContext (reflectively as well).
 * The property will be used on spring context creation, to detect which jars to scan.
 *
 */
public class SingleAppCoreServletListener implements ServletContextListener {
    protected Object appContextLoader;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            //need to put the following class to WebAppClassLoader, to share it between for web and core
            contextClassLoader.loadClass("com.haulmont.cuba.core.sys.remoting.LocalServiceDirectory");

            ServletContext servletContext = sce.getServletContext();
            String dependenciesFile;
            try {
                dependenciesFile = IOUtils.toString(servletContext.getResourceAsStream("/WEB-INF/core.dependencies"), "UTF-8");
            } catch (IOException e) {
                throw new RuntimeException("An error occurred while loading dependencies file", e);
            }

            String[] dependenciesNames = dependenciesFile.split("\\n");
            URL[] urls = Arrays.stream(dependenciesNames)
                    .map((String name) -> {
                        try {
                            return servletContext.getResource("/WEB-INF/lib/" + name);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException("An error occurred while loading dependency " + name, e);
                        }
                    })
                    .toArray(URL[]::new);
            URLClassLoader coreClassLoader = new CubaSingleAppClassLoader(urls, contextClassLoader);

            Thread.currentThread().setContextClassLoader(coreClassLoader);
            Class<?> appContextLoaderClass = coreClassLoader.loadClass("com.haulmont.cuba.core.sys.singleapp.SingleAppCoreContextLoader");
            appContextLoader = appContextLoaderClass.newInstance();

            Class<?> appContextClass = coreClassLoader.loadClass("com.haulmont.cuba.core.sys.AppContext");
            Method setProperty = ReflectionUtils.findMethod(appContextClass, "setProperty", String.class, String.class);
            ReflectionUtils.invokeMethod(setProperty, null, "JAR_DEPENDENCIES", dependenciesFile);

            Method contextInitialized = ReflectionUtils.findMethod(appContextLoader.getClass(), "contextInitialized", ServletContextEvent.class);
            ReflectionUtils.invokeMethod(contextInitialized, appContextLoader, sce);
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("An error occurred while starting single WAR application", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Method contextInitialized = ReflectionUtils.findMethod(appContextLoader.getClass(), "contextDestroyed", ServletContextEvent.class);
        ReflectionUtils.invokeMethod(contextInitialized, appContextLoader, sce);
    }
}
