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

package com.haulmont.cuba.gui.components.data.meta;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.TableItems;

import javax.annotation.Nullable;

/**
 * Interface for table data components bound to {@link Entity} type.
 *
 * @param <E> entity type
 */
public interface EntityTableItems<E extends Entity> extends TableItems<E>, EntityDataUnit {
    /**
     * @return the current item contained in the source
     */
    @Nullable
    E getSelectedItem();

    /**
     * Set current item in the source.
     *
     * @param item the item to set
     */
    void setSelectedItem(@Nullable E item);
}