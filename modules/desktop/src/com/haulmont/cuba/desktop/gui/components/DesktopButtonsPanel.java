/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopButtonsPanel extends DesktopHBox implements ButtonsPanel {

    public DesktopButtonsPanel() {
        setSpacing(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        Collection<Component> components = getComponents();
        for (Component button : components) {
            button.setEnabled(enabled);
        }
    }

    public void setFocusableForAllButtons(boolean focusable) {
        for (Component button : getComponents()) {
            JComponent jButton = DesktopComponentsHelper.unwrap(button);
            jButton.setFocusable(focusable);
        }
    }
}