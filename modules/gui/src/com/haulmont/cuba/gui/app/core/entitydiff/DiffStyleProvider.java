/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityBasicPropertyDiff;
import com.haulmont.cuba.core.global.EntityClassPropertyDiff;
import com.haulmont.cuba.core.global.EntityCollectionPropertyDiff;
import com.haulmont.cuba.core.global.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.Table;

/**
 * Set icons and colors for EntityDiff UI
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DiffStyleProvider implements Table.StyleProvider {

    @Override
    public String getStyleName(Entity item, Object property) {
        if (property != null) {
            MetaPropertyPath metaPropertyPath = (MetaPropertyPath) property;
            if ("name".equals(metaPropertyPath.getMetaProperty().getName())) {
                if (item instanceof EntityClassPropertyDiff) {
                    switch (((EntityPropertyDiff) item).getItemState()) {
                        case Added:
                            return "addedItem";

                        case Modified:
                            return "modifiedItem";

                        case Normal:
                            if (((EntityClassPropertyDiff) item).isLinkChange())
                                return "chainItem";
                            else
                                return "modifiedItem";

                        case Removed:
                            return "removedItem";
                    }
                } else if (item instanceof EntityCollectionPropertyDiff) {
                    return "categoryItem";
                } else if (item instanceof EntityBasicPropertyDiff) {
                    //return "modifiedItem";
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
