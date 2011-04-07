/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.app.ui.security.user;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.app.security.role.edit.RoleEditor;
import com.haulmont.cuba.gui.components.AbstractCompanion;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.config.MenuConfig;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class RoleEditorCompanion extends AbstractCompanion implements RoleEditor.Companion {

    public RoleEditorCompanion(AbstractFrame frame) {
        super(frame);
    }

    public void initTable(final Table table) {
        com.vaadin.ui.Table vTable = (com.vaadin.ui.Table) WebComponentsHelper.unwrap(table);
        MetaPropertyPath targetCol = table.getDatasource().getMetaClass().getPropertyEx("target");
        // TODO replace with generic column generator
        vTable.addGeneratedColumn(
                targetCol,
                new com.vaadin.ui.Table.ColumnGenerator() {
                    public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        Permission permission = (Permission) table.getDatasource().getItem(itemId);
                        if (permission.getTarget() == null)
                            return null;
                        if (permission.getType().equals(PermissionType.SCREEN)) {
                            String id = permission.getTarget();
                            String caption = MenuConfig.getMenuItemCaption(id.substring(id.indexOf(":") + 1));
                            return new com.vaadin.ui.Label(id + " (" + caption + ")");
                        } else {
                            return new com.vaadin.ui.Label(permission.getTarget());
                        }
                    }
                }
        );

        MetaPropertyPath valueCol = table.getDatasource().getMetaClass().getPropertyEx("value");
        vTable.addGeneratedColumn(
                valueCol,
                new com.vaadin.ui.Table.ColumnGenerator() {
                    public com.vaadin.ui.Component generateCell(com.vaadin.ui.Table source, Object itemId, Object columnId) {
                        Permission permission = (Permission) table.getDatasource().getItem(itemId);
                        if (permission.getValue() == null)
                            return null;
                        if (permission.getType().equals(PermissionType.ENTITY_ATTR)) {
                            if (permission.getValue() == 0)
                                return new com.vaadin.ui.Label(frame.getMessage("PropertyPermissionValue.DENY"));
                            else if (permission.getValue() == 1)
                                return new com.vaadin.ui.Label(frame.getMessage("PropertyPermissionValue.VIEW"));
                            else
                                return new com.vaadin.ui.Label(frame.getMessage("PropertyPermissionValue.MODIFY"));
                        } else {
                            if (permission.getValue() == 0)
                                return new com.vaadin.ui.Label(frame.getMessage("PermissionValue.DENY"));
                            else
                                return new com.vaadin.ui.Label(frame.getMessage("PermissionValue.ALLOW"));
                        }
                    }
                }
        );
    }
}
