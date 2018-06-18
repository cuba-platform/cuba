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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.communication.StateChangeEvent.StateChangeHandler;
import com.vaadin.client.ui.*;
import com.vaadin.client.ui.VAccordion.StackItem;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VFormLayout;
import com.vaadin.client.ui.VLink;
import com.vaadin.client.ui.VTabsheet.TabCaption;
import com.vaadin.client.ui.dd.VTransferable;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Button;
import com.vaadin.ui.Link;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VDragFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.VGrabFilter;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.accordion.VDDAccordion;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.*;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.tabsheet.VDDTabSheet;

/**
 * Utility class for Drag and Drop operations
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.5.0
 */
public final class VDragDropUtil {

    private VDragDropUtil() {
        // Prevent instantiation
    }

    /**
     * Get the vertical drop location in a ordered layout
     * 
     * @param element
     *            The target element or cell
     * @param clientY
     *            The client y-coordinate
     * @param topBottomRatio
     *            The ratio how the cell has been divided
     * @return The drop location
     */
    public static VerticalDropLocation getVerticalDropLocation(Element element,
            int clientY, double topBottomRatio) {
        int offsetHeight = element.getOffsetHeight();
        return getVerticalDropLocation(element, offsetHeight, clientY,
                topBottomRatio);
    }

    /**
     * Get the vertical drop location in a ordered layout
     * 
     * @param element
     *            The target element or cell
     * @param offsetHeight
     *            The height of the cell
     * @param clientY
     *            The width of the cell
     * @param topBottomRatio
     *            The ratio of the cell
     * @return The location of the drop
     */
    public static VerticalDropLocation getVerticalDropLocation(Element element,
            int offsetHeight, int clientY, double topBottomRatio) {

        int absoluteTop = element.getAbsoluteTop();
        int fromTop = clientY - absoluteTop;

        float percentageFromTop = (fromTop / (float) offsetHeight);
        if (percentageFromTop < topBottomRatio) {
            return VerticalDropLocation.TOP;
        } else if (percentageFromTop > 1 - topBottomRatio) {
            return VerticalDropLocation.BOTTOM;
        } else {
            return VerticalDropLocation.MIDDLE;
        }
    }

    /**
     * Get the horizontal drop location in an ordered layout
     * 
     * @param element
     *            The target element or cell
     * @param clientX
     *            The x-coordinate of the drop
     * @param leftRightRatio
     *            The ratio of how the cell has been divided
     * @return the drop location relative to the cell
     */
    public static HorizontalDropLocation getHorizontalDropLocation(
            Element element, int clientX, double leftRightRatio) {

        int absoluteLeft = element.getAbsoluteLeft();
        int offsetWidth = element.getOffsetWidth();
        int fromTop = clientX - absoluteLeft;

        float percentageFromTop = (fromTop / (float) offsetWidth);
        if (percentageFromTop < leftRightRatio) {
            return HorizontalDropLocation.LEFT;
        } else if (percentageFromTop > 1 - leftRightRatio) {
            return HorizontalDropLocation.RIGHT;
        } else {
            return HorizontalDropLocation.CENTER;
        }
    }

    /**
     * Creates a transferable for the tabsheet
     * 
     * @param tabsheet
     *            The tabsheet the event occurred
     * @param tab
     *            The tab on which the event occurred
     * @param event
     *            The event
     * @param root
     *            The root widget
     * @return
     */
    private static VTransferable createTabsheetTransferableFromMouseDown(
            VDDTabSheet tabsheet, TabCaption tab, NativeEvent event) {
        VTransferable transferable = new VTransferable();
        transferable.setDragSource(Util.findConnectorFor(tabsheet));
        transferable.setData(Constants.TRANSFERABLE_DETAIL_COMPONENT,
                tabsheet.getTab(tabsheet.getTabPosition(tab)));
        transferable.setData(Constants.TRANSFERABLE_DETAIL_INDEX,
                tabsheet.getTabPosition(tab));
        transferable.setData(Constants.TRANSFERABLE_DETAIL_MOUSEDOWN,
                MouseEventDetailsBuilder.buildMouseEventDetails(event)
                        .serialize());
        return transferable;
    }

