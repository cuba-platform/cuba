/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.app.security.role;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.ScreenPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;

import javax.swing.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class ScreenPermissionsFrameCompanion implements ScreenPermissionsFrame.Companion {

    protected Messages messages = AppBeans.get(Messages.NAME);

    @Override
    public void initPermissionColoredColumns(TreeTable screenPermissionsTree) {
        screenPermissionsTree.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator<BasicPermissionTarget>() {
            @Override
            public Component generateCell(BasicPermissionTarget entity) {
                Label label = AppConfig.getFactory().createComponent(Label.NAME);
                JLabel jLabel = (JLabel) DesktopComponentsHelper.unwrap(label);

                StringBuilder builder = new StringBuilder();

                PermissionVariant permissionVariant = entity.getPermissionVariant();

                if (permissionVariant != PermissionVariant.NOTSET) {
                    builder.append("<html>");
                    builder.append("<font color=\"").append(permissionVariant.getColor()).append("\">")
                            .append(messages.getMessage(permissionVariant)).append("</font>");

                    builder.append("</html>");
                }

                jLabel.setText(builder.toString());

                return label;
            }
        }, Label.class);
    }
}
