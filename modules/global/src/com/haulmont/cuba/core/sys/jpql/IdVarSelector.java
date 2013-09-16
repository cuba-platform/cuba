/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.tree.BaseJoinNode;
import com.haulmont.cuba.core.sys.jpql.tree.IdentificationVariableNode;
import com.haulmont.cuba.core.sys.jpql.tree.QueryNode;
import com.haulmont.cuba.core.sys.jpql.tree.SimpleConditionNode;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.TreeVisitorAction;

import java.util.*;

/**
 * User: Alex Chevelev
 * Date: 15.10.2010
 * Time: 23:10:59
 *
 * @version $Id$
 */
public class IdVarSelector implements TreeVisitorAction {
    private QueryVariableContext root;

    private List<ErrorRec> invalidIdVarNodes = new ArrayList<ErrorRec>();
    private List<CommonErrorNode> errorNodes = new ArrayList<CommonErrorNode>();
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
            errorNodes.add((CommonErrorNode) t);
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


        if ((node instanceof QueryNode) && isInWhereSubquery(node)) {
            stack.peekLast().setPropagateVariablesUp(false);
            return t;
        }

        if (node instanceof IdentificationVariableNode) {
            IdentificationVariableNode vnode = (IdentificationVariableNode) node;
            vnode.identifyVariableEntity(model, stack, invalidIdVarNodes);
            return t;
        }

        if (node instanceof BaseJoinNode) {
            BaseJoinNode vnode = (BaseJoinNode) node;
            vnode.identifyVariableEntity(model, stack, invalidIdVarNodes);
            return t;
        }

        return t;
    }

    private boolean isInWhereSubquery(CommonTree node) {
        return node.getParent() != null && node.getParent() instanceof SimpleConditionNode;
    }


    public List<ErrorRec> getInvalidIdVarNodes() {
        return Collections.unmodifiableList(invalidIdVarNodes);
    }

    public List<CommonErrorNode> getErrorNodes() {
        return Collections.unmodifiableList(errorNodes);
    }
}
