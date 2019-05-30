/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.testsupport;

import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;

/**
 * Indicates the required state of entity instances created by {@link TestEntityFactory}.
 */
public enum TestEntityState {
    NEW {
        @Override
        public void setState(Entity entity) {
        }
    },
    DETACHED {
        @Override
        public void setState(Entity entity) {
            if (entity instanceof BaseGenericIdEntity) {
                BaseEntityInternalAccess.setNew((BaseGenericIdEntity) entity, false);
                BaseEntityInternalAccess.setDetached((BaseGenericIdEntity) entity, true);
            } else
                throw new UnsupportedOperationException("entity is not BaseGenericIdEntity: " + entity.getClass());
        }
    };

    public abstract void setState(Entity entity);
}
