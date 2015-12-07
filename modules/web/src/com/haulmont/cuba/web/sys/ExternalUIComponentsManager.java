/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.sys;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.web.gui.WebComponentsFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

/**
 * Class registers external components that are supplied in separate jars or defined in 'cuba-ui-component.xml'
 * descriptor of 'web' module.
 *
 * @author gorbunkov
 * @version $Id$
 */
@org.springframework.stereotype.Component("cuba_ExternalUIComponentsManager")
public class ExternalUIComponentsManager {

    protected static final Log log = LogFactory.getLog(ExternalUIComponentsManager.class);

    @PostConstruct
    public void postConstruct() {
        try {
            registerComponents();
        } catch (Exception e) {
            log.error("Error on custom UI components registration", e);
        }
    }

    /**
     * Method finds all components descriptors ({@code cuba-ui-g component.xml} files in the {@code META-INF} directory of
     * the component jar), parses them and registers UI components and their loaders.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void registerComponents() throws IOException, ClassNotFoundException {
        //register components from external component jars
        _registerComponents("META-INF/cuba-ui-component.xml");
        //register components from web modules
        _registerComponents("cuba-ui-component.xml");
    }

    protected void _registerComponents(String componentDescriptorPath) throws IOException, ClassNotFoundException {
        Enumeration<URL> resources = ExternalUIComponentsManager.class.getClassLoader().getResources(componentDescriptorPath);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            try (InputStream is = url.openStream()) {
                Document document = Dom4j.readDocument(is);
                List<Element> components = document.getRootElement().elements("component");
                for (Element component : components) {
                    String name = component.elementText("name");
                    String componentLoaderClassName = component.elementText("componentLoader");
                    String componentClassName = component.elementText("class");

                    Class<?> componentLoaderClass = Class.forName(componentLoaderClassName);
                    Class<?> componentClass = Class.forName(componentClassName);

                    if (Component.class.isAssignableFrom(componentClass)) {
                        WebComponentsFactory.registerComponent(name, (Class<? extends Component>) componentClass);
                    } else {
                        log.warn("Component " + componentClassName + " is not a subclass of com.haulmont.cuba.gui.components.Component");
                    }

                    if (ComponentLoader.class.isAssignableFrom(componentLoaderClass)) {
                        LayoutLoaderConfig.registerLoader(name, (Class<? extends ComponentLoader>) componentLoaderClass);
                    } else {
                        log.warn("Component loader " + componentLoaderClassName + " is not a subclass of com.haulmont.cuba.gui.xml.layout.ComponentLoader");
                    }
                }
            }
        }
    }
}
