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
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts.details;

import java.util.Map;

import com.vaadin.event.dd.TargetDetailsImpl;
import com.vaadin.shared.MouseEventDetails;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAbsoluteLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;

public class AbsoluteLayoutTargetDetails extends TargetDetailsImpl {

    /**
     * Constructor
     * 
     * @param rawDropData
     *            Drop data
     */
    public AbsoluteLayoutTargetDetails(DDAbsoluteLayout layout,
            Map<String, Object> rawDropData) {
        super(rawDropData, layout);
    }

    /**
     * The absolute left coordinate in pixels measured from the windows left
     * edge
     * 
     * @return The amount of pixels from the left edge
     */
    public int getAbsoluteLeft() {
        return Integer.valueOf(
                getData(Constants.DROP_DETAIL_ABSOLUTE_LEFT).toString());
    }

    /**
     * The absolute top coordinate in pixels measured from the windows top edge
     * 
     * @return The amount of pixels from the top edge
     */
    public int getAbsoluteTop() {
        return Integer.valueOf(
                getData(Constants.DROP_DETAIL_ABSOLUTE_TOP).toString());
    }

    /**
     * The relative left coordinate in pixels measured from the containers left
     * edge
     * 
     * @return The amount of pixels from the left edge
     */
    public int getRelativeLeft() {
        return Integer.valueOf(
                getData(Constants.DROP_DETAIL_RELATIVE_LEFT).toString());
    }

    /**
     * The relative top coordinate in pixels measured from the containers top
     * edge
     * 
     * @return The amount of pixels from the top edge
     */
    public int getRelativeTop() {
        return Integer.valueOf(
                getData(Constants.DROP_DETAIL_RELATIVE_TOP).toString());
    }

    /**
     * The width of the dragged component measured in pixels
     * 
     * @return The width in pixels
     */
    public int getComponentHeight() {
        return Integer.valueOf(
                getData(Constants.DROP_DETAIL_COMPONENT_HEIGHT).toString());
    }

    /**
     * The height of the dragged component measured in pixels
     * 
     * @return The height in pixels
     */
    public int getComponentWidth() {
        return Integer.valueOf(
                getData(Constants.DROP_DETAIL_COMPONENT_WIDTH).toString());
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
