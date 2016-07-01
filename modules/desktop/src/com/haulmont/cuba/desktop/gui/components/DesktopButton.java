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

import com.haulmont.cuba.desktop.App;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareActionListener;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DesktopButton extends DesktopAbstractComponent<JButton> implements Button {

    protected Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    protected Action action;
    protected String caption;
    protected String icon;

    protected long responseEndTs = 0;
    protected boolean shouldBeFocused = true;

    protected PropertyChangeListener actionPropertyChangeListener;

    public DesktopButton() {
        impl = createImplementation();
        impl.addActionListener(new ValidationAwareActionListener() {
            @Override
            public void actionPerformedAfterValidation(ActionEvent e) {
                if (action != null) {
                    if (shouldBeFocused && !impl.isFocusOwner() && impl.isFocusable()) {
                        return;
                    }

                    if (e.getWhen() <= responseEndTs) {
                        return;
                    }

                    try {
                        userActionsLog.trace("Button (id = {}, caption = {}) on frame {} was clicked", id, caption,
                                frame == null ? " NULL " : frame.getId());

                        action.actionPerform(DesktopButton.this);
                    } finally {
                        responseEndTs = System.currentTimeMillis();
                    }
                }
            }
        });
        DesktopComponentsHelper.adjustSize(impl);
    }

    protected JButton createImplementation() {
        return new JButton();
    }

    @Override
    public com.haulmont.cuba.gui.components.Action getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        if (action != this.action) {
            if (this.action != null) {
                this.action.removeOwner(this);
                this.action.removePropertyChangeListener(actionPropertyChangeListener);
            }

            this.action = action;

            if (action != null) {
                String caption = action.getCaption();
                if (caption != null && getCaption() == null) {
                    setCaption(caption);
                }

                String description = action.getDescription();
                if (description == null && action.getShortcutCombination() != null) {
                    description = action.getShortcutCombination().format();
                }
                if (description != null && getDescription() == null) {
                    setDescription(description);
                }

                setEnabled(action.isEnabled());
                setVisible(action.isVisible());

                if (action.getIcon() != null && getIcon() == null) {
                    setIcon(action.getIcon());
                }

                action.addOwner(this);

                actionPropertyChangeListener = new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (Action.PROP_ICON.equals(evt.getPropertyName())) {
                            setIcon(DesktopButton.this.action.getIcon());
                        } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                            setCaption(DesktopButton.this.action.getCaption());
                        } else if (Action.PROP_DESCRIPTION.equals(evt.getPropertyName())) {
                            setDescription(DesktopButton.this.action.getDescription());
                        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                            setEnabled(DesktopButton.this.action.isEnabled());
                        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                            setVisible(DesktopButton.this.action.isVisible());
                        }
                    }
                };
                action.addPropertyChangeListener(actionPropertyChangeListener);

                assignAutoDebugId();
            }
        }
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        impl.setText(caption == null ? "" : caption);
    }

    @Override
    public String getDescription() {
        return impl.getToolTipText();
    }

    @Override
    public void setDescription(String description) {
        impl.setToolTipText(description);
        DesktopToolTipManager.getInstance().registerTooltip(impl);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (icon != null)
            impl.setIcon(App.getInstance().getResources().getIcon(icon));
        else
            impl.setIcon(null);
    }

    public boolean isShouldBeFocused() {
        return shouldBeFocused;
    }

    public void setShouldBeFocused(boolean shouldBeFocused) {
        this.shouldBeFocused = shouldBeFocused;
    }

    @Override
    protected String getAlternativeDebugId() {
        if (StringUtils.isNotEmpty(id)) {
            return id;
        }
        if (action != null && StringUtils.isNotEmpty(action.getId())) {
            return action.getId();
        }

        return getClass().getSimpleName();
    }

    @Override
    public boolean isFocusable() {
        return impl.isFocusable();
    }

    @Override
    public void setFocusable(boolean focusable) {
        impl.setFocusable(focusable);
    }
}