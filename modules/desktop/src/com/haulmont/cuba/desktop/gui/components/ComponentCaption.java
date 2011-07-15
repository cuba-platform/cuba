/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class ComponentCaption extends JLabel {

    private Component owner;

    public ComponentCaption(Component owner) {
        this.owner = owner;
        takeOwnerProperties();
    }

    private void takeOwnerProperties() {
        setText(((Component.HasCaption) owner).getCaption());
        setVisible(owner.isVisible());
        setEnabled(owner.isEnabled());
    }

    public void update() {
        takeOwnerProperties();
    }
}
