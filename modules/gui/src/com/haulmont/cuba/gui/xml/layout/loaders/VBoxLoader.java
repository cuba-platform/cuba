/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class VBoxLoader extends ContainerLoader implements ComponentLoader {

    public VBoxLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        BoxLayout component = factory.createComponent(VBoxLayout.class);

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(BoxLayout component, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadEnable(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

        loadSpacing(component, element);
        loadMargin(component, element);

        loadSubComponentsAndExpand(component, element, "visible");

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);
    }
}