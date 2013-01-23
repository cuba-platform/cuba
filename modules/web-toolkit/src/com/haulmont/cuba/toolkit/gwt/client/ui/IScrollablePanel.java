package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.ShortcutActionHandler;
import com.vaadin.terminal.gwt.client.ui.VTabsheet;

import java.util.Set;

/**
 * User: Nikolay Gorodnov
 * Date: 14.04.2009
 */
public class IScrollablePanel
        extends SimplePanel implements Container
{

    public static final String CLASSNAME = "v-scrollable-panel";

    ApplicationConnection client;

    String id;

    private final Element contentNode = DOM.createDiv();

    private Element errorIndicatorElement;

    private String height;

    private Paintable layout;

    ShortcutActionHandler shortcutHandler;

    private String width = "";

    private int scrollTop;

    private int scrollLeft;

    private RenderInformation renderInformation = new RenderInformation();

    private int borderPaddingHorizontal = -1;

    private int borderPaddingVertical = -1;

    private boolean rendering;

    private int contentMarginLeft = -1;

    private String previousStyleName;

    public IScrollablePanel()
    {
        super();

        contentNode.setClassName(CLASSNAME + "-content");

        getElement().appendChild(contentNode);
        setStyleName(CLASSNAME);
        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
        DOM.sinkEvents(contentNode, Event.ONSCROLL);
        contentNode.getStyle().setProperty("position", "relative");
        getElement().getStyle().setProperty("overflow", "hidden");
    }

    @Override
    protected Element getContainerElement() {
        return contentNode;
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;
        if (!uidl.hasAttribute("cached")) {
            // Handle caption displaying and style names, prior generics.
            // Affects size
            // calculations

            // Restore default stylenames
            contentNode.setClassName(CLASSNAME + "-content");

            // Add proper stylenames for all elements. This way we can prevent
            // unwanted CSS selector inheritance.
            if (uidl.hasAttribute("style")) {
                final String[] styles = uidl.getStringAttribute("style").split(
                        " ");
                final String contentBaseClass = CLASSNAME + "-content";
                String contentClass = contentBaseClass;
                for (int i = 0; i < styles.length; i++) {
                    contentClass += " " + contentBaseClass + "-" + styles[i];
                }
                contentNode.setClassName(contentClass);
            }
        }
        // Ensure correct implementation
        if (client.updateComponent(this, uidl, false)) {
            rendering = false;
            return;
        }

        this.client = client;
        id = uidl.getId();

        handleError(uidl);

        // Render content
        final UIDL layoutUidl = uidl.getChildUIDL(0);
        final Paintable newLayout = client.getPaintable(layoutUidl);
        if (newLayout != layout) {
            if (layout != null) {
                client.unregisterPaintable(layout);
            }
            setWidget((Widget) newLayout);
            layout = newLayout;
        }
        layout.updateFromUIDL(layoutUidl, client);

        runHacks(false);
        // We may have actions attached to this panel
        if (uidl.getChildCount() > 1) {
            final int cnt = uidl.getChildCount();
            for (int i = 1; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("actions")) {
                    if (shortcutHandler == null) {
                        shortcutHandler = new ShortcutActionHandler(id, client);
                    }
                    shortcutHandler.updateActionMap(childUidl);
                }
            }
        }

        if (uidl.hasVariable("scrollTop")
                && uidl.getIntVariable("scrollTop") != scrollTop) {
            scrollTop = uidl.getIntVariable("scrollTop");
            DOM.setElementPropertyInt(contentNode, "scrollTop", scrollTop);
        }

        if (uidl.hasVariable("scrollLeft")
                && uidl.getIntVariable("scrollLeft") != scrollLeft) {
            scrollLeft = uidl.getIntVariable("scrollLeft");
            DOM.setElementPropertyInt(contentNode, "scrollLeft", scrollLeft);
        }

        rendering = false;

        runWebkitOverflowAutoFix();
    }

    @Override
    public void setStyleName(String style) {
        if (!style.equals(previousStyleName)) {
            super.setStyleName(style);
            detectContainerBorders();
            previousStyleName = style;
        }
    }

    private void handleError(UIDL uidl) {
        if (uidl.hasAttribute("error")) {
            if (errorIndicatorElement == null) {
                errorIndicatorElement = DOM.createDiv();
                DOM.setElementProperty(errorIndicatorElement, "className",
                        "v-errorindicator");
                DOM.sinkEvents(errorIndicatorElement, Event.MOUSEEVENTS);
                sinkEvents(Event.MOUSEEVENTS);
            }
            DOM.insertChild(contentNode, errorIndicatorElement, 0);
        } else if (errorIndicatorElement != null) {
            DOM.removeChild(contentNode, errorIndicatorElement);
            errorIndicatorElement = null;
        }
    }

    public void runHacks(boolean runGeckoFix) {
        if (BrowserInfo.get().isIE6() && width != null && !width.equals("")) {
            int parentPadding = Util.measureHorizontalPaddingAndBorder(
                    getElement(), 0);

            int parentWidthExcludingPadding = getElement().getOffsetWidth()
                    - parentPadding;

            int contentMarginLeft = getContentMarginLeft();

            Util.setWidthExcludingPaddingAndBorder(contentNode,
                    parentWidthExcludingPadding - contentMarginLeft, 2, false);

        }

        if ((BrowserInfo.get().isIE() || BrowserInfo.get().isFF2())
                && (width == null || width.equals(""))) {
            /*
             * IE and FF2 needs width to be specified for the root DIV so we
             * calculate that from the sizes of the caption and layout
             */
            int layoutWidth = ((Widget) layout).getOffsetWidth()
                    + getContainerBorderWidth();
            int width = layoutWidth;

            super.setWidth(width + "px");
        }

        client.runDescendentsLayout(this);

        Util.runWebkitOverflowAutoFix(contentNode);
    }

    @Override
    public void onBrowserEvent(Event event) {
        final Element target = DOM.eventGetTarget(event);
        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
            return;
        }
        if (type == Event.ONSCROLL) {
            int newscrollTop = DOM.getElementPropertyInt(contentNode,
                    "scrollTop");
            int newscrollLeft = DOM.getElementPropertyInt(contentNode,
                    "scrollLeft");
            if (client != null
                    && (newscrollLeft != scrollLeft || newscrollTop != scrollTop)) {
                scrollLeft = newscrollLeft;
                scrollTop = newscrollTop;
                client.updateVariable(id, "scrollTop", scrollTop, false);
                client.updateVariable(id, "scrollLeft", scrollLeft, false);
            }
        }
    }

    @Override
    public void setHeight(String height) {
        this.height = height;
        super.setHeight(height);
        if (height != null && height != "") {
            final int targetHeight = getOffsetHeight();
            int containerHeight = targetHeight - getContainerBorderHeight();
            if (containerHeight < 0) {
                containerHeight = 0;
            }
            DOM.setStyleAttribute(contentNode, "height", containerHeight + "px");
        } else {
            DOM.setStyleAttribute(contentNode, "height", "");
        }
        if (!rendering) {
            runHacks(true);
        }
    }

    private int getContentMarginLeft() {
        if (contentMarginLeft < 0) {
            detectContainerBorders();
        }
        return contentMarginLeft;
    }

    private int getContainerBorderHeight() {
        if (borderPaddingVertical < 0) {
            detectContainerBorders();
        }
        return borderPaddingVertical;
    }

    @Override
    public void setWidth(String width) {
        if (this.width.equals(width)) {
            return;
        }

        this.width = width;
        super.setWidth(width);
        if (!rendering) {
            runHacks(true);

            if (height.equals("")) {
                // Width change may affect height
                Util.updateRelativeChildrenAndSendSizeUpdateEvent(client, this);
            }
        }
    }

    private int getContainerBorderWidth() {
        if (borderPaddingHorizontal < 0) {
            detectContainerBorders();
        }
        return borderPaddingHorizontal;
    }

    private void detectContainerBorders() {
        DOM.setStyleAttribute(contentNode, "overflow", "hidden");

        borderPaddingHorizontal = Util.measureHorizontalBorder(contentNode);
        borderPaddingVertical = Util.measureVerticalBorder(contentNode);

        DOM.setStyleAttribute(contentNode, "overflow", "auto");

        contentMarginLeft = Util.measureMarginLeft(contentNode);
    }

    public boolean hasChildComponent(Widget component) {
        if (component != null && component == layout) {
            return true;
        } else {
            return false;
        }
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        // TODO This is untested as no layouts require this
        if (oldComponent != layout) {
            return;
        }

        setWidget(newComponent);
        layout = (Paintable) newComponent;
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        int w = 0;
        int h = 0;

        if (width != null && !width.equals("")) {
            w = getOffsetWidth() - getContainerBorderWidth();
            if (w < 0) {
                w = 0;
            }
        }

        if (height != null && !height.equals("")) {
            h = contentNode.getOffsetHeight() - getContainerBorderHeight();
            if (h < 0) {
                h = 0;
            }
        }

        return new RenderSpace(w, h, true);
    }

    public boolean requestLayout(Set<Paintable> child) {
        if (height != null && height != "" && width != null && width != "") {
            /*
             * If the height and width has been specified the child components
             * cannot make the size of the layout change
             */
            return true;
        }
        runHacks(false);
        return !renderInformation.updateSize(getElement());
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // NOP: layouts caption, errors etc not rendered in Panel
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        detectContainerBorders();
    }

    protected void runWebkitOverflowAutoFix() {
        if (BrowserInfo.get().getWebkitVersion() > 0) {
            Widget w = this;
            Container container;
            while ((container = Util.getLayout(w)) != null) {
                w = (Widget) container;
                if (w instanceof VTabsheet) {
//                    ApplicationConnection.getConsole().log("Run overflow auto fix");
//                    ((VTabsheet) w).runWebkitOverflowAutoFix();
                    break;
                }
            }
        }
    }
}
