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

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.mainwindow.LogoutButton;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.widgets.CubaButton;
import org.apache.commons.lang.StringUtils;

public class WebLogoutButton extends WebAbstractComponent<CubaButton> implements LogoutButton {

    public static final String LOGOUT_BUTTON_STYLENAME = "c-logout-button";

    public WebLogoutButton() {
        component = new CubaButton();
        component.addStyleName(LOGOUT_BUTTON_STYLENAME);
        component.addClickListener(event -> logout());
    }

    protected void logout() {
        AppUI ui = ((AppUI) component.getUI());
        if (ui == null) {
            throw new IllegalStateException("Logout button is not attached to UI");
        }
        ui.getApp().logout();
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(LOGOUT_BUTTON_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(LOGOUT_BUTTON_STYLENAME, ""));
    }
}