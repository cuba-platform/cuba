/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:27:37
 * $Id: IFrameLoader.java 69 2009-01-22 12:19:45Z abramov $
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.ConfigProvider;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.ScriptingProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoader;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.io.InputStream;

public class IFrameLoader extends ContainerLoader implements ComponentLoader {

    public IFrameLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final String src = element.attributeValue("src");

        final LayoutLoader loader = new LayoutLoader(context, factory, LayoutLoaderConfig.getFrameLoaders());
        loader.setLocale(getLocale());
        loader.setMessagesPack(getMessagesPack());

        InputStream stream = null;
        if (ConfigProvider.getConfig(GlobalConfig.class).isGroovyClassLoaderEnabled()) {
            stream = ScriptingProvider.getResourceAsStream(src);
        }
        if (stream == null) {
            stream = getClass().getResourceAsStream(src);
            if (stream == null) {
                throw new RuntimeException("Bad template path: " + src);
            }
        }

        final IFrame component = (IFrame) loader.loadComponent(stream, parent, context.getParams());
        if (component.getMessagesPack() == null) {
            component.setMessagesPack(messagesPack);
        }

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

        loadHeight(component, element, "-1px");
        loadWidth(component, element);

        if (context.getFrame() != null)
            component.setFrame(context.getFrame());

        return component;
    }
}