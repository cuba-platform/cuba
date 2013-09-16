/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr.JPALexer;
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

        if (node.getType() == JPALexer.HAVING ||
                node.parent != null && node.parent.getType() == JPALexer.T_SIMPLE_CONDITION ||
                node.parent != null && node.parent.getType() == JPALexer.T_GROUP_BY ||
                node.parent != null && node.parent.getType() == JPALexer.T_ORDER_BY && node.getType() != JPALexer.T_ORDER_BY_FIELD ||
                node.parent != null && node.parent.getType() == JPALexer.T_CONDITION && node.getType() == JPALexer.LPAREN && node.childIndex - 1 >= 0 && node.parent.getChild(node.childIndex - 1).getType() != JPALexer.LPAREN ||
                node.getType() == JPALexer.AND ||
                node.parent != null && node.parent.getType() == JPALexer.T_ORDER_BY_FIELD ||
                node.getType() == JPALexer.OR ||
                node.getType() == JPALexer.DISTINCT && node.childIndex == 0 ||
                node.getType() == JPALexer.JOIN ||
                node.getType() == JPALexer.LEFT ||
                node.getType() == JPALexer.OUTER ||
                node.getType() == JPALexer.INNER ||
                node.getType() == JPALexer.FETCH
                ) {
            sb.appendSpace();
        }

        if (node.getType() == JPALexer.T_ORDER_BY_FIELD && node.childIndex - 1 >= 0 && node.parent.getChild(node.childIndex - 1).getType() == JPALexer.T_ORDER_BY_FIELD) {
            sb.appendString(", ");
        }

        if (node instanceof TreeToQueryCapable) {
            return ((TreeToQueryCapable) t).treeToQueryPre(sb, invalidNodes);
        }

        if (node.getType() == JPALexer.T_SELECTED_ITEMS) {
            return t;
        }

        if (node.getType() == JPALexer.T_SOURCES) {
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

        if (node.getType() == JPALexer.DISTINCT || node.getType() == JPALexer.FETCH)
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
