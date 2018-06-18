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

import java.io.InvalidObjectException;

import com.vaadin.ui.Component;

/**
 * A drag filter for either allowing all or allowing no drags
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 * 
 */
final class DragFilterLiteral implements DragFilter {

    private final boolean allowDragging;

    /**
     * Constructor
     * 
     * @param allowDragging
     *            Should dragging be allowed
     */
    public DragFilterLiteral(boolean allowDragging) {
        this.allowDragging = allowDragging;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isDraggable(Component component) {
        return allowDragging;
    }

    /**
     * Ensures that DragFilter.ALL or DragFilter.NONE is returned when
     * de-serializing
     * 
     * @return
     * @throws InvalidObjectException
     */
    private Object readResolve() throws InvalidObjectException {
        return allowDragging ? DragFilter.ALL : DragFilter.NONE;
    }
}
