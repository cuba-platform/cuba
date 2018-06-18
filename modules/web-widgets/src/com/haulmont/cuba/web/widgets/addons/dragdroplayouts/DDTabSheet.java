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
import com.vaadin.ui.Component;
import com.vaadin.ui.LegacyComponent;
import com.vaadin.ui.TabSheet;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.tabsheet.DDTabSheetState;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Tabsheet with drag and drop support
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
@SuppressWarnings("serial")
public class DDTabSheet extends TabSheet
        implements LayoutDragSource, DropTarget, ShimSupport, LegacyComponent,
        DragFilterSupport, DragImageReferenceSupport, DragGrabFilterSupport, HasDragCaptionProvider {

    /**
     * The drop handler which handles dropped components in the layout.
     */
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

    public class TabSheetTargetDetails extends TargetDetailsImpl {

        private Component over;

        private int index = -1;

        protected TabSheetTargetDetails(Map<String, Object> rawDropData) {
            super(rawDropData, DDTabSheet.this);

            // Get over which component (if any) the drop was made and the
            // index of it
            if (rawDropData.get(Constants.DROP_DETAIL_TO) != null) {
                Object to = rawDropData.get(Constants.DROP_DETAIL_TO);
                index = Integer.valueOf(to.toString());
            }

            if (index >= 0 && index < getComponentCount()) {
                Iterator<Component> iter = getComponentIterator();
                int counter = 0;
                while (iter.hasNext()) {
                    over = iter.next();
                    if (counter == index) {
                        break;
                    }
                    counter++;
                }
            } else {
                over = DDTabSheet.this;
            }
        }

        /**
         * The component over which the drop was made.
         * 
         * @return Null if the drop was not over a component, else the component
         */
        public Component getOverComponent() {
            return over;
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
         * Some details about the mouse event
         * 
         * @return details about the actual event that caused the event details.
         *         Practically mouse move or mouse up.
         */
        public MouseEventDetails getMouseEvent() {
            return MouseEventDetails.deSerialize(
                    (String) getData(Constants.DROP_DETAIL_MOUSE_EVENT));
        }

        /**
         * Get the horizontal position of the dropped component within the
         * underlying cell.
         * 
         * @return The drop location
         */
        public HorizontalDropLocation getDropLocation() {
            if (getData(
                    Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION) != null) {
                return HorizontalDropLocation.valueOf((String) getData(
                        Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION));
            }
            return null;
        }
    }

    /**
     * Creates a new tabsheet
     */
    public DDTabSheet() {
        super();
    }

    /**
     * Creates a new tabsheet with tabs
     * 
     * @param tabs
     *            the tabs to add to the tabsheet
     */
    public DDTabSheet(Component... tabs) {
        super(tabs);
    }

    public Transferable getTransferable(Map<String, Object> rawVariables) {
        if (rawVariables.get(Constants.TRANSFERABLE_DETAIL_INDEX) != null) {
            // We dragged a tab, substitute component with tab content
            int index = Integer.parseInt(rawVariables
                    .get(Constants.TRANSFERABLE_DETAIL_INDEX).toString());
            Iterator<Component> iter = getComponentIterator();
            int counter = 0;
            Component c = null;
            while (iter.hasNext()) {
                c = iter.next();
                if (counter == index) {
                    break;
                }
                counter++;
            }

            rawVariables.put(Constants.TRANSFERABLE_DETAIL_COMPONENT, c);
        } else if (rawVariables.get("component") == null) {
            rawVariables.put(Constants.TRANSFERABLE_DETAIL_COMPONENT,
                    DDTabSheet.this);
        }

        return new LayoutBoundTransferable(this, rawVariables);
    }

    /**
     * Sets the current handler which handles dropped components on the layout.
     * By setting a drop handler dropping components on the layout is enabled.
     * By setting the dropHandler to null dropping is disabled.
     * 
     * @param dropHandler
     *            The drop handler to handle drop events or null to disable
     *            dropping
     */
    public void setDropHandler(DropHandler dropHandler) {
        DDUtil.verifyHandlerType(this, dropHandler);
        if (this.dropHandler != dropHandler) {
            this.dropHandler = dropHandler;
            markAsDirty();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.event.dd.DropTarget#getDropHandler()
     */
    public DropHandler getDropHandler() {
        return dropHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.event.dd.DropTarget#translateDropTargetDetails(java.util.Map)
     */
    public TargetDetails translateDropTargetDetails(
            Map<String, Object> clientVariables) {
        return new TabSheetTargetDetails(clientVariables);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.jasoft.dragdroplayouts.interfaces.LayoutDragSource#getDragMode ()
     */
    public LayoutDragMode getDragMode() {
        return getState().ddState.dragMode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.jasoft.dragdroplayouts.interfaces.LayoutDragSource#setDragMode
     * (com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode)
     */
    public void setDragMode(LayoutDragMode mode) {
        getState().ddState.dragMode = mode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.ui.AbstractOrderedLayout#translateDropTargetDetails(java.util
     * .Map)
     */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (dropHandler != null && isEnabled()) {
            dropHandler.getAcceptCriterion().paint(target);
        }
    }

    /**
     * Sets the ratio which determines how a tab is divided into drop zones. The
     * ratio is measured from the left and right borders. For example, setting
     * the ratio to 0.3 will divide the drop zone in three equal parts
     * (left,middle,right). Setting the ratio to 0.5 will disable dropping in
     * the middle and setting it to 0 will disable dropping at the sides.
     * 
     * @param ratio
     *            A ratio between 0 and 0.5. Default is 0.2
     */
    public void setComponentHorizontalDropRatio(float ratio) {
        if (getState().tabLeftRightDropRatio != ratio) {
            if (ratio >= 0 && ratio <= 0.5) {
                getState().tabLeftRightDropRatio = ratio;
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
    public DDTabSheetState getState() {
        return (DDTabSheetState) super.getState();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        DDUtil.onBeforeClientResponse(this, getState());
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // FIXME Remove when drag&drop is no longer legacy
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
