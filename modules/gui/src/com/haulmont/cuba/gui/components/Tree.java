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

import com.google.common.reflect.TypeToken;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;

import javax.annotation.Nullable;
import java.util.function.Function;

public interface Tree<E extends Entity> extends ListComponent<E>, Component.Editable, HasButtonsPanel,
                                                Component.HasCaption, Component.HasIcon, LookupComponent,
                                                Component.Focusable {

    String NAME = "tree";

    static <T extends Entity> TypeToken<Tree<T>> of(Class<T> itemClass) {
        return new TypeToken<Tree<T>>() {};
    }

    void expandTree();
    void expand(Object itemId);

    void collapseTree();
    void collapse(Object itemId);

    /**
     * Expand tree including specified level
     *
     * @param level level of Tree nodes to expand, if passed level = 1 then root items will be expanded
     * @throws IllegalArgumentException if level &lt; 1
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

    /**
     * Sets a single style provider for tree items.
     *
     * @param styleProvider a style provider to set
     */
    void setStyleProvider(@Nullable Function<? super E, String> styleProvider);

    /**
     * Add a style provider for tree items.
     *
     * @param styleProvider a style provider to add
     */
    void addStyleProvider(Function<? super E, String> styleProvider);

    /**
     * Removes a previously added style provider.
     *
     * @param styleProvider a style provider to remove
     */
    void removeStyleProvider(Function<? super E, String> styleProvider);

    /**
     * Allows to define different styles for tree items.
     */
    @Deprecated
    interface StyleProvider<E extends Entity> extends Function<E, String> {
        @Override
        default String apply(E entity) {
            return getStyleName(entity);
        }

        /**
         * Called by {@link Tree} to get a style for item. <br>
         * All unhandled exceptions from StyleProvider in Web components by default are logged with ERROR level
         * and not shown to users.
         *
         * @param entity an entity instance represented by the current item
         * @return style name or null to apply the default
         */
        String getStyleName(E entity);
    }

    /**
     * Set the icon provider for the tree.
     */
    void setIconProvider(Function<? super E, String> iconProvider);

    /**
     * Repaint UI representation of the tree including style providers and icon providers without refreshing the tree data.
     */
    void repaint();

    /**
     * Set action to be executed on Enter key press.
     */
    void setEnterPressAction(Action action);
    /**
     * @return Enter key press action.
     */
    Action getEnterPressAction();

    void setMultiSelect(boolean multiselect);

    /**
     * @deprecated refresh datasource instead
     */
    @Deprecated
    void refresh();
}