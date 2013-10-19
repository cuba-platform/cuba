/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VOverlay;

import java.util.Iterator;
import java.util.Set;

public class PopupContainer extends VOverlay implements Container {

    private ApplicationConnection client;
    private Widget component;
    private UIDL uidl;

    public PopupContainer() {
        super(true, false, true);
        setStylePrimaryName("popup-container");
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;

        Iterator it = uidl.getChildIterator();
        UIDL paintableUidl = (UIDL) it.next();

        if (paintableUidl.hasAttribute("cached")) {
            return;
        }

        component = (Widget) client.getPaintable(paintableUidl);
        setWidget(component);

        this.uidl = paintableUidl;

        updateShadowSizeAndPosition();
    }

    public void showAt(final int left, final int top) {
        setPopupPositionAndShow(new PositionCallback() {
            @Override
            public void setPosition(int offsetWidth, int offsetHeight) {
                if (uidl != null) {
                    ((Paintable) component).updateFromUIDL(uidl, client);
                    uidl = null;
                }

                int l = left, t = top;

                offsetWidth = component.getOffsetWidth();
                if (offsetWidth + left > Window.getClientWidth()) {
                    l = left - offsetWidth;
                    if (l < 0) {
                        l = 0;
                    }
                }
                offsetHeight = component.getOffsetHeight();
                if (offsetHeight + top > Window.getClientHeight()) {
                    t = top - offsetHeight;
                    if (t < 0) {
                        t = 0;
                    }
                }
                setPopupPosition(l, t);

                client.updateComponentSize(component, "", "");
            }
        });
    }

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {
        return new RenderSpace(getContainerElement().getOffsetWidth(),
                getContainerElement().getOffsetHeight());
    }

    @Override
    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        //do nothing
    }

    @Override
    public boolean hasChildComponent(Widget component) {
        return component == this.component;
    }

    @Override
    public void updateCaption(Paintable component, UIDL uidl) {
        //do nothing
    }

    @Override
    public boolean requestLayout(Set<Paintable> children) {
        return false;
    }
}
