/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.menubar;

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