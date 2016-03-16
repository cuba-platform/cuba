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

package com.haulmont.cuba.web.toolkit.ui.client.label;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.haulmont.cuba.web.toolkit.ui.CubaLabel;
import com.vaadin.client.Profiler;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.label.LabelConnector;
import com.vaadin.shared.ui.Connect;

/**
 */
@Connect(value = CubaLabel.class, loadStyle = Connect.LoadStyle.EAGER)
public class CubaLabelConnector extends LabelConnector {

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        // CAUTION copied from superclass
        super.onStateChanged(stateChangeEvent);
        boolean sinkOnloads = false;
        Profiler.enter("LabelConnector.onStateChanged update content");
        switch (getState().contentMode) {
            case PREFORMATTED:
                PreElement preElement = Document.get().createPreElement();
                preElement.setInnerText(getState().text);
                // clear existing content
                getWidget().setHTML("");
                // add preformatted text to dom
                getWidget().getElement().appendChild(preElement);
                break;

            case TEXT:
                // clear existing content
                getWidget().setHTML("");
                // set multiline text if needed
                // Haulmont API
                String text = getState().text;
                if (text != null && text.contains("\n")) {
                    text = WidgetUtil.escapeHTML(text).replace("\n", "<br/>");
                    getWidget().setHTML(text);
                } else {
                    getWidget().setText(text);
                }
                break;

            case HTML:
            case RAW:
                sinkOnloads = true;
            case XML:
                getWidget().setHTML(getState().text);
                break;
            default:
                getWidget().setText("");
                break;
        }

        // Haulmont API
        if ("".equals(getWidget().getText()) || getWidget().getText() == null) {
            getWidget().addStyleDependentName("empty");
        } else {
            getWidget().removeStyleDependentName("empty");
        }

        Profiler.leave("LabelConnector.onStateChanged update content");

        if (sinkOnloads) {
            Profiler.enter("LabelConnector.onStateChanged sinkOnloads");
            WidgetUtil.sinkOnloadForImages(getWidget().getElement());
            Profiler.leave("LabelConnector.onStateChanged sinkOnloads");
        }
    }
}