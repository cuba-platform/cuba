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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.logging.UIPerformanceLogger;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import java.util.*;

/**
 * @param <T> Entity
 * @param <K> Key
 */
public abstract class AbstractTreeDatasource<T extends Entity<K>, K>
        extends CollectionDatasourceImpl<T, K>
        implements HierarchicalDatasource<T, K> {

    protected Tree<T> tree;
    protected Map<K, Node<T>> nodes;

    @Override
    protected void loadData(Map<String, Object> params) {
        String tag = getLoggingTag("TDS");
        StopWatch sw = new Log4JStopWatch(tag, Logger.getLogger(UIPerformanceLogger.class));

        clear();

        this.tree = loadTree(params);

        Map<K, Node<T>> targetNodes = new HashMap<>();
        if (tree != null) {
            for (Node<T> node : tree.toList()) {
                final T entity = node.getData();
                final K id = entity.getId();

                data.put(id, entity);
                attachListener(entity);

                targetNodes.put(id, node);
            }
        }

        this.nodes = targetNodes;

        sw.stop();
    }

    protected abstract Tree<T> loadTree(Map<String, Object> params);

    @Override
    public String getHierarchyPropertyName() {
        return null;
    }

    @Override
    public void setHierarchyPropertyName(String parentPropertyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<K> getRootItemIds() {
        if (state == State.NOT_INITIALIZED) {
            return Collections.emptyList();
        } else {
            if (tree == null) {
                return Collections.emptyList();
            }

            List ids = new ArrayList();
            for (Node<T> rootNode : tree.getRootNodes()) {
                ids.add(rootNode.getData().getId());
            }
            return (Collection<K>) Collections.unmodifiableCollection(ids);
        }
    }

    @Override
    public K getParent(K itemId) {
        if (nodes == null || tree == null) {
            return null;
        }

        final Node<T> node = nodes.get(itemId);
        return node == null ? null : node.getParent() == null ? null : node.getParent().getData().getId();
    }

    @Override
    public Collection<K> getChildren(K itemId) {
        if (nodes == null || tree == null) {
            return Collections.emptyList();
        }

        final Node<T> node = nodes.get(itemId);
        if (node == null) {
            return Collections.emptyList();
        } else {
            final List<Node<T>> children = node.getChildren();

            final List<K> ids = new ArrayList<>();
            for (Node<T> targetNode : children) {
                ids.add(targetNode.getData().getId());
            }

            return ids;
        }
    }

    @Override
    public boolean isRoot(K itemId) {
        if (nodes == null || tree == null) {
            return false;
        }

        final Node<T> node = nodes.get(itemId);

        for (Node<T> tNode : tree.getRootNodes()) {
            if (ObjectUtils.equals(tNode, node)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        backgroundWorker.checkUIAccess();

        // replaced refresh call with state initialization
        if (state != State.VALID) {
            invalidate();

            State prevState = state;
            if (!prevState.equals(State.VALID)) {
                valid();
                fireStateChanged(prevState);
            }
        }

        // Get items
        List<Object> collectionItems = new ArrayList<>(data.values());
        // Clear container
        data.clear();

        tree = null;
        nodes = null;

        // Notify listeners
        for (Object obj : collectionItems) {
            T item = (T) obj;
            detachListener(item);
        }

        setItem(null);

        fireCollectionChanged(Operation.CLEAR, Collections.emptyList());
    }

    @Override
    public boolean hasChildren(K itemId) {
        if (nodes == null || tree == null) {
            return false;
        }

        final Node<T> node = nodes.get(itemId);
        return node != null && !node.getChildren().isEmpty();
    }
}