/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.app.security.role;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.EntityPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.ui.OperationPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsFrameCompanion implements EntityPermissionsFrame.Companion {
    @Override
    public void initPermissionColoredColumns(Table entityPermissionsTable) {
        entityPermissionsTable.addGeneratedColumn("createPermissionVariant", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table, Object itemId) {
                OperationPermissionTarget target = getItem(table, (String) itemId);
                return generateLabelByPermissionVariant(target.getCreatePermissionVariant());
            }
        });

        entityPermissionsTable.addGeneratedColumn("readPermissionVariant", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table, Object itemId) {
                OperationPermissionTarget target = getItem(table, (String) itemId);
                return generateLabelByPermissionVariant(target.getReadPermissionVariant());
            }
        });

        entityPermissionsTable.addGeneratedColumn("updatePermissionVariant", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table, Object itemId) {
                OperationPermissionTarget target = getItem(table, (String) itemId);
                return generateLabelByPermissionVariant(target.getUpdatePermissionVariant());
            }
        });

        entityPermissionsTable.addGeneratedColumn("deletePermissionVariant", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table, Object itemId) {
                OperationPermissionTarget target = getItem(table, (String) itemId);
                return generateLabelByPermissionVariant(target.getDeletePermissionVariant());
            }
        });
    }

    private OperationPermissionTarget getItem(Table table, String itemId) {
        CollectionDatasource<OperationPermissionTarget, String> ds = table.getDatasource();
        return ds.getItem(itemId);
    }

    private Label generateLabelByPermissionVariant(PermissionVariant permissionVariant) {
        Label label = AppConfig.getFactory().createComponent(Label.NAME);
        JLabel jLabel = (JLabel) DesktopComponentsHelper.unwrap(label);

        StringBuilder builder = new StringBuilder();

        if (permissionVariant != PermissionVariant.NOTSET) {
            builder.append("<html>");
            builder.append("<font color=\"").append(permissionVariant.getColor()).append("\">")
                    .append(MessageProvider.getMessage(permissionVariant)).append("</font>");

            builder.append("</html>");
        }

        jLabel.setText(builder.toString());

        return label;
    }
}