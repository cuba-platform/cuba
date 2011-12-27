/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.app.security.role;

import com.haulmont.cuba.desktop.gui.components.DesktopComponentsHelper;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.AttributePermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.ui.AttributePermissionVariant;
import com.haulmont.cuba.security.entity.ui.AttributeTarget;
import com.haulmont.cuba.security.entity.ui.MultiplePermissionTarget;

import javax.swing.*;
import java.util.Iterator;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class AttributePermissionsFrameCompanion implements AttributePermissionsFrame.Companion {
    @Override
    public void initPermissionColoredColumn(final Table propertyPermissionsTable) {
        propertyPermissionsTable.addGeneratedColumn("permissionsInfo", new Table.ColumnGenerator() {
            @Override
            public Component generateCell(Table table, Object itemId) {
                Label label = AppConfig.getFactory().createComponent(Label.NAME);
                JLabel jLabel = (JLabel) DesktopComponentsHelper.unwrap(label);

                CollectionDatasource<MultiplePermissionTarget, String> datasource = propertyPermissionsTable.getDatasource();
                MultiplePermissionTarget target = datasource.getItem((String) itemId);

                int i = 0;
                StringBuilder builder = new StringBuilder("<html>");
                Iterator<AttributeTarget> iterator = target.getPermissions().iterator();
                while (iterator.hasNext() && i < 5) {
                    AttributeTarget attributeTarget = iterator.next();
                    AttributePermissionVariant permissionVariant = attributeTarget.getPermissionVariant();
                    if (permissionVariant != AttributePermissionVariant.NOTSET) {
                        if (i < 4) {
                            if (i > 0)
                                builder.append(", ");

                            builder.append("<font color=\"").append(permissionVariant.getColor()).append("\">")
                                   .append(attributeTarget.getId()).append("</font>");
                        } else {
                            builder.append(", ...");
                        }
                        i++;
                    }
                }
                builder.append("</html>");
                jLabel.setText(builder.toString());

                return label;
            }
        });
    }
}
