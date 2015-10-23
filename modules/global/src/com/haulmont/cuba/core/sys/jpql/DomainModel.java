/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.ExtendedEntities;
import com.haulmont.cuba.core.sys.jpql.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chevelev
 * @version $Id$
 */
public class DomainModel {
    protected List<Entity> entities = new ArrayList<>();
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

        entities.add(entity);
    }

    public List<Entity> findEntitiesStartingWith(String lastWord) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities) {
            String name = entity.getName();
            if (name.startsWith(lastWord)) {
                result.add(entity);
            }
        }
        return result;
    }

    public Entity getEntityByName(String requiredEntityName) throws UnknownEntityNameException {
        if (extendedEntities != null) {
            MetaClass effectiveMetaClass = extendedEntities.getEffectiveMetaClass(requiredEntityName);
            requiredEntityName = effectiveMetaClass.getName();
        }

        for (Entity entity : entities) {
            String name = entity.getName();
            if (name.equals(requiredEntityName)) {
                return entity;
            }
        }
        throw new UnknownEntityNameException(requiredEntityName);
    }
}