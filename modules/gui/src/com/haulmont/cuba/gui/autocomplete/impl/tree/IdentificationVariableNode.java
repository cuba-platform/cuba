package com.haulmont.cuba.jpql.impl.tree;

import com.haulmont.cuba.jpql.impl.DomainModel;
import com.haulmont.cuba.jpql.impl.ErrorRec;
import com.haulmont.cuba.jpql.impl.QueryVariableContext;
import com.haulmont.cuba.jpql.impl.UnknownEntityNameException;
import com.haulmont.cuba.jpql.impl.pointer.SimpleAttributePointer;
import com.haulmont.cuba.jpql.impl.pointer.NoPointer;
import com.haulmont.cuba.jpql.impl.pointer.Pointer;
import com.haulmont.cuba.jpql.impl.pointer.EntityPointer;
import com.haulmont.cuba.jpql.impl.model.*;
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
public class IdentificationVariableNode extends CommonTree {
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
                    throw new RuntimeException("�� ���������� �������, ����� ������������ ������");
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
        return new IdentificationVariableNode(token, variableName);
    }
}
