/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopButton extends DesktopAbstractComponent<JButton> implements Button {

    public DesktopButton() {
        jComponent = new JButton();
    }

    public com.haulmont.cuba.gui.components.Action getAction() {
        return null;
    }

    public void setAction(Action action) {
    }

    public String getCaption() {
        return jComponent.getText();
    }

    public void setCaption(String caption) {
        jComponent.setText(caption);
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
    }

    public String getIcon() {
        return null;
    }

    public void setIcon(String icon) {
    }
}
