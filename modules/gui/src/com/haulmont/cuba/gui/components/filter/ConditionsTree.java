/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.gui.components.filter.condition.AbstractCondition;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as a data structure to store conditions inside generic filter.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ConditionsTree extends Tree<AbstractCondition> {

    public ConditionsTree() {
        super(new ArrayList<Node<AbstractCondition>>());
    }

    /**
     * Get all conditions as a plain list.
     * @return  conditions list
     */
    public List<AbstractCondition> toConditionsList() {
        List<AbstractCondition> list = new ArrayList<>();
        for (Node<AbstractCondition> node : toList()) {
            list.add(node.getData());
        }
        return list;
    }

    /**
     * Get root conditions.
     * @return  root conditions list
     */
    public List<AbstractCondition> getRoots() {
        List<AbstractCondition> list = new ArrayList<>();
        for (Node<AbstractCondition> node : getRootNodes()) {
            list.add(node.getData());
        }
        return list;
    }

    /**
     * Get node corresponding to the condition.
     * @param condition condition
     * @return  node or null if not found
     */
    @Nullable
    public Node<AbstractCondition> getNode(AbstractCondition condition) {
        for (Node<AbstractCondition> node : toList()) {
            if (condition.equals(node.getData()))
                return node;
        }
        return null;
    }

    /**
     * Removes a node with condition from the tree. Do nothing if condition is not in the tree.
     * @param condition condition to remove
     */
    public void removeCondition(AbstractCondition condition) {
        Node<AbstractCondition> node = getNode(condition);
        if (node != null) {
            if (node.getParent() == null) {
                getRootNodes().remove(node);
            } else {
                node.getParent().getChildren().remove(node);
            }
        }
    }

    /**
     * Creates a copy of conditionsTree. Each node of new tree contains a copy of source condition.
     */
    public ConditionsTree createCopy() {
        ConditionsTree copyTree = new ConditionsTree();
        List<Node<AbstractCondition>> newRootNodes = new ArrayList<>();
        for (Node<AbstractCondition> rootNode : this.getRootNodes()) {
            Node<AbstractCondition> newRootNode = new Node<>();
            newRootNodes.add(newRootNode);
            recursivelyCopyNode(rootNode, newRootNode);
        }
        copyTree.setRootNodes(newRootNodes);
        return copyTree;
    }

    protected void recursivelyCopyNode(Node<AbstractCondition> srcNode, Node<AbstractCondition> dstNode) {
        AbstractCondition srcCondition = srcNode.getData();
        AbstractCondition dstCondition = srcCondition.createCopy();
        dstNode.setData(dstCondition);
        for (Node<AbstractCondition> srcChild : srcNode.getChildren()) {
            Node<AbstractCondition> dstChild = new Node<>();
            dstNode.addChild(dstChild);
            recursivelyCopyNode(srcChild, dstChild);
        }
    }
}