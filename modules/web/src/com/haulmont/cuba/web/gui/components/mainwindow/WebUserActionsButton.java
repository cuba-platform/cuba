/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.mainwindow.UserActionsButton;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.screen.OpenMode;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.widgets.CubaMenuBar;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;

public class WebUserActionsButton extends WebAbstractComponent<CubaMenuBar>
        implements UserActionsButton {

    public static final String USERACTIONS_BUTTON_STYLENAME = "c-useractions-button";

    public WebUserActionsButton() {
        component = new CubaMenuBar();
        component.addStyleName(USERACTIONS_BUTTON_STYLENAME);

        component.addAttachListener(event -> {
            UI ui = event.getConnector().getUI();

            if (ui instanceof AppUI) {
                initComponent((AppUI) ui);
            }
        });
    }

    protected void initComponent(AppUI ui) {
        boolean authenticated = ui.hasAuthenticatedSession();

        IconResolver iconResolver = beanLocator.get(IconResolver.class);
        Icons icons = beanLocator.get(Icons.class);
        Messages messages = beanLocator.get(Messages.class);

        MenuBar.MenuItem loginButton = component.addItem("", item -> login());
        loginButton.setDescription(messages.getMainMessage("loginBtnDescription"));
        loginButton.setIcon(iconResolver.getIconResource(icons.get(CubaIcon.SIGN_IN)));
        loginButton.setVisible(!authenticated);

        MenuBar.MenuItem userMenuBtn = component.addItem("");
        userMenuBtn.setDescription(messages.getMainMessage("userActionsBtnDescription"));
        userMenuBtn.setIcon(iconResolver.getIconResource(icons.get(CubaIcon.USER)));
        userMenuBtn.setVisible(authenticated);

        userMenuBtn.addItem(messages.getMainMessage("settings"),
                iconResolver.getIconResource(icons.get(CubaIcon.GEAR)), item -> openSettings());

        userMenuBtn.addItem(messages.getMainMessage("logoutBtnDescription"),
                iconResolver.getIconResource(icons.get(CubaIcon.SIGN_OUT)), item -> logout());
    }

    protected void login() {
        AppUI ui = ((AppUI) component.getUI());
        if (ui == null) {
            throw new IllegalStateException("Logout button is not attached to UI");
        }

        String loginScreenId = beanLocator.get(Configuration.class)
                .getConfig(WebConfig.class)
                .getLoginScreenId();

        Screen loginScreen = ui.getScreens().create(loginScreenId, OpenMode.ROOT);

        loginScreen.show();
    }

    protected void logout() {
        AppUI ui = ((AppUI) component.getUI());
        if (ui == null) {
            throw new IllegalStateException("Logout button is not attached to UI");
        }
        ui.getApp().logout();
    }

    protected void openSettings() {
        Screen settingsScreen = AppUI.getCurrent().getScreens()
                .create("settings", OpenMode.NEW_TAB);

        settingsScreen.show();
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(USERACTIONS_BUTTON_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(
                super.getStyleName().replace(USERACTIONS_BUTTON_STYLENAME, ""));
    }
}
