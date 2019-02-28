/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.tree.EnhancedTreeDataProvider;
import com.vaadin.data.SelectionModel;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.event.Action;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import com.vaadin.ui.components.grid.NoSelectionModel;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkArgument;

public class CubaTree<T> extends Tree<T> implements Action.ShortcutNotifier {

    protected String debugId;

    @Override
    protected TreeGrid<T> createTreeGrid() {
        return new CubaTreeGrid<>();
    }

    @Override
    public void setCubaId(String cubaId) {
        super.setCubaId(cubaId);

        getCompositionRoot().setCubaId(cubaId);
    }

    @Override
    public void setId(String id) {
        debugId = "cubaTree_" + id;
    }

    @Override
    public String getId() {
        return debugId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CubaTreeGrid<T> getCompositionRoot() {
        return (CubaTreeGrid<T>) super.getCompositionRoot();
    }

    public void setGridSelectionModel(GridSelectionModel<T> model) {
        getCompositionRoot().setGridSelectionModel(model);
    }

    @Override
    protected Grid.SelectionMode getSelectionMode() {
        SelectionModel<T> selectionModel = getSelectionModel();
        Grid.SelectionMode mode = null;
        if (selectionModel instanceof SingleSelectionModel) {
            mode = Grid.SelectionMode.SINGLE;
        } else if (selectionModel instanceof MultiSelectionModel) {
            mode = Grid.SelectionMode.MULTI;
        } else if (selectionModel instanceof NoSelectionModel) {
            mode = Grid.SelectionMode.NONE;
        }
        return mode;
    }

    @Override
    public void setDataProvider(DataProvider<T, ?> dataProvider) {
        if (!(dataProvider instanceof EnhancedTreeDataProvider)) {
            throw new IllegalArgumentException("DataProvider must implement " +
                    "com.haulmont.cuba.web.widgets.tree.EnhancedTreeDataProvider");
        }

        super.setDataProvider(dataProvider);
    }

    public Collection<T> getChildren(T item) {
        return getDataProvider()
                .fetchChildren(new HierarchicalQuery<>(null, item))
                .collect(Collectors.toList());
    }

    public boolean hasChildren(T item) {
        return getDataProvider().hasChildren(item);
    }

    @SuppressWarnings("unchecked")
    public Stream<T> getItems() {
        return ((EnhancedTreeDataProvider<T>) getDataProvider()).getItems();
    }

    @SuppressWarnings("unchecked")
    protected T getParentItem(T item) {
        return ((EnhancedTreeDataProvider<T>) getDataProvider()).getParent(item);
    }

    public void expandAll() {
        expandRecursively(getChildren(null), Integer.MAX_VALUE);
    }

    public void expandItemWithParents(T item) {
        List<T> itemsToExpand = new ArrayList<>();

        T current = item;
        while (current != null) {
            itemsToExpand.add(current);
            current = getParentItem(current);
        }

        expand(itemsToExpand);
    }

    public void collapseAll() {
        collapseRecursively(getChildren(null), Integer.MAX_VALUE);
    }

    public void collapseItemWithChildren(T item) {
        collapseRecursively(Collections.singleton(item), Integer.MAX_VALUE);
    }

    public void expandUpTo(int level) {
        checkArgument(level > 0, "level should be greater than 0");

        Collection<T> rootItems = getChildren(null);
        expandRecursively(rootItems, level - 1);
    }

    public void deselectAll() {
        getSelectionModel().deselectAll();
    }

    public void repaint() {
        markAsDirtyRecursive();
        getCompositionRoot().repaint();
    }
}