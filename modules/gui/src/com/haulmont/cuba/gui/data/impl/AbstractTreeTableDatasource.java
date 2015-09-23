/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.TreeTableDatasource;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author gorodnov
 * @version $Id$
 */
public abstract class AbstractTreeTableDatasource<T extends Entity<K>, K>
        extends AbstractTreeDatasource<T, K>
        implements TreeTableDatasource<T, K> {

    private class TreeTableNodeComparator<T extends Entity> implements Comparator<Node<T>> {
        private final EntityComparator<T> entityComparator;

        private TreeTableNodeComparator(MetaPropertyPath propertyPath, boolean asc) {
            entityComparator = new EntityComparator<>(propertyPath, asc);
        }

        @Override
        public int compare(Node<T> n1, Node<T> n2) {
            T e1 = n1.getData();
            T e2 = n2.getData();
            return entityComparator.compare(e1, e2);
        }
    }

    @Override
    protected void doSort() {
        if (tree == null) {
            log.warn("AbstractTreeTableDatasource.doSort: Tree is null, exiting");
            return;
        }

        sort(tree.getRootNodes());

        data.clear();
        for (Node<T> node : tree.toList()) {
            final T entity = node.getData();
            final K id = entity.getId();

            data.put(id, entity);
        }
    }

    private void sort(List<Node<T>> nodesList) {
        Collections.sort(nodesList, createEntityNodeComparator());
        for (Node<T> n :nodesList) {
            if (n.getNumberOfChildren() > 0) {
                sort(n.getChildren());
            }
        }
    }

    protected Comparator<Node<T>> createEntityNodeComparator() {
        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

        return new TreeTableNodeComparator<>(propertyPath, asc);
    }
}