/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.model.VirtualEntity;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;

/**
 * Author: Alexander Chevelev
 * Date: 06.04.2011
 * Time: 16:32:06
 */
public class EntityReferenceInferer {
    private String entityName;

    public EntityReferenceInferer(String entityName) {
        if (entityName == null)
            throw new NullPointerException("No entity name passed");

        this.entityName = entityName;
    }

    public EntityReference infer(QueryTreeTransformer queryAnalyzer) {
        String entityVariableNameInQuery = queryAnalyzer.getRootEntityVariableName(entityName);
        if (entityVariableNameInQuery != null) {
            return new VariableEntityReference(entityName, entityVariableNameInQuery);
        }
        PathNode path = queryAnalyzer.getSelectedPathNode();
        Entity entity = queryAnalyzer.getSelectedEntity(path);
        if (!(entity instanceof VirtualEntity) && entity.getName().equals(entityName)) {
            Entity pathStartingEntity = queryAnalyzer.getRootQueryVariableContext().
                    getEntityByVariableName(path.getEntityVariableName());
            return new PathEntityReference(path, pathStartingEntity.getName());
        }
        throw new RuntimeException("No variable or selected field of entity " + entityName + " found in query");

    }

    public String getEntityName() {
        return entityName;
    }
}
