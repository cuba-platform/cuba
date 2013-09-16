/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;

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

    public static EntityPath parseEntityPath(String lastWord) {
        String[] parts = lastWord.split("\\.");
        EntityPath result = new EntityPath();
        result.topEntityVariableName = parts[0];
        int consumedPartsCount = 1;
        if (lastWord.endsWith(".") || parts.length == 1) {
            result.lastEntityFieldPattern = "";
        } else {
            result.lastEntityFieldPattern = parts[parts.length - 1];
            consumedPartsCount = 2;
        }
        if (parts.length == 1) {
            result.traversedFields = new String[0];
        } else {
            result.traversedFields = new String[parts.length - consumedPartsCount];
            System.arraycopy(parts, 1, result.traversedFields, 0, parts.length - consumedPartsCount);
        }
        return result;
    }
}
