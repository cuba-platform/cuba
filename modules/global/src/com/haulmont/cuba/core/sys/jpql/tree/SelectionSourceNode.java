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
public class SelectionSourceNode extends BaseCustomNode {
    private SelectionSourceNode(Token token) {
        super(token);
    }

    public SelectionSourceNode(int type) {
        this(new CommonToken(type, ""));
    }

    @Override
    public String toString() {
        return "SELECTION_SOURCE";
    }

    @Override
    public Tree dupNode() {
        SelectionSourceNode result = new SelectionSourceNode(token);
        dupChildren(result);
        return result;
    }

    @Override
    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        CommonTree prevNode = getPrevNode();
        if (prevNode != null && prevNode instanceof SelectionSourceNode) {
            sb.appendString(", ");
        } else {
            sb.appendSpace();
        }
        return super.treeToQueryPre(sb, invalidNodes);
    }

    @Override
    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        return this;
    }
}