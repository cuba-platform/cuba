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

package com.haulmont.cuba.web.widgets;

import com.vaadin.v7.ui.Grid;

public class CubaSingleSelectionModel extends Grid.SingleSelectionModel {

    @Override
    protected boolean select(Object itemId, boolean refresh) {
        // We want to prevent exception when selecting an item
        // right after removing from the container (triggered from
        // a client side i.e. refresh is false)
        // See https://github.com/vaadin/framework/issues/9911
        if (!refresh && itemId != null) {
            if (!getParentGrid().getContainerDataSource().containsId(itemId)) {
                return false;
            }
        }
        return super.select(itemId, refresh);
    }
}
