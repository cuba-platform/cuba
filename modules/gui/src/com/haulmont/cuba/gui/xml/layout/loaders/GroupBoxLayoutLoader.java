/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class GroupBoxLayoutLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {

    public GroupBoxLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {

        final GroupBoxLayout component = factory.createComponent("groupBox");

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadCaption(component, element);

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

        loadOrientation(component, element);

        loadSubComponentsAndExpand(component, element, "caption", "description", "visible");

        loadCollapsible(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadSpacing(component, element);

        assignFrame(component);

        return component;
    }

    protected void loadOrientation(GroupBoxLayout component, Element element) {
        String orientation = element.attributeValue("orientation");
        if (orientation == null) {
            component.setOrientation(GroupBoxLayout.Orientation.VERTICAL);
            return;
        }

        if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(GroupBoxLayout.Orientation.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(GroupBoxLayout.Orientation.VERTICAL);
        } else {
            throw new GuiDevelopmentException("Invalid groupBox orientation value: " + orientation, context.getFullFrameId());
        }
    }
}