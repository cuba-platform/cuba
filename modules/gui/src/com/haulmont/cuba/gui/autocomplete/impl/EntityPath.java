package com.haulmont.cuba.jpql.impl;

import com.haulmont.cuba.jpql.impl.pointer.EntityPointer;
import com.haulmont.cuba.jpql.impl.pointer.Pointer;

/**
 * User: Alex Chevelev
 * Date: 13.10.2010
 * Time: 23:27:26
 */
public class EntityPath {
    public String topEntityVariableName;
    public String[] traversedFields;
    public String lastEntityFieldPattern;

    public Pointer walk(DomainModel model, QueryVariableContext queryVC) {
        Pointer pointer = EntityPointer.create(queryVC, topEntityVariableName);
        for (String traversedField : traversedFields) {
            pointer = pointer.next(model, traversedField);
        }
        return pointer;
    }
}
