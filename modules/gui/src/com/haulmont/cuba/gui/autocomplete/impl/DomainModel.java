package com.haulmont.cuba.jpql.impl;

import com.haulmont.cuba.jpql.impl.model.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 20:42:50
 */
public class DomainModel {
    private List<Entity> entities = new ArrayList<Entity>();

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
