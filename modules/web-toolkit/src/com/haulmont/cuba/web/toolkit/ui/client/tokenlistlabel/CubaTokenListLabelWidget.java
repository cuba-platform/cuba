/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tokenlistlabel;

import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.ui.VPanel;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaTokenListLabelWidget extends VPanel {

    public static final String CLASSNAME = "cuba-tokenlist-label";

    private Label label = new Label();
    private Element closeDiv = DOM.createDiv();

    private boolean editable;
    private boolean canOpen;

    protected TokenListLabelHandler handler;

    public CubaTokenListLabelWidget() {
        setStyleName(CLASSNAME);
        add(label);
        label.setStyleName("content");
        DOM.sinkEvents(label.getElement(), Event.ONCLICK);

        closeDiv = DOM.createDiv();
        closeDiv.setClassName(CLASSNAME + "-close");
        contentNode.appendChild(closeDiv);
        DOM.sinkEvents(closeDiv, Event.ONCLICK);
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (!editable) {
            getElement().addClassName("noedit");
        } else {
            getElement().removeClassName("noedit");
        }
    }

    public void setCanOpen(boolean canOpen) {
        this.canOpen = canOpen;
        if (canOpen)
            getElement().addClassName("open");
        else
            getElement().removeClassName("open");
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK && handler != null) {
            if (DOM.eventGetTarget(event) == closeDiv && editable) {
                handler.remove();
            } else if (DOM.eventGetTarget(event) == label.getElement() && canOpen) {
                handler.click();
            }
        }
    }

    public interface TokenListLabelHandler {
        void remove();

        void click();
    }
}