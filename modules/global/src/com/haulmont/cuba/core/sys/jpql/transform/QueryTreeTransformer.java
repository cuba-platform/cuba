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

import com.haulmont.cuba.core.sys.jpql.QueryTreeAnalyzer;
import com.haulmont.cuba.core.sys.jpql.UnknownEntityNameException;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryTreeTransformer extends QueryTreeAnalyzer {

    public QueryTreeTransformer() {
    }

    public void mixinWhereConditionsIntoTree(CommonTree whereTreeToMixIn) {
        CommonTree whereTreeToMixWithin = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_CONDITION);
        if (whereTreeToMixWithin == null) {
            FromNode fromNode = (FromNode) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
            List<Tree> endingNodes = new ArrayList<>();
            int deletionCount = tree.getChildCount() - (fromNode.getChildIndex() + 1);
            for (int i = 0; i < deletionCount; i++) {
                endingNodes.add((Tree) tree.deleteChild(fromNode.getChildIndex() + 1));
            }
            tree.addChild(whereTreeToMixIn);
            for (Tree endingNode : endingNodes) {
                tree.addChild(endingNode);
            }
            tree.freshenParentAndChildIndexes();
        } else {
            List<Tree> existingChildren = (List<Tree>) whereTreeToMixWithin.getChildren();
            CommonTree existingChildrenWithBraces = new CommonTree();
            existingChildrenWithBraces.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
            existingChildrenWithBraces.addChildren(existingChildren);
            existingChildrenWithBraces.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
            whereTreeToMixWithin.replaceChildren(0, whereTreeToMixWithin.getChildCount() - 1, existingChildrenWithBraces);

            CommonTree and = new CommonTree(new CommonToken(JPA2Lexer.AND, "and"));
            whereTreeToMixWithin.addChild(and);
            whereTreeToMixWithin.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
            for (Object o : whereTreeToMixIn.getChildren()) {
                CommonTree t = (CommonTree) o;
                whereTreeToMixWithin.addChild(t.dupNode());
            }
            whereTreeToMixWithin.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
            whereTreeToMixWithin.freshenParentAndChildIndexes();
        }
    }

    public void mixinJoinIntoTree(CommonTree joinClause, EntityReference entityRef, boolean renameVariable) {
        CommonTree from = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
        for (int i = 0; i < from.getChildCount(); i++) {
            SelectionSourceNode sourceNode = (SelectionSourceNode) from.getChild(i);
            if (sourceNode.getChild(0) instanceof IdentificationVariableNode) {
                IdentificationVariableNode entityVariableNode = (IdentificationVariableNode) sourceNode.getChild(0);
                if (entityRef.isJoinableTo(entityVariableNode)) {
                    String variableName = entityVariableNode.getVariableName();
                    JoinVariableNode joinVariableNode = (JoinVariableNode) joinClause;
                    if (renameVariable) {
                        PathNode path = joinVariableNode.findPathNode();
                        if (path != null) {
                            path.renameVariableTo(variableName);
                        }
                    }
                    sourceNode.addChild(joinClause);
                    from.freshenParentAndChildIndexes();
                    return;
                }
            }
        }
        throw new RuntimeException("Join mixing failed. Cannot find selected entity with name " + entityRef);
    }

    public void addSelectionSource(CommonTree selectionSource) {
        CommonTree from = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
        from.addChild(selectionSource);
        from.freshenParentAndChildIndexes();
    }

    public void addFirstSelectionSource(CommonTree selectionSource) {
        CommonTree from = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
        from.insertChild(0, selectionSource);
        from.freshenParentAndChildIndexes();
    }

    public void replaceWithCount(EntityReference entityRef) {
        Tree selectedItems = tree.getFirstChildWithType(JPA2Lexer.T_SELECTED_ITEMS);
        boolean isDistinct = "DISTINCT".equalsIgnoreCase(selectedItems.getChild(0).getText());
        if (!(isDistinct && selectedItems.getChildCount() == 2 ||
                selectedItems.getChildCount() == 1))
            throw new IllegalStateException("Cannot replace with count if multiple fields selected");

        SelectedItemNode selectedItemNode;
        if (isDistinct)
            selectedItems.deleteChild(0);

        selectedItemNode = (SelectedItemNode) selectedItems.getChild(0);
        AggregateExpressionNode countNode = createCountNode(entityRef, isDistinct);
        selectedItemNode.deleteChild(0);
        selectedItemNode.addChild(countNode);

        Tree orderBy = tree.getFirstChildWithType(JPA2Lexer.T_ORDER_BY);
        if (orderBy != null) {
            tree.deleteChild(orderBy.getChildIndex());
        }
        tree.freshenParentAndChildIndexes();
    }

    public void removeOrderBy() {
        Tree orderBy = tree.getFirstChildWithType(JPA2Lexer.T_ORDER_BY);
        if (orderBy != null) {
            tree.deleteChild(orderBy.getChildIndex());
        }
        tree.freshenParentAndChildIndexes();
    }

    public boolean removeDistinct() {
        Tree selectedItems = tree.getFirstChildWithType(JPA2Lexer.T_SELECTED_ITEMS);
        boolean isDistinct = "DISTINCT".equalsIgnoreCase(selectedItems.getChild(0).getText());
        if (isDistinct) {
            selectedItems.deleteChild(0);
        } else {
            return false;
        }
        tree.freshenParentAndChildIndexes();
        return true;
    }

    public void replaceEntityName(String newEntityName) {
        IdentificationVariableNode identificationVariable = getMainEntityIdentification();
        if (identificationVariable != null) {
            identificationVariable.deleteChild(0);
            identificationVariable.addChild(new CommonTree(new CommonToken(JPA2Lexer.WORD, newEntityName)));
            tree.freshenParentAndChildIndexes();
        }
    }

    public void replaceOrderBy(boolean desc, PathEntityReference... orderingFieldRefs) {
        removeOrderBy();
        CommonTree orderBy = new OrderByNode(JPA2Lexer.T_ORDER_BY);
        tree.addChild(orderBy);
        orderBy.addChild(new CommonTree(new CommonToken(JPA2Lexer.ORDER, "order")));
        orderBy.addChild(new CommonTree(new CommonToken(JPA2Lexer.BY, "by")));

        Set<String> addedJoinVariables = new HashSet<>();
        for (PathEntityReference orderingFieldRef : orderingFieldRefs) {
            OrderByFieldNode orderByField = new OrderByFieldNode(JPA2Lexer.T_ORDER_BY_FIELD);
            orderBy.addChild(orderByField);

            PathNode pathNode = orderingFieldRef.getPathNode();
            if (pathNode.getChildCount() > 1 && needJoinForOrderBy(orderingFieldRef)) {
                //todo eude what if we need more than 1 join for complex path?
                CommonTree lastNode = (CommonTree) pathNode.deleteChild(pathNode.getChildCount() - 1);
                String variableName = pathNode.asPathString('_');

                PathNode orderingNode = new PathNode(JPA2Lexer.T_SELECTED_FIELD, variableName);
                orderingNode.addDefaultChild(lastNode.getText());
                orderByField.addChild(orderingNode);

                if (!addedJoinVariables.contains(variableName)) {
                    JoinVariableNode joinNode = new JoinVariableNode(JPA2Lexer.T_JOIN_VAR, "left join", variableName);
                    joinNode.addChild(pathNode);

                    CommonTree from = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_SOURCES);
                    from.getChild(0).addChild(joinNode); // assumption
                    addedJoinVariables.add(variableName);
                }
            } else {
                orderByField.addChild(orderingFieldRef.createNode());
            }

            if (desc) {
                orderByField.addChild(new CommonTree(new CommonToken(JPA2Lexer.DESC, "desc")));
            }
            orderByField.freshenParentAndChildIndexes();
        }
    }

    public void handleCaseInsensitiveParam(String paramName) {
        List<SimpleConditionNode> conditionNodesWithParameter = findConditionsForParameter(paramName);

        for (SimpleConditionNode simpleConditionNode : conditionNodesWithParameter) {
            PathNode pathNode = (PathNode) simpleConditionNode.getFirstChildWithType(JPA2Lexer.T_SELECTED_FIELD);
            CommonTree loweredPathNode = new CommonTree();
            loweredPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.LOWER, "lower")));
            loweredPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
            loweredPathNode.addChild(pathNode);
            loweredPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));

            simpleConditionNode.replaceChildren(0, 0, loweredPathNode);
        }
        tree.freshenParentAndChildIndexes();
    }

    public void replaceInCondition(String paramName) {
        List<SimpleConditionNode> conditionNodesWithParameter = findConditionsForParameter(paramName);
        for (SimpleConditionNode simpleConditionNode : conditionNodesWithParameter) {
            Tree inToken = simpleConditionNode.getFirstChildWithType(JPA2Lexer.IN);
            if (inToken != null) {
                Tree notToken = simpleConditionNode.getFirstChildWithType(JPA2Lexer.NOT);
                simpleConditionNode.getChildren().clear();
                simpleConditionNode.addChild(
                        new CommonTree(new CommonToken(JPA2Lexer.WORD, notToken == null ? "1=0" : "1=1")));
            }
        }
        tree.freshenParentAndChildIndexes();
    }

    public void replaceWithSelectId() {
        PathNode returnedPathNode = getFirstReturnedPathNode();
        if (returnedPathNode != null) {
            returnedPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.WORD, "id")));
        }
    }

    public void replaceWithSelectEntityVariable(String selectEntityVariable) {
        PathNode returnedPathNode = getFirstReturnedPathNode();
        if (returnedPathNode != null) {
            returnedPathNode.renameVariableTo(selectEntityVariable);
            while (returnedPathNode.getChildCount() > 0) {
                returnedPathNode.deleteChild(0);
            }
        }
    }

    public List<SimpleConditionNode> findConditionsForParameter(String paramName) {
        CommonTree whereTree = (CommonTree) tree.getFirstChildWithType(JPA2Lexer.T_CONDITION);
        List<SimpleConditionNode> conditionNodes = getChildrenByClass(whereTree, SimpleConditionNode.class);
        return conditionNodes.stream()
                .filter((SimpleConditionNode n) -> {
                    ParameterNode parameter = (ParameterNode) n.getFirstChildWithType(JPA2Lexer.T_PARAMETER);
                    return parameter != null && parameter.getChild(0).getText().contains(paramName);
                }).collect(Collectors.toList());
    }

    protected AggregateExpressionNode createCountNode(EntityReference ref, boolean distinct) {
        AggregateExpressionNode result = new AggregateExpressionNode(JPA2Lexer.T_AGGREGATE_EXPR);

        result.addChild(new CommonTree(new CommonToken(JPA2Lexer.COUNT, "count")));
        result.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
        if (distinct) {
            result.addChild(new CommonTree(new CommonToken(JPA2Lexer.DISTINCT, "distinct")));
        }
        result.addChild(ref.createNode());
        result.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
        return result;
    }

    //if at least one reference attribute in path is not embedded we decide that order by need join
    protected boolean needJoinForOrderBy(PathEntityReference orderingFieldRef) {
        PathNode pathNode = orderingFieldRef.getPathNode();
        String entityName = orderingFieldRef.getPathStartingEntityName();

        try {
            JpqlEntityModel entity = model.getEntityByName(entityName);

            for (int i = 0; i < pathNode.getChildCount(); i++) {
                String fieldName = pathNode.getChild(i).toString();
                Attribute entityAttribute = entity.getAttributeByName(fieldName);
                if (entityAttribute.isEntityReferenceAttribute() && !entityAttribute.isEmbedded()) {
                    return true;
                }

                if (entityAttribute.isEntityReferenceAttribute()) {
                    entityName = entityAttribute.getReferencedEntityName();
                    entity = model.getEntityByName(entityName);
                }
            }
        } catch (UnknownEntityNameException e) {
            throw new RuntimeException("Could not find entity by name " + entityName, e);
        }

        return false;
    }
}