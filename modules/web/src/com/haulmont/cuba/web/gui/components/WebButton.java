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

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import org.apache.commons.lang.StringUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 */
public class WebButton extends WebAbstractComponent<CubaButton> implements Button {

    public static final String ICON_STYLE = "icon";

    protected Action action;
    protected String icon;
    protected PropertyChangeListener actionPropertyChangeListener;

    public WebButton() {
        component = new CubaButton();
        component.addClickListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                beforeActionPerformed();
                if (action != null) {
                    performAction(action);
                }
                afterActionPerformed();
            }
        });
        component.setDescription(null);
    }

    protected void performAction(Action action) {
        action.actionPerform(this);
    }

    // override in descendants if needed
    protected void beforeActionPerformed() {
    }

    // override in descendants if needed
    protected void afterActionPerformed() {
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
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
            }

            this.action = action;

            if (action != null) {
                String caption = action.getCaption();
                if (caption != null && component.getCaption() == null) {
                    component.setCaption(caption);
                }

                String description = action.getDescription();
                if (description == null && action.getShortcut() != null) {
                    description = action.getShortcut().format();
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

                actionPropertyChangeListener = new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (Action.PROP_ICON.equals(evt.getPropertyName())) {
                            setIcon(WebButton.this.action.getIcon());
                        } else if (Action.PROP_CAPTION.equals(evt.getPropertyName())) {
                            setCaption(WebButton.this.action.getCaption());
                        } else if (Action.PROP_DESCRIPTION.equals(evt.getPropertyName())) {
                            setDescription(WebButton.this.action.getDescription());
                        } else if (Action.PROP_ENABLED.equals(evt.getPropertyName())) {
                            setEnabled(WebButton.this.action.isEnabled());
                        } else if (Action.PROP_VISIBLE.equals(evt.getPropertyName())) {
                            setVisible(WebButton.this.action.isVisible());
                        }
                    }
                };
                action.addPropertyChangeListener(actionPropertyChangeListener);

                assignAutoDebugId();
            }
        }
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);
        if (getIcon() != null)
            component.addStyleName(ICON_STYLE);
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
        if (!StringUtils.isEmpty(icon)) {
            component.setIcon(WebComponentsHelper.getIcon(icon));
            component.addStyleName(ICON_STYLE);
        } else {
            component.setIcon(null);
            component.removeStyleName(ICON_STYLE);
        }
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
}