/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.EntityPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.ui.OperationPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

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
        com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
        vLabel.setContentMode(com.vaadin.ui.Label.CONTENT_XHTML);

        StringBuilder builder = new StringBuilder();
        if (permissionVariant != PermissionVariant.NOTSET) {
            builder.append("<span style=\"color:").append(permissionVariant.getColor()).append(";\">")
                    .append(MessageProvider.getMessage(permissionVariant)).append("</span>");
        }

        vLabel.setValue(builder.toString());

        return label;
    }
}