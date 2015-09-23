/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.model.Entity;

/**
 * @author chevelev
 * @version $Id$
 */
public class CollectionPointer implements com.haulmont.cuba.core.sys.jpql.pointer.Pointer {
    private Entity entity;

    public CollectionPointer(Entity entity) {
        if (entity == null)
            throw new NullPointerException("No entity passed");

        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public Pointer next(DomainModel model, String field) {
        return NoPointer.instance();
    }
}