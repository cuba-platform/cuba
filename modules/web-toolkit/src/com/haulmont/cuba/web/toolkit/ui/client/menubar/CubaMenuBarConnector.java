/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.menubar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.menubar.MenuBarConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaMenuBar.class)
public class CubaMenuBarConnector extends MenuBarConnector {

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);
    }

    @Override
    public CubaMenuBarState getState() {
        return (CubaMenuBarState) super.getState();
    }

    @Override
    public CubaMenuBarWidget getWidget() {
        return (CubaMenuBarWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaMenuBarWidget.class);
    }

    @Override
    public void layout() {
        super.layout();

        if (getState().vertical) {
            getWidget().addStyleName("cuba-menubar-vertical");
        } else {
            getWidget().removeStyleName("cuba-menubar-vertical");
        }
    }

    @Override
    public boolean isUseMoreMenuItem() {
        return !getState().vertical;
    }
}