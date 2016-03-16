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

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.UiPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionVariant;
import com.haulmont.cuba.web.gui.components.WebComponentsUtils;

/**
 */
public class UiPermissionsFrameCompanion implements UiPermissionsFrame.Companion {

    protected Messages messages = AppBeans.get(Messages.NAME);

    @Override
    public void initPermissionsColoredColumns(Table uiPermissionsTable) {
        uiPermissionsTable.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator<UiPermissionTarget>() {
            @Override
            public Component generateCell(UiPermissionTarget entity) {
                Label label = AppConfig.getFactory().createComponent(Label.class);

                WebComponentsUtils.allowHtmlContent(label);

                StringBuilder builder = new StringBuilder();

                UiPermissionVariant permissionVariant = entity.getPermissionVariant();

                if (permissionVariant != UiPermissionVariant.NOTSET) {
                    builder.append("<span style=\"role-permission-").append(permissionVariant.getColor()).append("\">")
                            .append(messages.getMessage(permissionVariant)).append("</span>");
                }

                label.setValue(builder.toString());

                return label;
            }
        });
    }
}