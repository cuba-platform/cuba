/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.ShortcutActionHandler;
import com.vaadin.terminal.gwt.client.ui.VTabsheetPanel;

import java.util.Set;

/*
todo
upCaption/downCaption in button
upImage/downImage
icon
caption
handle errors
scrolling
toggle position
*/
public class ITogglePanel extends ComplexPanel implements Container, ClickHandler {

    protected ShortcutActionHandler shortcutHandler;
    protected RenderInformation renderInformation = new RenderInformation();

    private class ToggleButtonPanel extends FlowPanel {
        private ToggleButton button;

        public ToggleButtonPanel(String upText, String downText, ClickHandler clickHandler) {
            button = new ToggleButton(upText, downText);
            add(button);
            button.addClickHandler(clickHandler);
            setStyleName(getStyle());
        }

        public void updateText(String upText, String downText) {
            if (upText != null) {
                button.getUpFace().setText(upText);
            }
            if (downText != null) {
                button.getDownFace().setText(downText);
            }
        }

        public void setDown(boolean b) {
            button.setDown(b);
        }
    }

    public static final String CLASSNAME = "v-panel";

    protected ApplicationConnection client;
    protected String paintableId;

    protected Element captionContainer = DOM.createDiv();
    protected Element captionText = DOM.createSpan();
    protected Element contentContainer = DOM.createDiv();
    protected Element toggleButtonContainer = DOM.createDiv();
    protected Element bottomDecoration = DOM.createDiv();

    protected ToggleButtonPanel toggleButtonPanel = null;
    protected VTabsheetPanel widgetsPanel = new VTabsheetPanel();

    protected boolean rendering = false;
    protected boolean waitingForResponse = false;

    protected boolean expanded = false;

    protected String width;
    protected String height;

    public ITogglePanel() {
        setElement(DOM.createDiv());
        createDOM();
        add(widgetsPanel, contentContainer);

        DOM.sinkEvents(getElement(), Event.ONKEYDOWN);
    }

    protected void createDOM() {
        setStyleName(CLASSNAME);

        final Element captionWrapper = DOM.createDiv();
        DOM.appendChild(captionWrapper, captionContainer);
        DOM.appendChild(captionContainer, captionText);

        DOM.setElementProperty(captionWrapper, "className", CLASSNAME + "-captionwrap");
        DOM.setElementProperty(captionContainer, "className", CLASSNAME + "-caption");
        DOM.setElementProperty(contentContainer, "className", CLASSNAME + "-content");
        DOM.setElementProperty(bottomDecoration, "className", CLASSNAME + "-deco");

        DOM.appendChild(getElement(), captionContainer);
        DOM.appendChild(getElement(), contentContainer);
        DOM.appendChild(getElement(), toggleButtonContainer);
        DOM.appendChild(getElement(), bottomDecoration);

        DOM.setStyleAttribute(contentContainer, "position", "relative");
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;

        if (client.updateComponent(this, uidl, false)) {
            rendering = false;
            return;
        }

        this.client = client;
        paintableId = uidl.getId();

        //Apply forsed value
        if (uidl.hasAttribute("expanded")) {
            expanded = uidl.getBooleanAttribute("expanded");
            if (toggleButtonPanel != null) {
                toggleButtonPanel.setDown(expanded);
            }
        }

        updateToggle(uidl);

        updateComponent(uidl);

        UIDL contentUidl = uidl.getChildUIDL(0);

        int activeIndex = activeIndex();

        Widget newWidget = (Widget) client.getPaintable(contentUidl);
        if (newWidget != null) {
            if (widgetsPanel.getWidgetCount() < activeIndex + 1) {
                widgetsPanel.add(newWidget);
            } else {
                Widget oldWidget = widgetsPanel.getWidget(activeIndex);
                if (oldWidget != newWidget) {
                    widgetsPanel.insert(newWidget, activeIndex);

                    widgetsPanel.remove(oldWidget);
                    client.unregisterPaintable((Paintable) oldWidget);
                }
            }

            widgetsPanel.showWidget(activeIndex);

            ((Paintable) newWidget).updateFromUIDL(contentUidl, client);

            if (contentUidl.hasAttribute("cached")) {
                client.handleComponentRelativeSize(newWidget);
            }
        }

        // We may have actions attached to this panel
        if (uidl.getChildCount() > 1) {
            final int cnt = uidl.getChildCount();
            for (int i = 1; i < cnt; i++) {
                UIDL childUidl = uidl.getChildUIDL(i);
                if (childUidl.getTag().equals("actions")) {
                    if (shortcutHandler == null) {
                        shortcutHandler = new ShortcutActionHandler(paintableId, client);
                    }
                    shortcutHandler.updateActionMap(childUidl);
                }
            }
        }

        if (isAttached()) {

            updateContentHeight();

            reSize();

            renderInformation.updateSize(getElement());
        }

        waitingForResponse = false;
        rendering = false;
    }

