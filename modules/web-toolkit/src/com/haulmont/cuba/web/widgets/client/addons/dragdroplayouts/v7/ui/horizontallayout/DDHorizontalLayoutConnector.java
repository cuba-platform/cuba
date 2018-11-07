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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.v7.ui.horizontallayout;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.v7.DDHorizontalLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDragDropUtil;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VHasDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.HTML5Support;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.orderedlayout.HorizontalLayoutConnector;
import com.vaadin.shared.ui.Connect;

@Connect(value = DDHorizontalLayout.class, loadStyle = Connect.LoadStyle.LAZY)
public class DDHorizontalLayoutConnector extends HorizontalLayoutConnector
        implements Paintable, VHasDragFilter {

    private HTML5Support html5Support;

    @Override
    public VDDHorizontalLayout getWidget() {
        return (VDDHorizontalLayout) super.getWidget();
    }

    @Override
    public DDHorizontalLayoutState getState() {
        return (DDHorizontalLayoutState) super.getState();
    }

    @Override
    public void init() {
        super.init();
        VDragDropUtil.listenToStateChangeEvents(this, getWidget());
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        VDragDropUtil.updateDropHandlerFromUIDL(uidl, this, new VDDHorizontalLayoutDropHandler(this));
        if (html5Support != null) {
            html5Support.disable();
            html5Support = null;
        }
        VDDHorizontalLayoutDropHandler dropHandler = getWidget().getDropHandler();
        if (dropHandler != null) {
            html5Support = HTML5Support.enable(this, dropHandler);
        }
    }

    @Override
    public void onUnregister() {
        if (html5Support != null) {
            html5Support.disable();
            html5Support = null;
        }
        super.onUnregister();
    }

    @Override
    public VDragFilter getDragFilter() {
        return getWidget().getDragFilter();
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        getWidget().setDragFilter(filter);
    }
}
