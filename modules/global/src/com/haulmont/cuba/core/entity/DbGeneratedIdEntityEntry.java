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

package com.haulmont.cuba.core.entity;

import java.io.Serializable;
import java.util.function.Function;

public abstract class DbGeneratedIdEntityEntry<K extends Number> extends PersistentEntityEntry<IdProxy<K>> {
    protected IdProxy<K> idProxy;

    public DbGeneratedIdEntityEntry(Entity entity) {
        super(entity);
    }

    protected static class IdExtractor<T extends Number> implements Function<Entity, T>, Serializable {
        @SuppressWarnings("unchecked")
        @Override
        public T apply(Entity entity) {
            return ((DbGeneratedIdEntityEntry<T>)entity.getEntityEntry()).getPersistentId();
        }
    }

    @Override
    public IdProxy<K> getId() {
        if (idProxy == null) {
            idProxy = new IdProxy<>(entity, new IdExtractor<>());
        }
        // return a copy cleaned from the reference to the entity
        return idProxy.copy();
    }

    @Override
    public void setId(IdProxy<K> idProxy) {
        this.idProxy = idProxy.copy(false);
        setPersistentId(this.idProxy.get());
        this.idProxy.setEntity(entity);
    }

    public abstract void setPersistentId(K dbId);

    public abstract K getPersistentId();


    @SuppressWarnings("unchecked")
    public void copySystemState(PersistentEntityEntry entityEntry) {
        super.copySystemState(entityEntry);
        if (entityEntry instanceof DbGeneratedIdEntityEntry) {
            setPersistentId((K) ((DbGeneratedIdEntityEntry<?>) entityEntry).getPersistentId());
        }
    }
}
