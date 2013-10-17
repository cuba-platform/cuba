/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.UiPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.security.entity.UiPermissionTarget;
import com.haulmont.cuba.gui.security.entity.UiPermissionVariant;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class UiPermissionsFrameCompanion implements UiPermissionsFrame.Companion {
    @Override
    public void initPermissionsColoredColumns(Table uiPermissionsTable) {
        uiPermissionsTable.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator<UiPermissionTarget>() {
            @Override
            public Component generateCell(UiPermissionTarget entity) {
                Label label = AppConfig.getFactory().createComponent(Label.NAME);
                com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
                vLabel.setContentMode(com.vaadin.ui.Label.CONTENT_XHTML);

                StringBuilder builder = new StringBuilder();

                UiPermissionVariant permissionVariant = entity.getPermissionVariant();

                if (permissionVariant != UiPermissionVariant.NOTSET) {
                    builder.append("<span style=\"color:").append(permissionVariant.getColor()).append(";\">")
                            .append(MessageProvider.getMessage(permissionVariant)).append("</span>");
                }

                vLabel.setValue(builder.toString());

                return label;
            }
        });
    }
}