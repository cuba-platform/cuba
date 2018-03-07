/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.widgets.client.menubar;

import com.vaadin.client.UIDL;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VMenuBar;

public class CubaMenuBarWidget extends VMenuBar {
    public CubaMenuBarWidget() {
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

            Icon icon = client.getIcon(item.getStringAttribute("icon"));
            if (icon != null) {
                itemHTML.append(icon.getElement().getString());
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
    protected void showChildMenuAt(CustomMenuItem item, int top, int left) {
        super.showChildMenuAt(item, top, left);

        if (popup != null) {
            popup.addStyleName("c-main-menu-popup");
        }
    }
}