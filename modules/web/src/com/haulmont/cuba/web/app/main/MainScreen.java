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

package com.haulmont.cuba.web.app.main;

import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.FtsConfigHelper;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.ScreenTools;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.ThemeResource;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.dev.LayoutAnalyzerContextMenuProvider;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.events.UserRemovedEvent;
import com.haulmont.cuba.gui.events.UserSubstitutionsChangedEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiControllerUtils;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.WebConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;

/**
 * Base class for a controller of application Main screen.
 */
@Route(path = "main", root = true)
@UiDescriptor("main-screen.xml")
@UiController("main")
public class MainScreen extends Screen implements Window.HasWorkArea, Window.HasUserIndicator {

    protected static final String APP_LOGO_IMAGE = "application.logoImage";

    public MainScreen() {
        addInitListener(this::initComponents);
    }

    protected void initComponents(@SuppressWarnings("unused") InitEvent e) {
        initLogoImage();
        initFtsField();
        initLayoutAnalyzerContextMenu();
        initTitleBar();
        initMenu();
    }

    protected void initLogoImage() {
        Image logoImage = getLogoImage();
        String logoImagePath = getBeanLocator().get(Messages.class)
                .getMainMessage(APP_LOGO_IMAGE);

        if (logoImage != null
                && StringUtils.isNotBlank(logoImagePath)
                && !APP_LOGO_IMAGE.equals(logoImagePath)) {
            logoImage.setSource(ThemeResource.class).setPath(logoImagePath);
        }
    }

    protected void initFtsField() {
        FtsField ftsField = getFtsField();
        if (ftsField != null && !FtsConfigHelper.getEnabled()) {
            ftsField.setVisible(false);
        }
    }

    protected void initLayoutAnalyzerContextMenu() {
        Image logoImage = getLogoImage();
        if (logoImage != null) {
            LayoutAnalyzerContextMenuProvider laContextMenuProvider =
                    getBeanLocator().get(LayoutAnalyzerContextMenuProvider.NAME);
            laContextMenuProvider.initContextMenu(getWindow(), logoImage);
        }
    }

    protected void initMenu() {
        Component menu = getAppMenu();
        if (menu == null) {
            menu = getSideMenu();
        }

        if (menu != null) {
            ((Component.Focusable) menu).focus();
        }
    }

    protected void initTitleBar() {
        Configuration configuration = getBeanLocator().get(Configuration.class);
        if (configuration.getConfig(WebConfig.class).getUseInverseHeader()) {
            Component titleBar = getTitleBar();
            if (titleBar != null) {
                titleBar.setStyleName("c-app-menubar c-inverse-header");
            }
        }
    }

    @Order(Events.LOWEST_PLATFORM_PRECEDENCE - 100)
    @EventListener
    protected void onUserSubstitutionsChange(UserSubstitutionsChangedEvent event) {
        UserIndicator userIndicator = getUserIndicator();
        if (userIndicator != null) {
            userIndicator.refreshUserSubstitutions();
        }
    }

    @Order(Events.LOWEST_PLATFORM_PRECEDENCE - 100)
    @EventListener
    protected void onUserRemove(UserRemovedEvent event) {
        UserIndicator userIndicator = getUserIndicator();
        if (userIndicator != null) {
            userIndicator.refreshUserSubstitutions();
        }
    }

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        Screens screens = UiControllerUtils.getScreenContext(this)
                .getScreens();
        getBeanLocator().get(ScreenTools.class)
                .openDefaultScreen(screens);
    }

    @Nullable
    @Override
    public AppWorkArea getWorkArea() {
        return (AppWorkArea) getWindow().getComponent("workArea");
    }

    @Nullable
    @Override
    public UserIndicator getUserIndicator() {
        return (UserIndicator) getWindow().getComponent("userIndicator");
    }

    @Nullable
    protected Image getLogoImage() {
        return (Image) getWindow().getComponent("logoImage");
    }

    @Nullable
    protected FtsField getFtsField() {
        return (FtsField) getWindow().getComponent("ftsField");
    }

    @Nullable
    protected Component getAppMenu() {
        return getWindow().getComponent("appMenu");
    }

    @Nullable
    protected Component getSideMenu() {
        return getWindow().getComponent("sideMenu");
    }

    @Nullable
    protected Component getTitleBar() {
        return getWindow().getComponent("titleBar");
    }
}
