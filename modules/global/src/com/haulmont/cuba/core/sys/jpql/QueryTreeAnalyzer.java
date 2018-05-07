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

package com.haulmont.cuba.core.sys.jpql;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import com.haulmont.cuba.core.sys.jpql.transform.NodesFinder;
import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryTreeAnalyzer {
    protected DomainModel model;
    protected IdVarSelector idVarSelector;
    protected CommonTree tree;

    public void prepare(DomainModel model, String query) throws RecognitionException {
        prepare(model, query, true);
    }

    public void prepare(DomainModel model, String query, boolean failOnErrors) throws RecognitionException {
        Preconditions.checkNotNull(query, "query is null");
        String modifiedQuery = StringUtils.replaceChars(query, "\n\r\t", "   ");

        this.model = model;
        this.tree = Parser.parse(modifiedQuery, failOnErrors);
        TreeVisitor visitor = new TreeVisitor();
        this.idVarSelector = new IdVarSelector(model);
        visitor.visit(tree, idVarSelector);
    }

    public QueryVariableContext getRootQueryVariableContext() {
        return idVarSelector.getContextTree();
    }

    public List<ErrorRec> getInvalidIdVarNodes() {
        return idVarSelector.getInvalidIdVarNodes();
    }

    public CommonTree getTree() {
        return tree;
    }

    public String getRootEntityVariableName(String entityName) {
        QueryVariableContext ctx = getRootQueryVariableContext();
        return ctx.getVariableNameByEntity(entityName);
    }

    public PathNode getSelectedPathNode() {
        Tree selectedItems = tree.getFirstChildWithType(JPA2Lexer.T_SELECTED_ITEMS);
        boolean isDistinct = "DISTINCT".equalsIgnoreCase(selectedItems.getChild(0).getText());
        SelectedItemNode selectedItemNode;
        if (isDistinct) {
            if (selectedItems.getChildCount() != 2)
                throw new IllegalStateException("Cannot select path node if multiple fields selected");
            selectedItemNode = (SelectedItemNode) selectedItems.getChild(1);
        } else {
            if (selectedItems.getChildCount() != 1)
                throw new IllegalStateException("Cannot select path node if multiple fields selected");
            selectedItemNode = (SelectedItemNode) selectedItems.getChild(0);
        }

        if (!(selectedItemNode.getChild(0) instanceof PathNode)) {
            throw new IllegalStateException("An entity path is assumed to be selected");
        }
        return (PathNode) selectedItemNode.getChild(0);
    }

    public JpqlEntityModel getSelectedEntity(PathNode path) {
        Pointer pointer = path.resolvePointer(model, getRootQueryVariableContext());
        if (!(pointer instanceof EntityPointer)) {
            throw new IllegalStateException("A path resulting in an entity is assumed to be selected");
        }
        return ((EntityPointer) pointer).getEntity();
    }

    @Nullable
    public IdentificationVariableNode getMainEntityIdentification() {
        List<IdentificationVariableNode> identificationVariables = getIdentificationVariableNodes();

        String returnedVariableName = getFirstReturnedVariableName();
        if (returnedVariableName != null) {
            for (IdentificationVariableNode identificationVariable : identificationVariables) {
                if (identificationVariable.getVariableName().equalsIgnoreCase(returnedVariableName)) {
                    return identificationVariable;
                }
            }
        }

        return identificationVariables.size() > 0 ? identificationVariables.get(0) : null;
    }

    @Nullable
    public String getFirstReturnedVariableName() {
        PathNode returnedPathNode = getFirstReturnedPathNode();
        if (returnedPathNode != null) {
            return returnedPathNode.getEntityVariableName();
        }

        return null;
    }

    @Nullable
    public PathNode getFirstReturnedPathNode() {
        List<PathNode> pathNodes = getReturnedPathNodes();
        if (CollectionUtils.isNotEmpty(pathNodes)) {
            PathNode pathNode = pathNodes.get(0);
            return pathNode;
        }

        return null;
    }

    @Nullable
    public List<PathNode> getReturnedPathNodes() {
        CommonTree selectedItems = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SELECTED_ITEMS);
        if (selectedItems == null) {
            return null;
        }

        return generateChildrenByClass(selectedItems, SelectedItemNode.class)
                .flatMap(selectedItemNode -> generateChildrenByClass(selectedItemNode, PathNode.class))
                .collect(Collectors.toList());
    }

    public List<IdentificationVariableNode> getIdentificationVariableNodes() {
        CommonTree sourceNode = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);

        return generateChildrenByClass(sourceNode, SelectionSourceNode.class)
                .flatMap(selectionSource -> generateChildrenByClass(selectionSource, IdentificationVariableNode.class))
                .collect(Collectors.toList());
    }

    public List<SimpleConditionNode> findAllConditionsForMainEntityAttribute(String attribute) {
        IdentificationVariableNode mainEntityIdentification = getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            return findAllConditions().stream().filter(condition -> {
                Stream<PathNode> childrenByClass = generateChildrenByClass(condition, PathNode.class);
                return childrenByClass.anyMatch(pathNode -> {
                            String pathNodeAttribute = StringUtils.join(pathNode.getChildren(), ".");
                            return pathNode.getEntityVariableName().equals(mainEntityIdentification.getVariableName())
                                    && attribute.equals(pathNodeAttribute);
                        }
                );
            }).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<SimpleConditionNode> findConditionsForParameter(String paramName) {
        CommonTree whereTree = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_CONDITION);
        Stream<SimpleConditionNode> conditionNodes = generateChildrenByClass(whereTree, SimpleConditionNode.class);
        return conditionNodes
                .filter((SimpleConditionNode n) -> {
                    ParameterNode parameter = (ParameterNode) n.getFirstChildWithType(JPA2Lexer.T_PARAMETER);
                    return parameter != null && (parameter.getChild(0).getText().contains(paramName) ||
                            parameter.getChildCount() > 1 && paramName.equals(parameter.getChild(1).getText()));
                }).collect(Collectors.toList());
    }

    public List<SimpleConditionNode> findAllConditions() {
        NodesFinder<SimpleConditionNode> nodesFinder = new NodesFinder<>(SimpleConditionNode.class);
        TreeVisitor treeVisitor = new TreeVisitor();
        treeVisitor.visit(tree, nodesFinder);
        return nodesFinder.getFoundNodes();
    }

    public boolean hasJoins() {
        CommonTree sourceNode = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
        List<SelectionSourceNode> selectionSourceNodes = getChildrenByClass(sourceNode, SelectionSourceNode.class);
        if (selectionSourceNodes.size() > 1) {
            return true;
        } else if (selectionSourceNodes.size() == 1) {
            NodesFinder<JoinVariableNode> nodesFinder = new NodesFinder<>(JoinVariableNode.class);
            TreeVisitor treeVisitor = new TreeVisitor();
            treeVisitor.visit(tree, nodesFinder);
            return !nodesFinder.getFoundNodes().isEmpty();
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> Stream<T> generateChildrenByClass(CommonTree commonTree, Class<T> clazz) {
        return commonTree.getChildren().stream()
                .filter(o -> clazz.isAssignableFrom(o.getClass()))
                .map(o -> (T)o);
    }

    protected <T> List<T> getChildrenByClass(CommonTree commonTree, Class<T> clazz) {
        return generateChildrenByClass(commonTree, clazz)
                .collect(Collectors.toList());
    }
}