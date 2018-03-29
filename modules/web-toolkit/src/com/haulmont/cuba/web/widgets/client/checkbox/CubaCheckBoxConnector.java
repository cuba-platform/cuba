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

package com.haulmont.cuba.web.widgets.client.checkbox;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.CubaCheckBox;
import com.vaadin.client.VTooltip;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.checkbox.CheckBoxConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.AbstractFieldState;

@Connect(value = CubaCheckBox.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaCheckBoxConnector extends CheckBoxConnector {

    public static final String CONTEXT_HELP_CLASSNAME = "c-context-help-button";
    public static final String CONTEXT_HELP_CLICKABLE_CLASSNAME = "c-context-help-button-clickable";

    @Override
    public boolean delegateCaptionHandling() {
        return getWidget().captionManagedByLayout;
    }

    @Override
    public CubaCheckBoxWidget getWidget() {
        return (CubaCheckBoxWidget) super.getWidget();
    }

    @Override
    public CubaCheckBoxState getState() {
        return (CubaCheckBoxState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        getWidget().captionManagedByLayout = getState().captionManagedByLayout;

        super.onStateChanged(stateChangeEvent);

        if (!getWidget().captionManagedByLayout
                && isContextHelpIconEnabled()) {
            if (getWidget().contextHelpIcon == null) {
                getWidget().contextHelpIcon = DOM.createSpan();
                getWidget().contextHelpIcon.setInnerHTML("?");
                getWidget().contextHelpIcon.setClassName(CONTEXT_HELP_CLASSNAME);

                if (hasContextHelpIconListeners()) {
                    getWidget().contextHelpIcon.addClassName(CONTEXT_HELP_CLICKABLE_CLASSNAME);
                }

                Roles.getTextboxRole().setAriaHiddenState(getWidget().contextHelpIcon, true);

                getWidget().getElement().appendChild(getWidget().contextHelpIcon);
                DOM.sinkEvents(getWidget().contextHelpIcon, VTooltip.TOOLTIP_EVENTS | Event.ONCLICK);
            } else {
                getWidget().contextHelpIcon.getStyle().clearDisplay();
            }
        } else if (getWidget().contextHelpIcon != null) {
            getWidget().contextHelpIcon.getStyle()
                    .setDisplay(Style.Display.NONE);

            getWidget().setAriaInvalid(false);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        super.onClick(event);

        Element target = Element.as(event.getNativeEvent().getEventTarget());
        if (target == getWidget().contextHelpIcon) {
            if (hasContextHelpIconListeners()) {
                contextHelpIconClick(event);
            }
        }
    }

    protected boolean isContextHelpIconEnabled() {
        return hasContextHelpIconListeners()
                || getState().contextHelpText != null
                && !getState().contextHelpText.isEmpty();
    }

    protected boolean hasContextHelpIconListeners() {
        return getState().registeredEventListeners != null
                && getState().registeredEventListeners.contains(AbstractFieldState.CONTEXT_HELP_ICON_CLICK_EVENT);
    }
}