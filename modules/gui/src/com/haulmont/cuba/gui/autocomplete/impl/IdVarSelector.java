/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.autocomplete.impl;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryVariableContext;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.JoinVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.*;

/**
 * User: Alex Chevelev
 * Date: 15.10.2010
 * Time: 23:10:59
 */
public class IdVarSelector implements TreeVisitorAction {
    private QueryVariableContext root;

    private List<ErrorRec> invalidIdVarNodes = new ArrayList<ErrorRec>();
    private DomainModel model;
    private Deque<QueryVariableContext> stack = new ArrayDeque<QueryVariableContext>();

    public IdVarSelector(DomainModel model) {
        this.model = model;
    }

    public QueryVariableContext getContextTree() {
        return root;
    }

    public Object pre(Object t) {
        if (!(t instanceof CommonTree))
            return t;

        if (t instanceof CommonErrorNode) {
            return t;
        }

        CommonTree node = (CommonTree) t;

        if (node instanceof QueryNode) {
            QueryVariableContext newCurrent = new QueryVariableContext(model, (QueryNode) node);
            if (root == null) {
                root = newCurrent;
            }
            QueryVariableContext last = stack.peekLast();
            if (last != null) {
                last.addChild(newCurrent);
            }
            stack.addLast(newCurrent);
        }
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


        if ((node instanceof QueryNode) && node.getParent() != null && "T_CONDITION".equals(((CommonTree) node.getParent()).token.getText())) {
            stack.peekLast().setPropagateVariablesUp(false);
            return t;
        }

        if (node instanceof IdentificationVariableNode) {
            IdentificationVariableNode vnode = (IdentificationVariableNode) node;
            vnode.identifyVariableEntity(model, stack, invalidIdVarNodes);
            return t;
        }

        if (node instanceof JoinVariableNode) {
            JoinVariableNode vnode = (JoinVariableNode) node;
            vnode.identifyVariableEntity(model, stack, invalidIdVarNodes);
            return t;
        }

        return t;
    }


    public List<ErrorRec> getInvalidNodes() {
        return Collections.unmodifiableList(invalidIdVarNodes);
    }

}
