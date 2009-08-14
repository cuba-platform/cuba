/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 12.08.2009 15:14:59
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ToggleBoxLayout;
import com.haulmont.cuba.gui.xml.layout.*;
import org.dom4j.Element;

public class ToggleBoxLoader extends ContainerLoader
        implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {

    public ToggleBoxLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(
            ComponentsFactory factory,
            Element element,
            Component parent
    ) throws InstantiationException, IllegalAccessException {
        final ToggleBoxLayout component = factory.createComponent("togglebox");

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

        loadExpandable(component, element);

        return component;
    }
}
