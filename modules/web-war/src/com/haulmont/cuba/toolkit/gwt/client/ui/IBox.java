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

public class IBox extends FlowPanel
        implements Container {

    public static final String CLASSNAME = "i-box";

    protected String paintableId;
    protected ApplicationConnection client;

    protected Element table = DOM.createTable();
    protected Element container = DOM.createTR();
    
    public IBox() {
        DOM.setElementProperty(getElement(), "className", CLASSNAME);

        DOM.setElementAttribute(table, "cellPadding", "0");
        DOM.setElementAttribute(table, "cellSpacing", "0");
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        Element tBody = DOM.createTBody();

        DOM.appendChild(tBody, container);
        DOM.appendChild(table, tBody);
        DOM.appendChild(getElement(), table);

        for (final Iterator<UIDL> it = uidl.getChildIterator(); it.hasNext();) {
            final UIDL childUIDL = it.next();
            final Paintable child = client.getPaintable(childUIDL);
            add((Widget) child);
            paintChild(child, childUIDL);
        }

/*
        Element clear = DOM.createDiv();
        DOM.setElementProperty(clear, "className", CLASSNAME + "-clear");
        DOM.appendChild(getElement(), clear);
*/
    }

    @Override
    public void add(Widget w) {
        Element childWrapper = DOM.createTD();
        DOM.setElementProperty(childWrapper, "className", CLASSNAME + "-child");

        DOM.appendChild(container, childWrapper);

        add(w, childWrapper);
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
