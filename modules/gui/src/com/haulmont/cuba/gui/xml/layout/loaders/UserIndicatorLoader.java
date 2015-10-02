/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;

/**
 * @author artamonov
 * @version $Id$
 */
public class UserIndicatorLoader extends AbstractComponentLoader<UserIndicator> {
    @Override
    public void createComponent() {
        resultComponent = (UserIndicator) factory.createComponent(UserIndicator.NAME);
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

        context.addPostInitTask((context1, window) -> resultComponent.refreshUserSubstitutions());
    }
}