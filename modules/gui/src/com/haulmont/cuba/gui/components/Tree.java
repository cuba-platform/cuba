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
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import javax.annotation.Nullable;

/**
 */
public interface Tree<E extends Entity> extends ListComponent<E>, Component.Editable {

    String NAME = "tree";

    void expandTree();
    void expand(Object itemId);

    void collapseTree();
    void collapse(Object itemId);

    /**
     * Expand tree including specified level
     *
     * @param level level of Tree nodes to expand, if passed level = 1 then root items will be expanded
     * @throws IllegalArgumentException if level < 1
     */
    void expandUpTo(int level);

    boolean isExpanded(Object itemId);

    CaptionMode getCaptionMode();
    void setCaptionMode(CaptionMode captionMode);

    String getCaptionProperty();
    void setCaptionProperty(String captionProperty);

    String getHierarchyProperty();
    void setDatasource(HierarchicalDatasource datasource);

    @Override
    HierarchicalDatasource getDatasource();

    /**
     * Assign action to be executed on double click inside a tree node.
     */
    void setItemClickAction(Action action);
    Action getItemClickAction();

    void setStyleProvider(@Nullable StyleProvider<E> styleProvider);

    void addStyleProvider(StyleProvider<E> styleProvider);

    void removeStyleProvider(StyleProvider<E> styleProvider);

    interface StyleProvider<E extends Entity> {
        @Nullable
        String getStyleName(E entity);
    }

    /**
     * Set the icon provider for the tree.
     */
    void setIconProvider(IconProvider<E> iconProvider);

    /**
     * Repaint UI representation of the tree including style providers and icon providers without refreshing the tree data.
     */
    void repaint();
}