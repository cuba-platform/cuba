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

package com.haulmont.cuba.web.gui.components.table;

import com.haulmont.cuba.gui.components.Table;

import javax.annotation.Nullable;

public abstract class CustomColumnGenerator implements com.vaadin.v7.ui.Table.ColumnGenerator {

    protected Table.ColumnGenerator columnGenerator;

    // Used for properly removing column from table
    protected Table.Column associatedRuntimeColumn;

    protected CustomColumnGenerator(Table.ColumnGenerator columnGenerator, @Nullable Table.Column associatedRuntimeColumn) {
        this.columnGenerator = columnGenerator;
        this.associatedRuntimeColumn = associatedRuntimeColumn;
    }

    public Table.Column getAssociatedRuntimeColumn() {
        return associatedRuntimeColumn;
    }

    public Table.ColumnGenerator getColumnGenerator() {
        return columnGenerator;
    }
}