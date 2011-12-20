/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.chile.core.model.MetaPropertyPath;
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
    public String getStyleName(Entity item, Object property) {
        if (property != null) {
            MetaPropertyPath metaPropertyPath = (MetaPropertyPath) property;
            if ("caption".equals(metaPropertyPath.getMetaProperty().getName())) {
                if (item instanceof BasicPermissionTarget) {
                    PermissionVariant permissionVariant = ((BasicPermissionTarget) item).getPermissionVariant();
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
    public String getItemIcon(Entity item) {
        return null;
    }
}