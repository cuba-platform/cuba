/*
 * Copyright (c) 2013 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.toolkit.ui.client.label;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.PreElement;
import com.haulmont.cuba.web.toolkit.ui.CubaLabel;
import com.vaadin.client.Profiler;
import com.vaadin.client.Util;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.label.LabelConnector;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
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
                    text = Util.escapeHTML(text).replace("\n", "<br/>");
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
        Profiler.leave("LabelConnector.onStateChanged update content");

        if (sinkOnloads) {
            Profiler.enter("LabelConnector.onStateChanged sinkOnloads");
            Util.sinkOnloadForImages(getWidget().getElement());
            Profiler.leave("LabelConnector.onStateChanged sinkOnloads");
        }
    }
}