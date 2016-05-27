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

package com.haulmont.cuba.web.app.mainwindow;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.FtsConfigHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.gui.components.mainwindow.AppWorkArea;
import com.haulmont.cuba.gui.components.mainwindow.FoldersPane;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.web.WebConfig;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.toolkit.ui.CubaHorizontalSplitPanel;
import com.vaadin.server.Sizeable;
import org.apache.commons.lang.StringUtils;
import org.vaadin.peter.contextmenu.ContextMenu;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class AppMainWindow extends AbstractMainWindow {

    @Inject
    protected AppMenu mainMenu;

    @Inject
    protected AppWorkArea workArea;

    @Inject
    protected BoxLayout titleBar;

    @Inject
    protected FoldersPane foldersPane;

    @Inject
    protected SplitPanel foldersSplit;

    @Inject
    protected FtsField ftsField;

    @Inject
    protected Embedded logoImage;

    @Inject
    protected WebConfig webConfig;

    @Inject
    protected Configuration configuration;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        mainMenu.requestFocus();

        String logoImagePath = messages.getMainMessage("application.logoImage");
        if (StringUtils.isNotBlank(logoImagePath) && !"application.logoImage".equals(logoImagePath)) {
            logoImage.setSource("theme://" + logoImagePath);
        }

        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        if (clientConfig.getLayoutAnalyzerEnabled()) {
            ContextMenu contextMenu = new ContextMenu();
            contextMenu.setOpenAutomatically(true);
            contextMenu.setAsContextMenuOf(logoImage.unwrap(com.vaadin.ui.Embedded.class));
            ContextMenu.ContextMenuItem analyzeLayout = contextMenu.addItem(messages.getMainMessage("actions.analyzeLayout"));
            analyzeLayout.addItemClickListener(new ContextMenu.ContextMenuItemClickListener() {
                @Override
                public void contextMenuItemClicked(ContextMenu.ContextMenuItemClickEvent event) {
                    Window window = AppMainWindow.this;
                    LayoutAnalyzer analyzer = new LayoutAnalyzer();
                    List<LayoutTip> tipsList = analyzer.analyze(window);

                    if (tipsList.isEmpty()) {
                        window.showNotification("No layout problems found", Frame.NotificationType.HUMANIZED);
                    } else {
                        window.openWindow("layoutAnalyzer", WindowManager.OpenType.DIALOG, ParamsMap.of("tipsList", tipsList));
                    }
                }
            });
        }

        if (webConfig.getUseInverseHeader()) {
            titleBar.setStyleName("cuba-app-menubar cuba-inverse-header");
        }

        if (!FtsConfigHelper.getEnabled()) {
            ftsField.setVisible(false);
        }

        if (webConfig.getFoldersPaneEnabled()) {
            if (webConfig.getFoldersPaneVisibleByDefault()) {
                foldersSplit.setSplitPosition(webConfig.getFoldersPaneDefaultWidth(), Component.UNITS_PIXELS);
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

            int foldersSplitIndex = indexOf(foldersSplit);

            remove(foldersSplit);
            add(workArea, foldersSplitIndex);

            expand(workArea);
        }
    }
}