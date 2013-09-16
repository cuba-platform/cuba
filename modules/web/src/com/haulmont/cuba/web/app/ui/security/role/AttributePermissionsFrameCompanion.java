/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.security.role;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.app.security.role.edit.tabs.AttributePermissionsFrame;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.security.entity.AttributePermissionVariant;
import com.haulmont.cuba.gui.security.entity.AttributeTarget;
import com.haulmont.cuba.gui.security.entity.MultiplePermissionTarget;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.shared.ui.label.ContentMode;

import java.util.Iterator;

/**
 * @author artamonov
 * @version $Id$
 */
public class AttributePermissionsFrameCompanion implements AttributePermissionsFrame.Companion {

    @Override
    public void initPermissionColoredColumn(final Table propertyPermissionsTable) {
        propertyPermissionsTable.addGeneratedColumn("permissionsInfo", new Table.ColumnGenerator<MultiplePermissionTarget>() {
            @Override
            public Component generateCell(MultiplePermissionTarget target) {
                Label label = AppConfig.getFactory().createComponent(Label.NAME);
                com.vaadin.ui.Label vLabel = (com.vaadin.ui.Label) WebComponentsHelper.unwrap(label);
                vLabel.setContentMode(ContentMode.HTML);

                int i = 0;
                StringBuilder builder = new StringBuilder();
                Iterator<AttributeTarget> iterator = target.getPermissions().iterator();
                while (iterator.hasNext() && i < MultiplePermissionTarget.SHOW_PERMISSIONS_COUNT) {
                    AttributeTarget attributeTarget = iterator.next();
                    AttributePermissionVariant permissionVariant = attributeTarget.getPermissionVariant();
                    if (permissionVariant != AttributePermissionVariant.NOTSET) {
                        if (i < MultiplePermissionTarget.SHOW_PERMISSIONS_COUNT - 1) {
                            if (i > 0)
                                builder.append(", ");

                            builder.append("<span style=\"color:").append(permissionVariant.getColor()).append(";\">")
                                    .append(attributeTarget.getId()).append("</span>");
                        } else {
                            builder.append(", ...");
                        }
                        i++;
                    }
                }
                vLabel.setValue(builder.toString());

                return label;
            }
        });
    }
}