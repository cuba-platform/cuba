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
 */

package com.haulmont.cuba.desktop.sys;

import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.gui.xml.layout.ExternalUIComponentsSource;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.loaders.FrameLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.WindowLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Please notice that {@code DesktopExternalUIComponentsSource} registers only window loaders.
 */
@Component(ExternalUIComponentsSource.NAME)
public class DesktopExternalUIComponentsSource implements ExternalUIComponentsSource {
    private static final String DESKTOP_COMPONENTS_CONFIG_XML_PROP = "cuba.desktop.componentsConfig";

    private final Logger log = LoggerFactory.getLogger(DesktopExternalUIComponentsSource.class);

    protected static final String WINDOW_LOADER_EL = "windowLoader";
    protected static final String FRAME_LOADER_EL = "frameLoader";
    protected static final String EDITOR_LOADER_EL = "editorLoader";
    protected static final String LOOKUP_LOADER_EL = "lookupLoader";

    protected static final Map<String, Class<? extends FrameLoader>> loaders = ImmutableMap.of(
            WINDOW_LOADER_EL, WindowLoader.class,
            FRAME_LOADER_EL, FrameLoader.class,
            EDITOR_LOADER_EL, WindowLoader.Editor.class,
            LOOKUP_LOADER_EL, WindowLoader.Lookup.class
    );

    @Inject
    protected Resources resources;

    protected volatile boolean initialized;

    @Override
    public void checkInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    log.debug("Init external UI components");

                    init();
                    initialized = true;
                }
            }
        }
    }

    protected void init() {
        try {
            _registerWindowLoaders();
        } catch (Exception e) {
            log.error("Error on custom UI components registration", e);
        }
    }

    protected void _registerWindowLoaders() {
        String configName = AppContext.getProperty(DESKTOP_COMPONENTS_CONFIG_XML_PROP);
        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    Element rootElement = Dom4j.readDocument(stream)
                            .getRootElement();
                    _loadWindowLoaders(rootElement);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource {} not found, ignore it", location);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void _loadWindowLoaders(Element rootElement) {
        Class loader = loadWindowLoader(rootElement, WINDOW_LOADER_EL);
        if (loader != null) {
            LayoutLoaderConfig.registerWindowLoader(loader);
        }

        loader = loadWindowLoader(rootElement, FRAME_LOADER_EL);
        if (loader != null) {
            LayoutLoaderConfig.registerFrameLoader(loader);
        }

        loader = loadWindowLoader(rootElement, EDITOR_LOADER_EL);
        if (loader != null) {
            LayoutLoaderConfig.registerEditorLoader(loader);
        }

        loader = loadWindowLoader(rootElement, LOOKUP_LOADER_EL);
        if (loader != null) {
            LayoutLoaderConfig.registerLookupLoader(loader);
        }
    }

    protected Class loadWindowLoader(Element rootElement, String loaderElem) {
        ClassLoader classLoader = App.class.getClassLoader();

        Element elem = rootElement.element(loaderElem);
        if (elem == null) {
            return null;
        }

        String loaderClass = elem.element("class").getStringValue();
        try {
            Class clazz = classLoader.loadClass(loaderClass);

            if (loaders.get(loaderElem).isAssignableFrom(clazz)) {
                //noinspection unchecked
                return clazz;
            }

            log.warn("Class {} is not suitable as {}", loaderClass, loaderElem);
        } catch (ClassNotFoundException e) {
            log.warn("Unable to load window loader class: {}", loaderClass);
        }

        return null;
    }
}