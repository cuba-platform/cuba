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
import com.haulmont.cuba.gui.components.GroupBox;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class GroupBoxLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    public GroupBoxLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final GroupBox component = factory.createComponent("groupBox");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadCaption(component, element);
        loadDescription(component, element);

        if (StringUtils.isEmpty(component.getCaption())) {
            // for backward compatibility
            Element captionElement = element.element("caption");
            if (captionElement != null) {
                String caption = captionElement.attributeValue("label");
                if (!StringUtils.isEmpty(caption)) {
                    caption = loadResourceString(caption);
                    component.setCaption(caption);
                }
            }
        }

        if (StringUtils.isEmpty(component.getCaption())) {
            // for backward compatibility
            Element descriptionElement = element.element("description");
            if (descriptionElement != null) {
                String description = descriptionElement.attributeValue("label");
                if (!StringUtils.isEmpty(description)) {
                    description = loadResourceString(description);
                    component.setDescription(description);
                }
            }
        }

        loadAlign(component, element);
        loadVisible(component, element);
        loadEnable(component, element);
        loadSubComponentsAndExpand(component, element, "caption", "description", "visible");

        loadCollapsible(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadSpacing(component, element);

        assignFrame(component);

        return component;
    }
}