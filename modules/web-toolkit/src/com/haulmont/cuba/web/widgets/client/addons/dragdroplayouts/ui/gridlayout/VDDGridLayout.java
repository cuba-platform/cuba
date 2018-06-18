/*
 * Copyright 2015 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.gridlayout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VGridLayout;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDGridLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

import java.util.Map;

/**
 * Client side implementation for {@link DDGridLayout}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDGridLayout extends VGridLayout implements VHasDragMode,
        VDDHasDropHandler<VDDGridLayoutDropHandler>, DragStartListener,
        VHasDragFilter, VHasIframeShims, VHasDragImageReferenceSupport, VHasGrabFilter, VHasDragCaptionProvider {

    public static final String CLASSNAME = "v-ddgridlayout";
    public static final String OVER = CLASSNAME + "-over";

    private VDDGridLayoutDropHandler dropHandler;

    final HTML dragShadow = new HTML("");

    protected ApplicationConnection client;

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private float cellLeftRightDropRatio;

    private float cellTopBottomDropRatio;

    // The drag mouse handler which handles the creation of the transferable
    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    public VDDGridLayout() {
        super();
    }

    @Override
    public void setDragCaptionProvider(VDragCaptionProvider dragCaption) {
        this.dragCaption = dragCaption;
    }

    @Override
    public VDragCaptionProvider getDragCaptionProvider() {
        return dragCaption;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        ddMouseHandler.addDragStartListener(this);
        setDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        ddMouseHandler.removeDragStartListener(this);
        ddMouseHandler.updateDragMode(LayoutDragMode.NONE);
        iframeCoverUtility.setIframeCoversEnabled(false, getElement(),
                LayoutDragMode.NONE);
    }

    /**
     * Returns the drop handler used when the user drops a component over the
     * Grid Layout
     */
    public VDDGridLayoutDropHandler getDropHandler() {
        return dropHandler;
    }

    public void setDropHandler(VDDGridLayoutDropHandler handler) {
        dropHandler = handler;
    }

    /**
     * Updates the drop details while dragging
     * 
     * @param event
     *            The drag event
     */
    public void updateDragDetails(VDragEvent event) {
        CellDetails cd = getCellDetails(event);
        if (cd != null) {
            Map<String, Object> ddetails = event.getDropDetails();

            // Add row
            ddetails.put(Constants.DROP_DETAIL_ROW, Integer.valueOf(cd.row));

            // Add column
            ddetails.put(Constants.DROP_DETAIL_COLUMN,
                    Integer.valueOf(cd.column));

            // Add horizontal position
            HorizontalDropLocation hl = getHorizontalDropLocation(cd, event);
            ddetails.put(Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION, hl);

            // Add vertical position
            VerticalDropLocation vl = getVerticalDropLocation(cd, event);
            ddetails.put(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION, vl);

            // Check if the cell we are hovering over has content
            Cell cell = getCell(cd.row, cd.column);
            ddetails.put(Constants.DROP_DETAIL_EMPTY_CELL, cell != null);

            // Get class information from child
            if (cell != null && cell.slot != null) {
                ComponentConnector child = cell.slot.getChild();
                if (child != null) {
                    String className = child.getWidget().getClass().getName();
                    ddetails.put(Constants.DROP_DETAIL_OVER_CLASS, className);
                } else {
                    ddetails.put(Constants.DROP_DETAIL_OVER_CLASS,
                            VDDGridLayout.this.getClass().getName());
                }
            } else {
                ddetails.put(Constants.DROP_DETAIL_OVER_CLASS,
                        VDDGridLayout.this.getClass().getName());
            }

            // Add mouse event details
            MouseEventDetails details = MouseEventDetailsBuilder
                    .buildMouseEventDetails(event.getCurrentGwtEvent(),
                            getElement());
            event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                    details.serialize());
        }
    }

    /**
     * Returns the horizontal drop location
     * 
     * @param cell
     *            The cell details
     * @param event
     *            The drag event
     * @return
     */
    protected HorizontalDropLocation getHorizontalDropLocation(CellDetails cell,
            VDragEvent event) {

        // Get the horizontal location
        HorizontalDropLocation hdetail;
        int x = Util.getTouchOrMouseClientX(event.getCurrentGwtEvent())
                - getAbsoluteLeft() - cell.x;

        assert(x >= 0 && x <= cell.width);

        if (x < cell.width * cellLeftRightDropRatio) {
            hdetail = HorizontalDropLocation.LEFT;
        } else if (x < cell.width * (1.0 - cellLeftRightDropRatio)) {
            hdetail = HorizontalDropLocation.CENTER;
        } else {
            hdetail = HorizontalDropLocation.RIGHT;
        }
        return hdetail;
    }

    /**
     * Returns the vertical drop location
     * 
     * @param cell
     *            The cell details
     * @param event
     *            The drag event
     * @return
     */
    protected VerticalDropLocation getVerticalDropLocation(CellDetails cell,
            VDragEvent event) {

        // Get the vertical location
        VerticalDropLocation vdetail;
        int y = Util.getTouchOrMouseClientY(event.getCurrentGwtEvent())
                - getAbsoluteTop() - cell.y;

        assert(y >= 0 && y <= cell.height);

        if (y < cell.height * cellTopBottomDropRatio) {
            vdetail = VerticalDropLocation.TOP;
        } else if (y < cell.height * (1.0 - cellTopBottomDropRatio)) {
            vdetail = VerticalDropLocation.MIDDLE;
        } else {
            vdetail = VerticalDropLocation.BOTTOM;
        }
        return vdetail;
    }

    /**
     * Emphasizes a component container when user is hovering a dragged
     * component over the container.
     * 
     * @param cell
     *            The container
     * @param event
     */
    protected void emphasis(CellDetails cell, VDragEvent event) {

        Style shadowStyle = dragShadow.getElement().getStyle();
        shadowStyle.setPosition(Position.ABSOLUTE);
        shadowStyle.setWidth(cell.width, Unit.PX);
        shadowStyle.setHeight(cell.height, Unit.PX);
        shadowStyle.setLeft(cell.x, Unit.PX);
        shadowStyle.setTop(cell.y, Unit.PX);

        // Remove any existing empasis
        deEmphasis();

        // Ensure we are not dragging ourself into ourself
        ComponentConnector draggedConnector = (ComponentConnector) event
                .getTransferable()
                .getData(Constants.TRANSFERABLE_DETAIL_COMPONENT);

        if (draggedConnector != null
                && draggedConnector.getWidget() == VDDGridLayout.this) {
            return;
        }

        HorizontalDropLocation hl = getHorizontalDropLocation(cell, event);
        VerticalDropLocation vl = getVerticalDropLocation(cell, event);

        // Apply over style
        setStyleName(dragShadow.getElement(), OVER, true);

        // Add vertical location dependent style
        setStyleName(dragShadow.getElement(),
                OVER + "-" + vl.toString().toLowerCase(), true);

        // Add horizontal location dependent style
        setStyleName(dragShadow.getElement(),
                OVER + "-" + hl.toString().toLowerCase(), true);

    }

    /**
     * Removes any emphasis previously set by emphasis
     */
    protected void deEmphasis() {

        setStyleName(dragShadow.getElement(), OVER, false);

        // Horizontal styles
        setStyleName(dragShadow.getElement(),
                OVER + "-"
                        + HorizontalDropLocation.LEFT.toString().toLowerCase(),
                false);
        setStyleName(dragShadow.getElement(), OVER + "-"
                + HorizontalDropLocation.CENTER.toString().toLowerCase(),
                false);
        setStyleName(dragShadow.getElement(),
                OVER + "-"
                        + HorizontalDropLocation.RIGHT.toString().toLowerCase(),
                false);

        // Vertical styles
        setStyleName(dragShadow.getElement(),
                OVER + "-" + VerticalDropLocation.TOP.toString().toLowerCase(),
                false);
        setStyleName(dragShadow.getElement(),
                OVER + "-"
                        + VerticalDropLocation.MIDDLE.toString().toLowerCase(),
                false);
        setStyleName(dragShadow.getElement(),
                OVER + "-"
                        + VerticalDropLocation.BOTTOM.toString().toLowerCase(),
                false);

    }

    public LayoutDragMode getDragMode() {
        return ddMouseHandler.getDragMode();
    }

    /**
     * A hook for extended components to post process the the drop before it is
     * sent to the server. Useful if you don't want to override the whole drop
     * handler.
     */
    protected boolean postDropHook(VDragEvent drag) {
        // Extended classes can add content here...
        remove(dragShadow);
        return true;
    }

    /**
     * A hook for extended components to post process the the enter event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postEnterHook(VDragEvent drag) {
        // Extended classes can add content here...
        insert(dragShadow, getElement(), 0, true);
    }

    /**
     * A hook for extended components to post process the the leave event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postLeaveHook(VDragEvent drag) {
        // Extended classes can add content here...
        remove(dragShadow);
    }

    /**
     * A hook for extended components to post process the the over event. Useful
     * if you don't want to override the whole drophandler.
     */
    protected void postOverHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * Can be used to listen to drag start events, must return true for the drag
     * to commence. Return false to interrupt the drag:
     */
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    /**
     * A helper class returned by getCellDetailsByCoordinates() which contains
     * positional and size data of the cell.
     */
    protected class CellDetails {
        public int row = -1;
        public int column = -1;
        public int x = -1;
        public int y = -1;
        public int width = -1;
        public int height = -1;
    }

    CellDetails getCellDetails(VDragEvent event) {
        int x = Util.getTouchOrMouseClientX(event.getCurrentGwtEvent())
                - getAbsoluteLeft();
        int y = Util.getTouchOrMouseClientY(event.getCurrentGwtEvent())
                - getAbsoluteTop();
        return getCellDetailsByCoordinates(x, y);
    }

    /**
     * Returns details of the cell under the given position
     * 
     * @param x
     *            The x-coordinate
     * @param y
     *            The y-coordinate
     * @return The details of the cell under the coordinate
     */
    private CellDetails getCellDetailsByCoordinates(int x, int y) {

        CellDetails cd = new CellDetails();
        int[] columnWidths = getColumnWidths();
        int[] rowHeights = getRowHeights();

        // Get column and x coordinate
        int temp = 0;
        for (int col = 0; col < columnWidths.length; col++) {
            if (x >= temp && x <= temp + columnWidths[col]) {
                cd.column = col;
                cd.x = temp;
                cd.width = columnWidths[col];
                break;
            }
            temp += columnWidths[col] + getHorizontalSpacing();
        }

        // get row
        temp = 0;
        for (int row = 0; row < rowHeights.length; row++) {
            if (y >= temp && y <= temp + rowHeights[row]) {
                cd.row = row;
                cd.y = temp;
                cd.height = rowHeights[row];
                break;
            }
            temp += rowHeights[row] + getVerticalSpacing();
        }

        // Sanity check
        if (cd.row == -1 || cd.column == -1 || cd.width == -1
                || cd.height == -1) {
            return null;
        }

        return cd;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VHasDragFilter#
     * getDragFilter ()
     */
    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    IframeCoverUtility getIframeCoverUtility() {
        return iframeCoverUtility;
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        this.dragFilter = filter;
    }

    public float getCellLeftRightDropRatio() {
        return cellLeftRightDropRatio;
    }

    public void setCellLeftRightDropRatio(float cellLeftRightDropRatio) {
        this.cellLeftRightDropRatio = cellLeftRightDropRatio;
    }

    public float getCellTopBottomDropRatio() {
        return cellTopBottomDropRatio;
    }

    public void setCellTopBottomDropRatio(float cellTopBottomDropRatio) {
        this.cellTopBottomDropRatio = cellTopBottomDropRatio;
    }

    @Override
    public void iframeShimsEnabled(boolean enabled) {
        iframeCovers = enabled;
        iframeCoverUtility.setIframeCoversEnabled(enabled, getElement(), mode);
    }

    @Override
    public boolean isIframeShimsEnabled() {
        return iframeCovers;
    }

    @Override
    public void setDragMode(LayoutDragMode mode) {
        this.mode = mode;
        ddMouseHandler.updateDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    public void setDragImageProvider(VDragImageProvider provider) {
        ddMouseHandler.setDragImageProvider(provider);
    }

    protected final VLayoutDragDropMouseHandler getMouseHandler() {
        return ddMouseHandler;
    }

    @Override
    public VGrabFilter getGrabFilter() {
        return grabFilter;
    }

    @Override
    public void setGrabFilter(VGrabFilter grabFilter) {
        this.grabFilter = grabFilter;
    }
}
