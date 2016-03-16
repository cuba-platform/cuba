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
import com.haulmont.cuba.gui.app.security.entity.OperationPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.EntityPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebComponentsUtils;

/**
 */
public class EntityPermissionsFrameCompanion implements EntityPermissionsFrame.Companion {

    protected Messages messages = AppBeans.get(Messages.NAME);

    @Override
    public void initPermissionColoredColumns(Table entityPermissionsTable) {
        addGeneratedColumnByOperation(entityPermissionsTable, "createPermissionVariant");
        addGeneratedColumnByOperation(entityPermissionsTable, "readPermissionVariant");
        addGeneratedColumnByOperation(entityPermissionsTable, "updatePermissionVariant");
        addGeneratedColumnByOperation(entityPermissionsTable, "deletePermissionVariant");
    }

    @Override
    public void initTextFieldFilter(TextField entityFilter, final Runnable runnable) {
        WebComponentsHelper.addEnterShortcut(entityFilter, runnable);
    }

    protected void addGeneratedColumnByOperation(Table entityPermissionsTable, final String propertyName) {
        entityPermissionsTable.addGeneratedColumn(propertyName, new Table.ColumnGenerator<OperationPermissionTarget>() {
            @Override
            public Component generateCell(OperationPermissionTarget entity) {
                return generateLabelByPermissionVariant(entity.<PermissionVariant>getValue(propertyName));
            }
        });
    }

    protected Label generateLabelByPermissionVariant(PermissionVariant permissionVariant) {
        Label label = AppConfig.getFactory().createComponent(Label.class);

        WebComponentsUtils.allowHtmlContent(label);

        StringBuilder builder = new StringBuilder();
        if (permissionVariant != PermissionVariant.NOTSET) {
            builder.append("<span class=\"role-permission-").append(permissionVariant.getColor()).append("\">")
                    .append(messages.getMessage(permissionVariant)).append("</span>");
        }

        label.setValue(builder.toString());

        return label;
    }
}