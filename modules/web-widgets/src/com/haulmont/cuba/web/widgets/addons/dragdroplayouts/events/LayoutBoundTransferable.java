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
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events;

import java.util.Map;

import com.vaadin.event.TransferableImpl;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;

/**
 * Abstract base class for layout based transferable's.
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
@SuppressWarnings("serial")
public class LayoutBoundTransferable extends TransferableImpl {

    /**
     * Default constructor
     * 
     * @param sourceComponent
     *            The layout from where the drag started
     * @param rawVariables
     *            The details of the drag
     */
    public LayoutBoundTransferable(Component sourceComponent,
            Map<String, Object> rawVariables) {
        super(sourceComponent, rawVariables);
    }

    /**
     * Get the component being dragged
     * 
     * @return The component
     */
    public Component getComponent() {
        return (Component) getData(Constants.TRANSFERABLE_DETAIL_COMPONENT);
    }

    /**
     * @return the mouse down event that started the drag and drop operation
     */
    public MouseEventDetails getMouseDownEvent() {
        return MouseEventDetails.deSerialize(
                (String) getData(Constants.TRANSFERABLE_DETAIL_MOUSEDOWN));
    }
}
