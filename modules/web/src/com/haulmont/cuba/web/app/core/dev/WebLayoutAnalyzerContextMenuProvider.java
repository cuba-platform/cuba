/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.app.core.dev;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.app.core.dev.LayoutAnalyzer;
import com.haulmont.cuba.gui.app.core.dev.LayoutTip;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame.NotificationType;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.dev.LayoutAnalyzerContextMenuProvider;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.ui.AbstractComponent;

import javax.inject.Inject;
import java.util.List;

@org.springframework.stereotype.Component(LayoutAnalyzerContextMenuProvider.NAME)
public class WebLayoutAnalyzerContextMenuProvider implements LayoutAnalyzerContextMenuProvider {
    @Inject
    protected Messages messages;

    @Inject
    protected Configuration configuration;

    @Override
    public void initContextMenu(Window window, Component contextMenuTarget) {
        ClientConfig clientConfig = configuration.getConfig(ClientConfig.class);
        if (clientConfig.getLayoutAnalyzerEnabled()) {
            ContextMenu contextMenu = new ContextMenu(contextMenuTarget.unwrap(AbstractComponent.class), true);
            MenuItem menuItem = contextMenu.addItem(messages.getMainMessage("actions.analyzeLayout"), c -> {
                LayoutAnalyzer analyzer = new LayoutAnalyzer();
                List<LayoutTip> tipsList = analyzer.analyze(window);

                if (tipsList.isEmpty()) {
                    window.showNotification("No layout problems found", NotificationType.HUMANIZED);
                } else {
                    window.openWindow("layoutAnalyzer", OpenType.DIALOG,
                            ParamsMap.of("tipsList", tipsList)
                    );
                }
            });
            menuItem.setStyleName("c-cm-item");
        }
    }
}