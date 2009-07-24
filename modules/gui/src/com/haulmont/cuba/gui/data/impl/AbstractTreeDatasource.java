/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 16.03.2009 11:24:47
 * $Id$
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.impl.CollectionDatasourceImpl;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.DataService;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;

public abstract class AbstractTreeDatasource<T extends Entity, K>
    extends
        CollectionDatasourceImpl<T, K>
    implements
        HierarchicalDatasource<T, K>
{
    protected Tree<T> tree;
    protected Map<K, Node<T>> nodes;

    public AbstractTreeDatasource(
            DsContext context, DataService dataservice,
                String id, MetaClass metaClass, String viewName)
    {
        super(context, dataservice, id, metaClass, viewName);
    }

    protected void loadData(Map<String, Object> params) {
        this.tree = loadTree(params);

        Map<K, Node<T>> targetNodes = new HashMap<K, Node<T>>();

        data.clear();
        for (Node<T> node : tree.toList()) {
            final T entity = node.getData();
            final K id = (K) entity.getId();

            data.put(id, entity);

            targetNodes.put(id, node);
        }

        this.nodes = targetNodes;
    }

    protected abstract Tree<T> loadTree(Map<String, Object> params);

    public String getHierarchyPropertyName() {
        return null;
    }

    public void setHierarchyPropertyName(String parentPropertyName) {
        throw new UnsupportedOperationException();
    }

    public Collection<K> getRootItemIds() {
        if (State.NOT_INITIALIZAED.equals(state)) {
            return Collections.emptyList();
        } else {
            if (tree == null) return Collections.emptyList();

            List ids = new ArrayList();
            for (Node<T> rootNode : tree.getRootNodes()) {
                ids.add(rootNode.getData().getId());
            }
            return (Collection<K>) Collections.unmodifiableCollection(ids);
        }
    }

     public K getParent(K itemId) {
        final Node<T> node = nodes.get(itemId);
        return node == null ? null : (K) (node.getParent() == null ? null : node.getParent().getData().getId());
    }

    public Collection<K> getChildren(K itemId) {
        final Node<T> node = nodes.get(itemId);
        if (node == null)
            return Collections.emptyList();
        else {
            final List<Node<T>> children = node.getChildren();

            final List<K> ids = new ArrayList<K>();
            for (Node<T> targetNode : children) {
                ids.add((K) targetNode.getData().getId());
            }

            return ids;
        }
    }

    public boolean isRoot(K itemId) {
        final Node<T> node = nodes.get(itemId);

        for (Node<T> tNode : tree.getRootNodes()) {
            if (ObjectUtils.equals(tNode, node)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasChildren(K itemId) {
        final Node<T> node = nodes.get(itemId);
        return node != null && !node.getChildren().isEmpty();
    }
}
