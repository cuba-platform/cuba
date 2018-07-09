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

package com.haulmont.cuba.core.app.events;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.contracts.Id;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.global.Metadata;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class EntityChangedEvent<E extends Entity<K>, K> extends ApplicationEvent implements ResolvableTypeProvider {

    public enum Type {
        CREATED,
        UPDATED,
        DELETED
    }

    private Id<E, K> entityId;
    private Type type;
    private AttributeChanges changes;

    public EntityChangedEvent(Object source, Id<E, K> entityId, Type type, AttributeChanges changes) {
        super(source);
        this.entityId = entityId;
        this.type = type;
        this.changes = changes;
    }

    public Id<E, K> getEntityId() {
        return entityId;
    }

    public Type getType() {
        return type;
    }

    public AttributeChanges getChanges() {
        return changes;
    }

    @Override
    public ResolvableType getResolvableType() {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        ExtendedEntities extendedEntities = metadata.getExtendedEntities();
        MetaClass metaClass = extendedEntities.getOriginalOrThisMetaClass(metadata.getClassNN(entityId.getEntityClass()));
        MetaProperty pkProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
        if (pkProperty == null) {
            throw new IllegalStateException("Unable to send EntityChangedEvent for " + metaClass + " because it has no primary key");
        }
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forClass(metaClass.getJavaClass()),
                ResolvableType.forClass(pkProperty.getJavaType()));
    }

    @Override
    public String toString() {
        return "EntityChangedEvent{" +
                "entityId=" + entityId +
                ", type=" + type +
                ", changes=" + changes +
                '}';
    }
}
