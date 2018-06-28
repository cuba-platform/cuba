/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.global;

import com.google.common.collect.ForwardingSet;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.entity.Entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of {@code Set<Entity>} with convenient methods for getting entities by a prototype instance
 * or by a class and id.
 *
 * @see #get(Entity)
 * @see #get(Class, Object)
 * @see #optional(Entity)
 * @see #optional(Class, Object)
 */
public class EntitySet extends ForwardingSet<Entity> implements Serializable {

    private Set<? extends Entity> entities;

    public EntitySet(Set<? extends Entity> entities) {
        this.entities = entities;
    }

    public EntitySet(Collection<? extends Entity> entities) {
        this.entities = new HashSet<>(entities);
    }

    /**
     * Creates the {@code EntitySet} wrapping an existing set.
     */
    public static EntitySet of(Set<? extends Entity> entities) {
        return new EntitySet(entities);
    }

    /**
     * Creates the {@code EntitySet} by copying the given collection to the internal set.
     */
    public static EntitySet of(Collection<? extends Entity> entities) {
        return new EntitySet(entities);
    }

    /**
     * Returns the entity wrapped in {@code Optional} if it exists in the set.
     *
     * @param entityClass class of entity
     * @param entityId entity id
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity<K>, K> Optional<T> optional(Class<T> entityClass, K entityId) {
        Preconditions.checkNotNullArgument(entityClass, "entityClass is null");
        Preconditions.checkNotNullArgument(entityId, "entityId is null");
        return (Optional<T>) entities.stream()
                .filter(entity -> entityClass.equals(entity.getClass()) && entity.getId().equals(entityId))
                .findFirst();
    }

    /**
     * Returns the entity wrapped in {@code Optional} if it exists in the set.
     *
     * @param prototype a prototype instance whose class and id are used to look up an entity in the set.
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> Optional<T> optional(T prototype) {
        Preconditions.checkNotNullArgument(prototype, "prototype entity is null");
        return (Optional<T>) optional(prototype.getClass(), prototype.getId());
    }

    /**
     * Returns the entity if it exists in the set.
     *
     * @param entityClass class of entity
     * @param entityId entity id
     * @throws IllegalArgumentException if the entity not found
     */
    public <T extends Entity<K>, K> T get(Class<T> entityClass, K entityId) {
        return optional(entityClass, entityId).orElseThrow(() -> new IllegalArgumentException("Entity not found"));
    }

    /**
     * Returns the entity if it exists in the set.
     *
     * @param prototype a prototype instance whose class and id are used to look up an entity in the set.
     * @throws IllegalArgumentException if the entity not found
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(T prototype) {
        Preconditions.checkNotNullArgument(prototype, "prototype entity is null");
        return (T) get(prototype.getClass(), prototype.getId());
    }

    @Override
    protected Set delegate() {
        return entities;
    }
}
