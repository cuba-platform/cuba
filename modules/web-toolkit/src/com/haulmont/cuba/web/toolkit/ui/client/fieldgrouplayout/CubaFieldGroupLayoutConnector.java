/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.fieldgrouplayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.CubaFieldGroupLayout;
import com.haulmont.cuba.web.toolkit.ui.client.caption.CubaCaptionWidget;
import com.haulmont.cuba.web.toolkit.ui.client.gridlayout.CubaGridLayoutConnector;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.shared.ui.Connect;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaFieldGroupLayout.class)
public class CubaFieldGroupLayoutConnector extends CubaGridLayoutConnector {

    protected boolean needUpdateCaptionSizes = false;

    @Override
    public CubaFieldGroupLayoutWidget getWidget() {
        return (CubaFieldGroupLayoutWidget) super.getWidget();
    }

    @Override
    protected Widget createWidget() {
        return GWT.create(CubaFieldGroupLayoutWidget.class);
    }

    @Override
    public CubaFieldGroupLayoutState getState() {
        return (CubaFieldGroupLayoutState) super.getState();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (needUpdateCaptionSizes) {
            updateCaptionSizes();
        }

        needUpdateCaptionSizes = false;
    }

    @Override
    protected void setDefaultCaptionParameters(CubaCaptionWidget widget) {
        super.setDefaultCaptionParameters(widget);

        widget.setCaptionPlacedAfterComponentByDefault(false);
    }

    @Override
    public void updateCaption(ComponentConnector childConnector) {
        super.updateCaption(childConnector);
        needUpdateCaptionSizes = true;
    }

    public void updateCaptionSizes() {
        for (VGridLayout.Cell[] column : getWidget().getCellMatrix()) {
            if (column != null) {
                updateCaptionSizes(column);
            }
        }
    }

    protected void updateCaptionSizes(VGridLayout.Cell[] column) {
        int maxCaptionWidth = 0;
        int maxIndicatorsWidth = 0;

        // reset indicators width
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                cell.slot.getCaption().getElement().getStyle().clearWidth();

                ((CubaFieldGroupLayoutComponentSlot) cell.slot).resetIndicatorsWidth();
            }
        }

        // calculate max widths
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                maxCaptionWidth = Math.max(maxCaptionWidth, cell.slot.getCaption().getRenderedWidth());

                maxIndicatorsWidth = Math.max(maxIndicatorsWidth, ((CubaFieldGroupLayoutComponentSlot) cell.slot).getIndicatorsWidth());
            }
        }

        // apply max widths
        for (VGridLayout.Cell cell : column) {
            if (cell != null && isCaptionInlineApplicable(cell)) {
                cell.slot.getCaption().setWidth(maxCaptionWidth + "px");

                ((CubaFieldGroupLayoutComponentSlot) cell.slot).setInidcatorsWidth(maxIndicatorsWidth + "px");
            }
        }
    }

    protected boolean isCaptionInlineApplicable(VGridLayout.Cell cell) {
        return cell.slot != null && cell.slot.getCaption() != null;
    }
}