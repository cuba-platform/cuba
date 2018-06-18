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
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts.interfaces;

import com.vaadin.event.dd.DragSource;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;

/**
 * Interface for layout drag sources
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
public interface LayoutDragSource extends DragSource {

    /**
     * Gets the drag mode which controls how drags can be made. The layout
     * supports two modes, NONE which disables dragging from the layout and
     * CLONE which enables dragging the component from the layout.
     * 
     * @return The drag mode
     */
    LayoutDragMode getDragMode();

    /**
     * Sets the drag mode which controls how drags can be made. The layout
     * supports two modes, NONE which disables dragging from the layout and
     * CLONE which enables dragging the component from the layout.
     * 
     * @param mode
     *            The mode to set
     */
    void setDragMode(LayoutDragMode mode);

    /**
     * Get a filter which determines which components can be dragged from the
     * layout and which cannot.
     * 
     * This does not effect the drag mode, but only provides a means to make
     * exceptions in the drag mode.
     * 
     * The drag filter is only used when dragging is enabled in the layout, i.e
     * drag mode is NOT {@link LayoutDragMode#NONE}
     * 
     * By default the drag filter permits dragging all components when the
     * layout drag mode allows it.
     * 
     * @return dragFilter
     *            The filter to use, by default {@link DragFilter#ALL} is used.
     */
    DragFilter getDragFilter();

    /**
     * Set a filter which determines which components can be dragged from the
     * layout and which cannot.
     * 
     * This does not effect the drag mode, but only provides a means to make
     * exceptions in the drag mode.
     * 
     * The drag filter is only used when dragging is enabled in the layout, i.e
     * drag mode is NOT {@link LayoutDragMode#NONE}
     * 
     * By default the drag filter permits dragging all components when the
     * layout drag mode allows it.
     * 
     * @param dragFilter
     *            The filter to use, by default {@link DragFilter#ALL} is used.
     */
    void setDragFilter(DragFilter dragFilter);
}
