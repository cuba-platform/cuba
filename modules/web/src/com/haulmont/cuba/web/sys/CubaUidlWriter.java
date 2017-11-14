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
import com.haulmont.cuba.web.ScreenProfiler;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.LegacyCommunicationManager;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.communication.UidlWriter;
import com.vaadin.ui.UI;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CubaUidlWriter extends UidlWriter {
    protected static final String JAVASCRIPT_EXTENSION = ".js";
    protected static final String CSS_EXTENSION = ".css";
    protected static final String VAADIN_WEBJARS_PREFIX = "VAADIN/webjars/";

    private final Logger log = LoggerFactory.getLogger(CubaUidlWriter.class);

    protected ScreenProfiler profiler = AppBeans.get(ScreenProfiler.NAME);

    @Override
    protected void writePerformanceData(UI ui, Writer writer) throws IOException {
        super.writePerformanceData(ui, writer);

        String profilerMarker = profiler.getCurrentProfilerMarker(ui);
        if (profilerMarker != null) {
            profiler.setCurrentProfilerMarker(ui, null);
            long lastRequestTimestamp = ui.getSession().getLastRequestTimestamp();
            writer.write(String.format(", \"profilerMarker\": \"%s\", \"profilerEventTs\": \"%s\", \"profilerServerTime\": %s",
                    profilerMarker, lastRequestTimestamp, System.currentTimeMillis() - lastRequestTimestamp));
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void handleAdditionalDependencies(List<Class<? extends ClientConnector>> newConnectorTypes,
                                                List<String> scriptDependencies, List<String> styleDependencies) {
        LegacyCommunicationManager manager = VaadinSession.getCurrent().getCommunicationManager();

        for (Class<? extends ClientConnector> connector : newConnectorTypes) {
            WebJarResource webJarResource = connector.getAnnotation(WebJarResource.class);
            if (webJarResource == null)
                continue;

            for (String uri : webJarResource.value()) {
                uri = processResourceUri(uri);

                if (uri.endsWith(JAVASCRIPT_EXTENSION)) {
                    scriptDependencies.add(manager.registerDependency(uri, connector));
                }

                if (uri.endsWith(CSS_EXTENSION)) {
                    styleDependencies.add(manager.registerDependency(uri, connector));
                }
            }
        }
    }

    protected String processResourceUri(String uri) {
        int propertyFirstIndex = uri.indexOf("${");
        if (propertyFirstIndex == -1) {
            return VAADIN_WEBJARS_PREFIX + uri;
        }

        int propertyLastIndex = uri.indexOf("}");
        if (propertyLastIndex == -1 || propertyLastIndex < propertyFirstIndex) {
            String errorMessage = String.format("Malformed URL of a WebJar resource: %s", uri);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }

        String propertyName = uri.substring(propertyFirstIndex + 2, propertyLastIndex);
        String[] splittedProperty = propertyName.split("\\?:");

        String webJarVersion = AppContext.getProperty(splittedProperty[0]);

        if (StringUtils.isEmpty(webJarVersion) && splittedProperty.length > 1) {
            webJarVersion = splittedProperty[1];
        }

        if (StringUtils.isEmpty(webJarVersion)) {
            String msg = String.format("Could not load WebJar version property value: %s. And default version is also not set",
                    propertyName);

            log.error(msg);
            throw new RuntimeException(msg);
        }

        return VAADIN_WEBJARS_PREFIX + uri.replace("${" + propertyName + "}", webJarVersion);
    }
}