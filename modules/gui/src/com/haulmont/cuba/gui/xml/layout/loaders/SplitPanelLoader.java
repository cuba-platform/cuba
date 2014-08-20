/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.Collection;

/**
 * @author abramov
 * @version $Id$
 */
public class SplitPanelLoader extends ContainerLoader{

    public SplitPanelLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final SplitPanel component = factory.createComponent(SplitPanel.NAME);

        assignXmlDescriptor(component, element);
        loadId(component, element);

        final String orientation = element.attributeValue("orientation");
        if (StringUtils.isEmpty(orientation)) {
            component.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        }

        loadVisible(component, element);
        loadStyleName(component, element);

        final Collection<Component> components = loadSubComponents(component, element, "visible");
        if (components.size() == 0) {
            throw new GuiDevelopmentException("Split panel must contain at least one child component",
                    context.getFullFrameId());
        } else if (components.size() == 1) {
            component.add(factory.createComponent(BoxLayout.VBOX));
        }

        final String pos = element.attributeValue("pos");
        if (!StringUtils.isEmpty(pos)) {
            component.setSplitPosition(Integer.parseInt(pos));
        }

        loadHeight(component, element, "-1px");
        loadWidth(component, element, "-1px");

        assignFrame(component);

        return component;
    }
}