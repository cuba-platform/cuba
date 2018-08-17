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

package com.haulmont.cuba.web.sys;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.events.AppContextInitializedEvent;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ExternalUIComponentsSource;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.loaders.FrameLoader;
import com.haulmont.cuba.gui.xml.layout.loaders.WindowLoader;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * Class registers external components that are supplied in separate jars or defined in 'cuba-ui-component.xml'
 * descriptor of 'web' module.
 */
@org.springframework.stereotype.Component(ExternalUIComponentsSource.NAME)
public class WebExternalUIComponentsSource implements ExternalUIComponentsSource {

    private static final String WEB_COMPONENTS_CONFIG_XML_PROP = "cuba.web.componentsConfig";

    private final Logger log = LoggerFactory.getLogger(WebExternalUIComponentsSource.class);

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
    @Inject
    protected WebComponentsFactory webComponentsFactory;
    @Inject
    protected LayoutLoaderConfig layoutLoaderConfig;

    @EventListener
    @Order(Events.HIGHEST_PLATFORM_PRECEDENCE + 100)
    public void init(@SuppressWarnings("unused") AppContextInitializedEvent event) {
        try {
            // register component from app components
            _registerAppComponents();
            //register components from external component jars
            _registerComponents("META-INF/cuba-ui-component.xml");
            //register components from web modules
            _registerComponents("cuba-ui-component.xml");

        } catch (Exception e) {
            log.error("Error on custom UI components registration", e);
        }
    }

    protected void _registerAppComponents() {
        String configNames = AppContext.getProperty(WEB_COMPONENTS_CONFIG_XML_PROP);

        if (Strings.isNullOrEmpty(configNames)) {
            return;
        }

        log.debug("Loading UI components from {}", configNames);

        StringTokenizer tokenizer = new StringTokenizer(configNames);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    _registerComponent(stream);
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException("Unable to load components config " + location, e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource {} not found, ignore it", location);
            }
        }
    }

    protected void _registerComponents(String componentDescriptorPath) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = App.class.getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(componentDescriptorPath);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try (InputStream is = url.openStream()) {
                _registerComponent(is);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void _registerComponent(InputStream is) throws ClassNotFoundException {
        ClassLoader classLoader = App.class.getClassLoader();

        Element rootElement = Dom4j.readDocument(is).getRootElement();
        List<Element> components = rootElement.elements("component");
        for (Element component : components) {
            String name = trimToEmpty(component.elementText("name"));
            String componentClassName = trimToEmpty(component.elementText("class"));

            String componentLoaderClassName = trimToEmpty(component.elementText("componentLoader"));
            String tag = trimToEmpty(component.elementText("tag"));
            if (StringUtils.isEmpty(tag)) {
                tag = name;
            }

            if (StringUtils.isEmpty(name) && StringUtils.isEmpty(tag)) {
                log.warn("You have to provide name or tag for custom component");
                // skip this <component> element
                continue;
            }

            if (StringUtils.isEmpty(componentLoaderClassName) && StringUtils.isEmpty(componentClassName)) {
                log.warn("You have to provide at least <class> or <componentLoader> for custom component {} / <{}>",
                        name, tag);
                // skip this <component> element
                continue;
            }

            if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(componentClassName)) {
                Class<?> componentClass = classLoader.loadClass(componentClassName);

                if (Component.class.isAssignableFrom(componentClass)) {
                    log.trace("Register component {} class {}", name, componentClass.getCanonicalName());

                    webComponentsFactory.register(name, (Class<? extends Component>) componentClass);
                } else {
                    log.warn("Component {} is not a subclass of com.haulmont.cuba.gui.components.Component", componentClassName);
                }
            }

            if (StringUtils.isNotEmpty(tag) && StringUtils.isNotEmpty(componentLoaderClassName)) {
                Class<?> componentLoaderClass = classLoader.loadClass(componentLoaderClassName);

                if (ComponentLoader.class.isAssignableFrom(componentLoaderClass)) {
                    log.trace("Register tag {} loader {}", tag, componentLoaderClass.getCanonicalName());

                    layoutLoaderConfig.registerLoader(tag, (Class<? extends ComponentLoader>) componentLoaderClass);
                } else {
                    log.warn("Component loader {} is not a subclass of com.haulmont.cuba.gui.xml.layout.ComponentLoader",
                            componentLoaderClassName);
                }
            }
        }

        _loadWindowLoaders(rootElement);
    }

    @SuppressWarnings("unchecked")
    protected void _loadWindowLoaders(Element rootElement) {
        Class loader = loadWindowLoader(rootElement, WINDOW_LOADER_EL);
        if (loader != null) {
            layoutLoaderConfig.registerWindowLoader(loader);
        }

        loader = loadWindowLoader(rootElement, FRAME_LOADER_EL);
        if (loader != null) {
            layoutLoaderConfig.registerFrameLoader(loader);
        }

        loader = loadWindowLoader(rootElement, EDITOR_LOADER_EL);
        if (loader != null) {
            layoutLoaderConfig.registerEditorLoader(loader);
        }

        loader = loadWindowLoader(rootElement, LOOKUP_LOADER_EL);
        if (loader != null) {
            layoutLoaderConfig.registerLookupLoader(loader);
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