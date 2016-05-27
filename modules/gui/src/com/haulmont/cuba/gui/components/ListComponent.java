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

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.Collection;

public interface ListComponent<E extends Entity> extends Component, Component.BelongToFrame, Component.ActionsHolder {

    boolean isMultiSelect();
    void setMultiSelect(boolean multiselect);

    @Nullable
    E getSingleSelected();

    Set<E> getSelected();

    void setSelected(@Nullable E item);
    void setSelected(Collection<E> items);

    CollectionDatasource getDatasource();

    /**
     * Allows to set icons for particular rows in the table.
     *
     * @param <E> entity class
     */
    interface IconProvider<E extends Entity> {
        /**
         * Called by {@link Table} to get an icon to be shown for a row.
         *
         * @param entity an entity instance represented by the current row
         * @return icon name or null to show no icon
         */
        @Nullable
        String getItemIcon(E entity);
    }

    void refresh();
}