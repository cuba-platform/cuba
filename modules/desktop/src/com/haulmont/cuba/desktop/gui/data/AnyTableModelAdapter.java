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

package com.haulmont.cuba.desktop.gui.data;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;

/**
 */
public interface AnyTableModelAdapter extends TableModel {

    void sort(List<? extends RowSorter.SortKey> sortKeys);

    Entity getItem(int rowIndex);

    int getRowIndex(Entity entity);

    void addGeneratedColumn(Table.Column column);

    void removeGeneratedColumn(Table.Column column);

    boolean isGeneratedColumn(Table.Column column);

    boolean hasGeneratedColumns();

    void addColumn(Table.Column column);

    void removeColumn(Table.Column column);

    Table.Column getColumn(int index);

    void addChangeListener(DataChangeListener changeListener);

    void removeChangeListener(DataChangeListener changeListener);

    interface DataChangeListener {
        void beforeChange(boolean structureChanged);

        void afterChange(boolean structureChanged);

        void dataSorted();
    }
}