/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.EntityPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.security.entity.OperationPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class EntityPermissionsFrameCompanion implements EntityPermissionsFrame.Companion {
    @Override
    public void initPermissionColoredColumns(Table entityPermissionsTable) {
        addGeneratedColumnByOperation(entityPermissionsTable, "createPermissionVariant");
        addGeneratedColumnByOperation(entityPermissionsTable, "readPermissionVariant");
        addGeneratedColumnByOperation(entityPermissionsTable, "updatePermissionVariant");
        addGeneratedColumnByOperation(entityPermissionsTable, "deletePermissionVariant");
    }

    private void addGeneratedColumnByOperation(Table entityPermissionsTable, final String propertyName) {
        entityPermissionsTable.addGeneratedColumn(propertyName, new Table.ColumnGenerator<OperationPermissionTarget>() {
            @Override
            public Component generateCell(OperationPermissionTarget entity) {
                return generateLabelByPermissionVariant(entity.<PermissionVariant>getValue(propertyName));
            }
        });
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