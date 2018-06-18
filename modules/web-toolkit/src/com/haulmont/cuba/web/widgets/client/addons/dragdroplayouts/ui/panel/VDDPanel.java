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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.panel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VPanel;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

public class VDDPanel extends VPanel implements VHasDragMode,
        VDDHasDropHandler<VDDPanelDropHandler>, DragStartListener,
        VHasDragFilter, VHasDragImageReferenceSupport, VHasIframeShims,
        VHasGrabFilter, VHasDragCaptionProvider {

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private boolean iframeCovers = false;

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private VDDPanelDropHandler dropHandler;

    public static final String OVER = "v-ddpanel-over";

    private Element currentEmphasis;

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

    @Override
    public void setDragCaptionProvider(VDragCaptionProvider dragCaption) {
        this.dragCaption = dragCaption;
    }

    @Override
    public VDragCaptionProvider getDragCaptionProvider() {
        return dragCaption;
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
    public void setDragImageProvider(VDragImageProvider provider) {
        ddMouseHandler.setDragImageProvider(provider);
    }

    @Override
    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        this.dragFilter = filter;
    }

    @Override
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    @Override
    public void setDropHandler(VDDPanelDropHandler drophandler) {
        this.dropHandler = drophandler;
    }

    @Override
    public VDDPanelDropHandler getDropHandler() {
        return dropHandler;
    }

    @Override
    public LayoutDragMode getDragMode() {
        return ddMouseHandler.getDragMode();
    }

    @Override
    public void setDragMode(LayoutDragMode mode) {
        this.mode = mode;
        ddMouseHandler.updateDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    /**
     * Emphasisizes a container element
     * 
     * @param element
     */
    protected void emphasis(Element element) {
        // Remove previous emphasis
        deEmphasis();

        // validate container
        if (element == null || !getElement().isOrHasChild(element)) {
            return;
        }

        currentEmphasis = element;
        element.addClassName(OVER);
    }

    /**
     * Removes any previous emphasis made by drag&amp;drag
     */
    protected void deEmphasis() {
        if (currentEmphasis != null) {
            currentEmphasis.removeClassName(OVER);
            currentEmphasis = null;
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

        Widget content = WidgetUtil.findWidget(over, null);

        if (content != null && content != this) {
            event.getDropDetails().put(Constants.DROP_DETAIL_OVER_CLASS,
                    content.getClass().getName());
        } else {
            event.getDropDetails().put(Constants.DROP_DETAIL_OVER_CLASS,
                    this.getClass().getName());
        }

        // Add mouse event details
        MouseEventDetails details = MouseEventDetailsBuilder
                .buildMouseEventDetails(event.getCurrentGwtEvent(),
                        getElement());
        event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                details.serialize());
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
