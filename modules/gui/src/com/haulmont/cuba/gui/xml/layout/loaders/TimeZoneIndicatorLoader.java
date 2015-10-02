/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.mainwindow.TimeZoneIndicator;

/**
 * @author artamonov
 * @version $Id$
 */
public class TimeZoneIndicatorLoader extends AbstractComponentLoader<TimeZoneIndicator> {
    @Override
    public void createComponent() {
        resultComponent = (TimeZoneIndicator) factory.createComponent(TimeZoneIndicator.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadStyleName(resultComponent, element);
        loadAlign(resultComponent, element);

        loadWidth(resultComponent, element);
        loadHeight(resultComponent, element);

        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);
    }
}