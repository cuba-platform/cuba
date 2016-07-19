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
 * If you need to create a proxy for an existing ID, use {@link #of(Long)} method.
 *
 * @see BaseDbGeneratedIdEntity
 * @see BaseIdentityIdEntity
 */
public class IdProxy extends Number implements Serializable {

    private static final long serialVersionUID = 1591247506604691467L;

    private BaseDbGeneratedIdEntity entity;

    private UUID uuid;

    private int hashCode;

    private Long value;

    IdProxy(BaseDbGeneratedIdEntity entity) {
        Preconditions.checkNotNullArgument(entity, "entity is null");
        this.entity = entity;
        if (entity instanceof HasUuid) {
            this.uuid = ((HasUuid) entity).getUuid();
            this.hashCode = this.uuid.hashCode();
        } else {
            this.uuid = UuidProvider.createUuid();
            this.hashCode = 0; // trade-off, see com.haulmont.cuba.primary_keys.IdentityTest.testEquality
        }
    }

    IdProxy(Long value) {
        Preconditions.checkNotNullArgument(value, "value is null");
        this.value = value;
    }

    /**
     * Create proxy for the specified ID value. You might need it for providing ID to the {@code EntityManager.find()}
     * method.
     * @param value real ID value
     */
    public static IdProxy of(Long value) {
        return new IdProxy(value);
    }

    /**
     * @return  real ID value or null if it is not assigned yet
     */
    @Nullable
    public Long get() {
        return value != null ? value : entity.getDbGeneratedId();
    }

    /**
     * @return  real ID value
     * @throws IllegalStateException if it is not assigned yet
     */
    public Long getNN() {
        if (value != null)
            return value;

        if (entity.getDbGeneratedId() == null)
            throw new IllegalStateException("ID is not assigned for entity " + entity);
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

    @Override
    public int intValue() {
        Long v = get();
        return v == null ? 0 : v.intValue();
    }

    @Override
    public long longValue() {
        Long v = get();
        return v == null ? 0 : v.longValue();
    }

    @Override
    public float floatValue() {
        Long v = get();
        return v == null ? 0 : v.floatValue();
    }

    @Override
    public double doubleValue() {
        Long v = get();
        return v == null ? 0 : v.doubleValue();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        IdProxy that = (IdProxy) other;

        if (value != null)
            return value.equals(that.value);

        if (entity.getDbGeneratedId() == null || that.entity.getDbGeneratedId() == null)
            return Objects.equals(uuid, that.uuid);

        return Objects.equals(entity.getDbGeneratedId(), that.entity.getDbGeneratedId());
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        if (value != null)
            return value.toString();

        if (entity.getDbGeneratedId() != null)
            return entity.getDbGeneratedId().toString();
        else
            return "?(" + uuid + ")";
    }
}
