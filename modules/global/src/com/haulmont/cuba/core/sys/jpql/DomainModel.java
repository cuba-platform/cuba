/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 20:42:50
 */
public class DomainModel {
    private List<Entity> entities = new ArrayList<Entity>();

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
        List<Entity> result = new ArrayList<Entity>();
        for (Entity entity : entities) {
            String name = entity.getName();
            if (name.startsWith(lastWord)) {
                result.add(entity);
            }
        }
        return result;
    }

    public Entity getEntityByName(String requiredEntityName) throws UnknownEntityNameException {
        for (Entity entity : entities) {
            String name = entity.getName();
            if (name.equals(requiredEntityName)) {
                return entity;
            }
        }
        throw new UnknownEntityNameException(requiredEntityName);
    }
}
