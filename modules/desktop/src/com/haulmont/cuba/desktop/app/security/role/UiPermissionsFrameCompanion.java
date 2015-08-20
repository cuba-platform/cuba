/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.app.security.role;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.UiPermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.UiPermissionVariant;

import javax.swing.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class UiPermissionsFrameCompanion implements UiPermissionsFrame.Companion {

    protected Messages messages = AppBeans.get(Messages.NAME);

    @Override
    public void initPermissionsColoredColumns(Table uiPermissionsTable) {
        uiPermissionsTable.addGeneratedColumn("permissionVariant", new Table.ColumnGenerator<UiPermissionTarget>() {
            @Override
            public Component generateCell(UiPermissionTarget entity) {
                Label label = AppConfig.getFactory().createComponent(Label.class);
                JLabel jLabel = (JLabel) DesktopComponentsHelper.unwrap(label);

                StringBuilder builder = new StringBuilder();

                UiPermissionVariant permissionVariant = entity.getPermissionVariant();

                if (permissionVariant != UiPermissionVariant.NOTSET) {
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