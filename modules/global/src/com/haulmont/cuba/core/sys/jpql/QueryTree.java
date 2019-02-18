/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.core.sys.jpql;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;
import org.antlr.runtime.tree.TreeVisitorAction;
import org.apache.commons.lang3.StringUtils;


import java.util.List;
import java.util.stream.Stream;

public class QueryTree {
    protected String queryString;
    protected DomainModel model;

    protected IdVarSelector idVarSelector;
    protected CommonTree tree;

    public QueryTree(DomainModel model, String query) {
        this(model, query, true);
    }

    public QueryTree(DomainModel model, String query, boolean failOnErrors) {
        Preconditions.checkNotNull(query, "query is null");
        String modifiedQuery = StringUtils.replaceChars(query, "\n\r\t", "   ");

        this.model = model;
        this.queryString = modifiedQuery;
        try {
            this.tree = Parser.parse(modifiedQuery, failOnErrors);
        } catch (RecognitionException e) {
            throw new JPA2RecognitionException("JPA grammar recognition error", e);
        }

        this.idVarSelector = new IdVarSelector(model);
        new TreeVisitor().visit(tree, idVarSelector);
    }

    public DomainModel getModel() {
        return model;
    }

    public String getQueryString() {
        return queryString;
    }

    public QueryVariableContext getQueryVariableContext() {
        return idVarSelector.getContextTree();
    }

    public List<ErrorRec> getInvalidIdVarNodes() {
        return idVarSelector.getInvalidIdVarNodes();
    }

    public CommonTree getAstTree() {
        return tree;
    }

    public String getVariableNameByEntity(String entityType) {
        return getQueryVariableContext().getVariableNameByEntity(entityType);
    }

    /**
     * @return returns tree for FROM statement
     */
    public CommonTree getAstFromNode() {
        return (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
    }

    /**
     * @return returns list of identification variable nodes (entityName entityAlias) from FROM statement
     */
    public Stream<IdentificationVariableNode> getAstIdentificationVariableNodes() {
        return generateChildrenByClass(getAstFromNode(), SelectionSourceNode.class)
                .flatMap(selectionSource -> generateChildrenByClass(selectionSource, IdentificationVariableNode.class));
    }

    /**
     * @return returns list of join variable nodes (JOIN entityName entityAlias ON clause) from FROM statement
     */
    public Stream<JoinVariableNode> getAstJoinVariableNodes() {
        return generateChildrenByClass(getAstFromNode(), SelectionSourceNode.class)
                .flatMap(selectionSource -> generateChildrenByClass(selectionSource, JoinVariableNode.class));
    }

    /**
     * @return returns tree for SELECT statement
     */
    public SelectedItemsNode getAstSelectedItemsNode() {
        return (SelectedItemsNode) tree.getFirstChildWithType(JPA2Lexer.T_SELECTED_ITEMS);
    }

    public Stream<PathNode> getAstSelectedPathNodes() {
        SelectedItemsNode selectedItems = getAstSelectedItemsNode();
        if (selectedItems != null) {
            return generateChildrenByClass(selectedItems, SelectedItemNode.class)
                    .flatMap(selectedItemNode -> generateChildrenByClass(selectedItemNode, PathNode.class));
        }
        return Stream.empty();
    }

    /**
     * @return returns tree for WHERE statement
     */
    public WhereNode getAstWhereNode() {
        return (WhereNode) tree.getFirstChildWithType(JPA2Lexer.T_CONDITION);
    }

    /**
     * @return returns tree for GROUP BY statement
     */
    public Tree getAstGroupByNode() {
        return tree.getFirstChildWithType(JPA2Lexer.T_GROUP_BY);
    }

    /**
     * @return returns tree for ORDER BY statement
     */
    public Tree getAstOrderByNode() {
        return tree.getFirstChildWithType(JPA2Lexer.T_ORDER_BY);
    }


    public <T extends TreeVisitorAction> T visit(T visitor) {
        new TreeVisitor().visit(tree, visitor);
        return visitor;
    }

    @SuppressWarnings("unchecked")
    protected <T> Stream<T> generateChildrenByClass(CommonTree commonTree, Class<T> clazz) {
        return commonTree.getChildren().stream()
                .filter(o -> clazz.isAssignableFrom(o.getClass()))
                .map(o -> (T) o);
    }
}
