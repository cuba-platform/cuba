/*
 * Copyright 2008 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VTooltip;


public class VButton extends Button implements Paintable {

    private String width = null;

    public static final String CLASSNAME = "v-button";

    String id;

    ApplicationConnection client;

    private Element errorIndicatorElement;

    private final Element captionElement = DOM.createSpan();

    private Icon icon;

    /**
     * Helper flat to handle special-case where the button is moved from under
     * mouse while clicking it. In this case mouse leaves the button without
     * moving.
     */
    private boolean clickPending;

    public VButton() {
        setStyleName(CLASSNAME);

        DOM.appendChild(getElement(), captionElement);

       addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (id == null || client == null) {
                    return;
                }
                if (BrowserInfo.get().isSafari()) {
                    VButton.this.setFocus(true);
                }
                client.updateVariable(id, "state", true, true);
                clickPending = false;
            }
        });
        sinkEvents(VTooltip.TOOLTIP_EVENTS);
        sinkEvents(Event.ONMOUSEDOWN);
        sinkEvents(Event.ONMOUSEUP);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

        // Ensure correct implementation,
        // but don't let container manage caption etc.
        if (client.updateComponent(this, uidl, false)) {
            return;
        }

        // Save details
        this.client = client;
        id = uidl.getId();

        // Set text
        setText(uidl.getStringAttribute("caption"));

        // handle error
        if (uidl.hasAttribute("error")) {
            if (errorIndicatorElement == null) {
                errorIndicatorElement = DOM.createDiv();
                DOM.setElementProperty(errorIndicatorElement, "className",
                        "v-errorindicator");
            }
            DOM.insertChild(getElement(), errorIndicatorElement, 0);

            // Fix for IE6, IE7
            if (BrowserInfo.get().isIE()) {
                DOM.setInnerText(errorIndicatorElement, " ");
            }

        } else if (errorIndicatorElement != null) {
            DOM.removeChild(getElement(), errorIndicatorElement);
            errorIndicatorElement = null;
        }

        if (uidl.hasAttribute("readonly")) {
            setEnabled(false);
        }

        if (uidl.hasAttribute("icon")) {
            if (icon == null) {
                icon = new Icon(client);
                DOM.insertChild(getElement(), icon.getElement(), 0);
            }
            icon.setUri(uidl.getStringAttribute("icon"));
        } else {
            if (icon != null) {
                DOM.removeChild(getElement(), icon.getElement());
                icon = null;
            }
        }
        if (BrowserInfo.get().isIE7()) {
            if (width.equals("")) {
                setWidth(getOffsetWidth() + "px");
            }
        }
    }

    @Override
    public void setText(String text) {
        DOM.setInnerText(captionElement, text);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        if (DOM.eventGetType(event) == Event.ONLOAD) {
            Util.notifyParentOfSizeChange(this, true);

        } else if (DOM.eventGetType(event) == Event.ONMOUSEDOWN
                && event.getButton() == Event.BUTTON_LEFT) {
            clickPending = true;
        } else if (DOM.eventGetType(event) == Event.ONMOUSEMOVE) {
            clickPending = false;
        } else if (DOM.eventGetType(event) == Event.ONMOUSEOUT) {
            if (clickPending) {
                click();
            }
            clickPending = false;
        }

        if (client != null) {
            client.handleTooltipEvent(event, this);
        }
    }

    @Override
    public void setWidth(String width) {
        /* Workaround for IE7 button size part 1 (#2014) */
        if (BrowserInfo.get().isIE7() && this.width != null) {
            if (this.width.equals(width)) {
                return;
            }

            if (width == null) {
                width = "";
            }
        }

        this.width = width;
        super.setWidth(width);

        /* Workaround for IE7 button size part 2 (#2014) */
        if (BrowserInfo.get().isIE7()) {
            super.setWidth(width);
        }
    }

}
