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

import com.haulmont.cuba.desktop.gui.components.DesktopComponent.HasContextHelpClickHandler;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.ToolTipButton;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Component.ContextHelpIconClickEvent;
import com.haulmont.cuba.gui.components.Component.HasContextHelp;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ComponentCaption extends JPanel {

    protected Component owner;
    protected JLabel label;
    protected ToolTipButton toolTipButton;
    protected ActionListener toolTipButtonActionListener;

    public ComponentCaption(Component owner) {
        BoxLayoutAdapter.create(this);
        this.owner = owner;
        takeOwnerProperties();
    }

    private void takeOwnerProperties() {
        if (!(owner instanceof DesktopCheckBox)) {
            if (label == null) {
                label = new JLabel();
                add(label);
            }

            label.setText(((Component.HasCaption) owner).getCaption());
        }

        String contextHelpText = getContextHelpText();
        boolean hasContextHelpIconClickListeners = hasContextHelpIconClickListeners();
        if (StringUtils.isNotEmpty(contextHelpText)
                || hasContextHelpIconClickListeners) {
            if (toolTipButton == null) {
                toolTipButton = new ToolTipButton();
                toolTipButton.setFocusable(false);
                add(toolTipButton);
            }

            if (hasContextHelpIconClickListeners) {
                if (toolTipButtonActionListener == null) {
                    toolTipButtonActionListener = e ->
                            fireContextHelpIconClickEvent();
                    toolTipButton.addActionListener(toolTipButtonActionListener);
                }

                toolTipButton.setToolTipText(null);
            } else {
                removeToolTipButtonActionListener();

                toolTipButton.setToolTipText(contextHelpText);
            }

            DesktopToolTipManager.getInstance().registerTooltip(toolTipButton);
        } else if (toolTipButton != null) {
            removeToolTipButtonActionListener();
            remove(toolTipButton);
            toolTipButton = null;
        }

        setVisible(owner.isVisibleRecursive());
        setEnabled(owner.isEnabledRecursive());
    }

    protected void removeToolTipButtonActionListener() {
        if (toolTipButtonActionListener != null) {
            toolTipButton.removeActionListener(toolTipButtonActionListener);
            toolTipButtonActionListener = null;
        }
    }

    protected String getContextHelpText() {
        if (owner instanceof HasContextHelp) {
            return DesktopComponentsHelper.getContextHelpText(
                    ((HasContextHelp) owner).getContextHelpText(),
                    ((HasContextHelp) owner).isContextHelpTextHtmlEnabled());
        }
        return null;
    }

    protected boolean hasContextHelpIconClickListeners() {
        return owner instanceof HasContextHelpClickHandler
                && ((HasContextHelpClickHandler) owner).getContextHelpIconClickHandler() != null;
    }

    protected void fireContextHelpIconClickEvent() {
        if (owner instanceof HasContextHelpClickHandler) {
            ContextHelpIconClickEvent event = new ContextHelpIconClickEvent((HasContextHelp) owner);
            ((HasContextHelpClickHandler) owner).fireContextHelpIconClickEvent(event);
        }
    }

    public void update() {
        takeOwnerProperties();
    }
}