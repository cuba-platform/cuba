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
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.LegacyComponent;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.absolutelayout.DDAbsoluteLayoutState;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.details.AbsoluteLayoutTargetDetails;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces.*;

import java.util.Map;

/**
 * Absolute layout with drag and drop support
 * 
 * @author John Ahlroos / www.jasoft.fi
 */
@SuppressWarnings("serial")
public class DDAbsoluteLayout extends AbsoluteLayout
        implements LayoutDragSource, DropTarget, ShimSupport, LegacyComponent,
        DragImageReferenceSupport, DragFilterSupport, DragGrabFilterSupport, HasDragCaptionProvider {

    // Drop handler which handles dd drop events
    private DropHandler dropHandler;

    // A filter for dragging components.
    private DragFilter dragFilter = DragFilter.ALL;

    private DragGrabFilter dragGrabFilter;

    private DragImageProvider dragImageProvider;

    private DragCaptionProvider dragCaptionProvider;

    /**
     * Creates an AbsoluteLayout with full size.
     */
    public DDAbsoluteLayout() {
        super();
    }

    /**
     * Construct a absolute layout with child components
     * 
     * @param components
     *            the child components to add
     */
    public DDAbsoluteLayout(Component... components) {
        addComponents(components);
    }

    /**
     * {@inheritDoc}
     */
    public void paintContent(PaintTarget target) throws PaintException {

        // Paint the drop handler criterions
        if (dropHandler != null && isEnabled()) {
            dropHandler.getAcceptCriterion().paint(target);
        }
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
     * Get the drophandler which handles component drops on the layout
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
        return new AbsoluteLayoutTargetDetails(this, clientVariables);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public DDAbsoluteLayoutState getState() {
        return (DDAbsoluteLayoutState) super.getState();
    }

    @Override
    protected DDAbsoluteLayoutState getState(boolean markAsDirty) {
        return (DDAbsoluteLayoutState) super.getState(markAsDirty);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // TODO Auto-generated method stub

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

    @Override
    public DragGrabFilter getDragGrabFilter() {
        return dragGrabFilter;
    }

    @Override
    public void setDragGrabFilter(DragGrabFilter dragGrabFilter) {
        this.dragGrabFilter = dragGrabFilter;
    }
}