    protected void updateToggle(UIDL uidl) {
        if (uidl.getBooleanAttribute("hideToggle")) {
            if (toggleButtonPanel != null) {
                remove(toggleButtonPanel);
                toggleButtonPanel = null;
            }
        } else if (toggleButtonPanel == null) {
            String upText = uidl.getStringAttribute("expandText");
            String downText = uidl.getStringAttribute("collapseText");
            toggleButtonPanel = new ToggleButtonPanel(
                    upText == null ? "+" : upText,
                    downText == null ? "-" : downText,
                    this);
            add(toggleButtonPanel, toggleButtonContainer);
        }
    }

    protected void reSize() {
        int w = -1;
        int h = -1;
        int minWidth = 0;

        if (!(width == null || "".equals(width))) {
            w = renderSpace.getWidth();
        } else {
            minWidth = getOffsetWidth();
        }
        if (!(height == null || "".equals(height))) {
            h = renderSpace.getHeight();
        }

        widgetsPanel.fixVisibleTabSize(w, h, minWidth);
        runHacks();
    }

    private void runHacks() {
        //Fix issue with height in Safari and Chrome
        if (BrowserInfo.get().getWebkitVersion() > 0) {
            if (height == null || "".equals(height)) {
                Widget widget = widgetsPanel.getWidget(widgetsPanel.getVisibleWidget());
                int h = widget.getOffsetHeight();
                DOM.setStyleAttribute(contentContainer, "height", h + "px");
            }
        }
    }

    protected void updateComponent(UIDL uidl) {
        // Handle caption displaying and style names, prior generics.
        // Affects size
        // calculations

        // Restore default stylenames
        DOM.setElementProperty(contentContainer, "className", CLASSNAME + "-content");
        DOM.setElementProperty(captionContainer, "className", CLASSNAME + "-caption");
        DOM.setElementProperty(bottomDecoration, "className", CLASSNAME + "-deco");
        if (toggleButtonPanel != null) {
            toggleButtonPanel.setStyleName(getStyle());
        }
        boolean hasCaption = false;
        if (uidl.hasAttribute("caption")
                && !uidl.getStringAttribute("caption").equals("")) {
            setCaption(uidl.getStringAttribute("caption"));
            hasCaption = true;
        } else {
            setCaption("");
            DOM.setElementProperty(captionContainer, "className", CLASSNAME + "-nocaption");
        }

        // Add proper stylenames for all elements. This way we can prevent
        // unwanted CSS selector inheritance.
        if (uidl.hasAttribute("style")) {
            final String[] styles = uidl.getStringAttribute("style").split(
                    " ");
            final String captionBaseClass = CLASSNAME
                    + (hasCaption ? "-caption" : "-nocaption");
            final String contentBaseClass = CLASSNAME + "-content";
            final String decoBaseClass = CLASSNAME + "-deco";
            String captionClass = captionBaseClass;
            String contentClass = contentBaseClass;
            String decoClass = decoBaseClass;
            for (final String style : styles) {
                captionClass += " " + captionBaseClass + "-" + style;
                contentClass += " " + contentBaseClass + "-" + style;
                decoClass += " " + decoBaseClass + "-" + style;
            }
            DOM.setElementProperty(contentContainer, "className", contentClass);
            DOM.setElementProperty(captionContainer, "className", captionClass);
            DOM.setElementProperty(bottomDecoration, "className", decoClass);
            if (toggleButtonPanel != null) {
                String toggleStyle = getStyle();
                for (final String style : styles) {
                    toggleStyle += " " + (CLASSNAME + "-toggle-") + style;
                }
                toggleButtonPanel.setStyleName(toggleStyle);
            }
        }
    }

