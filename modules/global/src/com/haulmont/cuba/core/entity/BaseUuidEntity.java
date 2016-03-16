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
package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with UUID identifier.<br>
 * Inherit from it if you need an entity without optimistic locking, update and soft deletion info.
 *
 */
@MappedSuperclass
public abstract class BaseUuidEntity extends BaseGenericIdEntity<UUID> {

    private static final long serialVersionUID = -2217624132287086972L;

    @Id
    @Column(name = "ID")
    protected UUID id;

    public BaseUuidEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return id;
    }
}