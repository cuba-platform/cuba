/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets.grid;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.components.grid.MultiSelectionModelImpl;

import java.util.Set;

public class CubaMultiSelectionModel<T> extends MultiSelectionModelImpl<T> {
    @Override
    protected boolean shouldRemoveAddedItem(T item, DataProvider<T, ?> dataProvider, Set<T> removedItems) {
        return item == null
                || super.shouldRemoveAddedItem(item, dataProvider, removedItems);
    }
}
