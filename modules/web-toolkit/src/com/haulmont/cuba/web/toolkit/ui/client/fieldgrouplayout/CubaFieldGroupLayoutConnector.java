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

import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroupLayout;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.toolkit.ui.client.gridlayout.CubaGridLayoutConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.shared.ui.Connect;

@Connect(CubaFieldGroupLayout.class)
public class CubaFieldGroupLayoutConnector extends CubaGridLayoutConnector {

    protected boolean needUpdateCaptionSizes = false;

    @Override
    public CubaFieldGroupLayoutWidget getWidget() {
        return (CubaFieldGroupLayoutWidget) super.getWidget();
    }

    @Override
    public CubaFieldGroupLayoutState getState() {
        return (CubaFieldGroupLayoutState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        // try layout now to smooth layout process
        if (needUpdateCaptionSizes) {
            updateCaptionSizes();
        }

        needUpdateCaptionSizes = false;
    }

    @Override
    public void layoutHorizontally() {
        if (needUpdateCaptionSizes) {
            updateCaptionSizes();
        }

        super.layoutHorizontally();

        needUpdateCaptionSizes = false;
    }

    @Override
    protected void setDefaultCaptionParameters(CubaCaptionWidget widget) {
        super.setDefaultCaptionParameters(widget);

        if (getState().useInlineCaption) {
            widget.setCaptionPlacedAfterComponentByDefault(false);
        }
    }

    @Override
    public void updateCaption(ComponentConnector childConnector) {
        super.updateCaption(childConnector);

        if (getState().useInlineCaption) {
            needUpdateCaptionSizes = true;

            // always relayout after caption changes
            getLayoutManager().setNeedsLayout(this);
        }
    }

    public void updateCaptionSizes() {
        int index = 0;
        for (VGridLayout.Cell[] column : getWidget().getCellMatrix()) {
            if (column != null) {
                updateCaptionSizes(index, column);
            }
            index++;
        }
    }

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        // construct component cells with known caption alignment
        getWidget().useInlineCaption = getState().useInlineCaption;

        super.onConnectorHierarchyChange(event);
    }

    protected void updateCaptionSizes(int index, VGridLayout.Cell[] column) {
        // reset indicators width
        resetIndicatorsWidth(column);

        int fixedCaptionWidth = -1;
        if (getState().fieldCaptionWidth > 0) {
            fixedCaptionWidth = getState().fieldCaptionWidth;
        }
        int[] columnCaptionWidth = getState().columnFieldCaptionWidth;
        if (columnCaptionWidth != null
                && index < columnCaptionWidth.length
                && columnCaptionWidth[index] > 0) {
            fixedCaptionWidth = columnCaptionWidth[index];
        }

        int maxIndicatorsWidth = 0;
        int maxCaptionWidth = 0;

        // calculate max widths
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                if (fixedCaptionWidth == -1) {
                    maxCaptionWidth = Math.max(maxCaptionWidth, cell.slot.getCaption().getRenderedWidth());
                }

                maxIndicatorsWidth = Math.max(maxIndicatorsWidth, ((CubaFieldGroupLayoutComponentSlot) cell.slot).getIndicatorsWidth());
            }
        }

        if (fixedCaptionWidth > 0) {
            maxCaptionWidth = fixedCaptionWidth;
        }

        // apply max widths
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                cell.slot.getCaption().setWidth(maxCaptionWidth + "px");

                if (cell.slot.isRelativeWidth()) {
                    ((CubaFieldGroupLayoutComponentSlot) cell.slot).setIndicatorsWidth(maxIndicatorsWidth + "px");
                }
            }
        }
    }

    protected void resetIndicatorsWidth(VGridLayout.Cell[] column) {
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                cell.slot.getCaption().getElement().getStyle().clearWidth();

                ((CubaFieldGroupLayoutComponentSlot) cell.slot).resetIndicatorsWidth();
            }
        }
    }

    protected boolean isCaptionInlineApplicable(VGridLayout.Cell cell) {
        return cell.slot != null && cell.slot.getCaption() != null;
    }
}