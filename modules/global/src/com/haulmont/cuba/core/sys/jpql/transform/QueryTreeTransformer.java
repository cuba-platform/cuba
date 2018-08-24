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

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.QueryTree;
import com.haulmont.cuba.core.sys.jpql.UnknownEntityNameException;
import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.Attribute;
import com.haulmont.cuba.core.sys.jpql.model.JpqlEntityModel;
import com.haulmont.cuba.core.sys.jpql.tree.*;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.commons.lang.StringUtils;

import java.util.*;

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
            existingChildrenWithBraces.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
            existingChildrenWithBraces.addChildren(existingChildren);
            existingChildrenWithBraces.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
            sourceWhere.replaceChildren(0, sourceWhere.getChildCount() - 1, existingChildrenWithBraces);

            CommonTree and = new CommonTree(new CommonToken(JPA2Lexer.AND, "and"));
            sourceWhere.addChild(and);
            sourceWhere.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
            for (Object o : targetWhere.getChildren()) {
                CommonTree t = (CommonTree) o;
                sourceWhere.addChild(t.dupNode());
            }
            sourceWhere.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
            sourceWhere.freshenParentAndChildIndexes();
        }
    }

    public void mixinJoinIntoTree(CommonTree joinClause, EntityReference entityReference, boolean renameVariable) {
        CommonTree from = queryTree.getAstFromNode();
        for (int i = 0; i < from.getChildCount(); i++) {
            SelectionSourceNode selectionSource = (SelectionSourceNode) from.getChild(i);
            if (selectionSource.getChild(0) instanceof IdentificationVariableNode) {
                IdentificationVariableNode identificationVariable = (IdentificationVariableNode) selectionSource.getChild(0);
                if (entityReference.supportsJoinTo(identificationVariable)) {
                    String variableName = identificationVariable.getVariableName();
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

    public void replaceWithCount(Tree node) {
        Tree selectedItems = queryTree.getAstSelectedItemsNode();
        boolean isDistinct = "distinct".equalsIgnoreCase(selectedItems.getChild(0).getText());
        if (!(isDistinct && selectedItems.getChildCount() == 2 ||
                selectedItems.getChildCount() == 1))
            throw new IllegalStateException("Cannot replace with count if multiple fields selected");

        SelectedItemNode selectedItemNode;
        if (isDistinct)
            selectedItems.deleteChild(0);

        selectedItemNode = (SelectedItemNode) selectedItems.getChild(0);
        AggregateExpressionNode countNode = createCountNode(node, isDistinct);
        selectedItemNode.deleteChild(0);
        selectedItemNode.addChild(countNode);

        Tree orderBy = queryTree.getAstTree().getFirstChildWithType(JPA2Lexer.T_ORDER_BY);
        if (orderBy != null) {
            queryTree.getAstTree().deleteChild(orderBy.getChildIndex());
        }
        queryTree.getAstTree().freshenParentAndChildIndexes();
    }

    public void removeOrderBy() {
        Tree orderBy = queryTree.getAstTree().getFirstChildWithType(JPA2Lexer.T_ORDER_BY);
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
            selectedItems.insertChild(0, new CommonTree(new CommonToken(JPA2Lexer.DISTINCT, "distinct")));
            selectedItems.freshenParentAndChildIndexes();
        }
    }

    public void replaceEntityName(String newEntityName, IdentificationVariableNode identificationVariable) {
        if (identificationVariable != null) {
            identificationVariable.deleteChild(0);
            identificationVariable.addChild(new CommonTree(new CommonToken(JPA2Lexer.WORD, newEntityName)));
            identificationVariable.freshenParentAndChildIndexes();
        }
    }

    public void replaceOrderBy(boolean desc, PathEntityReference... orderingFieldRefs) {
        removeOrderBy();
        CommonTree orderBy = new OrderByNode(JPA2Lexer.T_ORDER_BY);
        queryTree.getAstTree().addChild(orderBy);
        orderBy.addChild(new CommonTree(new CommonToken(JPA2Lexer.ORDER, "order")));
        orderBy.addChild(new CommonTree(new CommonToken(JPA2Lexer.BY, "by")));

        Set<String> addedJoinVariables = new HashSet<>();
        for (PathEntityReference orderingFieldRef : orderingFieldRefs) {
            OrderByFieldNode orderByField = new OrderByFieldNode(JPA2Lexer.T_ORDER_BY_FIELD);
            orderBy.addChild(orderByField);

            PathNode pathNode = orderingFieldRef.getPathNode();
            if (pathNode.getChildCount() > 1) {
                List<PathNode> pathNodes = getPathNodesForOrderBy(orderingFieldRef);
                if (pathNodes.size() > 1) {
                    orderByField.addChild(pathNodes.remove(pathNodes.size() - 1));
                    for (PathNode joinPathNode : pathNodes) {
                        String joinPathNodeVariableName = joinPathNode.asPathString('_');
                        if (!addedJoinVariables.contains(joinPathNodeVariableName)) {
                            JoinVariableNode joinNode = new JoinVariableNode(JPA2Lexer.T_JOIN_VAR, "left join", joinPathNodeVariableName);
                            joinNode.addChild(joinPathNode);

                            CommonTree from = queryTree.getAstFromNode();
                            from.getChild(0).addChild(joinNode); // assumption
                            addedJoinVariables.add(joinPathNodeVariableName);
                        }
                    }
                } else {
                    orderByField.addChild(orderingFieldRef.createNode());
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

    public void addEntityInGroupBy(String entityAlias) {
        Tree groupBy = queryTree.getAstGroupByNode();
        if (groupBy != null) {
            groupBy.addChild(new PathNode(JPA2Lexer.T_SELECTED_ENTITY, entityAlias));
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
                        loweredPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.LOWER, "lower")));
                        loweredPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
                        loweredPathNode.addChild(pathNode);
                        loweredPathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
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
                condition.addChild(new CommonTree(new CommonToken(JPA2Lexer.WORD, notToken == null ? "1=0" : "1=1")));
                condition.freshenParentAndChildIndexes();
            }
        }
    }

    public void replaceWithSelectId(String idProperty, PathNode pathNode) {
        if (pathNode != null) {
            pathNode.addChild(new CommonTree(new CommonToken(JPA2Lexer.WORD, idProperty)));
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

    protected AggregateExpressionNode createCountNode(Tree tree, boolean distinct) {
        AggregateExpressionNode result = new AggregateExpressionNode(JPA2Lexer.T_AGGREGATE_EXPR);

        result.addChild(new CommonTree(new CommonToken(JPA2Lexer.COUNT, "count")));
        result.addChild(new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "(")));
        if (distinct) {
            result.addChild(new CommonTree(new CommonToken(JPA2Lexer.DISTINCT, "distinct")));
        }
        result.addChild(tree);
        result.addChild(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
        return result;
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
                if (Objects.equals(existingPathNode.asPathString(), pathNode.asPathString())) {
                    return true;
                }
            }
        }

        return false;
    }

    protected List<PathNode> getPathNodesForOrderBy(PathEntityReference pathEntityReference) {
        List<PathNode> pathNodes = new ArrayList<>();
        String entityName = pathEntityReference.getPathStartingEntityName();
        PathNode pathNode = pathEntityReference.getPathNode();
        try {
            DomainModel model = queryTree.getModel();
            JpqlEntityModel entity = model.getEntityByName(entityName);
            String currentVariableName = pathNode.getEntityVariableName();
            PathNode currentPathNode = new PathNode(pathNode.getToken(), currentVariableName);
            for (int i = 0; i < pathNode.getChildCount(); i++) {
                String fieldName = pathNode.getChild(i).toString();
                Attribute entityAttribute = entity.getAttributeByName(fieldName);
                if (entityAttribute.isEntityReferenceAttribute() && !entityAttribute.isEmbedded()) {
                    currentPathNode.addDefaultChild(fieldName);
                    pathNodes.add(currentPathNode);
                    currentVariableName = currentPathNode.asPathString('_');
                    currentPathNode = new PathNode(pathNode.getToken(), currentVariableName);
                } else {
                    currentPathNode.addDefaultChild(fieldName);
                }
                if (entityAttribute.isEntityReferenceAttribute()) {
                    entityName = entityAttribute.getReferencedEntityName();
                    entity = model.getEntityByName(entityName);
                }
            }
            pathNodes.add(currentPathNode);
        } catch (UnknownEntityNameException e) {
            throw new RuntimeException(String.format("Could not find entity by name %s", entityName), e);
        }
        return pathNodes;
    }
}