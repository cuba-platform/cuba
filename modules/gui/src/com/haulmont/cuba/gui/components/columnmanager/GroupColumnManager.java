/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.components.columnmanager;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.GroupTable;

/**
 * Class provides methods for group column manipulation.
 *
 * @see Table.Column
 * @see GroupTable
 */
public interface GroupColumnManager {

    /**
     * Enables the column be a grouping column.
     *
     * @param column  column instance
     * @param allowed allowed option
     */
    void setColumnGroupAllowed(Table.Column column, boolean allowed);

    /**
     * Enables the column be a grouping column.
     *
     * @param columnId column id
     * @param allowed  allowed option
     */
    void setColumnGroupAllowed(String columnId, boolean allowed);
}
