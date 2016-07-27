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

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ExternalUIComponentsSource;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import static org.apache.commons.lang.StringUtils.trimToEmpty;

/**
 * Class registers external components that are supplied in separate jars or defined in 'cuba-ui-component.xml'
 * descriptor of 'web' module.
 */
@org.springframework.stereotype.Component(ExternalUIComponentsSource.NAME)
public class WebExternalUIComponentsSource implements ExternalUIComponentsSource {

    private static final String WEB_COMPONENTS_CONFIG_XML_PROP = "cuba.web.componentsConfig";

    private final Logger log = LoggerFactory.getLogger(WebExternalUIComponentsSource.class);

    @Inject
    protected Resources resources;

    @Inject
    protected WebComponentsFactory webComponentsFactory;

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
        String configName = AppContext.getProperty(WEB_COMPONENTS_CONFIG_XML_PROP);
        StrTokenizer tokenizer = new StrTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    _registerComponent(stream);
                } catch (ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
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

    protected void _registerComponent(InputStream is) throws ClassNotFoundException {
        ClassLoader classLoader = App.class.getClassLoader();

        Document document = Dom4j.readDocument(is);
        List<Element> components = document.getRootElement().elements("component");
        for (Element component : components) {
            String name = trimToEmpty(component.elementText("name"));
            String componentClassName = trimToEmpty(component.elementText("class"));

            String componentLoaderClassName = trimToEmpty(component.elementText("componentLoader"));
            String tag = trimToEmpty(component.elementText("tag"));
            if (StringUtils.isEmpty(tag)) {
                tag = name;
            }

            Class<?> componentLoaderClass = classLoader.loadClass(componentLoaderClassName);
            Class<?> componentClass = classLoader.loadClass(componentClassName);

            if (Component.class.isAssignableFrom(componentClass)) {
                log.trace("Register component {} class {}", name, componentClass.getCanonicalName());

                webComponentsFactory.register(name, (Class<? extends Component>) componentClass);
            } else {
                log.warn("Component {} is not a subclass of com.haulmont.cuba.gui.components.Component", componentClassName);
            }

            if (ComponentLoader.class.isAssignableFrom(componentLoaderClass)) {
                log.trace("Register tag {} loader {}", tag, componentLoaderClass.getCanonicalName());

                LayoutLoaderConfig.registerLoader(tag, (Class<? extends ComponentLoader>) componentLoaderClass);
            } else {
                log.warn("Component loader {} is not a subclass of com.haulmont.cuba.gui.xml.layout.ComponentLoader",
                        componentLoaderClassName);
            }
        }
    }
}