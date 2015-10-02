/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.PopupButton;
import org.apache.commons.lang.StringUtils;

/**
 * @author pavlov
 * @version $Id$
 */
public class PopupButtonLoader extends AbstractComponentLoader<PopupButton> {
    @Override
    public void createComponent() {
        resultComponent = (PopupButton) factory.createComponent(PopupButton.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);
        loadAlign(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);
        loadIcon(resultComponent, element);

        loadWidth(resultComponent, element);

        loadActions(resultComponent, element);

        String menuWidth = element.attributeValue("menuWidth");
        if (!StringUtils.isEmpty(menuWidth)) {
            resultComponent.setMenuWidth(menuWidth);
        }
    }
}