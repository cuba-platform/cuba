package com.haulmont.cuba.jpql.impl.tree;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

/**
 * Author: Alexander Chevelev
 * Date: 20.10.2010
 * Time: 23:20:41
 */
public class QueryNode extends CommonTree {
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
        return new QueryNode(token, lastToken);
    }

}
