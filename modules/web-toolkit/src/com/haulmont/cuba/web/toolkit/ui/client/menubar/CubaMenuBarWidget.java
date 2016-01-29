/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.menubar;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.UIDL;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VMenuBar;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMenuBarWidget extends VMenuBar implements BlurHandler {

    protected boolean mouseEvent = false;

    public CubaMenuBarWidget() {
        addBlurHandler(this);

        DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN);
    }

    @Override
    public String buildItemHTML(UIDL item) {
        // Construct html from the text and the optional icon
        // Haulmont API : Added support for shortcuts
        StringBuilder itemHTML = new StringBuilder();
        if (item.hasAttribute("separator")) {
            itemHTML.append("<span>---</span><span>---</span>");
        } else {
            itemHTML.append("<span class=\"")
                    .append(getStylePrimaryName())
                    .append("-menuitem-caption\">");
            if (item.hasAttribute("icon")) {
                itemHTML.append("<img src=\"")
                        .append(WidgetUtil.escapeAttribute(client.translateVaadinUri(item.getStringAttribute("icon"))))
                        .append("\" class=\"")
                        .append(Icon.CLASSNAME).append("\" alt=\"\" />");
            }
            String itemText = item.getStringAttribute("text");
            if (!htmlContentAllowed) {
                itemText = WidgetUtil.escapeHTML(itemText);
            }
            itemHTML.append(itemText);
            itemHTML.append("</span>");

            // Add submenu indicator
            if (item.getChildCount() > 0) {
                String bgStyle = "";
                itemHTML.append("<span class=\"")
                        .append(getStylePrimaryName())
                        .append("-submenu-indicator\"")
                        .append(bgStyle)
                        .append("><span class=\"")
                        .append(getStylePrimaryName())
                        .append("-submenu-indicator-icon\"")
                        .append("><span class=\"text\">&#x25BA;</span></span></span>");
            } else {
                itemHTML.append("<span class=\"");
                String shortcut = "";
                if (item.hasAttribute("shortcut")) {
                    shortcut = item.getStringAttribute("shortcut");
                } else {
                    itemHTML.append(getStylePrimaryName())
                            .append("-menuitem-empty-shortcut ");
                }

                itemHTML.append(getStylePrimaryName())
                        .append("-menuitem-shortcut\">")
                        .append(shortcut)
                        .append("</span");
            }
        }
        return itemHTML.toString();
    }

    @Override
    public void onBrowserEvent(Event e) {
        // select first item only on keyboard focus events
        if (e.getTypeInt() == Event.ONMOUSEDOWN) {
            mouseEvent = true;
        }

        super.onBrowserEvent(e);
    }

    protected void selectFirstItem() {
        for (CustomMenuItem item : items) {
            if (item.isSelectable()) {
                setSelected(item);
                break;
            }
        }
    }

    @Override
    public void onMenuClick(int clickedItemId) {
        super.onMenuClick(clickedItemId);

        mouseEvent = true;
    }

    @Override
    public void onFocus(FocusEvent event) {
        super.onFocus(event);

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                // select first item only on keyboard focus events
                if (getSelected() == null && !mouseEvent) {
                    selectFirstItem();
                }
                mouseEvent = false;
            }
        });

        addStyleDependentName("focus");
    }

    @Override
    public void onBlur(BlurEvent event) {
        removeStyleDependentName("focus");

        mouseEvent = false;

        if (!menuVisible) {
            setSelected(null);
        }
    }
}