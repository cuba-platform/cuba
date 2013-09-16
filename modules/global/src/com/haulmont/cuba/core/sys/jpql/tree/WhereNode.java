/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryBuilder;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 30.10.2010
 * Time: 4:15:07
 */
public class WhereNode extends BaseCustomNode {
    private Token whereT;

    private WhereNode(Token token, Token whereT) {
        super(token);
        this.whereT = whereT;
    }

    public WhereNode(int type, Token whereT) {
        this(new CommonToken(type, ""), whereT);
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "") + whereT;
    }

    @Override
    public Tree dupNode() {
        WhereNode result = new WhereNode(token, whereT);
        dupChildren(result);
        return result;
    }

    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendSpace();
        sb.appendString(whereT.getText());
        return this;
    }
}