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

package com.haulmont.cuba.web.widgets.client.label;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.vaadin.client.Profiler;
import com.vaadin.client.VTooltip;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VLabel;
import com.vaadin.client.ui.label.LabelConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaLabel.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaLabelConnector extends LabelConnector {

    public static final String CONTEXT_HELP_CLASSNAME = "c-context-help-button";
    public static final String CONTEXT_HELP_CLICKABLE_CLASSNAME = "c-context-help-button-clickable";

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    public CubaLabelWidget getWidget() {
        return (CubaLabelWidget) super.getWidget();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        // CAUTION copied from superclass
        // todo rework! extract extenstion points
        super.onStateChanged(stateChangeEvent);

        boolean sinkOnloads = false;
        Profiler.enter("LabelConnector.onStateChanged update content");
        CubaLabelWidget widget = getWidget();
        switch (getState().contentMode) {
            case PREFORMATTED:
                PreElement preElement = Document.get().createPreElement();
                preElement.setInnerText(getState().text);
                // clear existing content
                widget.setHTML("");
                // add preformatted text to dom
                widget.getElement().appendChild(preElement);
                break;

            case TEXT:
                // clear existing content
                widget.setHTML("");
                // set multiline text if needed
                // Haulmont API
                String text = getState().text;
                if (text != null && text.contains("\n")) {
                    text = WidgetUtil.escapeHTML(text).replace("\n", "<br/>");
                    widget.setHTML(text);
                } else {
                    widget.setText(text);
                }
                break;

            case HTML:
                sinkOnloads = true;
                widget.setHTML(getState().text);
                break;
        }

        // Haulmont API
        if ("".equals(getWidget().getText()) || getWidget().getText() == null) {
            getWidget().addStyleDependentName("empty");
        } else {
            getWidget().removeStyleDependentName("empty");
        }

        updateIcon(widget);

        updateContextHelp(widget);

        Profiler.leave("LabelConnector.onStateChanged update content");

        if (sinkOnloads) {
            Profiler.enter("LabelConnector.onStateChanged sinkOnloads");
            WidgetUtil.sinkOnloadForImages(widget.getElement());
            Profiler.leave("LabelConnector.onStateChanged sinkOnloads");
        }
    }

    protected void updateIcon(VLabel widget) {
        Icon icon = getIcon();
        if (icon != null) {
            widget.getElement().insertFirst(icon.getElement());
        }
    }

    protected void updateContextHelp(CubaLabelWidget widget) {
        if (isContextHelpIconEnabled(getState())) {
            widget.contextHelpIcon = DOM.createSpan();
            widget.contextHelpIcon.setInnerHTML("?");
            widget.contextHelpIcon.setClassName(CONTEXT_HELP_CLASSNAME);

            if (hasContextHelpIconListeners(getState())) {
                widget.contextHelpIcon.addClassName(CONTEXT_HELP_CLICKABLE_CLASSNAME);
            }

            Roles.getTextboxRole().setAriaHiddenState(widget.contextHelpIcon, true);

            widget.getElement().appendChild(widget.contextHelpIcon);

            widget.contextHelpClickHandler =
                    this::contextHelpIconClick;
        }
    }
}