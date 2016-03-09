/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.OptionsGroup;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class OptionsGroupLoader extends AbstractOptionsBaseLoader<OptionsGroup> {

    protected void loadOrientation(OptionsGroup component, Element element) {
        String orientation = element.attributeValue("orientation");

        if (orientation == null) {
            return;
        }

        if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(OptionsGroup.Orientation.HORIZONTAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(OptionsGroup.Orientation.VERTICAL);
        } else {
            throw new GuiDevelopmentException("Invalid orientation value for option group: " +
                    orientation, context.getFullFrameId(), "OptionsGroup ID", component.getId());
        }
    }

    @Override
    public void createComponent() {
        resultComponent = (OptionsGroup) factory.createComponent(OptionsGroup.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        loadDescription(resultComponent, element);
    }
}