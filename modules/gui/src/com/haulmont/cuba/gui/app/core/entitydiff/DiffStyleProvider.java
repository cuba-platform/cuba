/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityClassPropertyDiff;
import com.haulmont.cuba.core.global.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.Table;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class DiffStyleProvider implements Table.StyleProvider {
    @Override
    public String getStyleName(Entity item, Object property) {
        if (item instanceof EntityClassPropertyDiff) {
            switch (((EntityPropertyDiff) item).getItemState()){
                case Added:
                    return "green";

                case Modified:
                    return "blue";

                case Normal:
                    return "";

                case Removed:
                    return "red";
            }
        }
        return null;
    }

    @Override
    public String getItemIcon(Entity item) {
        return null;
    }
}
