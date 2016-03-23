/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.core.sys.jpql.transform;

import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.tree.Tree;

import java.util.ArrayList;
import java.util.List;

/**
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
        return pathStartingEntityName.equals(node.getEffectiveEntityName()) &&
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

    public String getPathStartingEntityName() {
        return pathStartingEntityName;
    }
}