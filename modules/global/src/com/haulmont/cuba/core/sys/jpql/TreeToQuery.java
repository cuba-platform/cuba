/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * Author: Alexander Chevelev
 * Date: 26.03.2011
 * Time: 3:09:04
 */
public class TreeToQuery implements TreeVisitorAction {
    private QueryBuilder sb = new QueryBuilder();
    private List<ErrorRec> invalidNodes = new ArrayList<ErrorRec>();

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
                node.parent != null && node.parent.getType() == JPA2Lexer.T_SIMPLE_CONDITION ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_GROUP_BY ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_ORDER_BY && node.getType() != JPA2Lexer.T_ORDER_BY_FIELD ||
                node.parent != null && node.parent.getType() == JPA2Lexer.T_CONDITION && node.getType() == JPA2Lexer.LPAREN && node.childIndex - 1 >= 0 && node.parent.getChild(node.childIndex - 1).getType() != JPA2Lexer.LPAREN ||
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

        if (node.getType() == JPA2Lexer.T_ORDER_BY_FIELD && node.childIndex - 1 >= 0 && node.parent.getChild(node.childIndex - 1).getType() == JPA2Lexer.T_ORDER_BY_FIELD) {
            sb.appendString(", ");
        }

        if (node instanceof TreeToQueryCapable) {
            return ((TreeToQueryCapable) t).treeToQueryPre(sb, invalidNodes);
        }

        if (node.getType() == JPA2Lexer.T_SELECTED_ITEMS) {
            return t;
        }

        if (node.getType() == JPA2Lexer.T_SOURCES) {
            sb.appendString("FROM ");
            return t;
        }

        sb.appendString(node.toString());
        return t;
    }

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