    /**
     * Creates a transferable for the Accordion
     * 
     * @param accordion
     *            The Accordion where the event occurred
     * @param tab
     *            The tab on which the event occurred
     * @param event
     *            The event
     * @param root
     *            The root widget
     * @return
     */
    private static VTransferable createAccordionTransferableFromMouseDown(
            VDDAccordion accordion, StackItem tab, NativeEvent event) {
        VTransferable transferable = new VTransferable();
        transferable.setDragSource(Util.findConnectorFor(accordion));
        transferable.setData(Constants.TRANSFERABLE_DETAIL_COMPONENT,
                accordion.getTab(accordion.getTabPosition(tab)));
        transferable.setData(Constants.TRANSFERABLE_DETAIL_INDEX,
                accordion.getTabPosition(tab));
        transferable.setData(Constants.TRANSFERABLE_DETAIL_MOUSEDOWN,
                MouseEventDetailsBuilder.buildMouseEventDetails(event)
                        .serialize());
        return transferable;
    }

    /**
     * Creates a transferable from a mouse down event. Returns null if creation
     * was not successful.
     * 
     * @param event
     *            The mouse down event
     * @param root
     *            The root layout from where the component is dragged
     * @return A transferable or NULL if something failed
     */
    public static VTransferable createLayoutTransferableFromMouseDown(
            NativeEvent event, Widget root, Widget target) {

        // NPE check
        if (target == null) {
            VConsole.error("Could not find widget");
            return null;
        }

        VConsole.log("Creating transferable for root:" + root.getElement()
                + "\t target:" + target.getElement());

        // Special treatment for Tabsheet
        if (root instanceof VDDTabSheet) {
            VDDTabSheet tabsheet = (VDDTabSheet) root;
            TabCaption tab = WidgetUtil.findWidget(target.getElement(),
                    TabCaption.class);
            if (tab != null
                    && tabsheet.getElement().isOrHasChild(tab.getElement())) {
                return createTabsheetTransferableFromMouseDown(tabsheet, tab,
                        event);
            } else {
                // Not a tab
                VConsole.error("Not on tab");
                return null;
            }
        }

        // Special treatment for Accordion
        if (root instanceof VDDAccordion) {
            VDDAccordion accordion = (VDDAccordion) root;
            StackItem tab = WidgetUtil.findWidget(target.getElement(),
                    StackItem.class);
            if (tab != null
                    && accordion.getElement().isOrHasChild(tab.getElement())) {
                return createAccordionTransferableFromMouseDown(accordion, tab,
                        event);
            } else {
                // Not on tab
                VConsole.error("Not on tab");
                return null;
            }
        }

        // Ensure we have the right widget
        target = getTransferableWidget(target);

        // Find the containing layout of the component
        ComponentConnector widgetConnector = Util.findConnectorFor(target);
        if (widgetConnector == null) {
            VConsole.error("No connector found for " + target);
            return null;
        }

        // Iterate until parent either is the root or a layout with drag and
        // drop enabled
        ComponentConnector layoutConnector = (ComponentConnector) widgetConnector
                .getParent();
        Widget layout = layoutConnector.getWidget();
        while (layout != root && layout != null && layoutConnector != null) {
            if (isDraggingEnabled(layoutConnector, target)) {
                // Found parent layout with support for drag and drop
                break;
            }
            target = layout;
            widgetConnector = layoutConnector;

            layoutConnector = (ComponentConnector) layoutConnector.getParent();
            if (layoutConnector == null) {
                break;
            }

            layout = layoutConnector.getWidget();
        }

        // Consistency check
        if (target == null) {
            VConsole.error("Target was null");
            return null;
        }
        if (root == target) {
            /*
             * Dispatch event again so parent layout can handle the drag of the
             * root
             */
            target.getElement().dispatchEvent(createMouseDownEvent(event));
            return null;
        }
        if (layoutConnector == null) {
            VConsole.error("No layout connector was found");
            return null;
        }

        return createTransferable(layoutConnector, widgetConnector, event);
    }

