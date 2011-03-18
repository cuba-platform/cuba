package com.haulmont.cuba.jpql.impl.pointer;

import com.haulmont.cuba.jpql.impl.DomainModel;
import com.haulmont.cuba.jpql.impl.model.Entity;

/**
 * Author: Alexander Chevelev
 * Date: 01.11.2010
 * Time: 0:27:12
 */
public class CollectionPointer implements Pointer {
    private Entity entity;

    public CollectionPointer(Entity entity) {
        if (entity == null)
            throw new NullPointerException("No entity passed");

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Pointer next(DomainModel model, String field) {
        return NoPointer.instance();
    }

}
