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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A layout where the components are laid out on a grid using cell coordinates.
 */
public interface GridLayout extends ComponentContainer, HasSpacing, HasMargin, Component.BelongToFrame,
        Component.HasIcon, Component.HasCaption, HasContextHelp, LayoutClickNotifier, ShortcutNotifier,
        HasHtmlCaption, HasHtmlDescription {

    String NAME = "grid";

    float getColumnExpandRatio(int col);
    void setColumnExpandRatio(int col, float ratio);

    float getRowExpandRatio(int row);
    void setRowExpandRatio(int row, float ratio);

    void add(Component component, int col, int row);
    void add(Component component, int col, int row, int col2, int row2);

    int getRows();
    void setRows(int rows);

    int getColumns();
    void setColumns(int columns);

    /**
     * Gets the Component at given row and column.
     *
     * @param columnIndex the column index, starting from 0 for the leftmost column.
     * @param rowIndex    the row index, starting from 0 for the topmost row.
     * @return Component in given cell or null if empty
     */
    @Nullable
    Component getComponent(int columnIndex, int rowIndex);

    /**
     * Gets the Component at given row and column.
     *
     * @param columnIndex the column index, starting from 0 for the leftmost column.
     * @param rowIndex    the row index, starting from 0 for the topmost row.
     * @return component. Throws exception if not found.
     */
    @Nonnull
    default Component getComponentNN(int columnIndex, int rowIndex) {
        Component component = getComponent(columnIndex, rowIndex);
        if (component == null) {
            throw new IllegalArgumentException(
                    String.format("Not found component with col %s and row %s", columnIndex, rowIndex)
            );
        }

        return component;
    }

    /**
     * Returns information about the area where given component is laid in the
     * GridLayout.
     *
     * @param component the component whose area information is requested.
     * @return an Area object that contains information how component is laid in the grid
     */
    @Nullable
    Area getComponentArea(Component component);

    /**
     * Defines a rectangular area of cells in a GridLayout.
     */
    class Area {
        private Component component;

        private int column1;
        private int row1;
        private int column2;
        private int row2;

        public Area(Component component, int column1, int row1, int column2, int row2) {
            this.component = component;
            this.column1 = column1;
            this.row1 = row1;
            this.column2 = column2;
            this.row2 = row2;
        }

        public Component getComponent() {
            return component;
        }

        public int getColumn1() {
            return column1;
        }

        public int getRow1() {
            return row1;
        }

        public int getColumn2() {
            return column2;
        }

        public int getRow2() {
            return row2;
        }

        @Override
        public String toString() {
            return "Area{" +
                    "component=@" + System.identityHashCode(component) +
                    ", column1=" + column1 +
                    ", row1=" + row1 +
                    ", column2=" + column2 +
                    ", row2=" + row2 +
                    '}';
        }
    }
}