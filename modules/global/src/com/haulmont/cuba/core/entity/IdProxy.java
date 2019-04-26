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
 */

package com.haulmont.cuba.core.entity;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.core.global.UuidProvider;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Class that is used as an entity ID when an actual identifier value is not available until the object is persisted
 * to the database.
 * <p>
 * If you need to create a proxy for an existing ID, use {@link #of(Number)} method.
 *
 * @see BaseDbGeneratedIdEntity
 * @see BaseIdentityIdEntity
 */
public class IdProxy<T extends Number> extends Number implements Serializable {

    private static final long serialVersionUID = 1591247506604691467L;

    private BaseDbGeneratedIdEntity<T> entity;

    private UUID uuid;

    private int hashCode;

    private T value;

    private IdProxy<T> copy;

    IdProxy(BaseDbGeneratedIdEntity<T> entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        this.entity = entity;
        if (entity instanceof HasUuid) {
            this.uuid = ((HasUuid) entity).getUuid();
            if (this.uuid == null)
                throw new IllegalStateException("Entity " + entity.getClass() + " implements HasUuid but its instance has no UUID assigned");
            this.hashCode = this.uuid.hashCode();
        } else {
            this.uuid = UuidProvider.createUuid();
            this.hashCode = 0; // trade-off, see com.haulmont.cuba.primary_keys.IdentityTest.testEquality
        }
    }

    IdProxy(T value, int hashCode) {
        Preconditions.checkNotNullArgument(value, "value is null");
        this.value = value;
        this.hashCode = hashCode;
    }

    private IdProxy() {
    }

    /**
     * Create proxy for the specified ID value. You might need it for providing ID to the {@code EntityManager.find()}
     * method.
     * <p>If you have stored a {@code HasUuid} entity in a hashtable using its ID as a key, use {@link #of(Number, UUID)}
     * method to construct the key when accessing the collection.
     *
     * @param value real ID value
     */
    public static <T extends Number> IdProxy<T> of(T value) {
        return new IdProxy<>(value, 0);
    }

    /**
     * Create proxy for the specified ID and UUID values. You might need it for finding {@code HasUuid} entities stored
     * in hashtables by ID.
     *
     * @param value real ID value
     * @param uuid entity's UUID
     */
    public static <T extends Number> IdProxy<T> of(T value, UUID uuid) {
        Preconditions.checkNotNullArgument(uuid, "uuid is null");
        return new IdProxy<>(value, uuid.hashCode());
    }

    /**
     * Returns a shared copy of this IdProxy cleaned from a reference to entity.
     * <p>DO NOT use shared copies when assigning the same ID to another entity!
     * @see #copy(boolean)
     */
    public IdProxy<T> copy() {
        return copy(true);
    }

    /**
     * Returns a copy of this IdProxy cleaned from a reference to entity.
     * @param shared if true, a shared instance of the copy will be returned to avoid creating new object.
     *               DO NOT use shared copies when assigning the same ID to another entity!
     */
    public IdProxy<T> copy(boolean shared) {
        if (copy == null
                || !shared
                || !Objects.equals(value, copy.value)
                || !Objects.equals(uuid, copy.uuid)
                || (copy.value == null && entity != null && entity.getDbGeneratedId() != null)) {
            copy = new IdProxy<>();
            if (value != null)
                copy.value = value;
            else if (entity != null && entity.getDbGeneratedId() != null)
                copy.value = entity.getDbGeneratedId();
            copy.uuid = uuid;
            copy.hashCode = hashCode;
        }
        return copy;
    }

    /**
     * @return  real ID value or null if it is not assigned yet
     */
    @Nullable
    public T get() {
        if (value != null)
            return value;

        if (entity == null)
            return null;

        return entity.getDbGeneratedId();
    }

    /**
     * @return  real ID value
     * @throws IllegalStateException if it is not assigned yet
     */
    public T getNN() {
        if (value != null)
            return value;

        if (entity == null)
            throw new IllegalStateException("Cannot get primary key value: entity is null");

        if (entity.getDbGeneratedId() == null)
            throw new IllegalStateException("Cannot get primary key value: ID is null in entity " + entity);

        return entity.getDbGeneratedId();
    }

    /**
     * INTERNAL
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * INTERNAL
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * INTERNAL
     */
    void setEntity(BaseDbGeneratedIdEntity<T> entity) {
        this.entity = entity;
    }

    @Override
    public int intValue() {
        T v = get();
        return v == null ? 0 : v.intValue();
    }

    @Override
    public long longValue() {
        T v = get();
        return v == null ? 0 : v.longValue();
    }

    @Override
    public float floatValue() {
        T v = get();
        return v == null ? 0 : v.floatValue();
    }

    @Override
    public double doubleValue() {
        T v = get();
        return v == null ? 0 : v.doubleValue();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        IdProxy that = (IdProxy) other;

        if (value != null) {
            if (that.value != null)
                return value.equals(that.value);

            if (that.entity != null && that.entity.getDbGeneratedId() != null)
                return value.equals(that.entity.getDbGeneratedId());
        }

        if (entity != null && entity.getDbGeneratedId() != null) {
            if (that.entity != null && that.entity.getDbGeneratedId() != null)
                return entity.getDbGeneratedId().equals(that.entity.getDbGeneratedId());

            if (that.value != null)
                return entity.getDbGeneratedId().equals(that.value);
        }

        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        if (value != null)
            return value.toString();

        if (entity != null && entity.getDbGeneratedId() != null)
            return entity.getDbGeneratedId().toString();
        else
            return "?(" + uuid + ")";
    }
}
