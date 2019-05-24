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

import com.haulmont.cuba.core.sys.jpql.NodesFinder;
import com.haulmont.cuba.core.sys.jpql.QueryTree;
import com.haulmont.cuba.core.sys.jpql.UnknownEntityNameException;
import com.haulmont.cuba.core.sys.jpql.EntityVariable;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;

import java.util.*;

import static com.haulmont.cuba.core.sys.jpql.JPATreeNodes.*;

public class QueryTreeTransformer {
    protected QueryTree queryTree;

    public QueryTreeTransformer(QueryTree queryTree) {
        this.queryTree = queryTree;
    }

    public void mixinWhereConditionsIntoTree(CommonTree targetWhere) {
        CommonTree sourceWhere = queryTree.getAstWhereNode();
        if (sourceWhere == null) {
            CommonTree tree = queryTree.getAstTree();
            CommonTree fromNode = queryTree.getAstFromNode();

            List<Tree> endingNodes = new ArrayList<>();
            int deletionCount = tree.getChildCount() - fromNode.getChildIndex() - 1;
            for (int i = 0; i < deletionCount; i++) {
                endingNodes.add((Tree) tree.deleteChild(fromNode.getChildIndex() + 1));
            }

            tree.addChild(targetWhere);

            for (Tree endingNode : endingNodes) {
                tree.addChild(endingNode);
            }

            tree.freshenParentAndChildIndexes();
        } else {
            @SuppressWarnings("unchecked")
            List<Tree> existingChildren = (List<Tree>) sourceWhere.getChildren();

            CommonTree existingChildrenWithBraces = new CommonTree();
            existingChildrenWithBraces.addChild(createLPAREN());
            existingChildrenWithBraces.addChildren(existingChildren);
            existingChildrenWithBraces.addChild(createRPAREN());
            sourceWhere.replaceChildren(0, sourceWhere.getChildCount() - 1, existingChildrenWithBraces);

            sourceWhere.addChild(createAnd());
            sourceWhere.addChild(createLPAREN());
            for (Object o : targetWhere.getChildren()) {
                CommonTree t = (CommonTree) o;
                sourceWhere.addChild(t.dupNode());
            }
            sourceWhere.addChild(createRPAREN());

            sourceWhere.freshenParentAndChildIndexes();
        }
    }

    public void mixinJoinIntoTree(CommonTree joinClause, EntityVariable entityReference, boolean renameVariable) {
        CommonTree from = queryTree.getAstFromNode();
        for (int i = 0; i < from.getChildCount(); i++) {
            SelectionSourceNode selectionSource = (SelectionSourceNode) from.getChild(i);
            if (selectionSource.getChild(0) instanceof IdentificationVariableNode) {
                IdentificationVariableNode identificationVariable = (IdentificationVariableNode) selectionSource.getChild(0);
                if (entityReference.supportsJoinTo(identificationVariable)) {
                    String variableName = identificationVariable.getVariableName();
                    if (!(joinClause instanceof JoinVariableNode)) {
                        throw new RuntimeException("Passed joinClause is not JoinVariableNode: " + joinClause.getClass());
                    }

                    JoinVariableNode joinNode = (JoinVariableNode) joinClause;

                    if (hasJoinNode(joinNode, selectionSource)) {
                        return;
                    }

                    if (renameVariable) {
                        PathNode path = joinNode.findPathNode();
                        if (path != null) {
                            path.renameVariableTo(variableName);
                        }
                    }

                    selectionSource.addChild(joinClause);
                    from.freshenParentAndChildIndexes();
                    return;
                }
            }
        }
        throw new RuntimeException("Join mixing failed. Cannot find selected entity with name " + entityReference);
    }

    public void addSelectionSource(CommonTree selectionSource) {
        CommonTree from = queryTree.getAstFromNode();
        from.addChild(selectionSource);
        from.freshenParentAndChildIndexes();
    }

    public void addFirstSelectionSource(CommonTree selectionSource) {
        CommonTree from = queryTree.getAstFromNode();
        from.insertChild(0, selectionSource);
        from.freshenParentAndChildIndexes();
    }

