/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.SpecificPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;
import com.haulmont.cuba.web.gui.components.WebComponentsUtils;

/**
 * @author artamonov
 * @version $Id$
 */
public class SpecificPermissionsFrameCompanion implements SpecificPermissionsFrame.Companion {

    protected Messages messages = AppBeans.get(Messages.NAME);

    @Override
    public void initPermissionColoredColumns(TreeTable specificPermissionsTree) {
        specificPermissionsTree.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator<BasicPermissionTarget>() {
            @Override
            public Component generateCell(BasicPermissionTarget entity) {
                Label label = AppConfig.getFactory().createComponent(Label.class);

                WebComponentsUtils.allowHtmlContent(label);

                StringBuilder builder = new StringBuilder();

                PermissionVariant permissionVariant = entity.getPermissionVariant();

                if (permissionVariant != PermissionVariant.NOTSET) {
                    builder.append("<span class=\"role-permission-").append(permissionVariant.getColor()).append("\">")
                            .append(messages.getMessage(permissionVariant)).append("</span>");
                }

                label.setValue(builder.toString());

                return label;
            }
        });
    }
}