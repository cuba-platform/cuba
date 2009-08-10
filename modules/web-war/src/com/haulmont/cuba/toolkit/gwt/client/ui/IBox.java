/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.08.2009 18:38:15
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.itmill.toolkit.terminal.gwt.client.*;

import java.util.Iterator;
import java.util.Set;

public class IBox extends FlowPanel //todo gorodnov: make table cell wrapper component
        implements Container {

    public static final String CLASSNAME = "i-box";

    protected String paintableId;
    protected ApplicationConnection client;
    
    public IBox() {
        DOM.setElementProperty(getElement(), "className", CLASSNAME);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        for (final Iterator<UIDL> it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL childUIDL = it.next();
            final Paintable child = client.getPaintable(childUIDL);
            add((Widget) child);
            paintChild(child, childUIDL);
        }
    }

    @Override
    public void add(Widget w) {
        Element container = DOM.createDiv();
        DOM.setElementProperty(container, "className", CLASSNAME + "-child");

        DOM.appendChild(getElement(), container);

        add(w, container);
    }

    protected void paintChild(Paintable p, UIDL uidl) {
        if (isAttached()) {
            p.updateFromUIDL(uidl, client);
        }
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        //todo
    }

    public boolean hasChildComponent(Widget component) {
        return getChildren().contains(component);
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        //ignored
    }

    public boolean requestLayout(Set<Paintable> children) {
        return true;
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        int w = getElement().getOffsetWidth();
        int h = getElement().getOffsetHeight();
        return new RenderSpace(w, h);
    }
}
