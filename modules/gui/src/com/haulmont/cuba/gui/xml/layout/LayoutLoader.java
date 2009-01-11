/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:15:51
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.DsContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;

public class LayoutLoader {
    private ComponentsFactory factory;
    private LayoutLoaderConfig config;
    private DsContext dsContext;

    public LayoutLoader(ComponentsFactory factory, LayoutLoaderConfig config, DsContext dsContext) {
        this.factory = factory;
        this.config = config;
        this.dsContext = dsContext;
    }

    public Component loadComponent(URL uri) {
        try {
            final InputStream stream = uri.openStream();

            SAXReader reader = new SAXReader();
            Document doc;
            try {
                doc = reader.read(stream);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

            Element element = doc.getRootElement();

            return loadComponent(element);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected ComponentLoader getLoader(Element element) throws InstantiationException, IllegalAccessException {
        Class<? extends ComponentLoader> loaderClass = config.getLoader(element.getName());
        if (loaderClass == null) {
            throw new IllegalStateException(String.format("Unknown component '%s'", element.getName()));
        }

        ComponentLoader loader;
        try {
            final Constructor<? extends ComponentLoader> constructor =
                    loaderClass.getConstructor(LayoutLoaderConfig.class, ComponentsFactory.class, DsContext.class);
            loader = constructor.newInstance(config, factory, dsContext);
        } catch (Throwable e) {
            loader = loaderClass.newInstance();
        }

        return loader;
    }

    public <T extends Component> T loadComponent(Element element) {
        try {
            ComponentLoader loader = getLoader(element);
            return (T) loader.loadComponent(factory, element);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

