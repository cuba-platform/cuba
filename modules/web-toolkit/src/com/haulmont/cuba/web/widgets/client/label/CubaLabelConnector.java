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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.vaadin.client.Profiler;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VLabel;
import com.vaadin.client.ui.label.LabelConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = CubaLabel.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaLabelConnector extends LabelConnector {

    @Override
    public boolean delegateCaptionHandling() {
        return false;
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        // CAUTION copied from superclass
        // todo rework! extract extenstion points
        super.onStateChanged(stateChangeEvent);

        boolean sinkOnloads = false;
        Profiler.enter("LabelConnector.onStateChanged update content");
        VLabel widget = getWidget();
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

        updateIcon();

        Profiler.leave("LabelConnector.onStateChanged update content");

        if (sinkOnloads) {
            Profiler.enter("LabelConnector.onStateChanged sinkOnloads");
            WidgetUtil.sinkOnloadForImages(widget.getElement());
            Profiler.leave("LabelConnector.onStateChanged sinkOnloads");
        }
    }

    protected void updateIcon() {
        Icon icon = getIcon();
        if (icon != null) {
            getWidget().getElement().insertFirst(icon.getElement());
        }
    }
}