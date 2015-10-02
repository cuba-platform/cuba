/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.xml.DeclarativeAction;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class ButtonLoader extends AbstractComponentLoader<Button> {

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

    @Override
    public void createComponent() {
        resultComponent = (Button) factory.createComponent(Button.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        boolean enabled = loadEnable(resultComponent, element);
        boolean visible = loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadAction(resultComponent, element);
        loadIcon(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);
        loadAlign(resultComponent, element);

        loadInvoke(resultComponent, enabled, visible, element);
    }
}