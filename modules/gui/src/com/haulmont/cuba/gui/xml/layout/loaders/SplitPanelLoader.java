/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 20.01.2009 17:20:11
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Collection;

public class SplitPanelLoader extends ContainerLoader{
    public SplitPanelLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final SplitPanel component = factory.createComponent("split");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        final Collection<Component> components = loadSubComponents(component, element, "visible");
        for (Component c : components) {
            c.setWidth("100%");
            c.setHeight("100%");
        }

        final String orientation = element.attributeValue("orientation");
        if (StringUtils.isEmpty(orientation)) {
            component.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        }

        final String pos = element.attributeValue("pos");
        if (!StringUtils.isEmpty(pos)) {
            component.setSplitPosition(Integer.valueOf(pos), Component.UNITS_PERCENTAGE);
        }

        return component;
    }
}
