/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.entity.diff.EntityBasicPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityClassPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityCollectionPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.Table;

/**
 * Set icons and colors for EntityDiff UI
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DiffStyleProvider implements Table.StyleProvider<EntityPropertyDiff> {

    @Override
    public String getStyleName(EntityPropertyDiff entity, String property) {
        if (property != null) {
            if ("name".equals(property)) {
                if (entity instanceof EntityClassPropertyDiff) {
                    switch (entity.getItemState()) {
                        case Added:
                            return "addedItem";

                        case Modified:
                            return "modifiedItem";

                        case Normal:
                            if (((EntityClassPropertyDiff) entity).isLinkChange())
                                return "chainItem";
                            else
                                return "modifiedItem";

                        case Removed:
                            return "removedItem";
                    }
                } else if (entity instanceof EntityCollectionPropertyDiff) {
                    return "categoryItem";
                } else if (entity instanceof EntityBasicPropertyDiff) {
                    //return "modifiedItem";
                }
            }
        }
        return null;
    }
}