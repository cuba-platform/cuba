/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author chevelev
 * @version $Id$
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