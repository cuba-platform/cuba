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
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.LegacyComponent;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.csslayout.DDCssLayoutState;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces.*;

import java.util.Map;

/**
 * CssLayout with drag and drop support
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.7.0
 * 
 */
@SuppressWarnings("serial")
public class DDCssLayout extends CssLayout
        implements LayoutDragSource, DropTarget, ShimSupport, LegacyComponent,
        DragFilterSupport, DragImageReferenceSupport, DragGrabFilterSupport, HasDragCaptionProvider {

    // Drop handler which handles dd drop events
    private DropHandler dropHandler;

    // A filter for dragging components.
    private DragFilter dragFilter = DragFilter.ALL;

    private DragGrabFilter dragGrabFilter;

    private DragImageProvider dragImageProvider;

    private DragCaptionProvider dragCaptionProvider;

    /**
     * Construct a new Css layout
     */
    public DDCssLayout() {
        super();
    }

    /**
     * Construct a new css layout with child components
     * 
     * @param components
     *            the children of the css layout
     */
    public DDCssLayout(Component... components) {
        super(components);
    }

    @Override
    public void setDragCaptionProvider(DragCaptionProvider provider) {
        this.dragCaptionProvider = provider;
    }

    @Override
    public DragCaptionProvider getDragCaptionProvider() {
        return dragCaptionProvider;
    }

    @Override
    public DragGrabFilter getDragGrabFilter() {
        return dragGrabFilter;
    }

    @Override
    public void setDragGrabFilter(DragGrabFilter dragGrabFilter) {
        this.dragGrabFilter = dragGrabFilter;
    }

    /**
     * Target details for dropping on a absolute layout.
     */
    public class CssLayoutTargetDetails extends TargetDetailsImpl {

        private int index = -1;

        private Component over;

        /**
         * Constructor
         * 
         * @param rawDropData
         *            The drop data
         */
        protected CssLayoutTargetDetails(Map<String, Object> rawDropData) {
            super(rawDropData, DDCssLayout.this);

            // Get over which component (if any) the drop was made and the
            // index of it
            if (getData(Constants.DROP_DETAIL_TO) != null) {
                index = Integer
                        .valueOf(getData(Constants.DROP_DETAIL_TO).toString());
                if (index >= 0 && index < components.size()) {
                    over = components.get(index);
                }
            } else {
                index = components.size();
            }

            // Was the drop over no specific cell
            if (over == null) {
                over = DDCssLayout.this;
            }
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

        /**
         * Get the horizontal position of the dropped component within the
         * underlying cell.
         * 
         * @return The drop location
         */
        public HorizontalDropLocation getHorizontalDropLocation() {
            return HorizontalDropLocation.valueOf((String) getData(
                    Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION));
        }

        /**
         * Get the horizontal position of the dropped component within the
         * underlying cell.
         * 
         * @return The drop location
         */
        public VerticalDropLocation getVerticalDropLocation() {
            return VerticalDropLocation.valueOf((String) getData(
                    Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION));
        }

        /**
         * The index over which the drop was made. If the drop was not made over
         * any component then it returns -1.
         * 
         * @return The index of the component or -1 if over no component.
         */
        public int getOverIndex() {
            return index;
        }

        /**
         * The component over which the drop was made.
         * 
         * @return Null if the drop was not over a component, else the component
         */
        public Component getOverComponent() {
            return over;
        }
    }

    /**
     * {@inheritDoc}
     */
    public Transferable getTransferable(Map<String, Object> rawVariables) {
        return new LayoutBoundTransferable(this, rawVariables);
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
     * gets the drop handler which handles component drops on the layout
     */
    public DropHandler getDropHandler() {
        return dropHandler;
    }

    /**
     * Sets the drop handler which handles component drops on the layout
     * 
     * @param dropHandler
     *            The drop handler to set
     */
    public void setDropHandler(DropHandler dropHandler) {
        DDUtil.verifyHandlerType(this, dropHandler);
        if (this.dropHandler != dropHandler) {
            this.dropHandler = dropHandler;
            markAsDirty();
        }
    }

    /**
     * {@inheritDoc}
     */
    public TargetDetails translateDropTargetDetails(
            Map<String, Object> clientVariables) {
        return new CssLayoutTargetDetails(clientVariables);
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
    public DragFilter getDragFilter() {
        return dragFilter;
    }

    /**
     * {@inheritDoc}
     */
    public void setDragFilter(DragFilter dragFilter) {
        this.dragFilter = dragFilter;
    }

    /**
     * {@inheritDoc}
     * 
     */
    public void paintContent(PaintTarget target) throws PaintException {
        if (dropHandler != null && isEnabled()) {
            dropHandler.getAcceptCriterion().paint(target);
        }
    }

    @Override
    public DDCssLayoutState getState() {
        return (DDCssLayoutState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        DDUtil.onBeforeClientResponse(this, getState());
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // TODO Auto-generated method stub
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
