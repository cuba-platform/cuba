package com.haulmont.cuba.web.widgets.client.treegrid;

import com.haulmont.cuba.web.widgets.CubaTreeGrid;
import com.haulmont.cuba.web.widgets.client.grid.CubaGridServerRpc;
import com.haulmont.cuba.web.widgets.client.grid.CubsGridClientRpc;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.treegrid.TreeGridConnector;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonObject;

import java.util.List;

@Connect(CubaTreeGrid.class)
public class CubaTreeGridConnector extends TreeGridConnector {

    public CubaTreeGridConnector() {
        registerRpc(CubsGridClientRpc.class, () -> getWidget().updateFooterVisibility());
    }

    @Override
    public CubaTreeGridWidget getWidget() {
        return (CubaTreeGridWidget) super.getWidget();
    }

    @Override
    public CubaTreeGridState getState() {
        return (CubaTreeGridState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent event) {
        super.onStateChanged(event);

        if (event.hasPropertyChanged("showEmptyState")) {
            getWidget().showEmptyState(getState().showEmptyState);
            if (getState().showEmptyState) {
                // as emptyState can be recreated set all messages
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
            List<Grid.Column<?, JsonObject>> currentColumns = getWidget().getColumns();

            for (Grid.Column<?, JsonObject> column : currentColumns) {
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
