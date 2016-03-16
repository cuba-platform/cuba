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
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * Base class for persistent entities with String identifier.
 * <p>
 * Does not define an identifier field. Inheritors must define a field of type String and add
 * {@link javax.persistence.Id} annotation to it, e.g.
 * <pre>
 *  &#64;Id
 *  &#64;Column(name = "CODE")
 *  protected String code;
 * </pre>
 *
 */
@MappedSuperclass
public abstract class BaseStringIdEntity extends BaseGenericIdEntity<String> {

    private static final long serialVersionUID = -1887225952123433245L;

    @Column(name = "UUID", nullable = false)
    protected UUID uuid;

    protected BaseStringIdEntity() {
        uuid = UuidProvider.createUuid();
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public abstract String getId();
}