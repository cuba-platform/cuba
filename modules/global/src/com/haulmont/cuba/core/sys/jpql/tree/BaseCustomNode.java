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
 * Date: 26.03.2011
 * Time: 15:40:55
 */
public class BaseCustomNode extends CommonTree implements TreeToQueryCapable {
    public BaseCustomNode(Token t) {
        super(t);
    }

    public CommonTree treeToQueryPre(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        return this;
    }

    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        return this;
    }

    protected CommonTree getNextNode() {
        return getChildIndex() == (getParent().getChildCount() - 1) ?
                null:
                (CommonTree) getParent().getChild(getChildIndex() + 1);
    }

    protected CommonTree getPrevNode() {
        return getChildIndex() == 0 ?
                null:
                (CommonTree) getParent().getChild(getChildIndex() - 1);
    }

    protected void dupChildren(CommonTree result) {
        for (Object child : children) {
            CommonTree t = (CommonTree) child;
            Tree copy = t.dupNode();
            result.addChild(copy);
        }
    }
}
