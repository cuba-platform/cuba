/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 23.04.2009 15:33:04
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

public class ScrollBoxLayoutLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    public ScrollBoxLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final ScrollBoxLayout component = factory.createComponent("scrollbox");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

//        loadSpacing(component, element);
//        loadMargin(component, element);

        loadSubComponentsAndExpand(component, element, "visible");

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);

        return component;
    }
}