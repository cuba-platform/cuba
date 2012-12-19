/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ToggleBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class ToggleBoxLayoutLoader extends ContainerLoader
        implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {

    public ToggleBoxLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent
    ) throws InstantiationException, IllegalAccessException {
        final ToggleBoxLayout component = factory.createComponent(ToggleBoxLayout.NAME);

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadAlign(component, element);

        Element onElement = element.element("on");
        if (onElement == null) {
            throw new IllegalStateException("Cannot find 'on' element");
        }

        Element offElement = element.element("off");
        if (offElement == null) {
            throw new IllegalStateException("Cannot find 'off' element");
        }

        loadSubComponents(component.getOnLayout(), onElement, "visible");
        loadSubComponents(component.getOffLayout(), offElement, "visible");

        loadHeight(component, element);
        loadWidth(component, element);

        assignFrame(component);

        return component;
    }
}
