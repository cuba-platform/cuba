/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql.tree;

import com.haulmont.cuba.core.sys.jpql.*;
import com.haulmont.cuba.core.sys.jpql.pointer.SimpleAttributePointer;
import com.haulmont.cuba.core.sys.jpql.pointer.NoPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.model.*;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import java.util.Deque;
import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 30.10.2010
 * Time: 4:15:07
 */
public class IdentificationVariableNode extends BaseCustomNode {
    private String variableName;

    private IdentificationVariableNode(Token token, String variableName) {
        super(token);
        this.variableName = variableName;
    }

    public IdentificationVariableNode(int type, String variableName) {
        this(new CommonToken(type, ""), variableName);
    }

    public String getVariableName() {
        return variableName;
    }

    public void identifyVariableEntity(
            DomainModel model,
            Deque<QueryVariableContext> stack,
            List<ErrorRec> invalidIdVarNodes) {
        List children = getChildren();

        if (children == null) {
            invalidIdVarNodes.add(new ErrorRec(this, "Null children"));
            return;
        }

        if (children.size() != 1) {
            invalidIdVarNodes.add(new ErrorRec(this, "Number of children not equals 1"));
            return;
        }

        if (children.get(0) instanceof CommonErrorNode) {
            invalidIdVarNodes.add(new ErrorRec(this, "Child 0 is an error node"));
            return;
        }

        CommonTree child0 = (CommonTree) children.get(0);
        String variableName = getVariableName();

        if (child0 instanceof QueryNode) {
            QueryVariableContext queryVC;
            do {
                queryVC = stack.removeLast();
            } while (!queryVC.isPropagateVariablesUpstairs());
            deductFields(queryVC, child0, model);
            stack.peekLast().addEntityVariable(variableName, queryVC.getEntity());
        } else {
            if (variableName != null) {
                try {
                    String entityName = child0.token.getText();
                    Entity entity = model.getEntityByName(entityName);
                    stack.peekLast().addEntityVariable(variableName, entity);
                } catch (UnknownEntityNameException e) {
                    stack.peekLast().addEntityVariable(variableName, NoEntity.getInstance());
                }
            }
        }
    }

    public void deductFields(QueryVariableContext queryVC, CommonTree node, DomainModel model) {
        List children = node.getChildren();
        CommonTree T_SELECTED_ITEMS_NODE = (CommonTree) children.get(0);
        for (Object o : T_SELECTED_ITEMS_NODE.getChildren()) {
            o = ((SelectedItemNode) o).getChild(0);
            if (!(o instanceof PathNode)) {
                throw new RuntimeException("Not a path node");
            }

            PathNode pathNode = (PathNode) o;
            Pointer pointer = pathNode.walk(model, queryVC);

            if (pointer instanceof NoPointer) {
                queryVC.setEntity(NoEntity.getInstance());
                return;
            }

            if (pointer instanceof SimpleAttributePointer) {
                SimpleAttributePointer saPointer = (SimpleAttributePointer) pointer;
                queryVC.getEntity().addAttributeCopy(saPointer.getAttribute());
            } else if (pointer instanceof EntityPointer) {
                if (T_SELECTED_ITEMS_NODE.getChildren().size() != 1) {
                    //todo implement
                    throw new RuntimeException("Не реализован вариант, когда возвращается массив");
                } else {
                    queryVC.setEntity(((EntityPointer) pointer).getEntity());
                }
            }
        }
    }

    @Override
    public String toString() {
        return (token != null ? token.getText() : "") + "Variable: " + variableName;
    }

    @Override
    public Tree dupNode() {
        IdentificationVariableNode result = new IdentificationVariableNode(token, variableName);
        dupChildren(result);
        return result;
    }


    @Override
    public CommonTree treeToQueryPost(QueryBuilder sb, List<ErrorRec> invalidNodes) {
        // должно появится после определения сущности, из которой выбирают, поэтому в post
        sb.appendSpace();
        sb.appendString(variableName);
        return this;
    }

    public String getEntityName() {
        return getChild(0).getText();
    }
}
