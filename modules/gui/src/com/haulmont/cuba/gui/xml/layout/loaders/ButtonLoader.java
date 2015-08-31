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
        final Button component = (Button) factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(Button component, Element element, Component parent) {
        assignXmlDescriptor(component, element);
        loadId(component, element);

        boolean enabled = loadEnable(component, element);
        boolean visible = loadVisible(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);
        loadDescription(component, element);
        loadAction(component, element);
        loadIcon(component, element);

        loadWidth(component, element);
        loadHeight(component, element);
        loadAlign(component, element);

        assignFrame(component);

        loadInvoke(component, enabled, visible, element);
    }

    protected void loadInvoke(Button component, boolean enabled, boolean visible, Element element) {
        if (!StringUtils.isBlank(element.attributeValue("action"))) {
            return;
        }

        final String methodName = element.attributeValue("invoke");
        if (StringUtils.isBlank(methodName)) {
            return;
        }

        String actionBaseId = component.getId();
        if (StringUtils.isEmpty(actionBaseId)) {
            actionBaseId = methodName;
        }

        DeclarativeAction action = new DeclarativeAction(actionBaseId + "_invoke",
                component.getCaption(), component.getDescription(), component.getIcon(),
                enabled, visible,
                methodName,
                component.getFrame()
        );
        component.setAction(action);
    }
}