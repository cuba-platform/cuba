/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class AppWorkAreaLoader extends ContainerLoader {

    public AppWorkAreaLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        AppWorkArea component = factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(AppWorkArea component, Element element, Component parent) {
        loadId(component, element);

        loadEnable(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);
        loadAlign(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        assignFrame(component);

        Element initialLayoutElement = element.element("initialLayout");
        if (initialLayoutElement != null) {
            com.haulmont.cuba.gui.xml.layout.ComponentLoader boxLoader = getLoader(BoxLayout.VBOX);
            VBoxLayout initialLayout = (VBoxLayout) boxLoader.loadComponent(factory, initialLayoutElement, null);
            component.setInitialLayout(initialLayout);
        }
    }
}