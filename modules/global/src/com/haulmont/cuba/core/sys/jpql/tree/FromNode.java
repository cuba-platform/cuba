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
 * @author chevelev
 * @version $Id$
 */
public class FromNode extends BaseCustomNode {
    private Token fromT;

    private FromNode(Token token, Token fromT) {
        super(token);
        this.fromT = fromT;
    }

    public FromNode(int type, Token fromT) {
        this(new CommonToken(type, ""), fromT);
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "") + fromT;
    }

    @Override
    public Tree dupNode() {
        FromNode result = new FromNode(token, fromT);
        dupChildren(result);
        return result;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendSpace();
        sb.appendString(fromT.getText());
        return this;
    }
}