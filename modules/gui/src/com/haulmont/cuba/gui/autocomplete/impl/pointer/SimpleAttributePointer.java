package com.haulmont.cuba.jpql.impl.pointer;

import com.haulmont.cuba.jpql.impl.DomainModel;
import com.haulmont.cuba.jpql.impl.model.Attribute;
import com.haulmont.cuba.jpql.impl.model.Entity;

/**
 * Author: Alexander Chevelev
 * Date: 21.10.2010
 * Time: 1:45:27
 */
public class SimpleAttributePointer implements Pointer {
    private Entity entity;
    private Attribute attribute;

    SimpleAttributePointer(Entity entity, Attribute attribute) {
        this.entity = entity;
        this.attribute = attribute;
    }

    public Pointer next(DomainModel model, String field) {
        return NoPointer.instance();
    }

    public Attribute getAttribute() {
        return attribute;
    }
}
