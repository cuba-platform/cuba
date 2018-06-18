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

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.dd.VDragEvent;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDDAbstractDropHandler;

public class VDDAbsoluteLayoutDropHandler
        extends VDDAbstractDropHandler<VDDAbsoluteLayout> {

    public VDDAbsoluteLayoutDropHandler(ComponentConnector connector) {
        super(connector);
    }

    @Override
    public boolean drop(VDragEvent drag) {
        if (super.drop(drag)) {
            getLayout().updateDragDetails(drag);
            return getLayout().postDropHook(drag);
        }
        return false;
    };

    @Override
    public void dragEnter(VDragEvent drag) {
        super.dragEnter(drag);
        getLayout().updateDragDetails(drag);
        getLayout().postEnterHook(drag);
    }

    @Override
    public void dragLeave(VDragEvent drag) {
        super.dragLeave(drag);

        // Due to http://dev.vaadin.com/ticket/14880 we need to abort if gwt
        // event is null
        if (drag.getCurrentGwtEvent() != null) {
            getLayout().updateDragDetails(drag);
            getLayout().postLeaveHook(drag);
        }
    };

    @Override
    public void dragOver(VDragEvent drag) {
        if (drag.getDragImage() != null) {
            drag.getDragImage().getStyle().setProperty("display", "");
        }
        getLayout().updateDragDetails(drag);
        getLayout().postOverHook(drag);
    }
}
