/*
 * Copyright (c) 2008-2019 Haulmont.
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

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.tree.AggregateExpressionNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.OrderByNode;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JPATreeNodes {
    private JPATreeNodes() {
    }

    public static OrderByNode createOrderBy() {
        OrderByNode node = new OrderByNode(JPA2Lexer.T_ORDER_BY);
        node.addChild(new CommonTree(new CommonToken(JPA2Lexer.ORDER, "order")));
        node.addChild(new CommonTree(new CommonToken(JPA2Lexer.BY, "by")));
        return node;
    }

    public static Tree createAnd() {
        return new CommonTree(new CommonToken(JPA2Lexer.AND, "and"));
    }

    public static Tree createLPAREN() {
        return new CommonTree(new CommonToken(JPA2Lexer.LPAREN, "("));
    }

    public static Tree createRPAREN() {
        return new CommonTree(new CommonTree(new CommonToken(JPA2Lexer.RPAREN, ")")));
    }

    public static Tree createCount() {
        return new CommonTree(new CommonToken(JPA2Lexer.COUNT, "count"));
    }

    public static Tree createDistinct() {
        return new CommonTree(new CommonToken(JPA2Lexer.DISTINCT, "distinct"));
    }

    public static Tree createDesc() {
        return new CommonTree(new CommonToken(JPA2Lexer.DESC, "desc"));
    }

    public static Tree createLower() {
        return new CommonTree(new CommonToken(JPA2Lexer.LOWER, "lower"));
    }

    public static AggregateExpressionNode createAggregateExpression() {
        return new AggregateExpressionNode(JPA2Lexer.T_AGGREGATE_EXPR);
    }

    public static JoinVariableNode createLeftJoinByPath(String joinVariable, PathNode pathNode) {
        JoinVariableNode joinVariableNode = new JoinVariableNode(JPA2Lexer.T_JOIN_VAR, "left join", joinVariable);
        joinVariableNode.addChild(pathNode);
        return joinVariableNode;
    }

    public static PathNode createPathNode(String entityVariable, String path) {
        PathNode pathNode = new PathNode(JPA2Lexer.T_SELECTED_FIELD, entityVariable);
        if (path != null) {
            pathNode.addDefaultChildren(path);
        }
        return pathNode;
    }

    public static PathNode createPathNode(String entityVariable) {
        return createPathNode(entityVariable, null);
    }

    public static Tree createWord(String word) {
        return new CommonTree(new CommonToken(JPA2Lexer.WORD, word));
    }

    public static AggregateExpressionNode createAggregateCount(Tree expression, boolean distinctValues) {
        AggregateExpressionNode node = createAggregateExpression();

        node.addChild(createCount());
        node.addChild(createLPAREN());

        if (distinctValues) {
            node.addChild(createDistinct());
        }

        node.addChild(expression);
        node.addChild(createRPAREN());
        return node;
    }
}
