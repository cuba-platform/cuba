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

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.absolutelayout.AbsoluteLayoutConnector;
import com.vaadin.shared.ui.Connect;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAbsoluteLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.LayoutDragMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDragCaptionProvider;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDragDropUtil;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VHasDragCaptionProvider;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VHasDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.VHasGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.HTML5Support;

@Connect(value = DDAbsoluteLayout.class, loadStyle = Connect.LoadStyle.LAZY)
public class DDAbsoluteLayoutConnector extends AbsoluteLayoutConnector
        implements Paintable, VHasDragFilter, VHasGrabFilter, VHasDragCaptionProvider {

    private HTML5Support html5Support;

    @Override
    public VDDAbsoluteLayout getWidget() {
        return (VDDAbsoluteLayout) super.getWidget();
    }

    @Override
    public DDAbsoluteLayoutState getState() {
        return (DDAbsoluteLayoutState) super.getState();
    }

    @Override
    protected void init() {
        super.init();
        VDragDropUtil.listenToStateChangeEvents(this, getWidget());
    }

    /**
     * 
     * TODO Remove this when drag &amp; drop is done properly in core
     */
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        VDragDropUtil.updateDropHandlerFromUIDL(uidl, this, new VDDAbsoluteLayoutDropHandler(this));
        if (html5Support != null) {
            html5Support.disable();
            html5Support = null;
        }
        VDDAbsoluteLayoutDropHandler dropHandler = getWidget().getDropHandler();
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

    public LayoutDragMode getDragMode() {
        return getWidget().getDragMode();
    }

    @Override
    public VDragFilter getDragFilter() {
        return getWidget().getDragFilter();
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        getWidget().setDragFilter(filter);
    }

    @Override
    public VGrabFilter getGrabFilter() {
        return getWidget().getGrabFilter();
    }

    @Override
    public void setGrabFilter(VGrabFilter grabFilter) {
        getWidget().setGrabFilter(grabFilter);
    }

    @Override
    public void setDragCaptionProvider(VDragCaptionProvider dragCaption) {
        getWidget().setDragCaptionProvider(dragCaption);
    }

    @Override
    public VDragCaptionProvider getDragCaptionProvider() {
        return getWidget().getDragCaptionProvider();
    }
}
