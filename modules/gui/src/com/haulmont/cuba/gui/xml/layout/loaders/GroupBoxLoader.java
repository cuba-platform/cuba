/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 17:57:24
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Layout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class GroupBoxLoader  extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    public GroupBoxLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Layout component = factory.createComponent("groupBox");

        final Element captionElement = element.element("caption");
        if (captionElement != null) {
            String caption = captionElement.attributeValue("label");
            if (!StringUtils.isEmpty(caption)) {
                caption = loadResourceString(caption);
                ((Component.HasCaption) component).setCaption(caption);
            }
        }

        loadAlign(component, element);
        loadSubcomponentsAndExpand(component, element, "caption", "visible");

        return component;
    }
}