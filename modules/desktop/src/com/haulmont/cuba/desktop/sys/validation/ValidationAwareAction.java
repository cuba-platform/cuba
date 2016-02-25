/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.validation;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Yuriy Artamonov
 */
public abstract class ValidationAwareAction extends AbstractAction {
    @Override
    public void actionPerformed(final ActionEvent e) {
        final RootPaneContainer window;
        if (e.getSource() instanceof Component) {
            window = DesktopComponentsHelper.getSwingWindow((Component) e.getSource());
        } else {
            window = null;
        }

        ValidationAlertHolder.runIfValid(new Runnable() {
            @Override
            public void run() {
                if (window == null
                        || window.getGlassPane() == null
                        || !window.getGlassPane().isVisible()) {
                    // check modal dialogs on the front of current component
                    actionPerformedAfterValidation(e);
                }
            }
        });
    }

    public abstract void actionPerformedAfterValidation(ActionEvent e);
}