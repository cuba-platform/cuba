/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 * @author chevelev
 * @version $Id$
 */
public class VariableEntityReference implements EntityReference {
    private String entityName;
    private String entityVariableName;

    public VariableEntityReference(String entityName, String entityVariableNameInQuery) {
        this.entityName = entityName;
        this.entityVariableName = entityVariableNameInQuery;
    }

    @Override
    public String replaceEntries(String queryPart, String replaceablePart) {
        return queryPart.replaceAll("\\{E\\}", entityVariableName);
    }

    @Override
    public void renameVariableIn(PathNode node) {
        node.renameVariableTo(entityVariableName);
    }

    @Override
    public Tree createNode() {
        return new CommonTree(new CommonToken(JPA2Lexer.WORD, entityVariableName));
    }

    @Override
    public boolean isJoinableTo(IdentificationVariableNode node) {
        return entityName.equals(node.getEntityName());
    }


    @Override
    public PathEntityReference addFieldPath(String fieldPath) {
        PathNode pathNode = new PathNode(JPA2Lexer.T_SELECTED_FIELD, entityVariableName);
        pathNode.addDefaultChildren(fieldPath);
        return new PathEntityReference(pathNode, entityName);
    }
}