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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.formlayout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VFormLayout;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.annotations.DelegateToWidget;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDFormLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

/**
 * Client side implementation for {@link DDFormLayout}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDFormLayout extends VFormLayout implements VHasDragMode,
        VDDHasDropHandler<VDDFormLayoutDropHandler>, DragStartListener,
        VHasDragFilter, VHasIframeShims, VHasDragImageReferenceSupport, VHasGrabFilter, VHasDragCaptionProvider {

    private Element currentlyEmphasised;

    private float cellTopBottomDropRatio;

    static final int COLUMN_CAPTION = 0;
    static final int COLUMN_ERRORFLAG = 1;
    static final int COLUMN_WIDGET = 2;

    public static final String OVER = "v-ddformlayout-over";

    public static final String OVER_SPACED = OVER + "-spaced";

    private VDDFormLayoutDropHandler dropHandler;

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    protected ApplicationConnection client;

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    public VDDFormLayout() {
        super();
        table = (VFormLayoutTable) getWidget();
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

    // The drag mouse handler which handles the creation of the transferable
    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    /**
     * Removes any applies drag and drop style applied by emphasis()
     */
    protected void deEmphasis() {
        if (currentlyEmphasised != null) {
            // Universal over style
            setStyleName(currentlyEmphasised, OVER, false);
            setStyleName(currentlyEmphasised, OVER_SPACED, false);

            // Vertical styles
            setStyleName(currentlyEmphasised,
                    OVER + "-"
                            + VerticalDropLocation.TOP.toString().toLowerCase(),
                    false);
            setStyleName(currentlyEmphasised, OVER + "-"
                    + VerticalDropLocation.MIDDLE.toString().toLowerCase(),
                    false);
            setStyleName(currentlyEmphasised, OVER + "-"
                    + VerticalDropLocation.BOTTOM.toString().toLowerCase(),
                    false);

            currentlyEmphasised = null;
        }
    }

    /**
     * Returns the horizontal location within the cell when hoovering over the
     * cell. By default the cell is devided into three parts: left,center,right
     * with the ratios 10%,80%,10%;
     * 
     * @param rowElement
     *            The row
     * @param event
     *            The drag event
     * @return The horizontal drop location
     */
    protected VerticalDropLocation getVerticalDropLocation(Element rowElement,
            VDragEvent event) {
        return VDragDropUtil.getVerticalDropLocation(
                (com.google.gwt.user.client.Element) rowElement,
                Util.getTouchOrMouseClientY(event.getCurrentGwtEvent()),
                cellTopBottomDropRatio);
    }

    private static boolean elementIsRow(Element e) {
        String className = e.getClassName() == null ? "" : e.getClassName();
        if (className.contains("v-formlayout-row")) {
            return true;
        }
        return false;
    }

    public static Element getRowFromChildElement(Element e, Element root) {
        while (!elementIsRow(e) && e != root && e.getParentElement() != null) {
            e = e.getParentElement().cast();
        }
        return e;
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
        /*
         * The horizontal position within the cell
         */
        event.getDropDetails().put(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION,
                getVerticalDropLocation(VDDFormLayout.getRowFromChildElement(
                        widget.getElement(), VDDFormLayout.this.getElement()),
                        event));

        /*
         * The index over which the drag is. Can be used by a client side
         * criteria to verify that a drag is over a certain index.
         */
        event.getDropDetails().put(Constants.DROP_DETAIL_TO, "-1");
        for (int i = 0; i < table.getRowCount(); i++) {
            Widget w = table.getWidget(i, COLUMN_WIDGET);
            if (widget.equals(w)) {
                event.getDropDetails().put(Constants.DROP_DETAIL_TO, i);
            }
        }

        /*
         * Add Classname of component over the drag. This can be used by a a
         * client side criteria to verify that a drag is over a specific class
         * of component.
         */
        String className = widget.getClass().getName();
        event.getDropDetails().put(Constants.DROP_DETAIL_OVER_CLASS, className);

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
     * @param widget
     *            The container which we are hovering over
     * @param event
     *            The drag event
     */
    protected void emphasis(Widget widget, VDragEvent event) {

        // Remove emphasis from previous hovers
        deEmphasis();

        // Validate
        if (widget == null || !getElement().isOrHasChild(widget.getElement())) {
            return;
        }

        /*
         * Get row for widget
         */
        Element rowElement = getRowFromChildElement(widget.getElement(),
                VDDFormLayout.this.getElement());

        currentlyEmphasised = rowElement;

        if (rowElement != this.getElement()) {
            VerticalDropLocation vl = getVerticalDropLocation(rowElement,
                    event);
            setStyleName(rowElement,
                    OVER + "-" + vl.toString().toLowerCase(), true);
        } else {
            setStyleName(rowElement, OVER, true);
        }
    }

    /**
     * Returns the current drag mode which determines how the drag is visualized
     */
    @Override
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
     * Get the drop handler attached to the Layout
     */
    @Override
    public VDDFormLayoutDropHandler getDropHandler() {
        return dropHandler;
    }

    public void setDropHandler(VDDFormLayoutDropHandler handler) {
        dropHandler = handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VHasDragFilter#
     * getDragFilter ()
     */
    @Override
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

    public float getCellTopBottomDropRatio() {
        return cellTopBottomDropRatio;
    }

    @DelegateToWidget
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
