/*
 * Copyright (c) 2008-2017 Haulmont.
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

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
@MetaClass(name = "sys$ReferenceToEntity")
@SystemLevel
public class ReferenceToEntity extends EmbeddableEntity {
    private static final long serialVersionUID = -3667689055952380345L;

    @Column(name = "ENTITY_ID")
    private UUID entityId;

    @Column(name = "STRING_ENTITY_ID", length = 255)
    private String stringEntityId;

    @Column(name = "INT_ENTITY_ID")
    private Integer intEntityId;

    @Column(name = "LONG_ENTITY_ID")
    private Long longEntityId;

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getStringEntityId() {
        return stringEntityId;
    }

    public void setStringEntityId(String stringEntityId) {
        this.stringEntityId = stringEntityId;
    }

    public Integer getIntEntityId() {
        return intEntityId;
    }

    public void setIntEntityId(Integer intEntityId) {
        this.intEntityId = intEntityId;
    }

    public Long getLongEntityId() {
        return longEntityId;
    }

    public void setLongEntityId(Long longEntityId) {
        this.longEntityId = longEntityId;
    }

    public Object getObjectEntityId() {
        if (entityId != null) {
            return entityId;
        } else if (longEntityId != null) {
            return longEntityId;
        } else if (intEntityId != null) {
            return intEntityId;
        } else if (stringEntityId != null) {
            return stringEntityId;
        } else {
            return null;
        }
    }

    public void setObjectEntityId(Object referenceId) {
        if (referenceId instanceof UUID) {
            setEntityId((UUID) referenceId);
            setLongEntityId(null);
            setIntEntityId(null);
            setStringEntityId(null);
        } else if (referenceId instanceof Long) {
            setEntityId(null);
            setLongEntityId((Long) referenceId);
            setIntEntityId(null);
            setStringEntityId(null);
        } else if (referenceId instanceof Integer) {
            setEntityId(null);
            setLongEntityId(null);
            setIntEntityId((Integer) referenceId);
            setStringEntityId(null);
        } else if (referenceId instanceof String) {
            setEntityId(null);
            setLongEntityId(null);
            setIntEntityId(null);
            setStringEntityId((String) referenceId);
        } else if (referenceId == null) {
            setEntityId(null);
            setLongEntityId(null);
            setIntEntityId(null);
            setStringEntityId(null);
        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported primary key type: %s", referenceId.getClass().getSimpleName()));
        }
    }
}
