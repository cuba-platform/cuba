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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.widgets.CubaButton;
import com.vaadin.shared.MouseEventDetails;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyChangeListener;
import java.util.Objects;

public class WebButton extends WebAbstractComponent<CubaButton> implements Button {

    protected Action action;
    protected PropertyChangeListener actionPropertyChangeListener;

    public WebButton() {
        component = new CubaButton();
        component.setClickHandler(this::buttonClicked);
    }

    // override in descendants if needed
    protected void beforeActionPerformed() {
    }

    protected void buttonClicked(@SuppressWarnings("unused") MouseEventDetails mouseEventDetails) {
        beforeActionPerformed();
        if (action != null) {
            action.actionPerform(getActionEventTarget());
        }
        afterActionPerformed();
    }

    protected Component getActionEventTarget() {
        return this;
    }

    // override in descendants if needed
    protected void afterActionPerformed() {
    }

    @Override
    public Action getAction() {
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
                if (caption != null && component.getCaption() == null) {
                    component.setCaption(caption);
                }

                String description = action.getDescription();
                if (description == null && action.getShortcutCombination() != null) {
                    description = action.getShortcutCombination().format();
                }
                if (description != null && component.getDescription() == null) {
                    component.setDescription(description);
                }

                component.setEnabled(action.isEnabled());
                component.setVisible(action.isVisible());

                if (action.getIcon() != null && getIcon() == null) {
                    setIcon(action.getIcon());
                }

                action.addOwner(this);

                actionPropertyChangeListener = evt -> {
                    if (Action.PROP_ICON.equals(evt.getPropertyName())) {
                        setIcon(this.action.getIcon());
                    } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                        setCaption(this.action.getCaption());
                    } else if (Action.PROP_DESCRIPTION.equals(evt.getPropertyName())) {
                        setDescription(this.action.getDescription());
                    } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                        setEnabled(this.action.isEnabled());
                    } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                        setVisible(this.action.isVisible());
                    }
                };
                action.addPropertyChangeListener(actionPropertyChangeListener);
            }

            boolean primaryAction = action instanceof AbstractAction && ((AbstractAction) action).isPrimary();
            if (primaryAction) {
                addStyleName("c-primary-action");
            } else {
                removeStyleName("c-primary-action");
            }
        }
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);
        if (getIcon() != null)
            component.addStyleName(ICON_STYLE);
    }

    @Override
    public String getStyleName() {
        if (getIcon() != null)
            return StringUtils.normalizeSpace(super.getStyleName().replace(ICON_STYLE, ""));

        return super.getStyleName();
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
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void setDisableOnClick(boolean value) {
        component.setDisableOnClick(value);
    }

    @Override
    public boolean isDisableOnClick() {
        return component.isDisableOnClick();
    }

    @Override
    public boolean isUseResponsePending() {
        return component.isUseResponsePending();
    }

    @Override
    public void setUseResponsePending(boolean useResponsePending) {
        component.setUseResponsePending(useResponsePending);
    }

    @Override
    public void setCaptionAsHtml(boolean captionAsHtml) {
        component.setCaptionAsHtml(captionAsHtml);
    }

    @Override
    public boolean isCaptionAsHtml() {
        return component.isCaptionAsHtml();
    }
}