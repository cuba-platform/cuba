/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

import com.haulmont.cuba.core.sys.jpql.antlr2.JPA2Lexer;
import com.haulmont.cuba.core.sys.jpql.model.Entity;
import com.haulmont.cuba.core.sys.jpql.pointer.EntityPointer;
import com.haulmont.cuba.core.sys.jpql.pointer.Pointer;
import com.haulmont.cuba.core.sys.jpql.tree.PathNode;
import com.haulmont.cuba.core.sys.jpql.tree.SelectedItemNode;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonErrorNode;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.runtime.tree.TreeVisitor;

import java.util.List;

/**
 * Author: Alexander Chevelev
 * Date: 06.04.2011
 * Time: 18:31:25
 */
public class QueryTreeAnalyzer {
    private DomainModel model;
    private IdVarSelector idVarSelector;
    protected CommonTree tree;

    public void prepare(DomainModel model, String query) throws RecognitionException {
        this.model = model;

        query = query.replace("\n", " ");
        query = query.replace("\r", " ");
        query = query.replace("\t", " ");
        tree = Parser.parse(query);
        TreeVisitor visitor = new TreeVisitor();
        idVarSelector = new IdVarSelector(model);
        visitor.visit(tree, idVarSelector);
    }

    public QueryVariableContext getRootQueryVariableContext() {
        return idVarSelector.getContextTree();
    }

    public List<ErrorRec> getInvalidIdVarNodes() {
        return idVarSelector.getInvalidIdVarNodes();
    }

    public List<CommonErrorNode> getErrorNodes() {
        return idVarSelector.getErrorNodes();
    }

    public CommonTree getTree() {
        return tree;
    }

    public String getRootEntityVariableName(String entityName) {
        QueryVariableContext ctx = getRootQueryVariableContext();
        return ctx.getVariableNameByEntity(entityName);
    }

    public PathNode getSelectedPathNode() {
        Tree selectedItems = tree.getFirstChildWithType(JPA2Lexer.T_SELECTED_ITEMS);
        boolean isDistinct = "DISTINCT".equalsIgnoreCase(selectedItems.getChild(0).getText());
        SelectedItemNode selectedItemNode;
        if (isDistinct) {
            if (selectedItems.getChildCount() != 2)
                throw new IllegalStateException("Cannot select path node if multiple fields selected");
            selectedItemNode = (SelectedItemNode) selectedItems.getChild(1);
        } else {
            if (selectedItems.getChildCount() != 1)
                throw new IllegalStateException("Cannot select path node if multiple fields selected");
            selectedItemNode = (SelectedItemNode) selectedItems.getChild(0);
        }

        if (!(selectedItemNode.getChild(0) instanceof PathNode)) {
            throw new IllegalStateException("An entity path is assumed to be selected");
        }
        return (PathNode) selectedItemNode.getChild(0);
    }

    public Entity getSelectedEntity(PathNode path) {
        Pointer pointer = path.walk(model, getRootQueryVariableContext());
        if (!(pointer instanceof EntityPointer)) {
            throw new IllegalStateException("A path resulting in an entity is assumed to be selected");
        }
        return ((EntityPointer)pointer).getEntity();
    }
}