    public void replaceWithCount(String entityName) {
        Tree selectedItems = queryTree.getAstSelectedItemsNode();
        boolean isDistinct = "distinct".equalsIgnoreCase(selectedItems.getChild(0).getText());
        if (!(isDistinct && selectedItems.getChildCount() == 2 ||
                selectedItems.getChildCount() == 1))
            throw new IllegalStateException("Cannot replace with count if multiple fields selected");

        SelectedItemNode selectedItemNode;
        if (isDistinct)
            selectedItems.deleteChild(0);

        selectedItemNode = (SelectedItemNode) selectedItems.getChild(0);
        AggregateExpressionNode countNode = createAggregateCount(createWord(entityName), isDistinct);
        selectedItemNode.deleteChild(0);
        selectedItemNode.addChild(countNode);

        Tree orderBy = queryTree.getAstTree().getFirstChildWithType(JPA2Lexer.T_ORDER_BY);
        if (orderBy != null) {
            queryTree.getAstTree().deleteChild(orderBy.getChildIndex());
        }
        queryTree.getAstTree().freshenParentAndChildIndexes();
    }

    public void removeOrderBy() {
        Tree orderBy = queryTree.getAstOrderByNode();
        if (orderBy != null) {
            queryTree.getAstTree().deleteChild(orderBy.getChildIndex());
        }
        queryTree.getAstTree().freshenParentAndChildIndexes();
    }

    public boolean removeDistinct() {
        Tree selectedItems = queryTree.getAstSelectedItemsNode();
        boolean isDistinct = "distinct".equalsIgnoreCase(selectedItems.getChild(0).getText());
        if (isDistinct) {
            selectedItems.deleteChild(0);
            selectedItems.freshenParentAndChildIndexes();
        } else {
            return false;
        }
        return true;
    }

    public void addDistinct() {
        CommonTree selectedItems = queryTree.getAstSelectedItemsNode();
        boolean isDistinct = "distinct".equalsIgnoreCase(selectedItems.getChild(0).getText());
        if (!isDistinct) {
            selectedItems.insertChild(0, createDistinct());
            selectedItems.freshenParentAndChildIndexes();
        }
    }

    public void replaceEntityName(String newEntityName, IdentificationVariableNode identificationVariable) {
        if (identificationVariable != null) {
            identificationVariable.deleteChild(0);
            identificationVariable.addChild(createWord(newEntityName));
            identificationVariable.freshenParentAndChildIndexes();
        }
    }

    public void replaceOrderByItems(String mainEntityName, List<OrderByFieldNode> orderByItems, boolean directionDesc) {
        removeOrderBy();

        OrderByNode orderBy = createOrderBy();
        queryTree.getAstTree().addChild(orderBy);

        Set<String> joinVariables = new HashSet<>();
        for (OrderByFieldNode orderByItem : orderByItems) {

            NodesFinder<PathNode> finder = NodesFinder.of(PathNode.class);
            new TreeVisitor().visit(orderByItem, finder);

            for (PathNode node : finder.getFoundNodes()) {
                if (node.getChildCount() > 1) {
                    int nodeIdx = node.getChildIndex();
                    List<PathNode> transitPaths = extractTransitPaths(node, mainEntityName);

                    //replace path expression in the order item to the last path expression from transit paths
                    PathNode lastNode = transitPaths.remove(transitPaths.size() - 1);
                    node.getParent().replaceChildren(nodeIdx, nodeIdx, lastNode);

                    //add left join for
                    for (PathNode joinPathNode : transitPaths) {
                        String joinVariable = joinPathNode.asPathString('_');
                        if (!joinVariables.contains(joinVariable)) {
                            JoinVariableNode join = createLeftJoinByPath(joinVariable, joinPathNode);

                            queryTree.getAstFromNode().getChild(0).addChild(join);
                            joinVariables.add(joinVariable);
                        }
                    }
                }
            }

            boolean anyDirection = orderByItem.getChildren().stream()
                    .map(item -> (Tree) item)
                    .anyMatch(item -> item.getType() == JPA2Lexer.ASC || item.getType() == JPA2Lexer.DESC);

            if (!anyDirection && directionDesc) {
                orderByItem.addChild(createDesc());
            }

            orderBy.addChild(orderByItem);
        }
    }

    public void orderById(String entityVariable, String pkName) {
        if (queryTree.getAstOrderByNode() == null) {
            Tree orderBy = createOrderBy();
            queryTree.getAstTree().addChild(orderBy);
            queryTree.getAstTree().freshenParentAndChildIndexes();

            OrderByFieldNode orderByField = new OrderByFieldNode(JPA2Lexer.T_ORDER_BY_FIELD);
            orderByField.addChild(createPathNode(entityVariable, pkName));
            orderByField.addChild(createDesc());
            orderByField.freshenParentAndChildIndexes();

            orderBy.addChild(orderByField);
            orderBy.freshenParentAndChildIndexes();
        }
    }


