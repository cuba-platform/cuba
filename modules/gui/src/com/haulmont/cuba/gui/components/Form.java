/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.components.data.HasValueSourceProvider;

import java.util.Collection;

/**
 * A multi-column form component.
 */
public interface Form extends Component, Component.BelongToFrame, Component.HasCaption, Component.HasIcon,
        ComponentContainer, Component.Editable, EditableChangeNotifier, HasContextHelp, ChildEditableController,
        HasValueSourceProvider, HasHtmlCaption, HasHtmlDescription {

    String NAME = "form";

    /**
     * Adds a component to the first column.
     *
     * @param childComponent a component to add
     */
    @Override
    void add(Component childComponent);

    /**
     * Sequentially adds components to the first column.
     *
     * @param childComponents components to add
     */
    @Override
    default void add(Component... childComponents) {
        ComponentContainer.super.add(childComponents);
    }

    /**
     * Adds a component to the column with the given index.
     *
     * @param childComponent a component to add
     * @param column         a columns index
     */
    void add(Component childComponent, int column);

    /**
     * Adds a component to a column with the given index with the given span.
     *
     * @param childComponent a component to add
     * @param column         a columns index
     * @param colSpan        a number of columns a component should span
     * @param rowSpan        a number of rows a component should span
     */
    void add(Component childComponent, int column, int colSpan, int rowSpan);

    /**
     * Adds a component to a column with the given index to the given position.
     *
     * @param childComponent a component to add
     * @param column         a columns index
     * @param row            a row index
     */
    void add(Component childComponent, int column, int row);

    /**
     * Adds a component to a column with the given index to the given position with the given span.
     *
     * @param childComponent a component to add
     * @param column         a columns index
     * @param row            a row index
     * @param colSpan        a number of columns a component should span
     * @param rowSpan        a number of rows a component should span
     */
    void add(Component childComponent, int column, int row, int colSpan, int rowSpan);

    /**
     * @param column a column index
     * @return a collection of components directly owned by a column with a given index
     */
    Collection<Component> getComponents(int column);

    /**
     * @param column a columns index
     * @param row    a row index
     * @return a component placed in a column with a given index in a given position
     */
    Component getComponent(int column, int row);

    /**
     * @return position of component captions
     */
    CaptionPosition getCaptionPosition();

    /**
     * Sets position of component captions.
     *
     * @param captionAlignment component captions position
     */
    void setCaptionPosition(CaptionPosition captionAlignment);

    /**
     * @return columns fixed caption width
     */
    int getChildrenCaptionWidth();

    /**
     * Sets fixed captions width for all columns. Set -1 to use auto size.
     *
     * @param width fixed caption width
     */
    void setChildrenCaptionWidth(int width);

    /**
     * Returns fixed caption width for column with a given index.
     *
     * @param column a column index
     * @return fixed caption width for column with a given index
     */
    int getChildrenCaptionWidth(int column);

    /**
     * Set fixed captions width for column with a given index. Set -1 to use auto size.
     *
     * @param column a column index
     * @param width  fixed caption width for column with a given index
     */
    void setChildrenCaptionWidth(int column, int width);

    /**
     * @return alignment of child component captions
     */
    CaptionAlignment getChildrenCaptionAlignment();

    /**
     * Sets alignment of child component captions in all columns.
     * <p>
     * Applicable only when captions position is {@link CaptionPosition#LEFT}.
     *
     * @param alignment captions alignment
     */
    void setChildrenCaptionAlignment(CaptionAlignment alignment);

    /**
     * Returns alignment of child component captions for a column with the given index.
     *
     * @param column a column index to get caption
     * @return alignment of child component captions for a column with the given index
     */
    CaptionAlignment getChildrenCaptionAlignment(int column);

    /**
     * Sets alignment of child component captions for a column with the given index.
     *
     * @param column    a column index to set caption
     * @param alignment alignment of child component captions for a column with the given index
     */
    void setChildrenCaptionAlignment(int column, CaptionAlignment alignment);

    /**
     * @return number of columns in the Form
     */
    int getColumns();

    /**
     * Sets the number of columns in the Form. The column count can not be
     * reduced if there are any components that would be outside of the shrunk Form.
     *
     * @param columns the new number of columns in the Form
     */
    void setColumns(int columns);

    /**
     * Caption position of form child components.
     */
    enum CaptionPosition {
        /**
         * Component captions will be placed in a separate column on the left side of the components.
         */
        LEFT,

        /**
         * Component captions will be placed above the components.
         */
        TOP
    }

    /**
     * Caption alignment of form child components.
     */
    enum CaptionAlignment {
        /**
         * Component captions will be aligned to the left.
         */
        LEFT,

        /**
         * Component captions will be aligned to the right.
         */
        RIGHT
    }
}
