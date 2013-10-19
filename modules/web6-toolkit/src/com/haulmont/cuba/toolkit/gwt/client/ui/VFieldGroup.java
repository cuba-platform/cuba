/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VForm;

public class VFieldGroup extends VForm {

    protected Element captionWrapper = DOM.createDiv();
    protected Element expander = DOM.createDiv();
    protected boolean expanded;
    protected boolean collapsable;

    public VFieldGroup() {
        super();
        DOM.removeChild(legend, caption);
        DOM.appendChild(legend, captionWrapper);
        DOM.appendChild(captionWrapper, expander);
        DOM.appendChild(captionWrapper, caption);
        DOM.setElementProperty(captionWrapper, "className", CLASSNAME + "-caption");

        DOM.sinkEvents(expander, Event.ONCLICK);

        setStyleName("v-fieldgroup");
        setStyleName(fieldSet, "v-fieldgroup-fieldset");
    }

    protected void renderContent(UIDL uidl, ApplicationConnection client) {
        final UIDL layoutUidl = uidl.getChildUIDL(0);
        if (layoutUidl != null) {
            Container newLo = (Container) client.getPaintable(layoutUidl);
            if (lo == null) {
                lo = newLo;
                add((Widget) lo, fieldContainer);
            } else if (lo != newLo) {
                client.unregisterPaintable(lo);
                remove((Widget) lo);
                lo = newLo;
                add((Widget) lo, fieldContainer);
            }
            lo.updateFromUIDL(layoutUidl, client);
        }
    }

    protected void renderDOM(UIDL uidl, ApplicationConnection client) {
        collapsable = uidl.getBooleanAttribute("collapsable");
        if (collapsable) {
            DOM.setStyleAttribute(expander, "display", "");
            removeStyleDependentName("nocollapsable");
        } else {
            addStyleDependentName("nocollapsable");
            DOM.setStyleAttribute(expander, "display", "none");
        }
        if (uidl.getBooleanAttribute("expanded") != expanded) {
            toggleExpand();
        }
        super.renderDOM(uidl, client);
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK && DOM.eventGetTarget(event) == expander) {
            toggleExpand();
            if (collapsable) {
                if (expanded) {
                    client.updateVariable(id, "expand", true, true);
                } else {
                    client.updateVariable(id, "collapse", true, true);
                }
            }
            DOM.eventCancelBubble(event, true);
        } else {
            super.onBrowserEvent(event);
        }
    }

    protected void toggleExpand() {
        expanded = !expanded;
        if (expanded) {
            captionWrapper.addClassName("expanded");
        } else {
            captionWrapper.removeClassName("expanded");
        }
    }
}
