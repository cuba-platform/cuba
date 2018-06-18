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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.csslayout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VCssLayout;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDCssLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

/**
 * Client side implementation for {@link DDCssLayout}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.7.0
 * 
 */
public class VDDCssLayout extends VCssLayout implements VHasDragMode,
        VDDHasDropHandler<VDDCssLayoutDropHandler>, DragStartListener,
        VHasDragFilter, VHasIframeShims, VHasDragImageReferenceSupport, VHasGrabFilter, VHasDragCaptionProvider {

    public static final String DRAG_SHADOW_STYLE_NAME = "v-ddcsslayout-drag-shadow";

    private VDDCssLayoutDropHandler dropHandler;

    private final VLayoutDragDropMouseHandler ddHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private double horizontalDropRatio = DDCssLayoutState.DEFAULT_HORIZONTAL_DROP_RATIO;

    private double verticalDropRatio = DDCssLayoutState.DEFAULT_VERTICAL_DROP_RATIO;

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    /**
     * Default constructor
     */
    public VDDCssLayout() {
        super();
    }

    /**
     * Can be used to listen to drag start events, must return true for the drag
     * to commence. Return false to interrupt the drag:
     */
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    @Override
    public void setDragCaptionProvider(VDragCaptionProvider dragCaption) {
        this.dragCaption = dragCaption;
    }

    @Override
    public VDragCaptionProvider getDragCaptionProvider() {
        return dragCaption;
    }

    /**
     * Returns the drop handler which handles the drop events
     */
    public VDDCssLayoutDropHandler getDropHandler() {
        return dropHandler;
    }

    public void setDropHandler(VDDCssLayoutDropHandler dropHandler) {
        this.dropHandler = dropHandler;
    }

    /**
     * Returns the drag mode
     * 
     * @return
     */
    public LayoutDragMode getDragMode() {
        return ddHandler.getDragMode();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        ddHandler.addDragStartListener(this);
        setDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        ddHandler.removeDragStartListener(this);
        ddHandler.updateDragMode(LayoutDragMode.NONE);
        iframeCoverUtility.setIframeCoversEnabled(false, getElement(),
                LayoutDragMode.NONE);
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

    private Element placeHolderElement;

    public void attachDragImageToLayout(VDragEvent drag) {
        if (placeHolderElement == null) {
            placeHolderElement = DOM.createDiv();
            placeHolderElement.setInnerHTML("&nbsp;");
        }
    }

    private void updatePlaceHolderStyleProperties(VDragEvent drag) {
        int width = 0;
        int height = 0;
        String className = "";

        placeHolderElement.setClassName(DRAG_SHADOW_STYLE_NAME);

        ComponentConnector draggedConnector = (ComponentConnector) drag
                .getTransferable()
                .getData(Constants.TRANSFERABLE_DETAIL_COMPONENT);
        if (draggedConnector != null) {
            height = Util.getRequiredHeight(draggedConnector.getWidget());
            width = Util.getRequiredWidth(draggedConnector.getWidget());
            className = draggedConnector.getWidget().getElement()
                    .getClassName();
            className = className.replaceAll(
                    VLayoutDragDropMouseHandler.ACTIVE_DRAG_SOURCE_STYLENAME,
                    "");
            placeHolderElement.addClassName(className);
        } else if (drag.getElementOver() != getElement()) {
            width = 3;
            height = drag.getElementOver().getOffsetHeight();
        }

        placeHolderElement.getStyle().setWidth(width, Unit.PX);
        placeHolderElement.getStyle().setHeight(height, Unit.PX);
    }

    public void detachDragImageFromLayout(VDragEvent drag) {
        if (placeHolderElement != null) {
            if (placeHolderElement.hasParentElement()) {
                placeHolderElement.removeFromParent();
            }
            placeHolderElement = null;
        }
    }

    /**
     * Updates the drop details while dragging. This is needed to ensure client
     * side criterias can validate the drop location.
     * 
     * @param event
     *            The drag event
     */
    protected void updateDragDetails(VDragEvent event) {

        Element over = event.getElementOver();
        if (placeHolderElement.isOrHasChild(over)) {
            // Dragging over the placeholder
            return;
        }

        Widget widget = (Widget) Util.findWidget(over, null);
        if (widget == null) {
            // Null check
            return;
        }

        int offset = 0;
        int index = -1;
        for (int i = 0; i < getElement().getChildCount(); i++) {
            Element child = getElement().getChild(i).cast();
            if (child.isOrHasChild(placeHolderElement)) {
                offset--;
            } else if (child.isOrHasChild(widget.getElement())) {
                index = i + offset;
                break;
            }
        }
        event.getDropDetails().put(Constants.DROP_DETAIL_TO, index);

        /*
         * The horizontal position within the cell
         */
        event.getDropDetails().put(
                Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION,
                getHorizontalDropLocation(widget, event));

        /*
         * The vertical position within the cell
         */
        event.getDropDetails().put(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION,
                getVerticalDropLocation(widget, event));

        // Add mouse event details
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event.getCurrentGwtEvent(),
                        getElement());
        event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                details.serialize());
    }

    public void updateDrag(VDragEvent drag) {

        if (placeHolderElement == null) {
            /*
             * Drag image might not have been detach due to lazy attaching in
             * the DragAndDropManager. Detach it again here if it has not been
             * detached.
             */
            attachDragImageToLayout(drag);
            return;
        }

        if (drag.getElementOver().isOrHasChild(placeHolderElement)) {
            return;
        }

        if (placeHolderElement.hasParentElement()) {
            /*
             * Remove the placeholder from the DOM so we can reposition
             */
            placeHolderElement.removeFromParent();
        }

        Widget w = Util.findWidget(drag.getElementOver(), null);

        ComponentConnector draggedConnector = (ComponentConnector) drag
                .getTransferable()
                .getData(Constants.TRANSFERABLE_DETAIL_COMPONENT);

        if (draggedConnector != null && w == draggedConnector.getWidget()) {
            /*
             * Dragging drag image over the placeholder should not have any
             * effect (except placeholder should be removed)
             */
            return;
        }

        if (w != null && w != this) {

            HorizontalDropLocation hl = getHorizontalDropLocation(w, drag);
            VerticalDropLocation vl = getVerticalDropLocation(w, drag);

            if (hl == HorizontalDropLocation.LEFT
                    || vl == VerticalDropLocation.TOP) {
                Element prev = w.getElement().getPreviousSibling().cast();
                if (draggedConnector == null || prev == null
                        || !draggedConnector.getWidget().getElement()
                                .isOrHasChild(prev)) {

                    w.getElement().getParentElement()
                            .insertBefore(placeHolderElement, w.getElement());

                }
            } else if (hl == HorizontalDropLocation.RIGHT
                    || vl == VerticalDropLocation.BOTTOM) {
                Element next = w.getElement().getNextSibling().cast();
                if (draggedConnector == null || next == null
                        || !draggedConnector.getWidget().getElement()
                                .isOrHasChild(next)) {
                    w.getElement().getParentElement()
                            .insertAfter(placeHolderElement, w.getElement());
                }

            } else {
                Element prev = w.getElement().getPreviousSibling().cast();
                if (draggedConnector == null || prev == null
                        || !draggedConnector.getWidget().getElement()
                                .isOrHasChild(prev)) {
                    w.getElement().getParentElement()
                            .insertBefore(placeHolderElement, w.getElement());
                }
            }

        } else {
            /*
             * First child or hoovering outside of current components
             */
            getElement().appendChild(placeHolderElement);
        }

        updatePlaceHolderStyleProperties(drag);
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
                horizontalDropRatio);
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
    protected VerticalDropLocation getVerticalDropLocation(Widget container,
            VDragEvent event) {
        return VDragDropUtil.getVerticalDropLocation(container.getElement(),
                Util.getTouchOrMouseClientY(event.getCurrentGwtEvent()),
                verticalDropRatio);
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

    public double getHorizontalDropRatio() {
        return horizontalDropRatio;
    }

    public void setHorizontalDropRatio(float horizontalDropRatio) {
        this.horizontalDropRatio = horizontalDropRatio;
    }

    public double getVerticalDropRatio() {
        return verticalDropRatio;
    }

    public void setVerticalDropRatio(float verticalDropRatio) {
        this.verticalDropRatio = verticalDropRatio;
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
        ddHandler.updateDragMode(mode);
        iframeShimsEnabled(isIframeShimsEnabled());
    }

    @Override
    public void setDragImageProvider(VDragImageProvider provider) {
        ddHandler.setDragImageProvider(provider);
    }

    protected final VLayoutDragDropMouseHandler getMouseHandler() {
        return ddHandler;
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
