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
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.TreeTableDatasource;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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