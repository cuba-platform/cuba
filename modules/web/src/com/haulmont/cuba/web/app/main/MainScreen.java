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

import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.FtsConfigHelper;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Route;
import com.haulmont.cuba.gui.ScreenTools;
import com.haulmont.cuba.gui.Screens;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.dev.LayoutAnalyzerContextMenuProvider;
import com.haulmont.cuba.gui.components.mainwindow.*;
import com.haulmont.cuba.gui.events.UserRemovedEvent;
import com.haulmont.cuba.gui.events.UserSubstitutionsChangedEvent;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.widgets.CubaHorizontalSplitPanel;
import com.vaadin.server.Sizeable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Base class for Main screen.
 */
@Route(path = "main", root = true)
@UiDescriptor("main-screen.xml")
@UiController("main")
public class MainScreen extends Screen implements Window.HasWorkArea, Window.HasUserIndicator {

    @Inject
    protected AppMenu mainMenu;
    @Inject
    protected BoxLayout titleBar;
    @Inject
    protected SplitPanel foldersSplit;
    @Inject
    protected FtsField ftsField;
    @Inject
    protected Image logoImage;
    @Inject
    protected AppWorkArea workArea;
    @Inject
    protected UserIndicator userIndicator;
    @Inject
    protected FoldersPane foldersPane;

    @Inject
    protected WebConfig webConfig;
    @Inject
    protected Messages messages;
    @Inject
    protected Screens screens;
    @Inject
    protected ScreenTools screenTools;

    @Subscribe
    protected void onInit(InitEvent event) {
        mainMenu.focus();

        initLogoImage(logoImage);
        initLayoutAnalyzerContextMenu(logoImage);
        initFtsField(ftsField);

        if (webConfig.getUseInverseHeader()) {
            titleBar.setStyleName("c-app-menubar c-inverse-header");
        }

        if (webConfig.getFoldersPaneEnabled()) {
            if (webConfig.getFoldersPaneVisibleByDefault()) {
                foldersSplit.setSplitPosition(webConfig.getFoldersPaneDefaultWidth(), SizeUnit.PIXELS);
            } else {
                foldersSplit.setSplitPosition(0);
            }

            CubaHorizontalSplitPanel vSplitPanel = (CubaHorizontalSplitPanel) WebComponentsHelper.unwrap(foldersSplit);
            vSplitPanel.setDefaultPosition(webConfig.getFoldersPaneDefaultWidth() + "px");
            vSplitPanel.setMaxSplitPosition(50, Sizeable.Unit.PERCENTAGE);
            vSplitPanel.setDockable(true);
        } else {
            foldersPane.setEnabled(false);
            foldersPane.setVisible(false);

            foldersSplit.remove(workArea);

            Window window = getWindow();

            int foldersSplitIndex = window.indexOf(foldersSplit);

            window.remove(foldersSplit);
            window.add(workArea, foldersSplitIndex);

            window.expand(workArea);
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
        screenTools.openDefaultScreen(screens);
    }

    protected void initLogoImage(Image logoImage) {
        String logoImagePath = messages.getMainMessage("application.logoImage");
        if (StringUtils.isNotBlank(logoImagePath) && !"application.logoImage".equals(logoImagePath)) {
            logoImage.setSource(ThemeResource.class).setPath(logoImagePath);
        }
    }

    protected void initFtsField(FtsField ftsField) {
        if (!FtsConfigHelper.getEnabled()) {
            ftsField.setVisible(false);
        }
    }

    protected void initLayoutAnalyzerContextMenu(Component contextMenuTarget) {
        LayoutAnalyzerContextMenuProvider laContextMenuProvider =
                getBeanLocator().get(LayoutAnalyzerContextMenuProvider.NAME);
        laContextMenuProvider.initContextMenu(getWindow(), contextMenuTarget);
    }

    @Nullable
    @Override
    public AppWorkArea getWorkArea() {
        return workArea;
    }

    @Nullable
    @Override
    public UserIndicator getUserIndicator() {
        return userIndicator;
    }
}
