/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.03.2009 16:26:56
 *
 * $Id: Tree.java 3028 2010-11-09 08:12:36Z krivopustov $
 */
package com.haulmont.bali.datastruct;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Tree<T> implements Serializable
{
    private static final long serialVersionUID = 701368293843044209L;
    
    private List<Node<T>> rootNodes;

    public Tree() {
        super();
    }

    public Tree(final Node<T> root) {
        this.rootNodes = new ArrayList<Node<T>>(Arrays.asList(root));
    }

    public Tree(List<Node<T>> rootNodes) {
        this.rootNodes = new ArrayList<Node<T>>(rootNodes);
    }

    public Node<T> getRootNode() {
        return rootNodes.isEmpty() ? null : rootNodes.get(0);
    }

    /**
     * Return the root Node of the tree.
     * @return the root element.
     */
    public List<Node<T>> getRootNodes() {
        return this.rootNodes;
    }

    /**
     * Set the root Element for the tree.
     * @param rootNodes the root element to set.
     */
    public void setRootNodes(List<Node<T>> rootNodes) {
        this.rootNodes = rootNodes;
    }

    /**
     * Returns the Tree<T> as a List of Node<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     * @return a List<Node<T>>.
     */
    public List<Node<T>> toList() {
        List<Node<T>> list = new ArrayList<Node<T>>();
        for (Node<T> rootNode : rootNodes) {
            walk(rootNode, list);
        }
        return list;
    }

    /**
     * Returns a String representation of the Tree. The elements are generated
     * from a pre-order traversal of the Tree.
     * @return the String representation of the Tree.
     */
    public String toString() {
        return toList().toString();
    }

    /**
     * Walks the Tree in pre-order style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference     * as it recurses down the tree.
     * @param element the starting element.
     * @param list the output of the walk.
     */
    private void walk(Node<T> element, List<Node<T>> list) {
        list.add(element);
        for (Node<T> data : element.getChildren()) {
            walk(data, list);
        }
    }
}
