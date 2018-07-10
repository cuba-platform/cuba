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

package com.haulmont.cuba.web.widgets.client.grid.selection;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.haulmont.cuba.web.widgets.grid.CubaMultiSelectionModel;
import com.haulmont.cuba.web.widgets.client.Tools;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.connectors.grid.MultiSelectionModelConnector;
import com.vaadin.client.renderers.Renderer;
import com.vaadin.client.widget.grid.CellReference;
import com.vaadin.client.widget.grid.DataAvailableEvent;
import com.vaadin.client.widget.grid.DataAvailableHandler;
import com.vaadin.client.widget.grid.events.BodyClickHandler;
import com.vaadin.client.widget.grid.events.GridClickEvent;
import com.vaadin.client.widget.grid.selection.SelectionModel;
import com.vaadin.client.widgets.Grid;
import com.vaadin.shared.Range;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonObject;

@Connect(CubaMultiSelectionModel.class)
public class CubaMultiSelectionModelConnector extends MultiSelectionModelConnector {

    protected HandlerRegistration clickHandler;

    @Override
    protected MultiSelectionModel createSelectionModel() {
        return Tools.isUseSimpleMultiselectForTouchDevice()
                ? super.createSelectionModel()
                : new MultiSelectionModel() {
            @Override
            public Renderer<Boolean> getRenderer() {
                return null;
            }
        };
    }

    @Override
    protected void extend(ServerConnector target) {
        super.extend(target);

        if (!Tools.isUseSimpleMultiselectForTouchDevice()) {
            if (clickHandler != null) {
                clickHandler.removeHandler();
                clickHandler = null;
            }

            Grid<JsonObject> grid = getGrid();
            BodyClickHandler handler = createBodyClickHandler(grid);
            clickHandler = grid.addBodyClickHandler(handler);
        }
    }

    protected MultiSelectionBodyClickHandler createBodyClickHandler(Grid<JsonObject> grid) {
        return new MultiSelectionBodyClickHandler(grid);
    }

    @Override
    public void onUnregister() {
        if (clickHandler != null) {
            clickHandler.removeHandler();
            clickHandler = null;
        }

        super.onUnregister();
    }

    public class MultiSelectionBodyClickHandler implements BodyClickHandler {

        protected Grid<JsonObject> grid;
        protected HandlerRegistration handler;
        protected int previous = -1;

        public MultiSelectionBodyClickHandler(Grid<JsonObject> grid) {
            this.grid = grid;
        }

        @Override
        public void onClick(GridClickEvent event) {
            SelectionModel<JsonObject> selectionModel = grid.getSelectionModel();
            if (!(selectionModel instanceof MultiSelectionModel)) {
                return;
            }

            //noinspection unchecked
            MultiSelectionModel model = (MultiSelectionModel) selectionModel;
            CellReference<JsonObject> cell = grid.getEventCell();

            if (!event.isShiftKeyDown() || previous < 0) {
                handleCtrlClick(model, cell, event);
                previous = cell.getRowIndex();
                return;
            }

            // This works on the premise that grid fires the data available event to
            // any newly added handlers.
            boolean ctrlOrMeta = event.isControlKeyDown() || event.isMetaKeyDown();
            handler = grid.addDataAvailableHandler(new ShiftSelector(cell, model, ctrlOrMeta));
        }

        protected void handleCtrlClick(MultiSelectionModel model,
                                       CellReference<JsonObject> cell, GridClickEvent event) {
            NativeEvent e = event.getNativeEvent();
            JsonObject row = cell.getRow();
            if (!e.getCtrlKey() && !e.getMetaKey()) {
                model.deselectAll();
            }

            if (model.isSelected(row)) {
                model.deselect(row);
            } else {
                model.select(row);
            }
        }

        protected final class ShiftSelector implements DataAvailableHandler {
            protected final CellReference<JsonObject> cell;
            protected final MultiSelectionModel model;
            protected boolean ctrlOrMeta;

            private ShiftSelector(CellReference<JsonObject> cell,
                                  MultiSelectionModel model, boolean ctrlOrMeta) {
                this.cell = cell;
                this.model = model;
                this.ctrlOrMeta = ctrlOrMeta;
            }

            @Override
            public void onDataAvailable(DataAvailableEvent event) {
                int current = cell.getRowIndex();
                int min = Math.min(current, previous);
                int max = Math.max(current, previous);

                if (!ctrlOrMeta) {
                    model.deselectAll();
                }

                Range dataAvailable = event.getAvailableRows();

                Range selected = Range.between(min, max + 1);
                Range[] partition = selected.partitionWith(dataAvailable);

                for (int i = partition[1].getStart(); i < partition[1].getEnd(); ++i) {
                    model.select(grid.getDataSource().getRow(i));
                }

                if (handler != null) {
                    handler.removeHandler();
                }
            }
        }
    }
}
