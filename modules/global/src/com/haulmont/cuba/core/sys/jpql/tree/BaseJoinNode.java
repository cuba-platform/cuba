/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.DomainModel;
import com.haulmont.cuba.core.sys.jpql.ErrorRec;
import com.haulmont.cuba.core.sys.jpql.QueryVariableContext;
import com.haulmont.cuba.core.sys.jpql.pointer.*;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;

import java.util.Deque;
import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 30.10.2010
 * Time: 4:15:07
 */
public class BaseJoinNode extends BaseCustomNode {
    protected String variableName;

    protected BaseJoinNode(Token token, String variableName) {
        super(token);
        this.variableName = variableName;
    }

    public BaseJoinNode(int type, String variableName) {
        this(new CommonToken(type, ""), variableName);
    }

    public String getVariableName() {
        return variableName;
    }

    public void identifyVariableEntity(DomainModel model,
                                       Deque<QueryVariableContext> stack,
                                       List<ErrorRec> invalidNodes) {
        String variableName = getVariableName();
        if (variableName == null) {
            invalidNodes.add(new ErrorRec(this, "No variable name found"));
            return;
        }

        List children = getChildren();
        if (children == null) {
            invalidNodes.add(new ErrorRec(this, "No children found"));
            return;
        }

        if (children.size() != 1) {
            invalidNodes.add(new ErrorRec(this, "Number of children not equals 1"));
            return;
        }

        CommonTree child0 = (CommonTree) children.get(0);
        if (child0 instanceof CommonErrorNode) {
            invalidNodes.add(new ErrorRec(this, "Child 0 is an error node"));
            return;
        }

        QueryVariableContext queryVC = stack.peekLast();
        PathNode pathNode = (PathNode) child0;
        Pointer pointer = pathNode.walk(model, queryVC);
        if (pointer instanceof NoPointer) {
            invalidNodes.add(new ErrorRec(this, "Cannot resolve joined entity"));
        } else if (pointer instanceof SimpleAttributePointer) {
            invalidNodes.add(new ErrorRec(this, "Joined entity resolved to a non-entity attribute"));
        } else if (pointer instanceof EntityPointer) {
            queryVC.addEntityVariable(variableName, ((EntityPointer) pointer).getEntity());
        } else if (pointer instanceof CollectionPointer) {
            queryVC.addEntityVariable(variableName, ((CollectionPointer) pointer).getEntity());
        } else {
            invalidNodes.add(new ErrorRec(this,
                    "Unexpected pointer variable type: " + pointer.getClass())
            );
        }
    }

    public void setVariableName(String newName) {
        variableName = newName;
    }
}