    private static NativeEvent createMouseDownEvent(NativeEvent e) {
        return Document.get().createMouseDownEvent(0, e.getScreenX(),
                e.getScreenY(), e.getClientX(), e.getClientY(), e.getCtrlKey(),
                e.getAltKey(), e.getShiftKey(), e.getMetaKey(), e.getButton());
    }

    private static VTransferable createTransferable(ComponentConnector layout,
            ComponentConnector widgetConnector, NativeEvent event) {
        VTransferable transferable = new VTransferable();
        transferable.setDragSource(layout);
        transferable.setData(Constants.TRANSFERABLE_DETAIL_COMPONENT,
                widgetConnector);
        transferable.setData(Constants.TRANSFERABLE_DETAIL_MOUSEDOWN,
                MouseEventDetailsBuilder.buildMouseEventDetails(event)
                        .serialize());
        return transferable;
    }

    /**
     * Resolve if widget is a Vaadin Caption
     * 
     * @param w
     *            Widget to check
     * @return True if the widget is a caption widget, false otherwise
     */
    public static boolean isCaption(Widget w) {
        return w instanceof VCaption || w instanceof VFormLayout.Caption
                || w instanceof TabCaption
                || w.getElement().getClassName().contains("v-panel-caption");
    }

    /**
     * Does the same as {@link #isCaption(Widget)} but also returns true for
     * Vaadin widgets that do not have a caption like {@link Button} and
     * {@link Link}
     * 
     * @param w
     *            The widget to check
     * @return True if the widget is a caption widget, false otherwise
     */
    public static boolean isCaptionOrCaptionless(Widget w) {
        return isCaption(w) || w instanceof VButton || w instanceof VLink;
    }

    public static Widget getTransferableWidget(Widget w) {

        if (isCaption(w)) {
            // Dragging caption means dragging component the caption belongs to
            Widget owner = null;
            if (w instanceof TabCaption) {
                TabCaption caption = (TabCaption) w;
                owner = caption.getTab().getTabsheet();
            }
            if (w instanceof VCaption) {
                ComponentConnector ownerConnector = ((VCaption) w).getOwner();
                owner = ownerConnector == null ? null
                        : ownerConnector.getWidget();
            } else if (w instanceof VFormLayout.Caption) {
                ComponentConnector ownerConnector = ((VFormLayout.Caption) w)
                        .getOwner();
                owner = ownerConnector == null ? null
                        : ownerConnector.getWidget();
            }
            if (owner != null) {
                w = owner;
            }
        } else {
            // Ensure we are dealing with a Vaadin component
            ComponentConnector connector = Util.findConnectorFor(w);
            while (connector == null) {
                w = w.getParent();
                connector = Util.findConnectorFor(w);
            }
        }

        return w;
    }

    /**
     * Is dragging enabled for a component container
     * 
     * @param layout
     *            The component container to check
     * @return
     */
    public static boolean isDraggingEnabled(ComponentConnector layout,
            Widget w) {
        boolean draggingEnabled = false;
        if (layout.getWidget() instanceof VHasDragMode) {
            LayoutDragMode dm = ((VHasDragMode) layout.getWidget())
                    .getDragMode();
            draggingEnabled = dm != LayoutDragMode.NONE;
        }
        if (layout instanceof VHasDragFilter) {
            draggingEnabled = draggingEnabled
                    && ((VHasDragFilter) layout).getDragFilter().isDraggable(w);
        }
        return draggingEnabled;
    }

    /**
     * Removes the Drag and drop fake paintable from an UIDL
     * 
     * @param uidl
     *            The uidl which contains a dragdrop paintable (-ac)
     * @return UIDL stripped of the paintable
     */
    public static native UIDL removeDragDropCriteraFromUIDL(UIDL uidl)
    /*-{
      var obj = new Array();
      for(key in uidl){
          if(uidl[key][0] != "-ac"){
             obj[key] = uidl[key];
          }
      }
      return obj;
    }-*/;

