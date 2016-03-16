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

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.haulmont.cuba.web.toolkit.ui.client.gridlayout.CubaGridLayoutWidget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;

/**
 */
public class CubaFieldGroupLayoutWidget extends CubaGridLayoutWidget {

    public static final String CLASSNAME = "cuba-fieldgrouplayout";

    protected boolean useInlineCaption = true;

    public CubaFieldGroupLayoutWidget() {
        setStyleName(CLASSNAME);
    }

    public class CubaFieldGroupLayoutCell extends CubaGridLayoutCell {
        public CubaFieldGroupLayoutCell(int row, int col) {
            super(row, col);
        }

        @Override
        protected ComponentConnectorLayoutSlot createComponentConnectorLayoutSlot(ComponentConnector component) {
            CubaFieldGroupLayoutComponentSlot slot =
                    new CubaFieldGroupLayoutComponentSlot(CubaFieldGroupLayoutWidget.CLASSNAME, component, getConnector());
            slot.setCaptionInline(useInlineCaption);
            return slot;
        }
    }

    @Override
    public Cell createNewCell(int row, int col) {
        // CAUTION copied from VGridLayout.createNewCell(int row, int col)
        Cell cell = new CubaFieldGroupLayoutCell(row, col);
        cells[col][row] = cell;
        return cell;
    }
}