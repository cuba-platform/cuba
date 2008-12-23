/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 17:57:24
 * $Id$
 */
package com.haulmont.cuba.gui.xml.loaders;

import com.haulmont.cuba.gui.xml.ComponentLoader;
import com.haulmont.cuba.gui.xml.ComponentsLoaderConfig;
import com.haulmont.cuba.gui.xml.ComponentsFactory;
import com.haulmont.cuba.gui.components.Component;
import org.dom4j.Element;

public class GroupBoxLoader  extends ContainerLoader implements ComponentLoader {
    public GroupBoxLoader(ComponentsLoaderConfig config, ComponentsFactory factory) {
        super(config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Component component = factory.createComponent("groupbox");

        final Element captionElement = element.element("caption");
        if (captionElement != null) {
            final String caption = captionElement.attributeValue("label");
            ((Component.HasCaption) component).setCaption(caption);
        }

        loadAlign(component, element);
        loadPack(component, element);

        loadSubComponents(component, element, "caption");

        return component;
    }
}