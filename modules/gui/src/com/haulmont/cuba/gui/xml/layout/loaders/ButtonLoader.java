/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.12.2008 15:55:55
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.dom4j.Element;

public class ButtonLoader extends com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoader {
    public ButtonLoader(Context context) {
        super(context);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final Button component = factory.createComponent(element.getName());

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);
        loadDescription(component, element);
        loadAction(component, element);
        loadIcon(component, element);

        loadExpandable(component, element);

        loadWidth(component, element);
        loadAlign(component, element);

        assignFrame(component);

        return component;
    }
}
