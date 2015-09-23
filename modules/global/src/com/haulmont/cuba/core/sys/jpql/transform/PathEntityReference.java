/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chevelev
 * @version $Id$
 */
public class PathEntityReference implements EntityReference {
    private PathNode pathNode;
    private String pathStartingEntityName;

    public PathEntityReference(PathNode pathNode, String pathStartingEntityName) {
        this.pathStartingEntityName = pathStartingEntityName;
        this.pathNode = pathNode.dupNode();
    }

    @Override
    public String replaceEntries(String queryPart, String replaceablePart) {
        return queryPart.replaceAll("\\{E\\}", pathNode.asPathString());
    }

    @Override
    public void renameVariableIn(PathNode node) {
        node.renameVariableTo(pathNode.getEntityVariableName());
        List newChildren = new ArrayList(pathNode.dupNode().getChildren());
        while (node.getChildCount() > 0) {
            newChildren.add(node.deleteChild(0));
        }
        node.addChildren(newChildren);
    }

    @Override
    public Tree createNode() {
        return pathNode.dupNode();
    }

    @Override
    public boolean isJoinableTo(IdentificationVariableNode node) {
        return pathStartingEntityName.equals(node.getEntityName()) &&
                pathNode.getEntityVariableName().equals(node.getVariableName());
    }

    @Override
    public PathEntityReference addFieldPath(String fieldPath) {
        PathNode newPathNode = pathNode.dupNode();
        newPathNode.addDefaultChildren(fieldPath);
        return new PathEntityReference(newPathNode, pathStartingEntityName);
    }

    public PathNode getPathNode() {
        return pathNode.dupNode();
    }
}