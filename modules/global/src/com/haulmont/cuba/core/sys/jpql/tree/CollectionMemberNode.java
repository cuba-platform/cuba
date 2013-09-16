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
public class CollectionMemberNode extends BaseJoinNode {

    private CollectionMemberNode(Token token, String variableName) {
        super(token, variableName);
    }

    public CollectionMemberNode(int type, String variableName) {
        this(new CommonToken(type, ""), variableName);
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "") + "Collection member decl: " + variableName;
    }

    @Override
    public Tree dupNode() {
        CollectionMemberNode result = new CollectionMemberNode(token, variableName);
        dupChildren(result);
        return result;
    }

    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        sb.appendSpace();
        sb.appendString("in(");
        return this;
    }

    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        // должно появится после определения сущности, из которой выбирают, поэтому в post
        sb.appendString(")");
        sb.appendSpace();
        sb.appendString(variableName);
        return this;
    }
}