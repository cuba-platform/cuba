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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.FtsConfigHelper;
import com.haulmont.cuba.gui.ScreenTools;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.dev.LayoutAnalyzerContextMenuProvider;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.gui.components.mainwindow.UserIndicator;
import com.haulmont.cuba.gui.events.UserRemovedEvent;
import com.haulmont.cuba.gui.events.UserSubstitutionsChangedEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Legacy base class for a controller of application Main window.
 */
public class AbstractMainWindow extends AbstractTopLevelWindow
        implements Window.HasWorkArea, Window.HasUserIndicator, Window.HasFoldersPane {

    protected static final String APP_LOGO_IMAGE = "application.logoImage";

    @Inject
    protected Screens screens;
    @Inject
    protected ScreenTools screenTools;

    protected AppWorkArea workArea;
    protected UserIndicator userIndicator;
    protected FoldersPane foldersPane;

    public AbstractMainWindow() {
        addInitListener(this::initComponents);
    }

    protected void initComponents(@SuppressWarnings("unused") InitEvent e) {
        workArea = getWorkArea();
        userIndicator = getUserIndicator();
        foldersPane = getFoldersPane();
    }

    @Override
    @Nullable
    public AppWorkArea getWorkArea() {
        return (AppWorkArea) getComponent("workArea");
    }

    @Override
    @Nullable
    public UserIndicator getUserIndicator() {
        return (UserIndicator) getComponent("userIndicator");
    }

    @Nullable
    @Override
    public FoldersPane getFoldersPane() {
        return (FoldersPane) getComponent("foldersPane");
    }

    protected void initLogoImage(Image logoImage) {
        String logoImagePath = messages.getMainMessage(APP_LOGO_IMAGE);
        if (logoImage != null
                && StringUtils.isNotBlank(logoImagePath)
                && !APP_LOGO_IMAGE.equals(logoImagePath)) {
            logoImage.setSource(ThemeResource.class).setPath(logoImagePath);
        }
    }

    protected void initFtsField(FtsField ftsField) {
        if (ftsField != null && !FtsConfigHelper.getEnabled()) {
            ftsField.setVisible(false);
        }
    }

    protected void initLayoutAnalyzerContextMenu(Component contextMenuTarget) {
        LayoutAnalyzerContextMenuProvider laContextMenuProvider =
                getBeanLocator().get(LayoutAnalyzerContextMenuProvider.NAME);
        laContextMenuProvider.initContextMenu(this, contextMenuTarget);
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

    @Override
    public void ready() {
        super.ready();

        screenTools.openDefaultScreen(screens);
    }
}
