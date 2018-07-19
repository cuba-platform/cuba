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

package com.haulmont.cuba.web.app.core.showinfo;

import com.haulmont.cuba.gui.app.core.showinfo.SystemInfoWindow;
import com.haulmont.cuba.gui.components.BoxLayout;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebButton;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.widgets.CubaTable;
import com.haulmont.cuba.web.widgets.CubaCopyButtonExtension;
import com.vaadin.ui.Notification;

import static com.vaadin.server.FontAwesome.CLIPBOARD;


public class SystemInfoWindowCompanion implements SystemInfoWindow.Companion {
    @Override
    public void initInfoTable(Table infoTable) {
        CubaTable webTable = (CubaTable) WebComponentsHelper.unwrap(infoTable);
        webTable.setTextSelectionEnabled(true);
    }

    @Override
    public void addCopyButton(BoxLayout boxLayout, String description, String success, String fail,
                              String cubaCopyLogContentClass, ComponentsFactory componentsFactory) {
        if (CubaCopyButtonExtension.browserSupportCopy()) {
            Button copyButton = componentsFactory.createComponent(WebButton.class);
            copyButton.setId("copy");
            copyButton.setVisible(false);
            com.vaadin.ui.Button button = copyButton.unwrap(com.vaadin.ui.Button.class);
            button.setIcon(CLIPBOARD);
            button.setDescription(description);
            CubaCopyButtonExtension copyExtension = CubaCopyButtonExtension.copyWith(button,
                    cubaCopyLogContentClass + " textarea");
            copyExtension.addCopyListener(event ->
                    Notification.show(event.isSuccess() ? success : fail,
                            Notification.Type.TRAY_NOTIFICATION));
            boxLayout.add(copyButton);
        }
    }
}