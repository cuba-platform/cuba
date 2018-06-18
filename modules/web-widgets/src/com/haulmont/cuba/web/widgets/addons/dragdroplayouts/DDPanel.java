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
import com.vaadin.ui.Component;
import com.vaadin.ui.LegacyComponent;
import com.vaadin.ui.Panel;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.panel.DDPanelState;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces.*;

import java.util.Map;

public class DDPanel extends Panel
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
     * @see Panel#Panel()
     */
    public DDPanel() {
        super();
    }

    /**
     * @see Panel#Panel(Component)
     */
    public DDPanel(Component content) {
        super(content);
    }

    /**
     * @see Panel#Panel(String)
     */
    public DDPanel(String caption) {
        super(caption);
    }

    /**
     * @see Panel#Panel(String, Component)
     */
    public DDPanel(String caption, Component content) {
        super(caption, content);
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

    @SuppressWarnings("serial")
    public class PanelTargetDetails extends TargetDetailsImpl {

        protected PanelTargetDetails(Map<String, Object> rawDropData) {
            super(rawDropData, DDPanel.this);
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
    }

    @Override
    public Transferable getTransferable(Map<String, Object> rawVariables) {
        return new LayoutBoundTransferable(this, rawVariables);
    }

    @Override
    public void setDragImageProvider(DragImageProvider provider) {
        this.dragImageProvider = provider;
        markAsDirty();
    }

    @Override
    public DragImageProvider getDragImageProvider() {
        return dragImageProvider;
    }

    @Override
    public void setShim(boolean shim) {
        getState().ddState.iframeShims = shim;
    }

    @Override
    public boolean isShimmed() {
        return getState().ddState.iframeShims;
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

    @Override
    public DropHandler getDropHandler() {
        return dropHandler;
    }

    @Override
    public TargetDetails translateDropTargetDetails(
            Map<String, Object> clientVariables) {
        return new PanelTargetDetails(clientVariables);
    }

    @Override
    public LayoutDragMode getDragMode() {
        return getState(false).ddState.dragMode;
    }

    @Override
    public void setDragMode(LayoutDragMode mode) {
        getState().ddState.dragMode = mode;
    }

    @Override
    public DragFilter getDragFilter() {
        return dragFilter;
    }

    @Override
    public void setDragFilter(DragFilter dragFilter) {
        this.dragFilter = dragFilter;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (dropHandler != null && isEnabled()) {
            dropHandler.getAcceptCriterion().paint(target);
        }
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        DDUtil.onBeforeClientResponse(this, getState());
    }

    @Override
    protected DDPanelState getState() {
        return (DDPanelState) super.getState();
    }

    @Override
    protected DDPanelState getState(boolean markAsDirty) {
        return (DDPanelState) super.getState(markAsDirty);
    }
}
