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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.accordion;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.client.Util;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.VAccordion;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAccordion;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VLayoutDragDropMouseHandler.DragStartListener;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.util.IframeCoverUtility;

import java.util.Map;

/**
 * Client side implementation for {@link DDAccordion}
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VDDAccordion extends VAccordion
        implements VHasDragMode, VDDHasDropHandler<VDDAccordionDropHandler>,
        DragStartListener, VDDTabContainer, VHasDragFilter,
        VHasDragImageReferenceSupport, VHasIframeShims, VHasGrabFilter, VHasDragCaptionProvider {

    public static final String CLASSNAME_OVER = "dd-over";
    public static final String CLASSNAME_SPACER = "spacer";

    private VDDAccordionDropHandler dropHandler;

    private StackItem currentlyEmphasised;

    private final Widget spacer;

    // The drag mouse handler which handles the creation of the transferable
    private final VLayoutDragDropMouseHandler ddMouseHandler = new VLayoutDragDropMouseHandler(
            this, LayoutDragMode.NONE);

    private VDragFilter dragFilter;

    private VDragCaptionProvider dragCaption;

    private VGrabFilter grabFilter;

    private final IframeCoverUtility iframeCoverUtility = new IframeCoverUtility();

    private float tabTopBottomDropRatio = DDAccordionState.DEFAULT_VERTICAL_RATIO;

    private LayoutDragMode mode = LayoutDragMode.NONE;

    private boolean iframeCovers = false;

    public VDDAccordion() {
        spacer = GWT.create(HTML.class);
        spacer.setWidth("100%");
        spacer.setStyleName(CLASSNAME_SPACER);
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.terminal.gwt.client.ui.dd.VHasDropHandler#getDropHandler()
     */
    public VDDAccordionDropHandler getDropHandler() {
        return dropHandler;
    }

    public void setDropHandler(VDDAccordionDropHandler dropHandler) {
        this.dropHandler = dropHandler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VHasDragMode#getDragMode()
     */
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
        if (event.getElementOver() == null) {
            return;
        }

        StackItem tab = WidgetUtil.findWidget(event.getElementOver(),
                StackItem.class);

        if (tab != null && getElement().isOrHasChild(tab.getElement())) {
            Map<String, Object> dropDetails = event.getDropDetails();

            int index = getTabPosition(tab);
            dropDetails.put(Constants.DROP_DETAIL_TO, index);

            VerticalDropLocation location = getDropLocation(tab, event);
            dropDetails.put(Constants.DROP_DETAIL_VERTICAL_DROP_LOCATION,
                    location);

            MouseEventDetails details = MouseEventDetailsBuilder
                    .buildMouseEventDetails(event.getCurrentGwtEvent(),
                            getElement());
            dropDetails.put(Constants.DROP_DETAIL_MOUSE_EVENT,
                    details.serialize());
        }
    }

    /**
     * Returns the drop location of a tab
     * 
     * @param tab
     *            The tab that was dragged
     * @param event
     *            The drag event
     * @return
     */
    protected VerticalDropLocation getDropLocation(StackItem tab,
            VDragEvent event) {
        VerticalDropLocation location;
        if (tab.isOpen()) {
            location = VDragDropUtil.getVerticalDropLocation(tab.getElement(),
                    Util.getTouchOrMouseClientY(event.getCurrentGwtEvent()),
                    tabTopBottomDropRatio);
        } else {
            location = VDragDropUtil
                    .getVerticalDropLocation(tab.getWidget(0).getElement(),
                            Util.getTouchOrMouseClientY(
                                    event.getCurrentGwtEvent()),
                    tabTopBottomDropRatio);
        }
        return location;
    }

    /**
     * Emphasisizes a container element
     * 
     * @param element
     */
    protected void emphasis(Element element, VDragEvent event) {

        // Find the tab
        StackItem tab = WidgetUtil.findWidget(element, StackItem.class);
        if (tab != null && getElement().isOrHasChild(tab.getElement())
                && currentlyEmphasised != tab) {
            VerticalDropLocation location = getDropLocation(tab, event);

            if (location == VerticalDropLocation.MIDDLE) {
                if (tab.isOpen()) {
                    tab.addStyleName(CLASSNAME_OVER);
                } else {
                    tab.getWidget(0).addStyleName(CLASSNAME_OVER);
                }
            } else if (!spacer.isAttached()) {
                if (location == VerticalDropLocation.TOP) {
                    insertSpacer(spacer, getElement(), getWidgetIndex(tab));
                    tab.setHeight(
                            (tab.getOffsetHeight() - spacer.getOffsetHeight())
                                    + "px");
                } else if (location == VerticalDropLocation.BOTTOM) {
                    insertSpacer(spacer, getElement(), getWidgetIndex(tab) + 1);
                    int newHeight = tab.getOffsetHeight()
                            - spacer.getOffsetHeight();
                    if (getWidgetIndex(spacer) == getWidgetCount() - 1) {
                        newHeight -= spacer.getOffsetHeight();
                    }
                    if (newHeight >= 0) {
                        tab.setHeight(newHeight + "px");
                    }
                }
            }
            currentlyEmphasised = tab;
        }
    }

    private void insertSpacer(Widget spacer,
            com.google.gwt.dom.client.Element container, int beforeIndex) {
        // Validate index; adjust if the widget is already a child of this
        // panel.
        beforeIndex = adjustIndex(spacer, beforeIndex);

        // Detach new child.
        spacer.removeFromParent();

        // We don't add the spacer to the children otherwise we mess the
        // accordion logic.

        // Physical attach.
        DOM.insertChild(container, spacer.getElement(), beforeIndex);

        // Adopt.
        adopt(spacer);
    }

    private boolean removeSpacer(Widget spacer) {
        // Validate.
        if (spacer.getParent() != this) {
            return false;
        }
        // Orphan.
        try {
            orphan(spacer);
        } finally {
            // Physical detach.
            Element elem = spacer.getElement();
            DOM.getParent(elem).removeChild(elem);

            // We don't remove the spacer from the children otherwise we mess
            // the accordion logic.
        }
        return true;
    }

    /**
     * Removes any previous emphasis made by drag&amp;drop
     */
    protected void deEmphasis() {
        if (currentlyEmphasised != null) {
            currentlyEmphasised.removeStyleName(CLASSNAME_OVER);
            currentlyEmphasised.getWidget(0).removeStyleName(CLASSNAME_OVER);
            if (spacer.isAttached()) {

                int newHeight = currentlyEmphasised.getHeight()
                        + spacer.getOffsetHeight();

                if (getWidgetIndex(spacer) == getWidgetCount() - 1) {
                    newHeight += spacer.getOffsetHeight();
                }

                currentlyEmphasised.setHeight(newHeight + "px");

                removeSpacer(spacer);
            }
            currentlyEmphasised = null;
        }
    }

    @Override
    public int getTabContentPosition(Widget w) {
        for (int i = 0; i < getTabCount(); i++) {
            ComponentConnector tabContent = getTab(i);
            if (tabContent.getWidget() == w) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getTabPosition(Widget tabWidget) {
        StackItem tab = WidgetUtil.findWidget(tabWidget.getElement(),
                StackItem.class);
        if (tab != null && getElement().isOrHasChild(tab.getElement())) {
            int i = 0;
            for (StackItem itm : getStackItems()) {
                if (tab == itm) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public VDragFilter getDragFilter() {
        return dragFilter;
    }

    IframeCoverUtility getIframeCoverUtility() {
        return iframeCoverUtility;
    }

    public float getTabTopBottomDropRatio() {
        return tabTopBottomDropRatio;
    }

    public void setTabTopBottomDropRatio(float tabTopBottomDropRatio) {
        this.tabTopBottomDropRatio = tabTopBottomDropRatio;
    }

    @Override
    public void setDragFilter(VDragFilter filter) {
        dragFilter = filter;
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
        iframeShimsEnabled(isIframeShimsEnabled());
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
