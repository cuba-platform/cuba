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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UuidProvider;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.UUID;

/**
 * Base class for persistent embeddable entities.
 *
 */
@MappedSuperclass
public abstract class EmbeddableEntity extends AbstractInstance implements Entity<UUID> {

    private static final long serialVersionUID = 266201862280559076L;

    @Transient
    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    public EmbeddableEntity() {
        id = UuidProvider.createUuid();
    }

    @Override
    public MetaClass getMetaClass() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getSession().getClass(getClass());
    }

    @Override
    public UUID getUuid() {
        return id;
    }
}