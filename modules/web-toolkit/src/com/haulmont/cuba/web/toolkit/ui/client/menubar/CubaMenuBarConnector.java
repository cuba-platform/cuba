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

package com.haulmont.cuba.web.toolkit.ui.client.menubar;

import com.haulmont.cuba.web.toolkit.ui.CubaMenuBar;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VMenuBar;
import com.vaadin.client.ui.menubar.MenuBarConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CubaMenuBar.class)
public class CubaMenuBarConnector extends MenuBarConnector {

    @Override
    public CubaMenuBarState getState() {
        return (CubaMenuBarState) super.getState();
    }

    @Override
    public CubaMenuBarWidget getWidget() {
        return (CubaMenuBarWidget) super.getWidget();
    }

    @Override
    public void layout() {
        super.layout();

        if (getState().vertical) {
            getWidget().addStyleName("c-menubar-vertical");
        } else {
            getWidget().removeStyleName("c-menubar-vertical");
        }
    }

    @Override
    protected boolean isUseMoreMenuItem() {
        return !getState().vertical;
    }

    @Override
    protected String getItemId(UIDL uidl) {
        if (uidl.hasAttribute("tid")) {
            return uidl.getStringAttribute("tid");
        }

        return null;
    }

    @Override
    protected void assignAdditionalAttributes(VMenuBar.CustomMenuItem currentItem, UIDL item) {
        if (item.hasAttribute("cid")) {
            currentItem.getElement().setAttribute("cuba-id", item.getStringAttribute("cid"));
        }
    }

    @Override
    protected void assignAdditionalMenuStyles(VMenuBar currentMenu, UIDL item) {
        String icon = item.getStringAttribute("icon");
        if (icon != null) {
            currentMenu.addStyleDependentName("has-icons");
        }
    }
}