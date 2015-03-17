/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
                getWidget().setText(getState().text);
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
            Util.sinkOnloadForImages(getWidget().getElement());
            Profiler.leave("LabelConnector.onStateChanged sinkOnloads");
        }
    }
}