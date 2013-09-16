/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.xml.DeclarativeAction;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class ButtonLoader extends com.haulmont.cuba.gui.xml.layout.loaders.ComponentLoader {
    public ButtonLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
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

        loadWidth(component, element);
        loadAlign(component, element);

        assignFrame(component);

        loadInvoke(component, element);

        return component;
    }

    protected void loadInvoke(Button component, Element element) {
        if (!StringUtils.isBlank(element.attributeValue("action"))) {
            return;
        }

        final String methodName = element.attributeValue("invoke");
        if (StringUtils.isBlank(methodName)) {
            return;
        }

        DeclarativeAction action = new DeclarativeAction(component.getId() + "_action",
                component.getCaption(), component.getIcon(),
                component.isEnabled(), component.isVisible(),
                methodName,
                component.getFrame()
        );
        component.setAction(action);
    }
}