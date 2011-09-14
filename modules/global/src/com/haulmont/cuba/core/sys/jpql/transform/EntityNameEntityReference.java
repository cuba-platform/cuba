/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.Tree;

/**
 * Author: Alexander Chevelev
 * Date: 13.04.2011
 * Time: 17:59:30
 */
public class EntityNameEntityReference implements EntityReference {
    private String entityName;

    public EntityNameEntityReference(String entityName) {
        this.entityName = entityName;
    }

    public String replaceEntries(String queryPart, String replaceablePart) {
        throw new UnsupportedOperationException();
    }

    public void renameVariableIn(PathNode node) {
        throw new UnsupportedOperationException();
    }

    public Tree createNode() {
        throw new UnsupportedOperationException();
    }

    public boolean isJoinableTo(IdentificationVariableNode node) {
        return entityName.equals(node.getEntityName());
    }

    @Override
    public PathEntityReference addFieldPath(String fieldPath) {
        throw new UnsupportedOperationException();
    }
}
