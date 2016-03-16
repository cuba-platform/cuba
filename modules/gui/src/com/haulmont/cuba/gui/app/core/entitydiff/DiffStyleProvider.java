/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.gui.app.core.entitydiff;

import com.haulmont.cuba.core.entity.diff.EntityBasicPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityClassPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityCollectionPropertyDiff;
import com.haulmont.cuba.core.entity.diff.EntityPropertyDiff;
import com.haulmont.cuba.gui.components.Table;

/**
 * Set icons and colors for EntityDiff UI
 *
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