    /**
     * Measures the left margin of an element
     * 
     * @param element
     *            The element to measure
     * @return Left margin in pixels
     */
    public static int measureMarginLeft(Element element) {
        return element.getAbsoluteLeft()
                - element.getParentElement().getAbsoluteLeft();
    }

    /**
     * Measures the top margin of an element
     * 
     * @param element
     *            The element to measure
     * @return Top margin in pixels
     */
    public static int measureMarginTop(Element element) {
        return element.getAbsoluteTop()
                - element.getParentElement().getAbsoluteTop();
    }

    /**
     * Adds a listener for listening for changes to
     * {@link DragAndDropAwareState}'s.
     * <p>
     * Preferrable add this to a layout connector's init() method.
     * 
     * @param connector
     *            The connector to attach to.
     * @param widget
     *            The layout widget returned by Connector.getWidget()
     */
    public static void listenToStateChangeEvents(
            final AbstractConnector connector, final Widget widget) {
        connector.addStateChangeHandler("ddState", new StateChangeHandler() {
            @Override
            public void onStateChanged(StateChangeEvent stateChangeEvent) {
                DDLayoutState state = ((DragAndDropAwareState) connector
                        .getState()).getDragAndDropState();

                if (widget instanceof VHasDragMode) {
                    ((VHasDragMode) widget).setDragMode(state.dragMode);
                }

                if (widget instanceof VHasIframeShims) {
                    ((VHasIframeShims) widget)
                            .iframeShimsEnabled(state.iframeShims);
                }

                if (widget instanceof VHasDragFilter) {
                    ((VHasDragFilter) widget)
                            .setDragFilter(new VDragFilter(state));
                }

                if (widget instanceof VHasGrabFilter) {
                    ((VHasGrabFilter) widget)
                            .setGrabFilter(new VGrabFilter(state));
                }

                if (widget instanceof VHasDragCaptionProvider) {
                    if (state.dragCaptions.size() > 0) {
                        ((VHasDragCaptionProvider) widget)
                                .setDragCaptionProvider(new VDragCaptionProvider(connector));
                    }
                }

                if (widget instanceof VHasDragImageReferenceSupport) {
                    ((VHasDragImageReferenceSupport) widget)
                            .setDragImageProvider(
                                    new VDDLayoutStateDragImageProvider(state));
                }
            }
        });
    }

    public static void updateDropHandlerFromUIDL(UIDL uidl,
            ComponentConnector connector, VDDAbstractDropHandler dropHandler) {
        VDDHasDropHandler widget = (VDDHasDropHandler) connector.getWidget();
        if (AbstractComponentConnector.isRealUpdate(uidl)
                && !uidl.hasAttribute("hidden")) {
            UIDL acceptCrit = uidl.getChildByTagName("-ac");
            if (acceptCrit == null) {
                widget.setDropHandler(null);
            } else {
                if (widget.getDropHandler() == null) {
                    widget.setDropHandler(dropHandler);
                }
                widget.getDropHandler().updateAcceptRules(acceptCrit);
            }
        }
    }

    /**
     * Returns the parent layout that the slot belongs to
     * 
     * @param slot
     *            the slot
     * @return the layout
     */
    public static native VAbstractOrderedLayout getSlotLayout(Slot slot)
    /*-{
        if(slot == null) return null;
        return slot.@com.vaadin.client.ui.orderedlayout.Slot::layout;
    }-*/;

    /**
     * Finds a slots index in a collection of slots and captions
     * 
     * @param children
     *            the children.
     * @param slot
     *            the slot to find.
     * @return the index of the slot
     */
    public static int findSlotIndex(WidgetCollection children, Slot slot) {
        int index = -1;
        for (int i = 0; i < children.size(); i++) {
            Widget w = children.get(i);
            if (w instanceof Slot) {
                index++;
                if (w == slot) {
                    break;
                }
            }
        }
        return index;
    }
}
