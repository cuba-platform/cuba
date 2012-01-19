/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.theme.impl;

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.desktop.theme.ComponentDecorator;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;

import javax.swing.*;
import java.util.Set;

/**
 * Assigns icon to swing or cuba label.
 * Can be used also for table cells.
 *
 * @author Alexander Budarov
 * @version $Id$
 */
public class IconDecorator implements ComponentDecorator {
    private String iconName;
    private Icon icon;

    public IconDecorator(String iconName) {
        this.iconName = iconName;
    }

    @Override
    public void decorate(Object component, Set<String> state) {
        JLabel label;
        if (component instanceof JLabel) {
            label = (JLabel) component;
        } else if (component instanceof Label) {
            label = (JLabel) DesktopComponentsHelper.unwrap((Component) component);
        } else {
            throw new RuntimeException("Component is not suitable: " + component);
        }

        Icon icon = App.getInstance().getResources().getIcon(iconName);
        label.setIcon(icon);
    }
}
