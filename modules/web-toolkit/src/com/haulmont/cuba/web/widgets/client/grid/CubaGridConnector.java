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
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.connectors.grid.GridConnector;
import com.vaadin.client.widgets.Grid.Column;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonObject;

import java.util.List;

@Connect(CubaGrid.class)
public class CubaGridConnector extends GridConnector {

    public CubaGridConnector() {
        registerRpc(CubsGridClientRpc.class, () -> getWidget().updateFooterVisibility());
    }

    @Override
    public CubaGridWidget getWidget() {
        return (CubaGridWidget) super.getWidget();
    }

    @Override
    public CubaGridState getState() {
        return (CubaGridState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent event) {
        super.onStateChanged(event);

        if (event.hasPropertyChanged("showEmptyState")) {
            getWidget().showEmptyState(getState().showEmptyState);
            if (getState().showEmptyState) {
                // as emptyState element can be recreated set all messages
                getWidget().getEmptyState().setMessage(getState().emptyStateMessage);
                getWidget().getEmptyState().setLinkMessage(getState().emptyStateLinkMessage);
                getWidget().getEmptyState().setLinkClickHandler(getWidget().emptyStateLinkClickHandler);
            }
        }
        if (event.hasPropertyChanged("emptyStateMessage")) {
            if (getWidget().getEmptyState() != null) {
                getWidget().getEmptyState().setMessage(getState().emptyStateMessage);
            }
        }
        if (event.hasPropertyChanged("emptyStateLinkMessage")) {
            if (getWidget().getEmptyState() != null) {
                getWidget().getEmptyState().setLinkMessage(getState().emptyStateLinkMessage);
            }
        }

        if (event.hasPropertyChanged("selectAllLabel")) {
            getWidget().setSelectAllLabel(getState().selectAllLabel);
        }

        if (event.hasPropertyChanged("deselectAllLabel")) {
            getWidget().setDeselectAllLabel(getState().deselectAllLabel);
        }
    }

    @Override
    protected void updateColumns() {
        super.updateColumns();

        if (getWidget().getColumnIds() != null) {
            getWidget().setColumnIds(null);
        }

        if (getState().columnIds != null) {
            List<Column<?, JsonObject>> currentColumns = getWidget().getColumns();

            for (Column<?, JsonObject> column : currentColumns) {
                String id = getColumnId(column);
                if (getState().columnIds.containsKey(id)) {
                    getWidget().addColumnId(column, getState().columnIds.get(id));
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        getWidget().emptyStateLinkClickHandler = () -> getRpcProxy(CubaGridServerRpc.class).onEmptyStateLinkClick();
    }
}