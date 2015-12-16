/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.entity.diff.EntityClassPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityCollectionPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.ListComponent.IconProvider;

import javax.annotation.Nullable;

/**
 * @author artamonov
 * @version $Id$
 */
public class DiffIconProvider implements IconProvider<EntityPropertyDiff> {

    @Nullable
    @Override
    public String getItemIcon(EntityPropertyDiff entity) {
        if (entity instanceof EntityClassPropertyDiff) {
            switch (entity.getItemState()) {
                case Added:
                    return "icons/plus.png";

                case Modified:
                    return "icons/edit.png";

                case Normal:
                    if (((EntityClassPropertyDiff) entity).isLinkChange()) {
                        return "icons/chain.png";
                    } else {
                        return "icons/edit.png";
                    }

                case Removed:
                    return "icons/minus.png";
            }
        } else if (entity instanceof EntityCollectionPropertyDiff) {
            return "icons/category.png";
        }

        return null;
    }
}