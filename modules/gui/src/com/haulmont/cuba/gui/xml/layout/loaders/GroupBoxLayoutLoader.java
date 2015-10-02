/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class GroupBoxLayoutLoader extends ContainerLoader<GroupBoxLayout> {

    @Override
    public void createComponent() {
        resultComponent = (GroupBoxLayout) factory.createComponent(GroupBoxLayout.NAME);
        loadId(resultComponent, element);
        loadOrientation(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadAlign(resultComponent, element);
        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadOrientation(resultComponent, element);

        loadCollapsible(resultComponent, element, false);

        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadSpacing(resultComponent, element);

        loadSubComponentsAndExpand(resultComponent, element);
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