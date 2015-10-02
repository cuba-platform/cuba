/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.SplitPanel;
import com.haulmont.cuba.gui.components.VBoxLayout;
import org.apache.commons.lang.StringUtils;

/**
 * @author abramov
 * @version $Id$
 */
public class SplitPanelLoader extends ContainerLoader<SplitPanel> {
    @Override
    public void createComponent() {
        resultComponent = (SplitPanel) factory.createComponent(SplitPanel.NAME);
        loadId(resultComponent, element);

        String orientation = element.attributeValue("orientation");
        if (StringUtils.isEmpty(orientation)) {
            resultComponent.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            resultComponent.setOrientation(SplitPanel.ORIENTATION_VERTICAL);
        } else if ("horizontal".equalsIgnoreCase(orientation)) {
            resultComponent.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        }

        createSubComponents(resultComponent, element);
        if (resultComponent.getOwnComponents().size() == 1) {
            resultComponent.add(factory.createComponent(VBoxLayout.NAME));
        }
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadStyleName(resultComponent, element);

        String pos = element.attributeValue("pos");
        if (!StringUtils.isEmpty(pos)) {
            resultComponent.setSplitPosition(Integer.parseInt(pos));
        }

        loadHeight(resultComponent, element, Component.AUTO_SIZE);
        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadAlign(resultComponent, element);

        loadSubComponents();
    }
}