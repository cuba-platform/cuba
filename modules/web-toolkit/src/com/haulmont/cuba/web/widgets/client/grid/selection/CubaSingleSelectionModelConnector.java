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
import com.haulmont.cuba.web.widgets.CubaSingleSelectionModel;
import com.haulmont.cuba.web.widgets.client.Tools;
import com.vaadin.client.ServerConnector;
import com.vaadin.v7.client.connectors.SingleSelectionModelConnector;
import com.vaadin.v7.client.widget.grid.events.BodyClickHandler;
import com.vaadin.v7.client.widget.grid.selection.ClickSelectHandler;
import com.vaadin.v7.client.widgets.Grid;
import com.vaadin.shared.ui.Connect;
import elemental.json.JsonObject;

@Connect(CubaSingleSelectionModel.class)
public class CubaSingleSelectionModelConnector extends SingleSelectionModelConnector {

    @Override
    protected void extend(ServerConnector target) {
        super.extend(target);
    }

    @Override
    protected ClickSelectHandler<JsonObject> createClickSelectHandler() {
        return Tools.isUseSimpleMultiselectForTouchDevice()
                ? super.createClickSelectHandler()
                : new CubaClickSelectHandler(getGrid());
    }

    protected class CubaClickSelectHandler
            extends com.vaadin.v7.client.widget.grid.selection.ClickSelectHandler<JsonObject> {

        public CubaClickSelectHandler(Grid<JsonObject> grid) {
            super(grid);
        }

        @Override
        protected BodyClickHandler createBodyClickHandler(Grid<JsonObject> grid) {
            return event -> {
                JsonObject row = grid.getEventCell().getRow();
                NativeEvent e = event.getNativeEvent();

                if (!e.getCtrlKey() && !e.getMetaKey()) {
                    if (!grid.isSelected(row)) {
                        grid.select(row);
                    }
                } else {
                    if (!grid.isSelected(row)) {
                        grid.select(row);
                    } else if (isDeselectAllowed()) {
                        grid.deselect(row);
                    }
                }
            };
        }
    }
}
