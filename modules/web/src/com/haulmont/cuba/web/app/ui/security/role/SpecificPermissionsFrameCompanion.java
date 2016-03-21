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

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.SpecificPermissionsFrame;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.gui.components.WebComponentsUtils;

import javax.inject.Inject;

/**
 */
public class SpecificPermissionsFrameCompanion implements SpecificPermissionsFrame.Companion {

    @Inject
    protected Messages messages;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Override
    public void initPermissionColoredColumns(TreeTable<BasicPermissionTarget> specificPermissionsTree) {
        specificPermissionsTree.addGeneratedColumn("permissionVariant", entity -> {
            PermissionVariant permissionVariant = entity.getPermissionVariant();
            if (permissionVariant == PermissionVariant.NOTSET)
                return null;

            Label label = componentsFactory.createComponent(Label.class);
            WebComponentsUtils.allowHtmlContent(label);
            String labelValue = "<span class=\"role-permission-" + permissionVariant.getColor() + "\">" +
                    messages.getMessage(permissionVariant) + "</span>";
            label.setValue(labelValue);

            return label;
        });
    }
}