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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.gui.icons.IconResolver;
import com.haulmont.cuba.desktop.sys.DesktopToolTipManager;
import com.haulmont.cuba.desktop.sys.validation.ValidationAwareActionListener;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.logging.UserActionsLogger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class DesktopButton extends DesktopAbstractComponent<JButton> implements Button {

    protected Logger userActionsLog = LoggerFactory.getLogger(UserActionsLogger.class);

    protected Action action;
    protected String icon;

    protected long responseEndTs = 0;
    protected boolean shouldBeFocused = true;

    protected PropertyChangeListener actionPropertyChangeListener;
    protected boolean disableOnClick = false;
    protected boolean useResponsePending = false;

    protected boolean captionAsHtml = false; // just stub

    public DesktopButton() {
        impl = createImplementation();
        impl.addActionListener(new ValidationAwareActionListener() {
            @Override
            public void actionPerformedAfterValidation(ActionEvent e) {
                if (action != null) {
                    if (shouldBeFocused && !impl.isFocusOwner() && impl.isFocusable()) {
                        return;
                    }

                    if (useResponsePending && e.getWhen() <= responseEndTs) {
                        return;
                    }

                    if (disableOnClick) {
                        impl.setEnabled(false);
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
                if (Objects.equals(this.action.getCaption(), getCaption())) {
                    setCaption(null);
                }
                if (Objects.equals(this.action.getDescription(), getDescription())) {
                    setDescription(null);
                }
                if (Objects.equals(this.action.getIcon(), getIcon())) {
                    setIcon(null);
                }
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
    protected void setCaptionToComponent(String caption) {
        super.setCaptionToComponent(caption);

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
            impl.setIcon(AppBeans.get(IconResolver.class).getIconResource(icon));
        else
            impl.setIcon(null);
    }

    @Override
    public void setIconFromSet(Icons.Icon icon) {
        String iconPath = AppBeans.get(Icons.class)
                .get(icon);
        setIcon(iconPath);
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

    @Override
    public void setDisableOnClick(boolean disableOnClick) {
        this.disableOnClick = disableOnClick;
    }

    @Override
    public boolean isDisableOnClick() {
        return disableOnClick;
    }

    @Override
    public boolean isUseResponsePending() {
        return useResponsePending;
    }

    @Override
    public void setUseResponsePending(boolean useResponsePending) {
        this.useResponsePending = useResponsePending;
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {
        this.captionAsHtml = captionAsHtml;
    }

    @Override
    public boolean isCaptionAsHtml() {
        return captionAsHtml;
    }
}