    protected int captionPaddingHorizontal = -1;
    protected int borderPaddingHorizontal = -1;
    protected int borderPaddingVertical = -1;

    private int getContainerBorderHeight() {
        if (borderPaddingVertical < 0) {
            detectContainerBorders();
        }
        return borderPaddingVertical;
    }

    private int getContainerBorderWidth() {
        if (borderPaddingHorizontal < 0) {
            detectContainerBorders();
        }
        return borderPaddingHorizontal;
    }

    private void detectContainerBorders() {
        DOM.setStyleAttribute(contentContainer, "overflow", "hidden");

        borderPaddingHorizontal = Util.measureHorizontalPaddingAndBorder(contentContainer, 2);
        borderPaddingVertical = Util.measureVerticalBorder(contentContainer);

        DOM.setStyleAttribute(contentContainer, "overflow", "auto");

        captionPaddingHorizontal = Util.measureHorizontalPaddingAndBorder(
                captionContainer, 26);
    }

    private void setCaption(String text) {
        DOM.setInnerHTML(captionText, text);
    }

    public void onClick(ClickEvent event) {
        if (waitingForResponse) {
            return;
        }

        client.updateVariable(paintableId, "toggle", "", true);

        togglePanel();

        waitingForResponse = true;
    }

    protected void togglePanel() {
        expanded = !expanded;
        if (toggleButtonPanel != null) {
            toggleButtonPanel.setDown(expanded);
        }
    }

    private String getStyle() {
        return CLASSNAME + "-toggle" + " " + CLASSNAME + "-toggle-" + (expanded ? "down" : "up");
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        widgetsPanel.replaceComponent(oldComponent, newComponent);
    }

    public boolean hasChildComponent(Widget component) {
        return widgetsPanel.getWidgetIndex(component) > -1;
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        //todo
    }

    public boolean requestLayout(Set<Paintable> children) {
        if (height != null && !"".equals(height)
                && width != null && !"".equals(width)) {
            return true;
        }

        reSize();

        return !renderInformation.updateSize(getElement());
    }

    private RenderSpace renderSpace = new RenderSpace(0, 0, false);

    public RenderSpace getAllocatedSpace(Widget child) {
        return renderSpace;
    }

    @Override
    public void setWidth(String width) {
        if (this.width != null && this.width.equals(width)) {
            return;
        }

        super.setWidth(width);

        this.width = width;

        if (width == null || "".equals(width)) {
            renderSpace.setWidth(0);
            DOM.setStyleAttribute(contentContainer, "width", "");
        } else {
            int contentWidth = getOffsetWidth() - getContainerBorderWidth();
            if (contentWidth < 0) {
                contentWidth = 0;
            }
            renderSpace.setWidth(contentWidth);
            DOM.setStyleAttribute(contentContainer, "width", contentWidth + "px");
        }

        if (!rendering) {
            reSize();
        }
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        this.height = height;

        updateContentHeight();

        if (!rendering) {
            reSize();
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    protected void updateContentHeight() {
        if (height == null || "".equals(height)) {
            renderSpace.setHeight(0);
            DOM.setStyleAttribute(contentContainer, "height", "");
        } else {
            int toggleButtonHeight = 0;
            if (toggleButtonPanel != null) {
                toggleButtonHeight = toggleButtonPanel.getOffsetHeight();
            }
            int contentHeight = getOffsetHeight() - getContainerBorderHeight()
                    - captionContainer.getOffsetHeight() - bottomDecoration.getOffsetHeight()
                    - toggleButtonHeight;
            if (contentHeight < 0) {
                contentHeight = 0;
            }
            renderSpace.setHeight(contentHeight);
            DOM.setStyleAttribute(contentContainer, "height", contentHeight + "px");
        }
    }

    private int expandedIndex = -1;
    private int collapsedIndex = -1;

    protected int activeIndex() {
        if (expandedIndex < 0 || collapsedIndex < 0) {
            if (expanded) {
                expandedIndex = 0;
                collapsedIndex = 1;
            } else {
                collapsedIndex = 0;
                expandedIndex = 1;
            }
        }
        return expanded ? expandedIndex : collapsedIndex;
    }
}
