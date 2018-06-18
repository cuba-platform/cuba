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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.absolutelayout;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.ui.VAbsoluteLayout;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAbsoluteLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

/**
 * Client side implementation for {@link DDAbsoluteLayout}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDAbsoluteLayout extends VAbsoluteLayout implements VHasDragMode,
        VDDHasDropHandler<VDDAbsoluteLayoutDropHandler>, DragStartListener,
        VHasDragFilter, VHasIframeShims, VHasDragImageReferenceSupport, VHasGrabFilter, VHasDragCaptionProvider {

    public static final String CLASSNAME = "v-ddabsolutelayout";

    private VDDAbsoluteLayoutDropHandler dropHandler;

    private final VLayoutDragDropMouseHandler ddHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private VDragFilter dragFilter;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private boolean iframeCovers = false;

    public VDDAbsoluteLayout() {
        super();
        addStyleName(CLASSNAME);
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

    /**
     * Can be used to listen to drag start events, must return true for the drag
     * to commence. Return false to interrupt the drag:
     */
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    /**
     * Returns the drop handler which handles the drop events
     */
    public VDDAbsoluteLayoutDropHandler getDropHandler() {
        return dropHandler;
    }

    public void setDropHandler(VDDAbsoluteLayoutDropHandler dropHandler) {
        this.dropHandler = dropHandler;
    }

    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    IframeCoverUtility getIframeCoverUtility() {
        return iframeCoverUtility;
    }

    public LayoutDragMode getDragMode() {
        return ddHandler.getDragMode();
    }

    protected void updateDragDetails(VDragEvent drag) {

        // Get absolute coordinates
        int absoluteLeft = drag.getCurrentGwtEvent().getClientX();
        int absoluteTop = drag.getCurrentGwtEvent().getClientY();

        drag.getDropDetails().put(Constants.DROP_DETAIL_ABSOLUTE_LEFT,
                absoluteLeft);
        drag.getDropDetails().put(Constants.DROP_DETAIL_ABSOLUTE_TOP,
                absoluteTop);

        // Get relative coordinates
        int offsetLeft = 0;
        if (drag.getDragImage() != null) {
            String offsetLeftStr = drag.getDragImage().getStyle()
                    .getMarginLeft();
            offsetLeft = Integer.parseInt(
                    offsetLeftStr.substring(0, offsetLeftStr.length() - 2));
        }

        int relativeLeft = Util
                .getTouchOrMouseClientX(drag.getCurrentGwtEvent())
                - canvas.getAbsoluteLeft() + offsetLeft;

        int offsetTop = 0;
        if (drag.getDragImage() != null) {
            String offsetTopStr = drag.getDragImage().getStyle().getMarginTop();
            offsetTop = Integer.parseInt(
                    offsetTopStr.substring(0, offsetTopStr.length() - 2));
        }

        int relativeTop = Util.getTouchOrMouseClientY(drag.getCurrentGwtEvent())
                - canvas.getAbsoluteTop() + offsetTop;

        drag.getDropDetails().put(Constants.DROP_DETAIL_RELATIVE_LEFT,
                relativeLeft);
        drag.getDropDetails().put(Constants.DROP_DETAIL_RELATIVE_TOP,
                relativeTop);

        // Get component size
        ComponentConnector widgetConnector = (ComponentConnector) drag
                .getTransferable()
                .getData(Constants.TRANSFERABLE_DETAIL_COMPONENT);
        if (widgetConnector != null) {
            drag.getDropDetails().put(Constants.DROP_DETAIL_COMPONENT_WIDTH,
                    widgetConnector.getWidget().getOffsetWidth());
            drag.getDropDetails().put(Constants.DROP_DETAIL_COMPONENT_HEIGHT,
                    widgetConnector.getWidget().getOffsetHeight());
        } else {
            drag.getDropDetails().put(Constants.DROP_DETAIL_COMPONENT_WIDTH,
                    -1);
            drag.getDropDetails().put(Constants.DROP_DETAIL_COMPONENT_HEIGHT,
                    -1);
        }

        // Add mouse event details
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(drag.getCurrentGwtEvent(),
                        getElement());
        drag.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                details.serialize());
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
        iframeShimsEnabled(iframeCovers);
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

    @Override
    public void setDragCaptionProvider(VDragCaptionProvider dragCaption) {
        this.dragCaption = dragCaption;
    }

    @Override
    public VDragCaptionProvider getDragCaptionProvider() {
        return dragCaption;
    }
}
