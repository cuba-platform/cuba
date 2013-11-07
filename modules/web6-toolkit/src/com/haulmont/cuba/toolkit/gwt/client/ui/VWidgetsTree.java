/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.Icon;
import com.vaadin.terminal.gwt.client.ui.VTree;
import com.vaadin.terminal.gwt.client.ui.layout.CellBasedLayout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author gorodnov
 * @version $Id$
 */
public class VWidgetsTree extends VTree implements Container {

    private static final String CLASSNAME = "widgets-tree";

    private Map<Widget, WidgetTreeNode> widgetNodes = new HashMap<Widget, WidgetTreeNode>();

    private CellBasedLayout.Spacing borderPaddingsInfo = null;

    public VWidgetsTree() {
        setStyleName(CLASSNAME);
    }

    @Override
    protected TreeNode createTreeNode(UIDL childUidl) {
        VConsole.log("[?] Has Widget " + childUidl.getBooleanAttribute("hasWidget"));

        if (childUidl.getBooleanAttribute("hasWidget")) {
            VConsole.log(">> Create Widget Node");

            return new WidgetTreeNode(childUidl);
        } else {
            VConsole.log(">> Create Node");

            return super.createTreeNode(childUidl);
        }
    }

    @Override
    public void setWidth(String width) {
        if (width == null) {
            return;
        }
        super.setWidth(width);
    }

