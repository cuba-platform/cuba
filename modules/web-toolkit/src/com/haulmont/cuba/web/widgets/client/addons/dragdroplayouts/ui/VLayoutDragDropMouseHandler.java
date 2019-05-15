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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.LabelBase;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.*;
import com.vaadin.client.ui.*;
import com.vaadin.client.ui.VAccordion.StackItem;
import com.vaadin.client.ui.VTabsheet.Tab;
import com.vaadin.client.ui.VTabsheet.TabCaption;
import com.vaadin.client.ui.dd.VDragAndDropManager;
import com.vaadin.client.ui.dd.VDragEvent;
import com.vaadin.client.ui.dd.VTransferable;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.accordion.VDDAccordion;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.formlayout.VDDFormLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Mouse handler for starting component drag operations
 *
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.4.0
 */
public class VLayoutDragDropMouseHandler implements MouseDownHandler,
        TouchStartHandler, VHasDragImageReferenceSupport {

    public static final String ACTIVE_DRAG_SOURCE_STYLENAME = "v-dd-active-drag-source";
    public static final String ACTIVE_DRAG_CUSTOM_IMAGE_STYLENAME = "v-dd-active-drag-custom-image";

    private LayoutDragMode dragMode = LayoutDragMode.NONE;

    private final Widget root;

    private Widget currentDraggedWidget;

    private HandlerRegistration mouseUpHandlerReg;

    private HandlerRegistration mouseDownHandlerReg;

    private final List<HandlerRegistration> handlers = new LinkedList<HandlerRegistration>();

    private final List<DragStartListener> dragStartListeners = new ArrayList<DragStartListener>();

    private Widget attachTarget;

    private VDragImageProvider dragImageProvider;

    private boolean startDragOnMove = true;

    /**
     * A listener to listen for drag start events
     */
    public interface DragStartListener {
        /**
         * Called when a drag is about to begin
         *
         * @param widget
         *            The widget which is about to be dragged
         * @param mode
         *            The draggin mode
         * @return Should the dragging be commenced.
         */
        boolean dragStart(Widget widget, LayoutDragMode mode);
    }

    /**
     * Constructor
     *
     * @param root
     *            The root element
     * @param dragMode
     *            The drag mode of the layout
     */
    public VLayoutDragDropMouseHandler(Widget root, LayoutDragMode dragMode) {
        this.dragMode = dragMode;
        this.root = root;
    }

    /**
     * Is the mouse down event a valid mouse drag event, i.e. left mouse button
     * is pressed without any modifier keys
     *
     * @param event
     *            The mouse event
     * @return Is the mouse event a valid drag event
     */
    private boolean isMouseDragEvent(NativeEvent event) {
        boolean hasModifierKey = event.getAltKey() || event.getCtrlKey()
                || event.getMetaKey() || event.getShiftKey();
        return !(hasModifierKey || event.getButton() > NativeEvent.BUTTON_LEFT);
    }

    @Override
    public void onTouchStart(TouchStartEvent event) {
        NativeEvent nativeEvent = event.getNativeEvent();
        if (isElementNode(nativeEvent) && isChildOfRoot(nativeEvent)) {
            if (startDragOnMove) {
                initiateDragOnMove(event.getNativeEvent());
            } else {
                initiateDrag(event.getNativeEvent());
            }
        }
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        NativeEvent nativeEvent = event.getNativeEvent();
        if (isElementNode(nativeEvent) && isChildOfRoot(nativeEvent)) {
            if (startDragOnMove) {
                initiateDragOnMove(event.getNativeEvent());
            } else {
                initiateDrag(event.getNativeEvent());
            }
        }
    }

    private boolean isChildOfRoot(NativeEvent event) {
        EventTarget eventTarget = event.getEventTarget();
        Element targetElement = Element.as(eventTarget);
        if (root.getElement() != targetElement
                && root.getElement().isOrHasChild(targetElement)) {
            return true;
        }
        return false;
    }

    private boolean isElementNode(NativeEvent event) {
        EventTarget eventTarget = event.getEventTarget();
        if (Element.is(eventTarget)) {
            return true;
        }
        return false;
    }

    /**
     * Initiates the drag only on the first move event
     *
     * @param originalEvent
     *            the original Mouse Down event. Only events on elements are
     *            passed in here (Element.as() is safe without check here)
     */
    protected void initiateDragOnMove(final NativeEvent originalEvent) {
        EventTarget eventTarget = originalEvent.getEventTarget();

        boolean stopEventPropagation = false;

        Element targetElement = Element.as(eventTarget);
        Widget target = WidgetUtil.findWidget(targetElement, null);
        Widget targetParent = target.getParent();

        // Stop event propagation and prevent default behaviour if
        // - target is *not* a VTabsheet.TabCaption or
        // - drag mode is caption mode and widget is caption
        boolean isTabCaption = WidgetUtil.findWidget(target.getElement(), TabCaption.class) != null;
        boolean isCaption = VDragDropUtil.isCaptionOrCaptionless(targetParent);

        if (dragMode == LayoutDragMode.CLONE && isTabCaption == false) {

            stopEventPropagation = true;

            // overwrite stopEventPropagation flag again if
            // - root implements VHasDragFilter but
            // - target is not part of its drag filter and
            // - target is not a GWT Label based widget
            if (root instanceof VHasDragFilter) {
                if (((VHasDragFilter) root).getDragFilter()
                        .isDraggable(target) == false &&
					(target instanceof LabelBase) == false) {
                        stopEventPropagation = false;
                }
            }

            if (root instanceof VHasGrabFilter) {
                VGrabFilter grabFilter = ((VHasGrabFilter) root).getGrabFilter();
                if (grabFilter != null && !grabFilter.canBeGrabbed(root, target)) {
                    return;
                }
            }
        }

        if (dragMode == LayoutDragMode.CAPTION && isCaption) {
            stopEventPropagation = true;
        }

        if (isElementNotDraggable(targetElement)) {
            stopEventPropagation = false;
        }

        if (stopEventPropagation) {
            originalEvent.stopPropagation();
            originalEvent.preventDefault();

            // Manually focus as preventDefault() will also cancel focus
            targetElement.focus();
        }

        mouseDownHandlerReg = Event
                .addNativePreviewHandler(new NativePreviewHandler() {

                    @Override
                    public void onPreviewNativeEvent(NativePreviewEvent event) {
                        int type = event.getTypeInt();
                        if (type == Event.ONMOUSEUP
                                || type == Event.ONTOUCHCANCEL
                                || type == Event.ONTOUCHEND) {
                            mouseDownHandlerReg.removeHandler();
                            mouseDownHandlerReg = null;

                        } else if (type == Event.ONMOUSEMOVE
                                || type == Event.ONTOUCHMOVE) {
                            mouseDownHandlerReg.removeHandler();
                            mouseDownHandlerReg = null;
                            initiateDrag(originalEvent);
                        }
                    }
                });
    }

    private boolean isElementNotDraggable(Element targetElement) {
        // do not try to drag tabsheet close button it breaks close on touch devices
        return targetElement.getClassName().contains("v-tabsheet-caption-close");
    }

    /**
     * Called when the dragging a component should be initiated by both a mouse
     * down event as well as a touch start event
     *
     * FIXME This method is a BIG hack to circumvent Vaadin's very poor client
     * side API's. This will break often. Refactor once Vaadin gets a grip.
     *
     * @param event
     */
    protected void initiateDrag(NativeEvent event) {
        // Check that dragging is enabled
        if (dragMode == LayoutDragMode.NONE) {
            return;
        }

        // Dragging can only be done with left mouse button and no modifier keys
        if (!isMouseDragEvent(event) && !Util.isTouchEvent(event)) {
            return;
        }

        // Get target widget
        EventTarget eventTarget = event.getEventTarget();
        Element targetElement = Element.as(eventTarget);
        Widget target = WidgetUtil.findWidget(targetElement, null);

        if (isEventOnScrollBar(event)) {
            return;
        }

        // do not drag close button of TabSheet tab
        if (isElementNotDraggable(targetElement)) {
            VDragAndDropManager.get().interruptDrag();
            return;
        }

        // Abort if drag mode is caption mode and widget is not a caption
        boolean isPanelCaption = target instanceof VPanel && targetElement
                .getParentElement().getClassName().contains("v-panel-caption");
        boolean isCaption = isPanelCaption
                || VDragDropUtil.isCaptionOrCaptionless(target);

        if (dragMode == LayoutDragMode.CAPTION && !isCaption) {
            /*
             * Ensure target is a caption in caption mode
             */
            return;
        }

        if (dragMode == LayoutDragMode.CAPTION && isCaption) {

            /*
             * Ensure that captions in nested layouts don't get accepted if in
             * caption mode
             */

            Widget w = VDragDropUtil.getTransferableWidget(target);
            ComponentConnector c = Util.findConnectorFor(w);
            ComponentConnector parent = (ComponentConnector) c.getParent();
            if (parent.getWidget() != root) {
                return;
            }
        }

        // Create the transfarable
        VTransferable transferable = VDragDropUtil
                .createLayoutTransferableFromMouseDown(event, root, target);

        // Are we trying to drag the root layout
        if (transferable == null) {
            VConsole.log("Creating transferable on mouse down returned null");
            return;
        }

        // Resolve the component
        final Widget w;
        ComponentConnector c = null, parent = null;

        if (target instanceof TabCaption) {
            TabCaption tabCaption = (TabCaption) target;
            Tab tab = tabCaption.getTab();
            int tabIndex = ((ComplexPanel) tab.getParent()).getWidgetIndex(tab);
            VTabsheet tabsheet = tab.getTabsheet();

            w = tab;
            c = tabsheet.getTab(tabIndex);
            parent = Util.findConnectorFor(tabsheet);

        } else if (root instanceof VDDAccordion) {
            w = target;
            parent = Util.findConnectorFor(root);

            StackItem tab = WidgetUtil.findWidget(targetElement,
                    StackItem.class);
            if (tab != null
                    && root.getElement().isOrHasChild(tab.getElement())) {
                c = ((VDDAccordion) root)
                        .getTab(((VDDAccordion) root).getTabPosition(tab));
            }

        } else if (transferable
                .getData(Constants.TRANSFERABLE_DETAIL_COMPONENT) != null) {

            ComponentConnector connector = (ComponentConnector) transferable
                    .getData(Constants.TRANSFERABLE_DETAIL_COMPONENT);
            w = connector.getWidget();
            c = Util.findConnectorFor(w);
            parent = (ComponentConnector) c.getParent();

        } else {
            // Failsafe if no widget was found
            w = root;
            c = Util.findConnectorFor(w);
            parent = (ComponentConnector) c.getParent();
            VConsole.log(
                    "Could not resolve component, using root as component");
        }

        VConsole.log("Dragging widget: " + w);
        VConsole.log(" in parent: " + parent);

        // Ensure component is draggable
        if (!VDragDropUtil.isDraggingEnabled(parent, w)) {
            VConsole.log("Dragging disabled for " + w.getClass().getName()
                    + " in " + parent.getWidget().getClass().getName());
            VDragAndDropManager.get().interruptDrag();
            return;
        }

        // Announce drag start to listeners
        for (DragStartListener dl : dragStartListeners) {
            if (!dl.dragStart(w, dragMode)) {
                VDragAndDropManager.get().interruptDrag();
                return;
            }
        }

        currentDraggedWidget = w;

        // Announce to handler that we are starting a drag operation
        VDragEvent currentDragEvent = VDragAndDropManager.get()
                .startDrag(transferable, event, true);

        /*
         * Create the drag image
         */
        boolean hasDragCaption = false;

        com.google.gwt.dom.client.Element dragImageElement = null;
        if (root instanceof VHasDragCaptionProvider) {
            VDragCaptionProvider dragCaptionProvider =
                    ((VHasDragCaptionProvider) root).getDragCaptionProvider();
            if (dragCaptionProvider != null) {
                hasDragCaption = true;
                dragImageElement = dragCaptionProvider
                        .getDragCaptionElement(currentDraggedWidget);
            }
        }

        if (!hasDragCaption && dragImageProvider != null) {
            dragImageElement = dragImageProvider.getDragImageElement(w);
        }

        if (dragImageElement != null) {

            // Set stylename to proxy component as well
            if (hasDragCaption) {
                dragImageElement.addClassName(ACTIVE_DRAG_CUSTOM_IMAGE_STYLENAME);
            } else {
                dragImageElement.addClassName(ACTIVE_DRAG_SOURCE_STYLENAME);
            }

        } else if (root instanceof VCssLayout) {
            /*
             * CSS Layout does not have an enclosing div so we just use the
             * component div
             */
            dragImageElement = w.getElement();

        } else if (root instanceof VTabsheet) {
            /*
             * Tabsheet should use the dragged tab as a drag image
             */
            dragImageElement = targetElement;

        } else if (root instanceof VAccordion) {
            /*
             * Accordion should use the dragged tab as a drag image
             */
            dragImageElement = targetElement;

        } else if (root instanceof VFormLayout) {
            /*
             * Dragging a component in a form layout should include the caption
             * and error indicator as well
             */
            Element rowElement = (Element) VDDFormLayout
                    .getRowFromChildElement(
                            (com.google.gwt.dom.client.Element) w.getElement()
                                    .cast(),
                            (com.google.gwt.dom.client.Element) root
                                    .getElement().cast())
                    .cast();

            dragImageElement = rowElement;

        } else {
            /*
             * For other layouts we just use the target element;
             */
            dragImageElement = w.getElement();
        }

        Element clone;
        if (hasDragCaption) {
            currentDragEvent.setDragImage(dragImageElement);
            clone = dragImageElement;
        } else {
            currentDragEvent.createDragImage(dragImageElement, true);
            clone = currentDragEvent.getDragImage();
        }

        assert(clone != null);

        // Lock drag image dimensions
        if (!hasDragCaption) {
            clone.getStyle().setWidth(dragImageElement.getOffsetWidth(), Style.Unit.PX);
            clone.getStyle().setHeight(dragImageElement.getOffsetHeight(), Style.Unit.PX);
        }

        if (c != null && c.delegateCaptionHandling()
                && !(root instanceof VTabsheet)
                && !(root instanceof VAccordion)) {
            /*
             * Captions are not being dragged with the widget since they are
             * separate. Manually add a clone of the caption to the drag image.
             */
            if (target instanceof VCaption) {
                clone.insertFirst(targetElement.cloneNode(true));
            }
        }

        if (BrowserInfo.get().isIE()) {
            // Fix IE not aligning the drag image correctly when dragging
            // layouts
            clone.getStyle().setPosition(Position.ABSOLUTE);
        }

        currentDraggedWidget.addStyleName(ACTIVE_DRAG_SOURCE_STYLENAME);

        // Listen to mouse up for cleanup
        mouseUpHandlerReg = Event
                .addNativePreviewHandler(new Event.NativePreviewHandler() {
                    @Override
                    public void onPreviewNativeEvent(NativePreviewEvent event) {
                        if (event.getTypeInt() == Event.ONMOUSEUP
                                || event.getTypeInt() == Event.ONTOUCHEND
                                || event.getTypeInt() == Event.ONTOUCHCANCEL) {
                            if (mouseUpHandlerReg != null) {
                                mouseUpHandlerReg.removeHandler();
                                if (currentDraggedWidget != null) {

                                    currentDraggedWidget.removeStyleName(
                                            ACTIVE_DRAG_SOURCE_STYLENAME);

                                    if (dragImageProvider != null) {
                                        com.google.gwt.dom.client.Element dragImageElement = dragImageProvider
                                                .getDragImageElement(
                                                        currentDraggedWidget);
                                        if (dragImageElement != null) {
                                            dragImageElement.removeClassName(
                                                    ACTIVE_DRAG_SOURCE_STYLENAME);
                                        }
                                    }

                                    currentDraggedWidget = null;
                                }
                            }

                            // Ensure capturing is turned off at mouse up
                            Event.releaseCapture(RootPanel.getBodyElement());
                        }
                    }
                });

    }

    /*
     * Whether the event was performed on a scrollbar.
     */
    private boolean isEventOnScrollBar(NativeEvent event) {
        Element element = Element.as(event.getEventTarget());
        ;

        if (WidgetUtil.mayHaveScrollBars(element)) {

            final int nativeScrollbarSize = WidgetUtil.getNativeScrollbarSize();

            int x = WidgetUtil.getTouchOrMouseClientX(event)
                    - element.getAbsoluteLeft();
            int y = WidgetUtil.getTouchOrMouseClientY(event)
                    - element.getAbsoluteTop();

            // Hopefully we have horizontal scroll.
            final int scrollWidth = element.getScrollWidth();
            final int clientWidth = element.getClientWidth();
            if (scrollWidth > clientWidth
                    && clientWidth - nativeScrollbarSize < x) {
                return true;
            }

            // Hopefully we have vertical scroll.
            final int scrollHeight = element.getScrollHeight();
            final int clientHeight = element.getClientHeight();
            if (scrollHeight > clientHeight
                    && clientHeight - nativeScrollbarSize < y) {
                return true;
            }

        }

        return false;
    }

    /**
     * Set the current drag mode
     *
     * @param dragMode
     *            The drag mode to use
     */
    public void updateDragMode(LayoutDragMode dragMode) {
        if (dragMode == this.dragMode) {
            return;
        }

        this.dragMode = dragMode;
        if (dragMode == LayoutDragMode.NONE) {
            detach();
        } else {
            attach();
        }
    }

    /**
     * Add a drag start listener to monitor drag starts
     *
     * @param listener
     */
    public void addDragStartListener(DragStartListener listener) {
        dragStartListeners.add(listener);
    }

    /**
     * Remove a drag start listener
     *
     * @param listener
     */
    public void removeDragStartListener(DragStartListener listener) {
        dragStartListeners.remove(listener);
    }

    /**
     * Start listening to events
     */
    private void attach() {
        if (handlers.isEmpty()) {
            if (attachTarget == null) {
                handlers.add(
                        root.addDomHandler(this, MouseDownEvent.getType()));
                handlers.add(
                        root.addDomHandler(this, TouchStartEvent.getType()));
            } else {
                handlers.add(attachTarget.addDomHandler(this,
                        MouseDownEvent.getType()));
                handlers.add(attachTarget.addDomHandler(this,
                        TouchStartEvent.getType()));
            }
        }
    }

    /**
     * Stop listening to events
     */
    private void detach() {
        for (HandlerRegistration reg : handlers) {
            reg.removeHandler();
        }
        handlers.clear();
    }

    public Widget getAttachTarget() {
        return attachTarget;
    }

    public void setAttachTarget(Widget attachTarget) {
        this.attachTarget = attachTarget;
    }

    public LayoutDragMode getDragMode() {
        return dragMode;
    }

    @Override
    public void setDragImageProvider(VDragImageProvider provider) {
        this.dragImageProvider = provider;
    }

    public boolean isStartDragOnMove() {
        return startDragOnMove;
    }

    public void setStartDragOnMove(boolean startDragOnMove) {
        this.startDragOnMove = startDragOnMove;
    }
}
