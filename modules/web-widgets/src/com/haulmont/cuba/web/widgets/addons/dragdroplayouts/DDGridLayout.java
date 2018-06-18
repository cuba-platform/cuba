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
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts;

import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.TargetDetailsImpl;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.LegacyComponent;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.gridlayout.DDGridLayoutState;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces.*;

import java.util.Map;

/**
 * Grid layout with drag and drop support
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
@SuppressWarnings("serial")
public class DDGridLayout extends GridLayout
        implements LayoutDragSource, DropTarget, ShimSupport, LegacyComponent,
        DragFilterSupport, DragImageReferenceSupport, DragGrabFilterSupport, HasDragCaptionProvider {

    private DropHandler dropHandler;

    // A filter for dragging components.
    private DragFilter dragFilter = DragFilter.ALL;

    private DragGrabFilter dragGrabFilter;

    private DragImageProvider dragImageProvider;

    private DragCaptionProvider dragCaptionProvider;

    @Override
    public DragGrabFilter getDragGrabFilter() {
        return dragGrabFilter;
    }

    @Override
    public void setDragGrabFilter(DragGrabFilter dragGrabFilter) {
        this.dragGrabFilter = dragGrabFilter;
    }

    @Override
    public void setDragCaptionProvider(DragCaptionProvider provider) {
        this.dragCaptionProvider = provider;
    }

    @Override
    public DragCaptionProvider getDragCaptionProvider() {
        return dragCaptionProvider;
    }

    /**
     * Target details for a drop event
     */
    public class GridLayoutTargetDetails extends TargetDetailsImpl {

        private Component over;

        private int row = -1;

        private int column = -1;

        protected GridLayoutTargetDetails(Map<String, Object> rawDropData) {
            super(rawDropData, DDGridLayout.this);

            if (getData(Constants.DROP_DETAIL_ROW) != null) {
                row = Integer
                        .valueOf(getData(Constants.DROP_DETAIL_ROW).toString());
            } else {
                row = -1;
            }

            if (getData(Constants.DROP_DETAIL_COLUMN) != null) {
                column = Integer.valueOf(
                        getData(Constants.DROP_DETAIL_COLUMN).toString());
            } else {
                column = -1;
            }

            if (row != -1 && column != -1) {
                over = getComponent(column, row);
            }

            if (over == null) {
                over = DDGridLayout.this;
            }
        }

        /**
         * Returns the component over which the dragged component was dropped.
         * Returns NULL if no component was under the dragged component
         * 
         * @return
         */
        public Component getOverComponent() {
            return over;
        }

        /**
         * Over which row was the component dropped
         * 
         * @return The index of the row over which the component was dropped
         */
        public int getOverRow() {
            return row;
        }

        /**
         * Over which column was the component dropped
         * 
         * @return The index of the column over which the component was dropped
         */
        public int getOverColumn() {
            return column;
        }

        /**
         * Returns the horizontal location within the cell the component was
         * dropped
         * 
         * @return
         */
        public HorizontalDropLocation getHorizontalDropLocation() {
            return HorizontalDropLocation.valueOf(
                    getData(Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION)
                            .toString());
        }

        /**
         * Returns the vertical location within the cell the component was
         * dropped
         * 
         * @return
         */
        public VerticalDropLocation getVerticalDropLocation() {
            return VerticalDropLocation.valueOf(
                    getData(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION)
                            .toString());
        }

        /**
         * Was the dropped component dropped in an empty cell
         * 
         * @return
         */
        public boolean overEmptyCell() {
            return Boolean.valueOf(
                    getData(Constants.DROP_DETAIL_EMPTY_CELL).toString());
        }

        /**
         * Some details about the mouse event
         * 
         * @return details about the actual event that caused the event details.
         *         Practically mouse move or mouse up.
         */
        public MouseEventDetails getMouseEvent() {
            return MouseEventDetails.deSerialize(
                    getData(Constants.DROP_DETAIL_MOUSE_EVENT).toString());
        }
    }

    /**
     * Contains the transferable details when dragging from a GridLayout.
     */
    public class GridLayoutTransferable extends LayoutBoundTransferable {

        /**
         * Constructor
         * 
         * @param sourceComponent
         *            The source layout from where the component was dragged
         * @param rawVariables
         *            The drag details
         */
        public GridLayoutTransferable(Component sourceComponent,
                Map<String, Object> rawVariables) {
            super(sourceComponent, rawVariables);
        }

        /**
         * The row from where the component was dragged
         * 
         * @return The row index
         */
        public int getSourceRow() {
            return Integer
                    .valueOf(getData(Constants.DROP_DETAIL_ROW).toString());
        }

        /**
         * The column from where the component was dragged
         * 
         * @return The column index
         */
        public int getSourceColumn() {
            return Integer
                    .valueOf(getData(Constants.DROP_DETAIL_COLUMN).toString());
        }
    }

    /**
     * Constructor for grid of given size (number of cells). Note that grid's
     * final size depends on the items that are added into the grid. Grid grows
     * if you add components outside the grid's area.
     * 
     * @param columns
     *            Number of columns in the grid.
     * @param rows
     *            Number of rows in the grid.
     */
    public DDGridLayout(int columns, int rows) {
        super(columns, rows);
    }

    /**
     * Constructor for grid of given size (number of cells). Note that grid's
     * final size depends on the items that are added into the grid. Grid grows
     * if you add components outside the grid's area.
     * 
     * @param columns
     *            Number of columns in the grid.
     * @param rows
     *            Number of rows in the grid.
     * @param children
     *            Child components
     */
    public DDGridLayout(int columns, int rows, Component... children) {
        super(columns, rows, children);
    }

    /**
     * Constructs an empty grid layout that is extended as needed.
     */
    public DDGridLayout() {
        super();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (dropHandler != null && isEnabled()) {
            dropHandler.getAcceptCriterion().paint(target);
        }
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // To be compatible with Designer drag & drop
    }

    /**
     * {@inheritDoc}
     */
    public DropHandler getDropHandler() {
        return dropHandler;
    }

    /**
     * {@inheritDoc}
     */
    public void setDropHandler(DropHandler dropHandler) {
        DDUtil.verifyHandlerType(this, dropHandler);
        if (dropHandler != this.dropHandler) {
            this.dropHandler = dropHandler;
            markAsDirty();
        }
    }

    /**
     * {@inheritDoc}
     */
    public LayoutDragMode getDragMode() {
        return getState().ddState.dragMode;
    }

    /**
     * {@inheritDoc}
     */
    public void setDragMode(LayoutDragMode mode) {
        getState().ddState.dragMode = mode;
    }

    /**
     * {@inheritDoc}
     */
    public TargetDetails translateDropTargetDetails(
            Map<String, Object> clientVariables) {
        return new GridLayoutTargetDetails(clientVariables);
    }

    /**
     * {@inheritDoc}
     */
    public Transferable getTransferable(Map<String, Object> rawVariables) {
        return new GridLayoutTransferable(this, rawVariables);
    }

    /**
     * Sets the ratio which determines how a cell is divided into drop zones.
     * The ratio is measured from the left and right borders. For example,
     * setting the ratio to 0.3 will divide the drop zone in three equal parts
     * (left,middle,right). Setting the ratio to 0.5 will disable dropping in
     * the middle and setting it to 0 will disable dropping at the sides.
     * 
     * @param ratio
     *            A ratio between 0 and 0.5. Default is 0.2
     */
    public void setComponentHorizontalDropRatio(float ratio) {
        if (ratio != getState().cellLeftRightDropRatio) {
            if (ratio >= 0 && ratio <= 0.5) {
                getState().cellLeftRightDropRatio = ratio;
            } else {
                throw new IllegalArgumentException(
                        "Ratio must be between 0 and 0.5");
            }
        }
    }

    /**
     * Sets the ratio which determines how a cell is divided into drop zones.
     * The ratio is measured from the top and bottom borders. For example,
     * setting the ratio to 0.3 will divide the drop zone in three equal parts
     * (top,center,bottom). Setting the ratio to 0.5 will disable dropping in
     * the center and setting it to 0 will disable dropping at the sides.
     * 
     * @param ratio
     *            A ratio between 0 and 0.5. Default is 0.2
     */
    public void setComponentVerticalDropRatio(float ratio) {
        if (ratio != getState().cellTopBottomDropRatio) {
            if (ratio >= 0 && ratio <= 0.5) {
                getState().cellTopBottomDropRatio = ratio;
            } else {
                throw new IllegalArgumentException(
                        "Ratio must be between 0 and 0.5");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setShim(boolean shim) {
        getState().ddState.iframeShims = shim;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isShimmed() {
        return getState().ddState.iframeShims;
    }

    /**
     * {@inheritDoc}
     */
    public DragFilter getDragFilter() {
        return dragFilter;
    }

    /**
     * {@inheritDoc}
     */
    public void setDragFilter(DragFilter dragFilter) {
        this.dragFilter = dragFilter;
    }

    @Override
    public DDGridLayoutState getState() {
        return (DDGridLayoutState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        DDUtil.onBeforeClientResponse(this, getState());
    }

    @Override
    public void setDragImageProvider(DragImageProvider provider) {
        this.dragImageProvider = provider;
        markAsDirty();
    }

    @Override
    public DragImageProvider getDragImageProvider() {
        return this.dragImageProvider;
    }

}
