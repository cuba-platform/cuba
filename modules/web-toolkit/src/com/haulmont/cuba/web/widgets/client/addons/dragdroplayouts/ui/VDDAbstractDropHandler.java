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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.dd.VAbstractDropHandler;
import com.vaadin.client.ui.dd.VDragEvent;

public abstract class VDDAbstractDropHandler<W extends Widget>
        extends VAbstractDropHandler {

    private final ComponentConnector connector;

    public VDDAbstractDropHandler(ComponentConnector connector) {
        this.connector = connector;
    }

    @Override
    public ApplicationConnection getApplicationConnection() {
        return connector.getConnection();
    }

    public void cancelDrag(VDragEvent drag) {
        dragLeave(drag);
    }

    @Override
    protected void dragAccepted(VDragEvent drag) {
        // NOP
    }

    @Override
    public ComponentConnector getConnector() {
        return connector;
    }

    protected W getLayout() {
        return (W) connector.getWidget();
    }
}
