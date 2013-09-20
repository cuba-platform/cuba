/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.menubar;

import com.vaadin.client.UIDL;
import com.vaadin.client.Util;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VMenuBar;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaMenuBarWidget extends VMenuBar {

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
                        .append(Util.escapeAttribute(client.translateVaadinUri(item.getStringAttribute("icon"))))
                        .append("\" class=\"")
                        .append(Icon.CLASSNAME).append("\" alt=\"\" />");
            }
            String itemText = item.getStringAttribute("text");
            if (!htmlContentAllowed) {
                itemText = Util.escapeHTML(itemText);
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
                String shortcut = "";
                if (item.hasAttribute("shortcut")) {
                    shortcut = item.getStringAttribute("shortcut");
                }

                itemHTML.append("<span class=\"")
                        .append(getStylePrimaryName())
                        .append("-menuitem-shortcut\">")
                        .append(shortcut)
                        .append("</span");
            }
        }
        return itemHTML.toString();
    }
}