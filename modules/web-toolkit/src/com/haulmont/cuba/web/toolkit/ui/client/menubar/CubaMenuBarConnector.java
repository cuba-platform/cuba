/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.menubar;

import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VMenuBar;
import com.vaadin.client.ui.menubar.MenuBarConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaMenuBar.class)
public class CubaMenuBarConnector extends MenuBarConnector {

    @Override
    public CubaMenuBarState getState() {
        return (CubaMenuBarState) super.getState();
    }

    @Override
    public CubaMenuBarWidget getWidget() {
        return (CubaMenuBarWidget) super.getWidget();
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
    protected boolean isUseMoreMenuItem() {
        return !getState().vertical;
    }

    @Override
    protected String getItemId(UIDL uidl) {
        if (uidl.hasAttribute("tid")) {
            return uidl.getStringAttribute("tid");
        }

        return null;
    }

    @Override
    protected void assignAdditionalAttributes(VMenuBar.CustomMenuItem currentItem, UIDL item) {
        if (item.hasAttribute("cid")) {
            currentItem.getElement().setAttribute("cuba-id", item.getStringAttribute("cid"));
        }
    }
}