/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryBuilder;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 20.10.2010
 * Time: 23:20:41
 */
public class QueryNode extends BaseCustomNode {
    private Token lastToken;

    private QueryNode(Token token, Token lastToken) {
        super(token);
        this.lastToken = lastToken;
    }

    public QueryNode(int type, Token token) {
        this(token, null);
    }

    public QueryNode(int type, Token token, Token lastToken) {
        this(token, lastToken);
    }

    public boolean contains(int caret) {
        return lastToken == null ||
                caret >= token.getCharPositionInLine() && (caret - lastToken.getCharPositionInLine()) <= lastToken.getText().length();


    }

    @Override
    public Tree dupNode() {
        QueryNode result = new QueryNode(token, lastToken);
        dupChildren(result);
        return result;
    }

    @Override
    public String toStringTree() {
        return super.toStringTree();
    }

    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendString(getText());
        return this;
    }

    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        if (parent != null) {
            if (' ' == sb.getLast())
                sb.deleteLast();
            sb.appendString(") ");
        }
        return this;
    }
}
