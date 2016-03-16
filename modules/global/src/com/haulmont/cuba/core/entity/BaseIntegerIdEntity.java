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
 * Base class for persistent entities with Integer identifier.
 *
 */
@MappedSuperclass
public abstract class BaseIntegerIdEntity extends BaseGenericIdEntity<Integer> {

    private static final long serialVersionUID = 1748237513475338490L;

    @Id
    @Column(name = "ID")
    protected Integer id;

    @Column(name = "UUID", nullable = false)
    protected UUID uuid;

    protected BaseIntegerIdEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}