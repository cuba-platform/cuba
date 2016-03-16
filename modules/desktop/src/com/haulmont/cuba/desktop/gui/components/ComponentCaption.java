/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.ToolTipButton;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;

/**
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