    @Override
    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        //do nothing
    }

    @Override
    public boolean hasChildComponent(Widget component) {
        return widgetNodes.containsKey(component);
    }

    @Override
    public void updateCaption(Paintable component, UIDL uidl) {
        //do nothing
    }

    @Override
    public boolean requestLayout(Set<Paintable> children) {
        return false;
    }

    @Override
    public RenderSpace getAllocatedSpace(Widget child) {
        WidgetTreeNode node = widgetNodes.get(child);
        if (borderPaddingsInfo == null) {
            detectNodeBorderPaddings(node);
        }
        int w = node.nodeWidget.getOffsetWidth() - borderPaddingsInfo.hSpacing;
        int h = node.nodeWidget.getOffsetHeight() - borderPaddingsInfo.vSpacing;
        return new RenderSpace(w, h);
    }

    private void detectNodeBorderPaddings(WidgetTreeNode node) {
        if (isAttached() && borderPaddingsInfo == null) {
            Element el = node.nodeWidget.getElement();
            DOM.setStyleAttribute(el, "overflow", "hidden");
            DOM.setStyleAttribute(el, "width", "0px");
            DOM.setStyleAttribute(el, "height", "0px");

            int w = el.getOffsetWidth();
            int h = el.getOffsetHeight();

            borderPaddingsInfo = new CellBasedLayout.Spacing(w, h);

            DOM.setStyleAttribute(el, "overflow", "visible");
            DOM.setStyleAttribute(el, "width", "");
            DOM.setStyleAttribute(el, "height", "");
        }
    }

    class WidgetTreeNode extends TreeNode {
        private FlowPanel nodeContent;
        private SimplePanel nodeWidget;

        private boolean dynWidth;
        private boolean dynHeight;

        private boolean relativeSize;

        WidgetTreeNode(UIDL uidl) {
            setStyleName(CLASSNAME);
            if (!uidl.hasAttribute("caption")) {
                addStyleName(CLASSNAME + "-nocaption");
            }
        }

        public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
            setText(uidl.getStringAttribute("caption"));
            key = uidl.getStringAttribute("key");

            keyToNode.put(key, this);

            if (uidl.hasAttribute("al")) {
                actionKeys = uidl.getStringArrayAttribute("al");
            }

            boolean isNode = uidl.getTag().equals("node");

            // always render child UIDL
            // either for node or for leaf
            renderChildNodes(uidl.getChildIterator());
            childNodeContainer.setVisible(childNodeContainer.getWidgetCount() != 0);

            if (!isNode) {
                addStyleName(CLASSNAME + "-leaf");
            }

            addStyleName(CLASSNAME);
            if (uidl.hasAttribute("style")) {
                addStyleName(CLASSNAME + "-" + uidl.getStringAttribute("style"));
                Widget.setStyleName(nodeCaptionDiv, CLASSNAME + "-caption-"
                        + uidl.getStringAttribute("style"), true);
                childNodeContainer.addStyleName(CLASSNAME + "-children-"
                        + uidl.getStringAttribute("style"));
            }

            if (uidl.getBooleanAttribute("expanded") && !getState()) {
                setState(true, false);
            }

            if (uidl.getBooleanAttribute("selected")) {
                setSelected(true);
                // ensure that identifier is in selectedIds array (this may be a
                // partial update)
                selectedIds.add(key);
            }

            if (uidl.hasAttribute("icon")) {
                if (icon == null) {
                    icon = new Icon(client);
                    DOM.insertBefore(DOM.getFirstChild(nodeCaptionDiv), icon
                            .getElement(), nodeCaptionSpan);
                }
                icon.setUri(uidl.getStringAttribute("icon"));
            } else {
                if (icon != null) {
                    DOM.removeChild(DOM.getFirstChild(nodeCaptionDiv), icon
                            .getElement());
                    icon = null;
                }
            }

            if (isNode) {
                canExpand = uidl.hasAttribute("hasChildren");
                if (canExpand) {
                    removeStyleName("noChildren");
                } else {
                    addStyleName("noChildren");
                }
            }

            if (BrowserInfo.get().isIE6() && isAttached()) {
                fixWidth();
            }
        }

        @Override
        protected void constructDom() {
            nodeCaptionDiv = DOM.createDiv();
            DOM.setElementProperty(nodeCaptionDiv, "className", CLASSNAME + "-caption");
            Element wrapper = DOM.createDiv();
            nodeCaptionSpan = DOM.createSpan();
            DOM.appendChild(getElement(), nodeCaptionDiv);
            DOM.appendChild(nodeCaptionDiv, wrapper);
            DOM.appendChild(wrapper, nodeCaptionSpan);

            nodeContent = new FlowPanel();
            nodeContent.setStylePrimaryName(CLASSNAME + "-content");
            setWidget(nodeContent);

            nodeWidget = new SimplePanel();
            nodeWidget.setStylePrimaryName(CLASSNAME + "-widget");
            nodeContent.add(nodeWidget);

            childNodeContainer = new FlowPanel();
            childNodeContainer.setStylePrimaryName(CLASSNAME + "-children");
            nodeContent.add(childNodeContainer);
        }

        @Override
        protected void attachEvents() {
            DOM.sinkEvents(nodeCaptionDiv, Event.ONCLICK);
        }

        @Override
        protected void setState(boolean state, boolean notifyServer) {
            if (state) {
                childrenLoaded = false;
            }
            super.setState(state, notifyServer);
            if (state) {
                nodeCaptionDiv.addClassName("expanded");
            } else {
                nodeCaptionDiv.removeClassName("expanded");
            }
        }

        @Override
        protected void renderChildNodes(Iterator i) {
            childNodeContainer.clear();
            childNodeContainer.setVisible(true);

            TreeNode childTree = null;
            while (i.hasNext()) {
                final UIDL childUidl = (UIDL) i.next();
                VConsole.log(">> Child tag: " + childUidl.getTag());

                if ("widget".equals(childUidl.getTag())) {
                    VConsole.log(">> Render widget");
                    Iterator it = childUidl.getChildIterator();
                    paintWidget((UIDL) it.next());
                    continue;
                }

                // actions are in bit weird place, don't mix them with children,
                // but current node's actions
                if ("actions".equals(childUidl.getTag())) {
                    updateActionMap(childUidl);
                    continue;
                }

                childTree = createTreeNode(childUidl);
                childNodeContainer.add(childTree);
                childTree.updateFromUIDL(childUidl, client);
            }
            if (childTree != null) {
                childTree.addStyleName("last");
            }
            if (childNodeContainer.getWidgetCount() > 0) {
                childrenLoaded = true;
            }
        }

        @Override
        public void onAttach() {
            super.onAttach();
            if (borderPaddingsInfo == null) {
                detectNodeBorderPaddings(this);
            }
        }

        protected void paintWidget(UIDL uidl) {
            Paintable p = client.getPaintable(uidl);
            nodeWidget.setWidget((Widget) p);
            widgetNodes.put((Widget) p, this);
            
            p.updateFromUIDL(uidl, client);

            if (isAttached()) {
                updateWidgetSize(uidl);
            }
        }

        private void updateWidgetSize(UIDL uidl) {
            //update dynamic size
            String w = uidl.hasAttribute("width") ? uidl.getStringAttribute("width") : "";
            String h = uidl.hasAttribute("height") ? uidl.getStringAttribute("height") : "";

            dynWidth = "".equals(w);
            dynHeight = "".equals(h);

            if (isDynWidth() || isDynHeight()) {
                updateWidgetDynamicSize();
            }

            //update relative size
            float relativeWidth = Util.parseRelativeSize(w);
            float relativeHeight = Util.parseRelativeSize(h);

            if (relativeWidth >= 0f || relativeHeight >= 0f) {
                relativeSize = true;
                updateComponentRelativeSize();
            }
        }

        public boolean isRelativeSize() {
            return relativeSize;
        }

        private void updateComponentRelativeSize() {
            if (relativeSize) {
                client.handleComponentRelativeSize(nodeWidget.getWidget());
            }
        }

        private void updateWidgetDynamicSize() {
            if (isDynWidth()) {
                nodeWidget.getWidget().setWidth("");
                int w = nodeWidget.getWidget().getOffsetWidth();
                nodeWidget.getWidget().setWidth(w + "px");
            }
            if (isDynHeight()) {
                nodeWidget.getWidget().setHeight("");
                int h = nodeWidget.getWidget().getOffsetHeight();
                nodeWidget.getWidget().setHeight(h + "px");
            }
        }

        public boolean isDynHeight() {
            return dynHeight;
        }

        public boolean isDynWidth() {
            return dynWidth;
        }

        @Override
        protected void handleBrowserEvent(Event event) {
            if (disabled) return;
            if (canExpand && DOM.eventGetType(event) == Event.ONCLICK) {
                Element target = DOM.eventGetCurrentTarget(event);
                if (target == nodeCaptionDiv) {
                    toggleState();
                    DOM.eventCancelBubble(event, true);
                }
            }
        }
    }
}