/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import javax.swing.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class JTabbedPaneExt extends JTabbedPane {

    protected static boolean focusOnSelectionChange = true;

    @Override
    public void requestFocus() {
        if (focusOnSelectionChange) {
            super.requestFocus();
        }
    }

    public static boolean isFocusOnSelectionChange() {
        return focusOnSelectionChange;
    }

    public static void setFocusOnSelectionChange(boolean focusOnSelectionChange) {
        JTabbedPaneExt.focusOnSelectionChange = focusOnSelectionChange;
    }
}