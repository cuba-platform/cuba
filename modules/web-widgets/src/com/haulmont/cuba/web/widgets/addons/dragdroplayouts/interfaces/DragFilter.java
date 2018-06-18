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

import java.io.Serializable;

import com.vaadin.ui.Component;

/**
 * A Filter for disabling dragging for some components in the layout
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 * 
 */
public interface DragFilter extends Serializable {

    /**
     * Is the component draggable
     * 
     * @param component
     *            The component to test
     * @return Is the given component draggable or not
     */
    boolean isDraggable(Component component);

    /**
     * A filter for allowing dragging all components in a layout (default)
     */
    static final DragFilter ALL = new DragFilterLiteral(true);

    /**
     * A filter for preventing dragging any component in a layout
     */
    static final DragFilter NONE = new DragFilterLiteral(false);
}
