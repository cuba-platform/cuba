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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.tabsheet;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VTabsheet;
import com.vaadin.client.ui.VTabsheetPanel;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDTabSheet;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

/**
 * Client side implementation for {@link DDTabSheet}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDTabSheet extends VTabsheet
        implements VHasDragMode, VDDHasDropHandler<VDDTabsheetDropHandler>,
        DragStartListener, VDDTabContainer, VHasDragFilter,
        VHasDragImageReferenceSupport, VHasIframeShims, VHasGrabFilter, VHasDragCaptionProvider {

    public static final String CLASSNAME_NEW_TAB = "new-tab";
    public static final String CLASSNAME_NEW_TAB_LEFT = "new-tab-left";
    public static final String CLASSNAME_NEW_TAB_RIGHT = "new-tab-right";
    public static final String CLASSNAME_NEW_TAB_CENTER = "new-tab-center";

    private VDDTabsheetDropHandler dropHandler;

    private final ComplexPanel tabBar;
    private final VTabsheetPanel tabPanel;

    private final Element spacer;

    private Element currentlyEmphasised;

    private final Element newTab = DOM.createDiv();

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private double tabLeftRightDropRatio = DDTabSheetState.DEFAULT_HORIZONTAL_DROP_RATIO;

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    public VDDTabSheet() {
        super();

        newTab.setClassName(CLASSNAME_NEW_TAB);

        // Get the tabBar
        tabBar = (ComplexPanel) getChildren().get(0);

        // Get the content
        tabPanel = (VTabsheetPanel) getChildren().get(1);

        // Get the spacer
        Element tBody = tabBar.getElement();
        spacer = tBody.getChild(tBody.getChildCount() - 1).getChild(0)
                .getChild(0).cast();
    }

    @Override
    public void setDragCaptionProvider(VDragCaptionProvider dragCaption) {
        this.dragCaption = dragCaption;
    }

    @Override
    public VDragCaptionProvider getDragCaptionProvider() {
        return dragCaption;
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        ddMouseHandler.addDragStartListener(this);
        ddMouseHandler.setAttachTarget(tabBar);
        setDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        ddMouseHandler.removeDragStartListener(this);
        ddMouseHandler.updateDragMode(LayoutDragMode.NONE);
        iframeCoverUtility.setIframeCoversEnabled(false, getElement(),
                LayoutDragMode.NONE);
    }

    @Override
    public VDDTabsheetDropHandler getDropHandler() {
        return dropHandler;
    }

    @Override
    public void setDropHandler(VDDTabsheetDropHandler handler) {
        this.dropHandler = handler;
    }

    @Override
    public LayoutDragMode getDragMode() {
        return ddMouseHandler.getDragMode();
    }

    /**
     * A hook for extended components to post process the the drop before it is
     * sent to the server. Useful if you don't want to override the whole drop
     * handler.
     */
    protected boolean postDropHook(VDragEvent drag) {
        // Extended classes can add content here...
        return true;
    }

    /**
     * A hook for extended components to post process the the enter event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postEnterHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * A hook for extended components to post process the the leave event.
     * Useful if you don't want to override the whole drophandler.
     */
    protected void postLeaveHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * A hook for extended components to post process the the over event. Useful
     * if you don't want to override the whole drophandler.
     */
    protected void postOverHook(VDragEvent drag) {
        // Extended classes can add content here...
    }

    /**
     * Can be used to listen to drag start events, must return true for the drag
     * to commence. Return false to interrupt the drag:
     */
    @Override
    public boolean dragStart(Widget widget, LayoutDragMode mode) {
        ComponentConnector layout = Util.findConnectorFor(this);
        return VDragDropUtil.isDraggingEnabled(layout, widget);
    }

    /**
     * Updates the drop details while dragging. This is needed to ensure client
     * side criterias can validate the drop location.
     * 
     * @param event
     *            The drag event
     */
    protected void updateDragDetails(VDragEvent event) {
        Element element = event.getElementOver();
        if (element == null)
            return;

        if (tabBar.getElement().isOrHasChild(element)) {
            Widget w = Util.findWidget(element, null);

            if (w == tabBar) {
                // Ove3r the spacer

                // Add index
                event.getDropDetails().put(Constants.DROP_DETAIL_TO,
                        tabBar.getWidgetCount() - 1);

                // Add drop location
                event.getDropDetails().put(
                        Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION,
                        HorizontalDropLocation.RIGHT);

            } else {

                // Add index
                event.getDropDetails().put(Constants.DROP_DETAIL_TO,
                        getTabPosition(w));

                // Add drop location
                HorizontalDropLocation location = VDragDropUtil
                        .getHorizontalDropLocation(DOM.asOld(element),
                                Util.getTouchOrMouseClientX(
                                        event.getCurrentGwtEvent()),
                                tabLeftRightDropRatio);
                event.getDropDetails().put(
                        Constants.DROP_DETAIL_HORIZONTAL_DROP_LOCATION,
                        location);
            }

            // Add mouse event details
            MouseEventDetails details = MouseEventDetailsBuilder
                    .buildMouseEventDetails(event.getCurrentGwtEvent(),
                            getElement());
            event.getDropDetails().put(Constants.DROP_DETAIL_MOUSE_EVENT,
                    details.serialize());
        }
    }

    /**
     * Emphasisizes a container element
     * 
     * @param element
     */
    protected void emphasis(Element element, VDragEvent event) {

        boolean internalDrag = event.getTransferable().getDragSource() == this;

        if (tabBar.getElement().isOrHasChild(element)) {
            Widget w = Util.findWidget(element, null);

            if (w == tabBar && !internalDrag) {
                // Over spacer
                Element spacerContent = spacer.getChild(0).cast();
                spacerContent.appendChild(newTab);
                currentlyEmphasised = element;

            } else if (w instanceof VCaption) {

                // Over a tab
                HorizontalDropLocation location = VDragDropUtil
                        .getHorizontalDropLocation(DOM.asOld(element),
                                Util.getTouchOrMouseClientX(
                                        event.getCurrentGwtEvent()),
                                tabLeftRightDropRatio);

                if (location == HorizontalDropLocation.LEFT) {

                    int index = getTabPosition(w);

                    if (index == 0) {

                        currentlyEmphasised = tabBar.getWidget(0).getElement()
                                .getFirstChildElement().cast();
                        currentlyEmphasised
                                .addClassName(CLASSNAME_NEW_TAB_LEFT);
                    } else {
                        Widget prevTab = tabBar.getWidget(index - 1);
                        currentlyEmphasised = prevTab.getElement();
                        currentlyEmphasised
                                .addClassName(CLASSNAME_NEW_TAB_RIGHT);
                    }

                } else if (location == HorizontalDropLocation.RIGHT) {
                    int index = getTabPosition(w);
                    currentlyEmphasised = tabBar.getWidget(index).getElement();
                    currentlyEmphasised.addClassName(CLASSNAME_NEW_TAB_RIGHT);
                } else {
                    int index = getTabPosition(w);
                    currentlyEmphasised = tabBar.getWidget(index).getElement();
                    currentlyEmphasised.addClassName(CLASSNAME_NEW_TAB_CENTER);
                }

            }
        }
    }

    /**
     * Removes any previous emphasis made by drag&amp;drop
     */
    protected void deEmphasis() {
        if (currentlyEmphasised != null
                && tabBar.getElement().isOrHasChild(currentlyEmphasised)) {
            Widget w = Util.findWidget(currentlyEmphasised, null);

            currentlyEmphasised.removeClassName(CLASSNAME_NEW_TAB_LEFT);
            currentlyEmphasised.removeClassName(CLASSNAME_NEW_TAB_RIGHT);
            currentlyEmphasised.removeClassName(CLASSNAME_NEW_TAB_CENTER);

            if (w == tabBar) {
                // Over spacer
                Element spacerContent = spacer.getChild(0).cast();
                spacerContent.removeChild(newTab);
            }

            currentlyEmphasised = null;
        }
    }

    @Override
    public int getTabPosition(Widget tab) {
        if (tab instanceof TabCaption) {
            tab = tab.getParent();
        }
        return tabBar.getWidgetIndex(tab);
    }

    @Override
    public int getTabContentPosition(Widget content) {
        return tabPanel.getWidgetIndex(content);
    }

    @Override
    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    IframeCoverUtility getIframeCoverUtility() {
        return iframeCoverUtility;
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        this.dragFilter = filter;
    }

    /**
     * Get the distribution of how a drop should be interpreted over a tab
     */
    public double getTabLeftRightDropRatio() {
        return tabLeftRightDropRatio;
    }

    /**
     * Set the distribution of how a drop should be interpreted over a tab
     */
    public void setTabLeftRightDropRatio(double tabLeftRightDropRatio) {
        this.tabLeftRightDropRatio = tabLeftRightDropRatio;
    }

    @Override
    public void iframeShimsEnabled(boolean enabled) {
        iframeCovers = enabled;
        iframeCoverUtility.setIframeCoversEnabled(enabled, getElement(), mode);
    }

    @Override
    public boolean isIframeShimsEnabled() {
        return iframeCovers;
    }

    @Override
    public void setDragMode(LayoutDragMode mode) {
        this.mode = mode;
        ddMouseHandler.updateDragMode(mode);
        iframeShimsEnabled(iframeCovers);
    }

    @Override
    public void setDragImageProvider(VDragImageProvider provider) {
        ddMouseHandler.setDragImageProvider(provider);
    }

    protected final VLayoutDragDropMouseHandler getMouseHandler() {
        return ddMouseHandler;
    }

    @Override
    public VGrabFilter getGrabFilter() {
        return grabFilter;
    }

    @Override
    public void setGrabFilter(VGrabFilter grabFilter) {
        this.grabFilter = grabFilter;
    }
}
