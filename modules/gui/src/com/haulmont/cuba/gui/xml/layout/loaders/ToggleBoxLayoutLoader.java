/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ToggleBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

import java.util.Collections;

/**
 * @author gorodnov
 * @version $Id$
 */
public class ToggleBoxLayoutLoader extends ContainerLoader
        implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {

    public ToggleBoxLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final ToggleBoxLayout component = factory.createComponent(ToggleBoxLayout.NAME);

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

        Element onElement = element.element("on");

        if (onElement == null)
            throw new GuiDevelopmentException("Can't find 'on' element", context.getFullFrameId(),
                    Collections.<String,Object>singletonMap("ToggleBox ID",element.attributeValue("id")));

        Element offElement = element.element("off");
        if (offElement == null)
            throw new GuiDevelopmentException("Can't find 'off' element", context.getFullFrameId(),
                    Collections.<String,Object>singletonMap("ToggleBox ID", element.attributeValue("id")));

        loadSubComponents(component.getOnLayout(), onElement, "visible");
        loadSubComponents(component.getOffLayout(), offElement, "visible");

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);

        return component;
    }
}
