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

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.impl.AbstractInstance;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.MetadataTools;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Normalizes references between items of a collection.
 */
@Component(EntityReferencesNormalizer.NAME)
public class EntityReferencesNormalizer {

    public static final String NAME = "cuba_EntityReferencesNormalizer";

    @Inject
    private EntityStates entityStates;
    @Inject
    private MetadataTools metadataTools;

    /**
     * For each entity in the collection, updates to-one reference properties to point to instances which are items of
     * the collection.
     */
    public void updateReferences(Collection<Entity> entities) {
        updateReferences(entities, entities);
    }

    /**
     * For each entity in the first collection, updates to-one reference properties to point to instances from
     * the second collection.
     */
    public void updateReferences(Collection<Entity> entities, Collection<Entity> references) {
        for (Entity entity : entities) {
            if (entity == null)
                continue;
            for (Entity refEntity : references) {
                if (entity != refEntity) {
                    updateReferences(entity, refEntity, new HashSet<>());                }
            }
        }
    }

    protected void updateReferences(Entity entity, Entity refEntity, Set<Entity> visited) {
        if (visited.contains(entity))
            return;
        visited.add(entity);

        MetaClass refEntityMetaClass = refEntity.getMetaClass();
        for (MetaProperty property : entity.getMetaClass().getProperties()) {
            if (!property.getRange().isClass() || !property.getRange().asClass().equals(refEntityMetaClass))
                continue;
            if (entityStates.isLoaded(entity, property.getName())) {
                if (property.getRange().getCardinality().isMany()) {
                    Collection collection = entity.getValue(property.getName());
                    if (collection != null) {
                        for (Object obj : collection) {
                            updateReferences((Entity) obj, refEntity, visited);
                        }
                    }
                } else {
                    Entity value = entity.getValue(property.getName());
                    if (value != null) {
                        if (value.getId().equals(refEntity.getId())) {
                            if (entity instanceof AbstractInstance) {
                                if (property.isReadOnly() && metadataTools.isNotPersistent(property)) {
                                    continue;
                                }
                                ((AbstractInstance) entity).setValue(property.getName(), refEntity, false);
                            }
                        } else {
                            updateReferences(value, refEntity, visited);
                        }
                    }
                }
            }
        }
    }
}
