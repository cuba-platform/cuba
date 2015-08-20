/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.mainwindow.LogoutButton;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class LogoutButtonLoader extends ComponentLoader {
    public LogoutButtonLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        LogoutButton component = (LogoutButton) factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(LogoutButton component, Element element, Component parent) {
        loadId(component, element);

        loadCaption(component, element);
        loadDescription(component, element);

        loadStyleName(component, element);
        loadAlign(component, element);
        loadIcon(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        loadEnable(component, element);
        loadVisible(component, element);

        assignFrame(component);
    }
}