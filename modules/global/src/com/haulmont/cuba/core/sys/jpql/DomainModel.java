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

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.sys.jpql.model.Entity;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@NotThreadSafe
public class DomainModel {
    protected Map<String, Entity> entities = new HashMap<>();
    protected ExtendedEntities extendedEntities;

    public DomainModel(ExtendedEntities extendedEntities, Entity... initialEntities) {
        this(initialEntities);
        this.extendedEntities = extendedEntities;
    }

    public DomainModel(Entity... initialEntities) {
        for (Entity initialEntity : initialEntities) {
            add(initialEntity);
        }
    }

    public void add(Entity entity) {
        if (entity == null)
            throw new NullPointerException("No entity passed");

        entities.put(entity.getName(), entity);
    }

    public List<Entity> findEntitiesStartingWith(String lastWord) {
        List<Entity> result = entities.values().stream()
                .filter(entity -> entity.getName().startsWith(lastWord))
                .collect(Collectors.toList());
        return result;
    }

    public Entity getEntityByName(String requiredEntityName) throws UnknownEntityNameException {
        if (extendedEntities != null) {
            MetaClass effectiveMetaClass = extendedEntities.getEffectiveMetaClass(requiredEntityName);
            requiredEntityName = effectiveMetaClass.getName();
        }

        Entity entity = entities.get(requiredEntityName);
        if (entity == null) {
            throw new UnknownEntityNameException(requiredEntityName);
        } else {
            return entity;
        }
    }
}