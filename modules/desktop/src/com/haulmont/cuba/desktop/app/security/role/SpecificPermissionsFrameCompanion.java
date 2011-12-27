/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.app.security.role;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.SpecificPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class SpecificPermissionsFrameCompanion implements SpecificPermissionsFrame.Companion {
    @Override
    public void initPermissionColoredColumns(TreeTable specificPermissionsTree) {
        specificPermissionsTree.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table, Object itemId) {
                Label label = AppConfig.getFactory().createComponent(Label.NAME);
                JLabel jLabel = (JLabel) DesktopComponentsHelper.unwrap(label);

                StringBuilder builder = new StringBuilder();

                CollectionDatasource<BasicPermissionTarget, String> ds = table.getDatasource();
                PermissionVariant permissionVariant = ds.getItem((String) itemId).getPermissionVariant();

                if (permissionVariant != PermissionVariant.NOTSET) {
                    builder.append("<html>");
                    builder.append("<font color=\"").append(permissionVariant.getColor()).append("\">")
                            .append(MessageProvider.getMessage(permissionVariant)).append("</font>");

                    builder.append("</html>");
                }

                jLabel.setText(builder.toString());

                return label;
            }
        });
    }
}
