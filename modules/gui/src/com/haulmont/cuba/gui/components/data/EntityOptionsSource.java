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

package com.haulmont.cuba.gui.components.data;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Map;

public interface EntityOptionsSource<E extends Entity> extends OptionsSource<E> {
    MetaClass getEntityMetaClass();

    void setSelectedItem(E item);

    /**
     * @return true if the underlying collection contains an item with the specified ID
     */
    boolean containsItem(E item);

    /**
     * Update an item in the collection if it is already there.
     */
    void updateItem(E item);

    /**
     * Refreshes the source moving it to the {@link BindingState#ACTIVE} state
     */
    void refresh();

    /**
     * Refreshes the source passing specified parameters to the query.
     * <p>These parameters may be referenced in the query text by "custom$" prefix.</p>
     *
     * @param parameters parameters map
     */
    void refresh(Map<String, Object> parameters);
}