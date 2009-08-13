/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 10.08.2009 19:19:26
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import com.itmill.toolkit.terminal.gwt.client.*;
import com.itmill.toolkit.terminal.gwt.client.ui.ITabsheetPanel;

import java.util.Set;

/*
todo
support actions
upCaption/downCaption in button
upImage/downImage
icon
caption
handle errors
scrolling
toggle position


может лучше сделать DeckPanel которая будет управляться с сервера?
 */
public class ITogglePanel extends ComplexPanel implements Container, ClickListener {

    private class ToggleButtonPanel extends FlowPanel {
        private ToggleButton button = new ToggleButton("+", "-");

        private ToggleButtonPanel(ClickListener listener) {
            add(button);
            button.addClickListener(listener);
            setStyleName(CLASSNAME + "-toggle");
        }

        public void updateButton(boolean newValue) {
            button.setDown(newValue);
        }
    }

    public static final String CLASSNAME = "i-panel";

    protected ApplicationConnection client;
    protected String paintableId;

    protected Element captionContainer = DOM.createDiv();
    protected Element captionText = DOM.createSpan();
    protected Element contentContainer = DOM.createDiv();
    protected Element toggleButtonContainer = DOM.createDiv();
    protected Element bottomDecoration = DOM.createDiv(); //as in Panel

    protected ToggleButtonPanel toggleButtonPanel = null;
    protected ITabsheetPanel widgetsPanel = new ITabsheetPanel();

    protected boolean rendering = false;
    protected boolean waitingForResponse = false;

    protected boolean expanded = false;

    protected String width;
    protected String height;

    public ITogglePanel() {
        setElement(DOM.createDiv());
        createDOM();
        add(widgetsPanel, contentContainer);
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

        updateComponent(uidl);

        if (uidl.getBooleanAttribute("hideToggle")) {
            if (toggleButtonPanel != null) {
                remove(toggleButtonPanel);
                toggleButtonPanel = null;
            }
        } else if (toggleButtonPanel == null) {
            toggleButtonPanel = new ToggleButtonPanel(this);
            add(toggleButtonPanel, toggleButtonContainer);
        }

        //Apply forsed value
        if (uidl.hasAttribute("expanded")) {
            expanded = uidl.getBooleanAttribute("expanded");
            if (toggleButtonPanel != null) {
                toggleButtonPanel.updateButton(expanded);
            }
        }

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

        if (isAttached()) {
            reSize();
        }

        waitingForResponse = false;
        rendering = false;
    }

    protected void reSize() {

        updateContentHeight();

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
    }

    protected void updateComponent(UIDL uidl) {
        // Handle caption displaying and style names, prior generics.
        // Affects size
        // calculations

        // Restore default stylenames
        DOM.setElementProperty(contentContainer, "className", CLASSNAME + "-content");
        DOM.setElementProperty(captionContainer, "className", CLASSNAME + "-caption");
        DOM.setElementProperty(bottomDecoration, "className", CLASSNAME + "-deco");
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

    public void onClick(Widget sender) {
        if (waitingForResponse) {
            return;
        }

//        DeferredCommand.addCommand(new Command() {
//            public void execute() {
                Widget w = widgetsPanel.getWidget(widgetsPanel.getVisibleWidget());
                DOM.setStyleAttribute(DOM.getParent(w.getElement()),
                        "visibility", "hidden");
                client.updateVariable(paintableId, "toggle", "", true);
//            }
//        });

        togglePanel();

        waitingForResponse = true;
    }

    protected void togglePanel() {
        expanded = !expanded;
        if (toggleButtonPanel != null) {
            toggleButtonPanel.updateButton(expanded);
        }
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
        return false;
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
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        this.height = height;
        
        updateContentHeight();
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
