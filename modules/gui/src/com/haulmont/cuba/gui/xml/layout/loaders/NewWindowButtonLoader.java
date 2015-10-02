/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.mainwindow.NewWindowButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class NewWindowButtonLoader extends AbstractComponentLoader<NewWindowButton> {
    @Override
    public void createComponent() {
        resultComponent = (NewWindowButton) factory.createComponent(NewWindowButton.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);
        loadIcon(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);
    }
}