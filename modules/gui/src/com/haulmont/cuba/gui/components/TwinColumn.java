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

import java.util.Collection;

/**
 * A component with two lists: left list for available options, right list for selected values.
 *
 * @param <V> value and options type for the component
 */
public interface TwinColumn<V> extends OptionsField<Collection<V>, V> {

    String NAME = "twinColumn";

    /**
     * Gets the number of columns for the component.
     *
     * @see #setWidth(String)
     * @deprecated "Columns" does not reflect the exact number of characters that will be displayed. Use
     * {@link #getWidth()} instead.
     */
    @Deprecated
    int getColumns();

    /**
     * Sets the width of the component so that it displays approximately the given number of letters in each of the
     * two selects.
     *
     * @param columns the number of columns to set.
     * @deprecated "Columns" does not reflect the exact number of characters that will be displayed. Use
     * {@link #setWidth(String)} instead.
     */
    @Deprecated
    void setColumns(int columns);

    /**
     * @return the number of visible rows
     */
    int getRows();

    /**
     * Sets the number of visible rows.
     *
     * @param rows number of visible rows
     */
    void setRows(int rows);

    /**
     * @param styleProvider style provider
     * @deprecated use {@link #setOptionStyleProvider(OptionStyleProvider)} instead
     */
    @Deprecated
    default void setStyleProvider(StyleProvider styleProvider) {
        if (styleProvider == null) {
            setOptionStyleProvider(null);
        } else {
            setOptionStyleProvider((item, selected) -> {
                if (item instanceof Entity) {
                    return styleProvider.getStyleName((Entity) item, ((Entity) item).getId(), selected);
                } else {
                    return null;
                }
            });
        }
    }

    /**
     * Enables "Add all" and "Remove all" buttons.
     *
     * @param enabled true if buttons should be enabled
     */
    void setAddAllBtnEnabled(boolean enabled);

    /**
     * @return true if buttons are enabled
     */
    boolean isAddAllBtnEnabled();

    /**
     * Set caption for the left column.
     *
     * @param leftColumnCaption
     */
    void setLeftColumnCaption(String leftColumnCaption);
    /**
     * Return caption of the left column.
     *
     * @return caption text or null if not set.
     */
    String getLeftColumnCaption();

    /**
     * Set caption for the right column.
     *
     * @param rightColumnCaption
     */
    void setRightColumnCaption(String rightColumnCaption);
    /**
     * Return caption of the right column.
     *
     * @return caption text or null if not set.
     */
    String getRightColumnCaption();

    /**
     * Sets option style provider. It defines a style for each value.
     *
     * @param optionStyleProvider option style provider function
     */
    void setOptionStyleProvider(OptionStyleProvider<V> optionStyleProvider);

    /**
     * @return option style provider function
     */
    OptionStyleProvider<V> getOptionStyleProvider();

    /**
     * @deprecated use {@link #setOptionStyleProvider(OptionStyleProvider)}
     */
    @Deprecated
    interface StyleProvider {
        @Deprecated
        String getStyleName(Entity item, Object property, boolean selected);
    }

    /**
     * @param <V> option type
     */
    interface OptionStyleProvider<V> {

        /**
         * Handles style name for the item.
         *
         * @param item     item to create style name
         * @param selected is item selected
         * @return style name for the item
         */
        String getStyleName(V item, boolean selected);
    }
}