/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.pointer;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.Entity;

/**
 * @author chevelev
 * @version $Id$
 */
public class SimpleAttributePointer implements Pointer {
    private Entity entity;
    private Attribute attribute;

    SimpleAttributePointer(Entity entity, Attribute attribute) {
        this.entity = entity;
        this.attribute = attribute;
    }

    @Override
    public Pointer next(DomainModel model, String field) {
        return NoPointer.instance();
    }

    public Attribute getAttribute() {
        return attribute;
    }
}