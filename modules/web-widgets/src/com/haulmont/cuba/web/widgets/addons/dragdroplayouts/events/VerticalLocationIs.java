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

import com.vaadin.event.dd.acceptcriteria.TargetDetailIs;
import com.vaadin.shared.ui.dd.VerticalDropLocation;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;

/**
 * A client side criterion for determining the vertical location
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
@SuppressWarnings("serial")
public final class VerticalLocationIs extends TargetDetailIs {

    /**
     * Was the drop made top of the centerline of the component
     */
    public static final VerticalLocationIs TOP = new VerticalLocationIs(
            VerticalDropLocation.TOP);

    /**
     * Was the drop made below the centeline of the component
     */
    public static final VerticalLocationIs BOTTOM = new VerticalLocationIs(
            VerticalDropLocation.BOTTOM);

    /**
     * Was the drop made in the middle of the component
     */
    public static final VerticalLocationIs MIDDLE = new VerticalLocationIs(
            VerticalDropLocation.MIDDLE);

    /**
     * A target detail for the vertical location of a drop
     * 
     * @param location
     *            The drop location
     */
    private VerticalLocationIs(VerticalDropLocation location) {
        super(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION, location.name());
    }
}
