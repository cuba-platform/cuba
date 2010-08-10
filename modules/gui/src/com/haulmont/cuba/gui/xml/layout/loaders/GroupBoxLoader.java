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
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.Layout;
import com.haulmont.cuba.gui.components.GroupBox;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class GroupBoxLoader extends ContainerLoader implements com.haulmont.cuba.gui.xml.layout.ComponentLoader {
    public GroupBoxLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final GroupBox component = factory.createComponent("groupBox");

        final Element captionElement = element.element("caption");
        if (captionElement != null) {
            String caption = captionElement.attributeValue("label");
            if (!StringUtils.isEmpty(caption)) {
                caption = loadResourceString(caption);
                component.setCaption(caption);
            }
        }

        loadAlign(component, element);
        loadSubComponentsAndExpand(component, element, "caption", "visible");

        loadExpandable(component, element);

        loadCollapsable(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        return component;
    }

    private void loadCollapsable(GroupBox component, Element element) {
        String collapsable = element.attributeValue("collapsable");
        if (!StringUtils.isEmpty(collapsable)) {
            boolean b = BooleanUtils.toBoolean(collapsable);
            component.setCollapsable(b);
            if (b) {
                String expanded = element.attributeValue("expanded");
                if (!StringUtils.isBlank(expanded)) {
                    component.setExpanded(BooleanUtils.toBoolean(expanded));
                }
            }
        }
    }

}