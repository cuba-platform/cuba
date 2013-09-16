/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

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