/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.QueryVariableContext;
import com.haulmont.cuba.core.sys.jpql.UnknownEntityNameException;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.Entity;

/**
 * @author chevelev
 * @version $Id$
 */
public class EntityPointer implements Pointer {
    private Entity entity;

    private EntityPointer(Entity entity) {
        if (entity == null)
            throw new NullPointerException("No entity passed");

        this.entity = entity;
    }

    public static Pointer create(QueryVariableContext queryVC, String variableName) {
        Entity entity = queryVC.getEntityByVariableName(variableName);
        return entity == null ? NoPointer.instance() : new EntityPointer(entity);
    }

    @Override
    public Pointer next(DomainModel model, String field) {
        Attribute attribute = entity.getAttributeByName(field);
        if (attribute == null) {
            return NoPointer.instance();
        }
        if (!attribute.isEntityReferenceAttribute()) {
            return new SimpleAttributePointer(entity, attribute);
        }

        String targetEntityName = attribute.getReferencedEntityName();
        try {
            Entity targetEntity = model.getEntityByName(targetEntityName);
            return attribute.isCollection() ?
                    new CollectionPointer(targetEntity) :
                    new EntityPointer(targetEntity);

        } catch (UnknownEntityNameException e) {
            return NoPointer.instance();
        }
    }

    public Entity getEntity() {
        return entity;
    }
}