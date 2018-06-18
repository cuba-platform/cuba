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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.gridlayout;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.dd.VAcceptCallback;
import com.vaadin.client.ui.dd.VDragEvent;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDDAbstractDropHandler;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.gridlayout.VDDGridLayout.CellDetails;

public class VDDGridLayoutDropHandler
        extends VDDAbstractDropHandler<VDDGridLayout> {

    public VDDGridLayoutDropHandler(ComponentConnector connector) {
        super(connector);
    }

    @Override
    public void dragEnter(VDragEvent drag) {
        super.dragEnter(drag);
        getLayout().updateDragDetails(drag);
        getLayout().postEnterHook(drag);
    }

    @Override
    public boolean drop(VDragEvent drag) {

        // Update the detail of the drop
        getLayout().updateDragDetails(drag);

        // Remove emphasis
        getLayout().deEmphasis();

        return getLayout().postDropHook(drag);
    };

    @Override
    public void dragOver(VDragEvent drag) {

        // Remove emphasis from previous selection
        getLayout().deEmphasis();

        // Update the drop details so we can then validate them
        getLayout().updateDragDetails(drag);

        getLayout().postOverHook(drag);

        // Emphasis drop location
        validate(new VAcceptCallback() {
            public void accepted(VDragEvent event) {
                CellDetails cd = getLayout().getCellDetails(event);
                if (cd != null) {
                    getLayout().emphasis(cd, event);
                }
            }
        }, drag);
    }

    @Override
    public void dragLeave(VDragEvent drag) {
        getLayout().deEmphasis();
        getLayout().postLeaveHook(drag);
        super.dragLeave(drag);
    }
}
