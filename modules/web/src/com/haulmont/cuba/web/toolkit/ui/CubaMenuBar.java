/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.menubar.CubaMenuBarState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMenuBar extends com.vaadin.ui.MenuBar {

    @Override
    protected CubaMenuBarState getState() {
        return (CubaMenuBarState) super.getState();
    }

    @Override
    protected CubaMenuBarState getState(boolean markAsDirty) {
        return (CubaMenuBarState) super.getState(markAsDirty);
    }

    public boolean isVertical() {
        return getState(false).vertical;
    }

    public void setVertical(boolean useMoreMenuItem) {
        if (useMoreMenuItem != isVertical()) {
            getState().vertical = useMoreMenuItem;
        }
    }
}