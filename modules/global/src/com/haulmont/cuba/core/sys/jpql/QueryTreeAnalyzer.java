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

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import com.haulmont.cuba.core.sys.jpql.transform.NodesFinder;
import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Alexander Chevelev
 * Date: 06.04.2011
 * Time: 18:31:25
 */
public class QueryTreeAnalyzer {
    protected DomainModel model;
    protected IdVarSelector idVarSelector;
    protected CommonTree tree;

    public void prepare(DomainModel model, String query) throws RecognitionException {
        prepare(model, query, true);
    }

    public void prepare(DomainModel model, String query, boolean failOnErrors) throws RecognitionException {
        this.model = model;

        query = query.replace("\n", " ");
        query = query.replace("\r", " ");
        query = query.replace("\t", " ");
        tree = Parser.parse(query, failOnErrors);
        TreeVisitor visitor = new TreeVisitor();
        idVarSelector = new IdVarSelector(model);
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

    public Entity getSelectedEntity(PathNode path) {
        Pointer pointer = path.walk(model, getRootQueryVariableContext());
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

        return getChildrenByClass(selectedItems, SelectedItemNode.class).stream()
                .flatMap(selectedItemNode -> getChildrenByClass(selectedItemNode, PathNode.class).stream())
                .collect(Collectors.toList());
    }

    public List<IdentificationVariableNode> getIdentificationVariableNodes() {
        CommonTree sourceNode = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
        List<IdentificationVariableNode> identificationVariableNodes = new ArrayList<>();

        List<SelectionSourceNode> selectionSources = getChildrenByClass(sourceNode, SelectionSourceNode.class);
        for (SelectionSourceNode selectionSource : selectionSources) {
            identificationVariableNodes.addAll(getChildrenByClass(selectionSource, IdentificationVariableNode.class));
        }

        return identificationVariableNodes;
    }

    public List<SimpleConditionNode> findAllConditionsForMainEntityAttribute(String attribute) {
        IdentificationVariableNode mainEntityIdentification = getMainEntityIdentification();
        if (mainEntityIdentification != null) {
            return findAllConditions().stream().filter(condition -> {
                List<PathNode> childrenByClass = getChildrenByClass(condition, PathNode.class);
                return childrenByClass.stream().anyMatch(pathNode -> {
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


    public List<SimpleConditionNode> findAllConditions() {
        NodesFinder<SimpleConditionNode> nodesFinder = new NodesFinder<>(SimpleConditionNode.class);
        TreeVisitor treeVisitor = new TreeVisitor();
        treeVisitor.visit(tree, nodesFinder);
        return nodesFinder.getFoundNodes();
    }


    protected <T> List<T> getChildrenByClass(CommonTree commonTree, Class<T> clazz) {
        List<Object> childrenByClass = new ArrayList<>();
        for (Object o : commonTree.getChildren()) {
            if (clazz.isAssignableFrom(o.getClass())) {
                childrenByClass.add(o);
            }
        }

        return (List<T>) childrenByClass;
    }
}
