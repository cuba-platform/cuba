/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 12.08.2010 18:36:56
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VMarginInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class VFlowLayout extends FlowPanel implements Container {

    public static final String CLASSNAME = "flow-layout";

    private Element container = DOM.createDiv();

    private Map<Widget, ChildContainer> widgetContainers = new HashMap<Widget, ChildContainer>();

    private ApplicationConnection client;
    private String paintalbeId;

    private boolean isRendering;
    private boolean sizeHasChangedDuringRendering;

    private String width;
    private String height;

    private RenderInformation.Size activeLayoutSize = new RenderInformation.Size(0, 0);
    private VMarginInfo activeMarginsInfo = new VMarginInfo(-1);

    private int borderPaddingsWidth = -1;
    private int borderPaddingsHeight = -1;

    private boolean spacingEnabled;

    private class ChildContainer extends SimplePanel {

        private RenderInformation.FloatSize relatizeSize;

        ChildContainer(Widget w) {
            setStylePrimaryName(CLASSNAME + "-widget");
            setWidget(w);
        }

        public void setRelatizeSize(RenderInformation.FloatSize relatizeSize) {
            this.relatizeSize = relatizeSize;
        }

        public boolean hasRelativeWidth() {
            return relatizeSize != null && relatizeSize.getWidth() >= 0;
        }

        public boolean hasRelativeHeight() {
            return relatizeSize != null && relatizeSize.getHeight() >= 0;
        }

        void renderChild(UIDL uidl, ApplicationConnection client) {
            ((Paintable) getWidget()).updateFromUIDL(uidl, client);
        }
    }

    public VFlowLayout() {
        super();

        setStylePrimaryName(CLASSNAME);

        DOM.appendChild(getElement(), container);
        DOM.setElementProperty(getContainerElement(), "className", CLASSNAME + "-content");
        DOM.setInnerHTML(getContainerElement(), "<div style=\"clear:both;width:0px;height:0px;\"></div>");

        DOM.setStyleAttribute(getElement(), "overflow", "auto");
    }

    private void addChild(Widget w) {
        assert !widgetContainers.containsKey(w) : "Widget is already added";
        ChildContainer childContainer = new ChildContainer(w);
        widgetContainers.put(w, childContainer);
        add(childContainer);
    }

    @Override
    public void add(Widget w) {
        assert w instanceof ChildContainer;
        add(w, getContainerElement());
    }

    private void insertChild(Widget w, int beforeIndex) {
        assert !widgetContainers.containsKey(w) : "Widget is already inserted";
        ChildContainer childContainer = new ChildContainer(w);
        widgetContainers.put(w, childContainer);
        insert(childContainer, beforeIndex);
    }

    @Override
    public void insert(Widget w, int beforeIndex) {
        assert w instanceof ChildContainer;
        insert(w, getContainerElement(), beforeIndex, true);
    }

    @Override
    public boolean remove(Widget w) {
        assert w instanceof ChildContainer;
        return super.remove(w);
    }

    @Override
    public void setWidth(String width) {
        if (width == null || !isVisible()) {
            return;
        }

        RenderInformation.Size sizeBefore = new RenderInformation.Size(
                activeLayoutSize.getWidth(), activeLayoutSize.getHeight());

        this.width = width;

        super.setWidth(width);

        if (!"".equals(width)) {
            setActiveLayoutWidth(getOffsetWidth() - 2 * getBorderPaddingsWidth());
            super.setWidth(activeLayoutSize.getWidth() + "px");
        }

        if (isRendering) {
            sizeHasChangedDuringRendering = true;
        } else {
            boolean sameSize = (sizeBefore.equals(activeLayoutSize));
            if (!sameSize) {
                /* Must inform child components about possible size updates */
                client.runDescendentsLayout(this);
            }
        }
    }

    @Override
    public void setHeight(String height) {
        if (height == null || !isVisible()) {
            return;
        }

        RenderInformation.Size sizeBefore = new RenderInformation.Size(activeLayoutSize.getWidth(),
                activeLayoutSize.getHeight());

        this.height = height;

        super.setHeight(height);

        if (!height.equals("")) {
            setActiveLayoutHeight(getOffsetHeight() - 2 * getBorderPaddingsHeight());
            super.setHeight(activeLayoutSize.getHeight() + "px");
        }

        if (isRendering) {
            sizeHasChangedDuringRendering = true;
        } else {
            boolean sameSize = (sizeBefore.equals(activeLayoutSize));
            if (!sameSize) {
                /* Must inform child components about possible size updates */
                client.runDescendentsLayout(this);
            }
        }
    }

    private int getBorderPaddingsWidth() {
        if (borderPaddingsWidth == -1) {
            detectBorderPaddings();
        }
        return borderPaddingsWidth;
    }

    private int getBorderPaddingsHeight() {
        if (borderPaddingsHeight == -1) {
            detectBorderPaddings();
        }
        return borderPaddingsHeight;
    }

    private void detectBorderPaddings() {
        super.setWidth("0px");
        super.setHeight("0px");
        DOM.setStyleAttribute(getElement(), "overflow", "hidden");

        borderPaddingsWidth = getOffsetWidth();
        borderPaddingsHeight = getOffsetHeight();

        //restore previous component state
        DOM.setStyleAttribute(getElement(), "overflow", "auto");
        if (width != null) {
            setWidth(width);
        }
        if (height != null) {
            setHeight(height);
        }
    }

    private void setActiveLayoutWidth(int width) {
        if (width < 0) {
            width = 0;
        }
        activeLayoutSize.setWidth(width);
    }

    private void setActiveLayoutHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        activeLayoutSize.setHeight(height);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        detectBorderPaddings();
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        this.client = client;
        paintalbeId = uidl.getId();

        isRendering = true;

        if (uidl.getBooleanAttribute("cached") || uidl.getBooleanAttribute("invisible")
                || client.updateComponent(this, uidl, true)) {
            isRendering = false;
            return;
        }

        final Map<Widget, UIDL> relativeSizeChildren = new HashMap<Widget, UIDL>();

        int pos = 0;
        for (final Iterator it = uidl.getChildIterator(); it.hasNext(); pos++) {
            final UIDL childUidl = (UIDL) it.next();

            final Paintable p = client.getPaintable(childUidl);
            Widget w = (Widget) p;

            ChildContainer childContainer;
            if ((childContainer = getChildContainer(w)) == null) {
                insertChild(w, pos);
                childContainer = getChildContainer(w);
            } else {
                insert(childContainer, getContainerElement(), pos, true);
            }

            if (childUidl.hasAttribute("invisible") && childContainer.isVisible()) {
                childContainer.setVisible(false);
            } else if (!(childUidl.hasAttribute("invisible") || childContainer.isVisible())) {
                childContainer.setVisible(true);
            }

            if (!Util.isCached(childUidl)) {
                RenderInformation.FloatSize relativeSize = Util.parseRelativeSize(childUidl);
                childContainer.setRelatizeSize(relativeSize);
            }

            if (childContainer.hasRelativeWidth()) {
                relativeSizeChildren.put(w, childUidl);
            } else {
                childContainer.renderChild(childUidl, client);
                if (sizeHasChangedDuringRendering && Util.isCached(childUidl)) {
                    client.handleComponentRelativeSize(w);
                }
            }
        }

        cleanupDeletedAndRelocatedWidgets(pos);

        for (final Map.Entry<Widget, UIDL> entry : relativeSizeChildren.entrySet()) {
            final ChildContainer childContainer = getChildContainer(entry.getKey());
            childContainer.renderChild(entry.getValue(), client);
            if (sizeHasChangedDuringRendering && Util.isCached(entry.getValue())) {
                client.handleComponentRelativeSize(entry.getKey());
            }
        }

        updateMarginsAndSpacing(uidl);

        isRendering = false;
        sizeHasChangedDuringRendering = false;
    }

    private void updateMarginsAndSpacing(UIDL uidl) {
        if (!uidl.hasAttribute("invisible")) {
            int bitMask = uidl.getIntAttribute("margins");
            if (activeMarginsInfo.getBitMask() != bitMask) {
                activeMarginsInfo = new VMarginInfo(bitMask);
                if (activeMarginsInfo.hasTop()) {
                    addStyleName(CLASSNAME + "-m-t");
                } else {
                    removeStyleName(CLASSNAME + "-m-t");
                }
                if (activeMarginsInfo.hasRight()) {
                    addStyleName(CLASSNAME + "-m-r");
                } else {
                    removeStyleName(CLASSNAME + "-m-r");
                }
                if (activeMarginsInfo.hasBottom()) {
                    addStyleName(CLASSNAME + "-m-b");
                } else {
                    removeStyleName(CLASSNAME + "-m-b");
                }
                if (activeMarginsInfo.hasLeft()) {
                    addStyleName(CLASSNAME + "-m-l");
                } else {
                    removeStyleName(CLASSNAME + "-m-l");
                }
                detectBorderPaddings();   //recalculate border and paddings size
            }
            boolean spacing = uidl.getBooleanAttribute("spacing");
            if (spacingEnabled != spacing) {
                spacingEnabled = spacing;
                if (spacing) {
                    addStyleName(CLASSNAME + "-s");
                } else {
                    removeStyleName(CLASSNAME + "-s");
                }
            }
        }
    }

    private void cleanupDeletedAndRelocatedWidgets(int pos) {
        int toRemove = getChildren().size() - pos;
        while (toRemove-- > 0) {
            /* flag to not if widget has been moved and rendered elsewhere */
            boolean relocated = false;
            ChildContainer child = (ChildContainer) getChildren()
                    .get(pos);
            Widget w = child.getWidget();
            if (w == null) {
                // a rare case where child component has been relocated and
                // rendered elsewhere
                // clean widgetToComponentContainer map by iterating the correct
                // mapping
                final Iterator<Widget> it = widgetContainers.keySet()
                        .iterator();
                while (it.hasNext()) {
                    final Widget key = it.next();
                    if (widgetContainers.get(key) == child) {
                        w = key;
                        relocated = true;
                        it.remove();
                        break;
                    }
                }
                if (w == null) {
                    throw new NullPointerException();
                }
            } else {
                widgetContainers.remove(w);
            }

            remove(child);

            if (!relocated) {
                Paintable p = (Paintable) w;
                client.unregisterPaintable(p);
            }
        }

    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        ChildContainer componentContainer = widgetContainers
                .remove(oldComponent);
        if (componentContainer == null) {
            return;
        }

        componentContainer.setWidget(newComponent);
        client.unregisterPaintable((Paintable) oldComponent);
        widgetContainers.put(newComponent, componentContainer);
    }

    public boolean hasChildComponent(Widget component) {
        return widgetContainers.containsKey(component);
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        //do nothing
    }

    public boolean requestLayout(Set<Paintable> children) {
        return true;
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        int width;
        int height;

        ChildContainer childContainer = getChildContainer(child);

        if (childContainer.hasRelativeWidth()) {
            width = activeLayoutSize.getWidth();
        } else {
            width = child.getOffsetWidth();
        }

        if (childContainer.hasRelativeHeight()) {
            child.setHeight("");
        }
        height = child.getOffsetHeight();

        return new RenderSpace(width, height);
    }

    protected Element getContainerElement() {
        return container;
    }

    private ChildContainer getChildContainer(Widget w) {
        return widgetContainers.get(w);
    }
}