    public void addEntityInGroupBy(String entityVariable) {
        Tree groupBy = queryTree.getAstGroupByNode();
        if (groupBy != null) {
            groupBy.addChild(createPathNode(entityVariable));
            groupBy.freshenParentAndChildIndexes();
        }
    }

    public void applyLowerCaseForConditions(List<SimpleConditionNode> conditions) {
        for (SimpleConditionNode condition : conditions) {
            List<PathNode> pathNodes = new ArrayList<>();
            for (int idx = 0; idx < condition.getChildCount(); idx++) {
                Tree child = condition.getChild(idx);
                if (child.getType() == JPA2Lexer.T_SELECTED_FIELD) {
                    pathNodes.add((PathNode) child);
                }
            }
            for (PathNode pathNode : pathNodes) {
                for (int idx = 0; idx < condition.getChildCount(); idx++) {
                    Tree child = condition.getChild(idx);
                    if (child == pathNode) {
                        CommonTree loweredPathNode = new CommonTree();
                        loweredPathNode.addChild(createLower());
                        loweredPathNode.addChild(createLPAREN());
                        loweredPathNode.addChild(pathNode);
                        loweredPathNode.addChild(createRPAREN());
                        condition.replaceChildren(idx, idx, loweredPathNode);
                        break;
                    }
                }
            }
            condition.freshenParentAndChildIndexes();
        }
    }

    public void clearInConditions(List<SimpleConditionNode> conditions) {
        for (SimpleConditionNode condition : conditions) {
            Tree inToken = condition.getFirstChildWithType(JPA2Lexer.IN);
            if (inToken != null) {
                Tree notToken = condition.getFirstChildWithType(JPA2Lexer.NOT);
                condition.getChildren().clear();
                condition.addChild(createWord(notToken == null ? "1=0" : "1=1"));
                condition.freshenParentAndChildIndexes();
            }
        }
    }

    public void replaceIsNullStatements(List<SimpleConditionNode> conditions, boolean isNullValue) {
        for (SimpleConditionNode condition : conditions) {
            Tree notToken = condition.getFirstChildWithType(JPA2Lexer.NOT);
            condition.getChildren().clear();
            condition.addChild(createWord((isNullValue && notToken == null)
                    || (!isNullValue && notToken != null) ? "1=1" : "1=0"));
            condition.freshenParentAndChildIndexes();
        }
    }

    public void replaceSelectedEntityVariable(String newVariableName, PathNode pathNode) {
        if (pathNode != null) {
            pathNode.renameVariableTo(newVariableName);
            //remove path elements
            while (pathNode.getChildCount() > 0) {
                pathNode.deleteChild(0);
            }
        }
    }

    protected boolean hasJoinNode(JoinVariableNode joinNode, SelectionSourceNode selectionSource) {
        JoinVariableNode existingJoinNode = selectionSource.getChildren().stream()
                .filter(e -> e instanceof JoinVariableNode)
                .map(e -> (JoinVariableNode) e)
                .filter(e -> Objects.equals(e.getVariableName(), joinNode.getVariableName()))
                .findFirst().orElse(null);

        if (existingJoinNode != null) {
            PathNode existingPathNode = existingJoinNode.findPathNode();
            PathNode pathNode = joinNode.findPathNode();
            if (existingPathNode != null && pathNode != null) {
                return Objects.equals(existingPathNode.asPathString(), pathNode.asPathString());
            }
        }

        return false;
    }

    protected List<PathNode> extractTransitPaths(PathNode pathNode, String entityName) {
        List<PathNode> nodes = new ArrayList<>();
        try {
            JpqlEntityModel entity = queryTree.getModel().getEntityByName(entityName);
            PathNode currentPathNode = new PathNode(pathNode.getToken(), pathNode.getEntityVariableName());

            for (Object child : pathNode.getChildren()) {
                Attribute entityAttribute = entity.getAttributeByName(child.toString());
                if (entityAttribute.isEntityReferenceAttribute()) {

                    if (!entityAttribute.isEmbedded()) {
                        nodes.add(currentPathNode);
                        currentPathNode.addDefaultChild(child.toString());
                        currentPathNode = new PathNode(pathNode.getToken(), currentPathNode.asPathString('_'));
                    } else {
                        currentPathNode.addDefaultChild(child.toString());
                    }

                    entityName = entityAttribute.getReferencedEntityName();
                    entity = queryTree.getModel().getEntityByName(entityName);

                } else {
                    currentPathNode.addDefaultChild(child.toString());
                }
            }
            nodes.add(currentPathNode);
        } catch (UnknownEntityNameException e) {
            throw new RuntimeException(String.format("Could not find entity by name %s", entityName), e);
        }
        return nodes;
    }
}