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

package com.haulmont.cuba.core.sys.logging;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class LogbackConfigurator extends ContextAwareBase implements Configurator {

    private static final String APP_HOME_PROPERTY = "app.home";

    private static final String DEFAULT_CLASSPATH_CONFIG = "app-logback.xml";

    @Override
    public void configure(LoggerContext loggerContext) {
        printInfo("Setting up CUBA default logging configuration");

        try {
            URL url = findURLOfConfigurationFile(true);
            if (url != null) {
                configureByResource(url);
            } else {
                printInfo("Configuring console output with WARN threshold");
                BasicConfigurator basicConfigurator = new BasicConfigurator();
                basicConfigurator.setContext(loggerContext);
                basicConfigurator.configure(loggerContext);
                Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
                rootLogger.setLevel(Level.WARN);
            }
        } catch (Exception e) {
            printError("Failed to configure CUBA default logging: " + e);
        }
    }

    private void printInfo(String message) {
        System.out.println(getClass().getSimpleName() + " INFO " + message);
    }

    private void printError(String message) {
        System.out.println(getClass().getSimpleName() + " ERROR " + message);
    }

    private void configureByResource(URL url) throws JoranException {
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        configurator.doConfigure(url);
    }

    public URL findURLOfConfigurationFile(boolean updateStatus) {
        ClassLoader myClassLoader = Loader.getClassLoaderOfObject(this);
        URL url = findConfigFileUrlInAppHome(myClassLoader, updateStatus);
        if (url != null) {
            return url;
        }
        return getResource(DEFAULT_CLASSPATH_CONFIG, myClassLoader, updateStatus);
    }

    private URL findConfigFileUrlInAppHome(ClassLoader classLoader, boolean updateStatus) {
        String appHome = OptionHelper.getSystemProperty(APP_HOME_PROPERTY);
        if (appHome != null) {
            String logbackConfigFile = appHome + "/logback.xml";
            URL result = null;
            try {
                result = new URL(logbackConfigFile);
                return result;
            } catch (MalformedURLException e) {
                // so, resource is not a URL:
                // attempt to get the resource from the class path
                result = Loader.getResource(logbackConfigFile, classLoader);
                if (result != null) {
                    return result;
                }
                File f = new File(logbackConfigFile);
                if (f.exists() && f.isFile()) {
                    try {
                        result = f.toURI().toURL();
                        return result;
                    } catch (MalformedURLException e1) {
                    }
                }
            } finally {
                if (updateStatus) {
                    statusOnResourceSearch(logbackConfigFile, classLoader, result);
                }
            }
        }
        return null;
    }

    private URL getResource(String filename, ClassLoader myClassLoader, boolean updateStatus) {
        URL url = Loader.getResource(filename, myClassLoader);
        if (updateStatus) {
            statusOnResourceSearch(filename, myClassLoader, url);
        }
        return url;
    }

    private void statusOnResourceSearch(String resourceName, ClassLoader classLoader, URL url) {
        if (url == null) {
            printInfo("Could NOT find resource [" + resourceName + "]");
        } else {
            printInfo("Found resource [" + url.toString() + "]");
            multiplicityWarning(resourceName, classLoader);
        }
    }

    private void multiplicityWarning(String resourceName, ClassLoader classLoader) {
        Set<URL> urlSet = null;
        try {
            urlSet = Loader.getResources(resourceName, classLoader);
        } catch (IOException e) {
            printError("Failed to get url list for resource [" + resourceName + "]");
        }
        if (urlSet != null && urlSet.size() > 1) {
            printError("Resource [" + resourceName + "] occurs multiple times on the classpath.");
            for (URL url : urlSet) {
                printInfo("Resource [" + resourceName + "] occurs at [" + url.toString() + "]");
            }
        }
    }
}
