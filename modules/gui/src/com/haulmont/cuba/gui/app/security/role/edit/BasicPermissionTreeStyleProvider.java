/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.security.entity.ui.BasicPermissionTarget;
import com.haulmont.cuba.security.entity.ui.PermissionVariant;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class BasicPermissionTreeStyleProvider implements Table.StyleProvider {
    @Override
    public String getStyleName(Entity entity, String property) {
        if (property != null) {
            if ("caption".equals(property)) {
                if (entity instanceof BasicPermissionTarget) {
                    PermissionVariant permissionVariant = ((BasicPermissionTarget) entity).getPermissionVariant();
                    switch (permissionVariant) {
                        case ALLOWED:
                            return "allowedItem";

                        case DISALLOWED:
                            return "disallowedItem";
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getItemIcon(Entity entity) {
        return null;
    }
}