/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopButtonsPanel extends DesktopHBox implements ButtonsPanel {

    public DesktopButtonsPanel() {
        setSpacing(true);
    }

    public void setFocusableForAllButtons(boolean focusable) {
        for (Component button : getComponents()) {
            JComponent jButton = DesktopComponentsHelper.unwrap(button);
            if (button instanceof DesktopButton) {
                ((DesktopButton) button).setShouldBeFocused(focusable);
            }
            jButton.setFocusable(focusable);
        }
    }
}