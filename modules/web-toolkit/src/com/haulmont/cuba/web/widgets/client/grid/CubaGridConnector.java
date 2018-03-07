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
 */

package com.haulmont.cuba.web.widgets.client.grid;

import com.haulmont.cuba.web.widgets.CubaGrid;
import com.vaadin.v7.client.connectors.GridConnector;
import com.vaadin.v7.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;
import com.vaadin.v7.shared.ui.grid.GridColumnState;
import elemental.json.JsonObject;

@Connect(CubaGrid.class)
public class CubaGridConnector extends GridConnector {

    @Override
    public CubaGridWidget getWidget() {
        return (CubaGridWidget) super.getWidget();
    }

    @Override
    public CubaGridState getState() {
        return (CubaGridState) super.getState();
    }

    @Override
    protected void preUpdateColumnFromState(Grid.Column<?, JsonObject> column, GridColumnState columnState) {
        if (getState().columnIds != null && getState().columnIds.containsKey(columnState.id)) {
            getWidget().addColumnId(column, getState().columnIds.get(columnState.id));
        }
    }
}