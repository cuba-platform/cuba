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
import java.util.List;
import java.util.ArrayList;

public class Node<T> implements Serializable {

    private static final long serialVersionUID = -7441713052548471005L;
    
    public T data;
    public Node<T> parent;
    public List<Node<T>> children;

    public Node() {
        super();
    }

    /**
     * Convenience constructor to create a {@code Node<T>} with an instance of T.
     *
     * @param data an instance of T.
     */
    public Node(T data) {
        this();
        setData(data);
    }

    /**
     * Return the children of {@code Node<T>}. The {@code Tree<T>} is represented by a single
     * root {@code Node<T>} whose children are represented by a {@code List<Node<T>>}. Each of
     * these {@code Node<T>} elements in the List can have children. The getChildren()
     * method will return the children of a {@code Node<T>}.
     *
     * @return the children of {@code Node<T>}
     */
    public List<Node<T>> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }

    public Node<T> getParent() {
        return parent;
    }

    /**
     * Sets the children of a {@code Node<T>} object. See docs for getChildren() for
     * more information.
     *
     * @param children the {@code List<Node<T>>} to set.
     */
    public void setChildren(List<Node<T>> children) {
        for (Node<T> child : children) {
            child.parent = this;
        }
        this.children = children;
    }

    /**
     * Returns the number of immediate children of this {@code Node<T>}.
     *
     * @return the number of immediate children.
     */
    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    /**
     * Adds a child to the list of children for this {@code Node<T>}. The addition of
     * the first child will create a new {@code List<Node<T>>}.
     *
     * @param child a {@code Node<T>} object to set.
     */
    public void addChild(Node<T> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        child.parent = this;
        children.add(child);
    }

    /**
     * Inserts a {@code Node<T>} at the specified position in the child list. Will
     * throw an ArrayIndexOutOfBoundsException if the index does not exist.
     *
     * @param index the position to insert at.
     * @param child the {@code Node<T>} object to insert.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void insertChildAt(int index, Node<T> child) throws IndexOutOfBoundsException {
        if (index == getNumberOfChildren()) {
            // this is really an append
            addChild(child);
        } else {
            if (index <0 || index >= children.size()) {
                throw new ArrayIndexOutOfBoundsException("Unable to insert child at " + index);
            }

            children.add(index, child);
            child.parent = this;
        }
    }

    /**
     * Remove the {@code Node<T>} element at index index of the {@code List<Node<T>>}.
     *
     * @param index the index of the element to delete.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(getData().toString()).append(",[");
        int i = 0;
        for (Node<T> e : getChildren()) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(e.getData().toString());
            i++;
        }
        sb.append("]").append("}");
        return sb.toString();
    }
}