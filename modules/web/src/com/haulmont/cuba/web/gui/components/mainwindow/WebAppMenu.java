/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.sys.MenuBuilder;
import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;

/**
 * @author artamonov
 * @version $Id$
 */
public class WebAppMenu extends WebAbstractComponent<CubaMenuBar> implements AppMenu {

    public static final String MENU_STYLENAME = "cuba-main-menu";

    public WebAppMenu() {
        component = new CubaMenuBar();
        component.addStyleName(MENU_STYLENAME);
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(MENU_STYLENAME);
    }

    @Override
    public void loadMenu() {
        MenuBuilder menuBuilder = new MenuBuilder(this);
        menuBuilder.build();
    }
}