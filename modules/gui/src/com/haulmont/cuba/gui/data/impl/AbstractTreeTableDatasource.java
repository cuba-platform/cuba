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
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.model.impl.EntityValuesComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractTreeTableDatasource<T extends Entity<K>, K>
        extends AbstractTreeDatasource<T, K>
        implements HierarchicalDatasource<T, K> {

    @Override
    protected void doSort() {
        if (tree == null) {
            Logger log = LoggerFactory.getLogger(AbstractTreeDatasource.class);
            log.warn("AbstractTreeTableDatasource.doSort: Tree is null, exiting");
            return;
        }

        sort(tree.getRootNodes());

        data.clear();
        for (Node<T> node : tree.toList()) {
            T entity = node.getData();
            K id = entity.getId();

            data.put(id, entity);
        }
    }

    protected void sort(List<Node<T>> nodesList) {
        nodesList.sort(createEntityNodeComparator());
        for (Node<T> n :nodesList) {
            if (n.getNumberOfChildren() > 0) {
                sort(n.getChildren());
            }
        }
    }

    protected Comparator<Node<T>> createEntityNodeComparator() {
        final MetaPropertyPath propertyPath = sortInfos[0].getPropertyPath();
        final boolean asc = Order.ASC.equals(sortInfos[0].getOrder());

        return Comparator.comparing(node -> node != null ? node.getData().getValueEx(propertyPath) : null,
                EntityValuesComparator.asc(asc));
    }
}