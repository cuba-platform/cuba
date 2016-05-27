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

package com.haulmont.cuba.gui.app.security.role.edit;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.gui.app.security.entity.PermissionVariant;

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