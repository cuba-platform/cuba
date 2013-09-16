/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.ToolTipButton;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;

/**
 * @author budarov
 * @version $Id$
 */
public class ComponentCaption extends JPanel {

    private Component owner;
    private JLabel label;
    private ToolTipButton toolTipButton;

    public ComponentCaption(Component owner) {
        BoxLayoutAdapter.create(this);
        this.owner = owner;
        takeOwnerProperties();
    }

    private void takeOwnerProperties() {
        if (label == null) {
            label = new JLabel();
            add(label);
        }

        label.setText(((Component.HasCaption) owner).getCaption());
        if (((Component.HasCaption) owner).getDescription() != null) {
            if (toolTipButton == null) {
                toolTipButton = new ToolTipButton();
                toolTipButton.setFocusable(false);
                DesktopToolTipManager.getInstance().registerTooltip(toolTipButton);
                add(toolTipButton);
            }
            toolTipButton.setToolTipText(((Component.HasCaption) owner).getDescription());
        } else if (toolTipButton != null) {
            remove(toolTipButton);
            toolTipButton = null;
        }

        setVisible(owner.isVisible());
        setEnabled(owner.isEnabled());
    }

    public void update() {
        takeOwnerProperties();
    }
}