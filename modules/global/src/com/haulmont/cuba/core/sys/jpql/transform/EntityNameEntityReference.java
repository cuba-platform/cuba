/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.Tree;

/**
 * @author chevelev
 * @version $Id$
 */
public class EntityNameEntityReference implements EntityReference {
    private String entityName;

    public EntityNameEntityReference(String entityName) {
        this.entityName = entityName;
    }

    @Override
    public String replaceEntries(String queryPart, String replaceablePart) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renameVariableIn(PathNode node) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Tree createNode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isJoinableTo(IdentificationVariableNode node) {
        return entityName.equals(node.getEntityName());
    }

    @Override
    public PathEntityReference addFieldPath(String fieldPath) {
        throw new UnsupportedOperationException();
    }
}