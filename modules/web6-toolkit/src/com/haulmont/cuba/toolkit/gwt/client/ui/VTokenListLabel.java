/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTokenListLabel extends SimplePanel implements Paintable {

    public static final String CLASSNAME = "token-list-label";

    private Label label = new Label();

    private ApplicationConnection client;
    private String paintableId;

    private String key;
    private boolean editable;
    private boolean canOpen;

    public VTokenListLabel() {
        super();

        setStyleName(CLASSNAME);

        setWidget(label);
        label.setStyleName("content");

        Element x = DOM.createSpan();
        DOM.setInnerText(x, "x");
        getElement().appendChild(x);

        DOM.sinkEvents(getElement(), Event.ONCLICK);
    }

    @Override
    public void setWidth(String width) {
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;
        this.paintableId = uidl.getId();

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        key = uidl.getStringAttribute("key");

        editable = uidl.getBooleanAttribute("editable");
        if (!editable) {
            getElement().addClassName("noedit");
        } else {
            getElement().removeClassName("noedit");
        }

        canOpen = uidl.getBooleanAttribute("canopen");
        if (canOpen)
            getElement().addClassName("open");
        else
            getElement().removeClassName("open");

        label.setText(uidl.getChildString(0));
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK && DOM.eventGetTarget(event) != label.getElement() && editable) {
            client.updateVariable(paintableId, "removeToken", key, true);
        } else if (DOM.eventGetType(event) == Event.ONCLICK && DOM.eventGetTarget(event) == label.getElement()) {
            client.updateVariable(paintableId, "itemClick", key, true);
        }
    }
}