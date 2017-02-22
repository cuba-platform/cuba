/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.HasUuid;
import com.haulmont.cuba.core.entity.IdProxy;

import java.util.UUID;

public class ReferenceToPkSupport {
    protected String uuidProperty;
    protected String longProperty;
    protected String intProperty;
    protected String stringProperty;

    public void setReference(Entity source, Entity reference) {
        Object pkValue = getPkValue(reference);
        if (pkValue instanceof UUID) {
            source.setValue(uuidProperty, pkValue);
        } else if (pkValue instanceof Long) {
            source.setValue(longProperty, pkValue);
        } else if (pkValue instanceof Integer) {
            source.setValue(intProperty, pkValue);
        } else if (pkValue instanceof String) {
            source.setValue(stringProperty, pkValue);
        } else if (pkValue == null) {
            source.setValue(uuidProperty, null);
            source.setValue(longProperty, null);
            source.setValue(intProperty, null);
            source.setValue(stringProperty, null);
        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported primary key type: %s", pkValue.getClass().getSimpleName()));
        }
    }

    public Object getPkValue(Entity entity) {
        if (entity instanceof HasUuid) {
            return ((HasUuid) entity).getUuid();
        }
        Object entityId = entity.getId();
        if (entityId instanceof IdProxy) {
            return ((IdProxy) entityId).get();
        }
        return entity.getId();
    }
}
