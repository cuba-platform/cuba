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
import com.haulmont.cuba.core.sys.jpql.tree.TreeToQueryCapable;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreeToQuery implements TreeVisitorAction {
    private QueryBuilder sb = new QueryBuilder();
    private List<ErrorRec> invalidNodes = new ArrayList<>();

    @Override
    public Object pre(Object t) {
        if (!(t instanceof CommonTree)) {
            return t;
        }

        if (t instanceof CommonErrorNode) {
            invalidNodes.add(new ErrorRec((CommonErrorNode) t, "Error node"));
            return t;
        }

        CommonTree node = (CommonTree) t;

        if (node.token == null)
            return t;

        if (node.getType() == JPA2Lexer.HAVING ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_SIMPLE_CONDITION
                        && !parentNodeHasPreviousLparen(node) ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_GROUP_BY ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_ORDER_BY && node.getType() != JPA2Lexer.T_ORDER_BY_FIELD ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_CONDITION && node.getType() == JPA2Lexer.LPAREN && (node.childIndex == 0 || node.parent.getChild(node.childIndex - 1).getType() != JPA2Lexer.LPAREN) ||
                node.getType() == JPA2Lexer.AND ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_ORDER_BY_FIELD ||
                node.getType() == JPA2Lexer.OR ||
                node.getType() == JPA2Lexer.DISTINCT && node.childIndex == 0 ||
                node.getType() == JPA2Lexer.JOIN ||
                node.getType() == JPA2Lexer.LEFT ||
                node.getType() == JPA2Lexer.OUTER ||
                node.getType() == JPA2Lexer.INNER ||
                node.getType() == JPA2Lexer.FETCH
                ) {
            sb.appendSpace();
        }

        if (node.getType() == JPA2Lexer.T_ORDER_BY_FIELD && node.childIndex > 0 && node.parent.getChild(node.childIndex - 1).getType() == JPA2Lexer.T_ORDER_BY_FIELD) {
            sb.appendString(", ");
        }

        if (node instanceof TreeToQueryCapable) {
            return ((TreeToQueryCapable) t).treeToQueryPre(sb, invalidNodes);
        }

        if (node.getType() == JPA2Lexer.T_SELECTED_ITEMS) {
            return t;
        }

        if (node.getType() == JPA2Lexer.T_SOURCES) {
            sb.appendString("from ");
            return t;
        }

        sb.appendString(node.toString());
        return t;
    }

    private boolean parentNodeHasPreviousLparen(CommonTree node) {
        return (node.childIndex == 0 && node.parent.childIndex > 0 && node.parent.parent.getChild(node.parent.childIndex - 1).getType() == JPA2Lexer.LPAREN );
    }

    @Override
    public Object post(Object t) {
        if (!(t instanceof CommonTree))
            return t;

        if (t instanceof CommonErrorNode) {
            return t;
        }

        CommonTree node = (CommonTree) t;

        if (node.token == null)
            return t;

        if (node.getType() == JPA2Lexer.DISTINCT || node.getType() == JPA2Lexer.FETCH)
            sb.appendSpace();


        if (node instanceof TreeToQueryCapable) {
            return ((TreeToQueryCapable) t).treeToQueryPost(sb, invalidNodes);
        }

        return t;
    }

    public List<ErrorRec> getInvalidNodes() {
        return Collections.unmodifiableList(invalidNodes);
    }

    public String getQueryString() {
        return sb.toString();
    }
}