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

import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.app.core.showinfo.SystemInfoWindow;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.ComponentContainer;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.web.widgets.CubaCopyButtonExtension;
import com.haulmont.cuba.web.widgets.CubaTable;
import com.vaadin.ui.Notification;

public class SystemInfoWindowCompanion implements SystemInfoWindow.Companion {
    @Override
    public void initInfoTable(Table infoTable) {
        CubaTable vTable = infoTable.unwrap(CubaTable.class);
        vTable.setTextSelectionEnabled(true);
    }

    @Override
    public void addCopyButton(ComponentContainer container, String description,
                              String successMessage, String failMessage,
                              String cubaCopyLogContentClass, UiComponents uiComponents) {
        if (CubaCopyButtonExtension.browserSupportCopy()) {
            Button copyButton = uiComponents.create(Button.class);

            copyButton.setIconFromSet(CubaIcon.CLIPBOARD);
            copyButton.setId("copy");
            copyButton.setVisible(false);
            copyButton.setDescription(description);

            com.vaadin.ui.Button button = copyButton.unwrap(com.vaadin.ui.Button.class);
            CubaCopyButtonExtension copyExtension =
                    CubaCopyButtonExtension.copyWith(button, cubaCopyLogContentClass + " textarea");

            copyExtension.addCopyListener(event ->
                    Notification.show(event.isSuccess() ? successMessage : failMessage,
                            Notification.Type.TRAY_NOTIFICATION));
            container.add(copyButton);
        }
    }
}