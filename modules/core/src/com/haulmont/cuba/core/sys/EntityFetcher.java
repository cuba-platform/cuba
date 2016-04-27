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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.entity.EmbeddableEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

/**
 * Fetches entities by views by accessing reference attributes.
 * Used for cached entities.
 */
@Component(EntityFetcher.NAME)
public class EntityFetcher {

    public static final String NAME = "cuba_EntityFetcher";

    private Logger log = LoggerFactory.getLogger(EntityFetcher.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected Persistence persistence;

    public void fetch(Entity instance, View view) {
        if (view == null)
            return;
        fetch(instance, view, new HashMap<>());
    }

    protected void fetch(Entity entity, View view, Map<Instance, Set<View>> visited) {
        Set<View> views = visited.get(entity);
        if (views == null) {
            views = new HashSet<>();
            visited.put(entity, views);
        } else if (views.contains(view)) {
            return;
        }
        views.add(view);

        if (log.isTraceEnabled()) log.trace("Fetching instance " + entity);

        MetaClass metaClass = metadata.getClassNN(entity.getClass());
        for (ViewProperty property : view.getProperties()) {
            MetaProperty metaProperty = metaClass.getPropertyNN(property.getName());
            if (!metaProperty.getRange().isClass())
                continue;

            if (log.isTraceEnabled()) log.trace("Fetching property " + property.getName());

            Object value = entity.getValue(property.getName());
            View propertyView = property.getView();
            if (value != null && propertyView != null) {
                if (value instanceof Collection) {
                    for (Object item : ((Collection) value)) {
                        if (item instanceof Entity)
                            fetch((Entity) item, propertyView, visited);
                    }
                } else if (value instanceof Entity) {
                    Entity e = (Entity) value;
                    if (PersistenceHelper.isDetached(value) && !(value instanceof EmbeddableEntity)) {
                        if (log.isTraceEnabled()) {
                            log.trace("Object " + value + " is detached, loading it");
                        }
                        value = persistence.getEntityManager().find(e.getClass(), e.getId());
                        if (value == null) {
                            // the instance is most probably deleted
                            continue;
                        }
                        entity.setValue(property.getName(), value);
                    }
                    fetch(e, propertyView, visited);
                }
            }
        }
    }
}
