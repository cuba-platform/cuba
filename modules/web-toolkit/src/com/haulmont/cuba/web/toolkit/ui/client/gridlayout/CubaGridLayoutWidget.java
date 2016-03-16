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

package com.haulmont.cuba.web.toolkit.ui.client.gridlayout;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;

/**
 */
public class CubaGridLayoutWidget extends VGridLayout {

    public VGridLayout.Cell[][] getCellMatrix() {
        return cells;
    }

    public class CubaGridLayoutCell extends Cell {

        public CubaGridLayoutCell(int row, int col) {
            super(row, col);
        }

        @Override
        protected ComponentConnectorLayoutSlot createComponentConnectorLayoutSlot(ComponentConnector component) {
            return new CubaGridLayoutSlot(VGridLayout.CLASSNAME, component, getConnector());
        }
    }

    @Override
    public Cell createNewCell(int row, int col) {
        // CAUTION copied from VGridLayout.createNewCell(int row, int col)
        Cell cell = new CubaGridLayoutCell(row, col);
        cells[col][row] = cell;
        return cell;
    }
}