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

package com.haulmont.cuba.web.sys;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.web.widgets.WebJarResource;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.LegacyCommunicationManager;
import com.vaadin.server.communication.UidlWriter;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.HasDependencies;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CubaUidlWriter extends UidlWriter {
    private static final Logger log = LoggerFactory.getLogger(CubaUidlWriter.class);

    protected static final String JAVASCRIPT_EXTENSION = ".js";
    protected static final String CSS_EXTENSION = ".css";

    protected static final Pattern OLD_WEBJAR_IDENTIFIER = Pattern.compile("([^:]+)/.+/(.+)");
    protected static final Pattern NEW_WEBJAR_IDENTIFIER = Pattern.compile("(.+):(.+)");

    protected static final Pattern WEB_JAR_PROPERTY_DEFAULT_VALUE_PATTERN = Pattern.compile("\\?:");

    protected static final String WEB_JAR_PREFIX = "webjar://";

    protected final ServletContext servletContext;

    public CubaUidlWriter(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void handleAdditionalDependencies(List<Class<? extends ClientConnector>> newConnectorTypes,
                                                LegacyCommunicationManager manager, List<Dependency> dependencies) {
        super.handleAdditionalDependencies(newConnectorTypes, manager, dependencies);

        for (Class<? extends ClientConnector> connectorClass : newConnectorTypes) {
            WebJarResource webJarResource = connectorClass.getAnnotation(WebJarResource.class);
            if (webJarResource == null)
                continue;

            String overridePath = webJarResource.overridePath();

            for (String uri : webJarResource.value()) {
                String resourceUri = processResourceUri(uri);
                String resourcePath = getResourceActualPath(resourceUri, overridePath);

                if (resourcePath.endsWith(JAVASCRIPT_EXTENSION)) {
                    String url = manager.registerDependency(resourcePath, connectorClass);
                    dependencies.add(new Dependency(Dependency.Type.JAVASCRIPT, url));
                }

                if (resourcePath.endsWith(CSS_EXTENSION)) {
                    String url = manager.registerDependency(resourcePath, connectorClass);
                    dependencies.add(new Dependency(Dependency.Type.STYLESHEET, url));
                }
            }
        }
    }

    @Override
    protected void addAdditionalDependencies(List<HasDependencies.ClientDependency> allDependencies,
                                             List<HasDependencies.ClientDependency> dependenciesToAdd) {
        if (!dependenciesToAdd.isEmpty()) {
            allDependencies.addAll(dependenciesToAdd);
        }
    }

    @Override
    protected void handleAdditionalDependencies(List<HasDependencies.ClientDependency> dependenciesToAdd,
                                                List<Dependency> dependencies, LegacyCommunicationManager manager) {
        for (HasDependencies.ClientDependency dependency : dependenciesToAdd) {
            String resourcePath;
            String dependencyPath = dependency.getPath();
            if (dependencyPath.startsWith(WEB_JAR_PREFIX)) {
                String resourceUri = processResourceUri(dependencyPath.replace(WEB_JAR_PREFIX, ""));
                resourcePath = getResourceActualPath(resourceUri, "");
            } else {
                resourcePath = dependencyPath;
            }

            Dependency.Type type = dependency.getType() != null
                    ? dependency.getType()
                    : resolveTypeFromPath(dependencyPath);
            // If we can't resolve dependency type, i.e. it might have unsupported type,
            // then we don't add such dependency
            if (type != null) {
                String url = manager.registerDependency(resourcePath, CubaUidlWriter.class);
                dependencies.add(new Dependency(type, url));
            }
        }
    }

    @Nullable
    protected Dependency.Type resolveTypeFromPath(String path) {
        if (path.endsWith(JAVASCRIPT_EXTENSION)) {
            return Dependency.Type.JAVASCRIPT;
        }
        if (path.endsWith(CSS_EXTENSION)) {
            return Dependency.Type.STYLESHEET;
        }
        return null;
    }

    protected String getResourceActualPath(String uri, String overridePath) {
        Matcher matcher = OLD_WEBJAR_IDENTIFIER.matcher(uri);
        if (matcher.matches()) {
            return getWebJarResourcePath(matcher.group(1), matcher.group(2), overridePath);
        }

        matcher = NEW_WEBJAR_IDENTIFIER.matcher(uri);
        if (matcher.matches()) {
            return getWebJarResourcePath(matcher.group(1), matcher.group(2), overridePath);
        }

        log.error("Malformed WebJar resource path: {}", uri);
        throw new RuntimeException("Malformed WebJar resource path: " + uri);
    }

    protected String getWebJarResourcePath(String webJar, String resource, String overridePath) {
        String staticResourcePath = getWebJarStaticResourcePath(overridePath, resource);
        if (staticResourcePath != null && !staticResourcePath.isEmpty()) {
            return staticResourcePath;
        }

        WebJarResourceResolver resolver = AppBeans.get(WebJarResourceResolver.NAME);

        String webJarPath = resolver.getWebJarPath(webJar, resource);
        return resolver.translateToWebPath(webJarPath);
    }

    protected String getWebJarStaticResourcePath(String overridePath, String resource) {
        if (overridePath == null || overridePath.isEmpty()) {
            return null;
        }

        if (!overridePath.endsWith("/")) {
            overridePath += "/";
        }

        String resourcePath = overridePath + resource;
        String path = CubaWebJarsHandler.VAADIN_WEBJARS_PATH_PREFIX + resourcePath;

        URL resourceUrl = null;
        try {
            resourceUrl = servletContext.getResource(path);
        } catch (MalformedURLException e) {
            log.warn("Malformed path of static version of WebJar resource: {}", resourcePath, e);
        }

        return resourceUrl != null ? path.substring(1) : null;
    }

    protected String processResourceUri(String uri) {
        int propertyFirstIndex = uri.indexOf("${");
        if (propertyFirstIndex == -1) {
            return uri;
        }

        int propertyLastIndex = uri.indexOf("}");
        if (propertyLastIndex == -1 || propertyLastIndex < propertyFirstIndex) {
            log.error("Malformed URL of a WebJar resource: {}", uri);
            throw new RuntimeException("Malformed URL of a WebJar resource: " + uri);
        }

        String propertyName = uri.substring(propertyFirstIndex + 2, propertyLastIndex);
        String[] splittedProperty = WEB_JAR_PROPERTY_DEFAULT_VALUE_PATTERN.split(propertyName);

        String webJarVersion = AppContext.getProperty(splittedProperty[0]);

        if (StringUtils.isEmpty(webJarVersion) && splittedProperty.length > 1) {
            webJarVersion = splittedProperty[1];
        }

        if (StringUtils.isEmpty(webJarVersion)) {
            String msg = String.format("Unable to load WebJar version property value: %s. Default version is not set",
                    propertyName);

            log.error(msg);
            throw new RuntimeException(msg);
        }

        return StringUtils.replace(uri, "${" + propertyName + "}", webJarVersion);
    }
}