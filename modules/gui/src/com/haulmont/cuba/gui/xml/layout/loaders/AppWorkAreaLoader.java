/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class AppWorkAreaLoader extends ContainerLoader<AppWorkArea> {

    protected ComponentLoader initialLayoutLoader;

    @Override
    public void createComponent() {
        resultComponent = (AppWorkArea) factory.createComponent(AppWorkArea.NAME);
        loadId(resultComponent, element);

        Element initialLayoutElement = element.element("initialLayout");
        initialLayoutLoader = getLoader(initialLayoutElement, VBoxLayout.NAME);
        initialLayoutLoader.createComponent();
        VBoxLayout initialLayout = (VBoxLayout) initialLayoutLoader.getResultComponent();
        resultComponent.setInitialLayout(initialLayout);
    }

    @Override
    public void loadComponent() {
        loadId(resultComponent, element);
        assignFrame(resultComponent);

        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        initialLayoutLoader.loadComponent();
    }

    @Override
    public void setMessagesPack(String messagesPack) {
        super.setMessagesPack(messagesPack);

        if (initialLayoutLoader != null) {
            initialLayoutLoader.setMessagesPack(messagesPack);
        }
    }
}