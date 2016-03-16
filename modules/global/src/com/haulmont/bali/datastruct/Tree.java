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
package com.haulmont.bali.datastruct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @param <T>
 */
public class Tree<T> implements Serializable {
    private static final long serialVersionUID = 701368293843044209L;

    private List<Node<T>> rootNodes;

    public Tree() {
    }

    public Tree(final Node<T> root) {
        this.rootNodes = new ArrayList<>(Arrays.asList(root));
    }

    public Tree(List<Node<T>> rootNodes) {
        this.rootNodes = new ArrayList<>(rootNodes);
    }

    /**
     * Return the first root Node of the tree.
     *
     * @return the root element.
     */
    public Node<T> getRootNode() {
        return rootNodes.isEmpty() ? null : rootNodes.get(0);
    }

    /**
     * Return the root nodes of the tree.
     *
     * @return the root elements.
     */
    public List<Node<T>> getRootNodes() {
        if (rootNodes == null) {
            rootNodes = new ArrayList<>();
        }

        return this.rootNodes;
    }

    /**
     * Set the root Element for the tree.
     *
     * @param rootNodes the root element to set.
     */
    public void setRootNodes(List<Node<T>> rootNodes) {
        this.rootNodes = rootNodes;
    }

    /**
     * Returns the Tree<T> as a List of Node<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     *
     * @return a List<Node<T>>.
     */
    public List<Node<T>> toList() {
        List<Node<T>> list = new ArrayList<>();
        if (rootNodes != null) {
            for (Node<T> rootNode : rootNodes) {
                walk(rootNode, list);
            }
        }
        return list;
    }

    /**
     * Returns a String representation of the Tree. The elements are generated
     * from a pre-order traversal of the Tree.
     *
     * @return the String representation of the Tree.
     */
    public String toString() {
        return toList().toString();
    }

    /**
     * Walks the Tree in pre-order style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference     * as it recurses down the tree.
     *
     * @param element the starting element.
     * @param list    the output of the walk.
     */
    private void walk(Node<T> element, List<Node<T>> list) {
        list.add(element);
        for (Node<T> data : element.getChildren()) {
            walk(data, list);
        }
    }
}