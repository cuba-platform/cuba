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

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
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