/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.security.entity.PermissionVariant;

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
}