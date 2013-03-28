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
import com.haulmont.cuba.gui.security.entity.OperationPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;

import javax.swing.*;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsFrameCompanion implements EntityPermissionsFrame.Companion {
    @Override
    public void initPermissionColoredColumns(Table entityPermissionsTable) {
        entityPermissionsTable.addGeneratedColumn("createPermissionVariant", new Table.ColumnGenerator<OperationPermissionTarget>() {
            @Override
            public Component generateCell(OperationPermissionTarget target) {
                return generateLabelByPermissionVariant(target.getCreatePermissionVariant());
            }
        });

        entityPermissionsTable.addGeneratedColumn("readPermissionVariant", new Table.ColumnGenerator<OperationPermissionTarget>() {
            @Override
            public Component generateCell(OperationPermissionTarget target) {
                return generateLabelByPermissionVariant(target.getReadPermissionVariant());
            }
        });

        entityPermissionsTable.addGeneratedColumn("updatePermissionVariant", new Table.ColumnGenerator<OperationPermissionTarget>() {
            @Override
            public Component generateCell(OperationPermissionTarget target) {
                return generateLabelByPermissionVariant(target.getUpdatePermissionVariant());
            }
        });

        entityPermissionsTable.addGeneratedColumn("deletePermissionVariant", new Table.ColumnGenerator<OperationPermissionTarget>() {
            @Override
            public Component generateCell(OperationPermissionTarget target) {
                return generateLabelByPermissionVariant(target.getDeletePermissionVariant());
            }
        });
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