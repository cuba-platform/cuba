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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.horizontallayout;

import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VHorizontalLayout;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDHorizontalLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

/**
 * Client side implementation for {@link DDHorizontalLayout}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDHorizontalLayout extends VHorizontalLayout
        implements VHasDragMode,
        VDDHasDropHandler<VDDHorizontalLayoutDropHandler>, DragStartListener,
        VHasDragFilter, VHasDragImageReferenceSupport, VHasIframeShims, VHasGrabFilter, VHasDragCaptionProvider {

    public static final String OVER = "v-ddorderedlayout-over";
    public static final String OVER_SPACED = OVER + "-spaced";

    private Widget currentlyEmphasised;

    private VDDHorizontalLayoutDropHandler dropHandler;

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    // Value delegated from state
    private double cellLeftRightDropRatio = DDHorizontalLayoutState.DEFAULT_HORIZONTAL_DROP_RATIO;

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    public VDDHorizontalLayout() {
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
     * Removes any applies drag and drop style applied by emphasis()
     */
    protected void deEmphasis() {
        if (currentlyEmphasised != null) {
            // Universal over style
            setStyleName(currentlyEmphasised.getElement(), OVER,
                    false);
            setStyleName(currentlyEmphasised.getElement(), OVER_SPACED,
                    false);

            // Horizontal styles
            setStyleName(currentlyEmphasised.getElement(), OVER + "-"
                    + HorizontalDropLocation.LEFT.toString().toLowerCase(),
                    false);
            setStyleName(currentlyEmphasised.getElement(), OVER + "-"
                    + HorizontalDropLocation.CENTER.toString().toLowerCase(),
                    false);
            setStyleName(currentlyEmphasised.getElement(), OVER + "-"
                    + HorizontalDropLocation.RIGHT.toString().toLowerCase(),
                    false);

            currentlyEmphasised = null;
        }
    }

    /**
     * Returns the horizontal location within the cell when hoovering over the
     * cell. By default the cell is devided into three parts: left,center,right
     * with the ratios 10%,80%,10%;
     * 
     * @param container
     *            The widget container
     * @param event
     *            The drag event
     * @return The horizontal drop location
     */
    protected HorizontalDropLocation getHorizontalDropLocation(Widget container,
            VDragEvent event) {
        return VDragDropUtil.getHorizontalDropLocation(container.getElement(),
                Util.getTouchOrMouseClientX(event.getCurrentGwtEvent()),
                cellLeftRightDropRatio);
    }

    /**
     * A hook for extended components to post process the the drop before it is
     * sent to the server. Useful if you don't want to override the whole drop
     * handler.
     */
    protected boolean postDropHook(VDragEvent drag) {
        // Extended classes can add content here...
        return true;
    }

    /**
     * A hook for extended components to post process the the enter event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postEnterHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * A hook for extended components to post process the the leave event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postLeaveHook(VDragEvent drag) {
        // Extended classes can add content here...
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
    @Override
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    /**
     * Updates the drop details while dragging. This is needed to ensure client
     * side criterias can validate the drop location.
     * 
     * @param widget
     *            The container which we are hovering over
     * @param event
     *            The drag event
     */
    protected void updateDragDetails(Widget widget, VDragEvent event) {
        if (widget == null) {
            return;
        }

        /*
         * The horizontal position within the cell{
         */
        event.getDropDetails().put(
                Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION,
                getHorizontalDropLocation(widget, event));

        /*
         * The index over which the drag is. Can be used by a client side
         * criteria to verify that a drag is over a certain index.
         */
        int index = -1;
        if (widget instanceof Slot) {
            WidgetCollection captionsAndSlots = getChildren();
            index = VDragDropUtil.findSlotIndex(captionsAndSlots,
                    (Slot) widget);
        }

        event.getDropDetails().put(Constants.DROP_DETAIL_TO, index);

        // Add mouse event details
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event.getCurrentGwtEvent(),
                        getElement());
        event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                details.serialize());
    }

    /**
     * Empasises the drop location of the component when hovering over a
     * ĆhildComponentContainer. Passing null as the container removes any
     * previous emphasis.
     * 
     * @param container
     *            The container which we are hovering over
     * @param event
     *            The drag event
     */
    protected void emphasis(Widget container, VDragEvent event) {

        // Remove emphasis from previous hovers
        deEmphasis();

        // validate container
        if (container == null
                || !getElement().isOrHasChild(container.getElement())) {
            return;
        }

        currentlyEmphasised = container;

        HorizontalDropLocation location = null;

        // Add drop location specific style
        if (currentlyEmphasised != this) {
            location = getHorizontalDropLocation(container, event);

        } else {
            location = HorizontalDropLocation.CENTER;
        }

        setStyleName(currentlyEmphasised.getElement(), OVER, true);
        setStyleName(currentlyEmphasised.getElement(),
                OVER + "-" + location.toString().toLowerCase(), true);
    }

    /**
     * Returns the current drag mode which determines how the drag is visualized
     */
    public LayoutDragMode getDragMode() {
        return ddMouseHandler.getDragMode();
    }

    /**
     * Creates a drop handler if one does not already exist and updates it from
     * the details received from the server.
     * 
     * @param dropHandler
     *            The drop handler
     */
    public void setDropHandler(VDDHorizontalLayoutDropHandler dropHandler) {
        this.dropHandler = dropHandler;
    }

    /**
     * Get the drop handler attached to the Layout
     */
    public VDDHorizontalLayoutDropHandler getDropHandler() {
        return dropHandler;
    }

    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    IframeCoverUtility getIframeCoverUtility() {
        return iframeCoverUtility;
    }

    public double getCellLeftRightDropRatio() {
        return cellLeftRightDropRatio;
    }

    public void setCellLeftRightDropRatio(float cellLeftRightDropRatio) {
        this.cellLeftRightDropRatio = cellLeftRightDropRatio;
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        this.dragFilter = filter;
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
