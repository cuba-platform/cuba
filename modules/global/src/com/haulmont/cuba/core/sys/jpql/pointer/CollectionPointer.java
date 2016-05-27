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

package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.model.Entity;

public class CollectionPointer implements com.haulmont.cuba.core.sys.jpql.pointer.Pointer {
    private Entity entity;

    public CollectionPointer(Entity entity) {
        if (entity == null)
            throw new NullPointerException("No entity passed");

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public Pointer next(DomainModel model, String field) {
        return NoPointer.instance();
    }
}