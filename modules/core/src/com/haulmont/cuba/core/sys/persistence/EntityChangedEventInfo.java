/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.core.sys.persistence;

import com.haulmont.cuba.core.app.events.AttributeChanges;
import com.haulmont.cuba.core.app.events.EntityChangedEvent;
import com.haulmont.cuba.core.entity.Entity;

import static com.haulmont.cuba.core.app.events.EntityChangedEvent.Type.*;

public class EntityChangedEventInfo {
    private final Object source;
    private final Entity entity;
    private EntityChangedEvent.Type type;
    private final AttributeChanges changes;

    public EntityChangedEventInfo(Object source,
                                  Entity entity,
                                  EntityChangedEvent.Type type,
                                  AttributeChanges changes) {
        this.source = source;
        this.entity = entity;
        this.type = type;
        this.changes = changes;
    }

    public Object getSource() {
        return source;
    }

    public Entity getEntity() {
        return entity;
    }

    public EntityChangedEvent.Type getType() {
        return type;
    }

    public AttributeChanges getChanges() {
        return changes;
    }

    public void mergeWith(EntityChangedEventInfo otherInfo) {
        if (otherInfo.type == DELETED)
            type = DELETED;
        else if (otherInfo.type == CREATED)
            type = CREATED;

        changes.mergeWith(otherInfo.getChanges());
    }
}
