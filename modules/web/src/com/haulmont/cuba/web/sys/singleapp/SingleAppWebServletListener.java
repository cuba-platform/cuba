/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys.singleapp;

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
import java.util.stream.Collectors;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class SingleAppWebServletListener implements ServletContextListener {
    protected Object appContextLoader;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Class<?> localServiceDirectory = contextClassLoader.loadClass("com.haulmont.cuba.core.sys.remoting.LocalServiceDirectory");

            ServletContext servletContext = sce.getServletContext();
            String dependenciesFile;
            try {
                dependenciesFile = IOUtils.toString(servletContext.getResourceAsStream("/WEB-INF/web.dependencies"), "UTF-8");
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
                    .collect(Collectors.toList())
                    .toArray(new URL[dependenciesNames.length]);
            URLClassLoader webClassLoader = new CubaSingleAppClassLoader(urls, contextClassLoader);

            Thread.currentThread().setContextClassLoader(webClassLoader);
            Class<?> appContextLoaderClass = webClassLoader.loadClass("com.haulmont.cuba.web.sys.singleapp.SingleAppWebContextLoader");
            appContextLoader = appContextLoaderClass.newInstance();

            Class<?> appContextClass = webClassLoader.loadClass("com.haulmont.cuba.core.sys.AppContext");
            Method setProperty = ReflectionUtils.findMethod(appContextClass, "setProperty", String.class, String.class);
            ReflectionUtils.invokeMethod(setProperty, null, "JAR_DEPENDENCIES", dependenciesFile);

            Method contextInitialized = ReflectionUtils.findMethod(appContextLoader.getClass(), "contextInitialized", ServletContextEvent.class);
            ReflectionUtils.invokeMethod(contextInitialized, appContextLoader, sce);
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
