/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
 */
public class ConditionsTree extends Tree<AbstractCondition> {

    public ConditionsTree() {
        super(new ArrayList<>());
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