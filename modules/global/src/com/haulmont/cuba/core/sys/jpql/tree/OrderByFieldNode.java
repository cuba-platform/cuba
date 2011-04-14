package com.haulmont.cuba.core.sys.jpql.tree;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.Tree;

/**
 * Author: Alexander Chevelev
 * Date: 30.10.2010
 * Time: 4:15:07
 */
public class OrderByFieldNode extends BaseCustomNode {
    private OrderByFieldNode(Token token) {
        super(token);
    }

    public OrderByFieldNode(int type) {
        this(new CommonToken(type, ""));
    }

    @Override
    public String toString() {
        return "ORDER BY FIELD";
    }

    @Override
    public Tree dupNode() {
        OrderByFieldNode result = new OrderByFieldNode(token);
        dupChildren(result);
        return result;
    }
}