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

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.mainwindow.LogoutButton;
import com.haulmont.cuba.web.Connection;
import com.haulmont.cuba.web.WebWindowManager;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.toolkit.ui.CubaButton;
import com.vaadin.ui.Button;

public class WebLogoutButton extends WebAbstractComponent<CubaButton> implements LogoutButton {

    public static final String LOGOUT_BUTTON_STYLENAME = "cuba-logout-button";

    public WebLogoutButton() {
        component = new CubaButton();
        component.addStyleName(LOGOUT_BUTTON_STYLENAME);
        component.addClickListener((Button.ClickListener) event ->
                logout()
        );
        component.setDescription(null);
    }

    protected void logout() {
        Window window = ComponentsHelper.getWindow(this);
        if (window == null) {
            throw new IllegalStateException("Unable to find Frame for logout button");
        }

        window.saveSettings();

        final WebWindowManager wm = (WebWindowManager) window.getWindowManager();
        wm.checkModificationsAndCloseAll(() -> {
            Connection connection = wm.getApp().getConnection();
            connection.logout();
        });
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(LOGOUT_BUTTON_STYLENAME);
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
    public void setDescription(String description) {
        component.setDescription(description);
    }
}