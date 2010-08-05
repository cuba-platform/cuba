/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 04.08.2010 11:30:52
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.*;
import com.vaadin.terminal.gwt.client.ui.VTree;

import java.util.Iterator;
import java.util.Set;

public class VWidgetsTree extends VTree {

    private static final String CLASSNAME = "widgets-tree";

    public VWidgetsTree() {
        super();
        setStyleName(CLASSNAME);
    }

    @Override
    protected TreeNode createTreeNode(UIDL childUidl) {
        if (childUidl.getBooleanAttribute("hasWidget")) {
            return new WidgetTreeNode(childUidl);
        } else {
            return super.createTreeNode(childUidl);
        }
    }

    class WidgetTreeNode extends TreeNode implements Container {

        private FlowPanel nodeContent;
        private SimplePanel nodeWidget;

        private boolean dynWidth;
        private boolean dynHeight;

        private UIDL widgetUidl;

        WidgetTreeNode(UIDL uidl) {
            widgetUidl = uidl;
            setStyleName(CLASSNAME);
            if (!uidl.hasAttribute("caption")) {
                addStyleName(CLASSNAME + "-nocaption");
            }
        }

        @Override
        protected void constructDom() {
            // workaround for a very weird IE6 issue #1245
            if (BrowserInfo.get().isIE6()) {
                ie6compatnode = DOM.createDiv();
                setStyleName(ie6compatnode, CLASSNAME + "-ie6compatnode");
                DOM.setInnerText(ie6compatnode, " ");
                DOM.appendChild(getElement(), ie6compatnode);

                DOM.sinkEvents(ie6compatnode, Event.ONCLICK);
            }

            nodeCaptionDiv = DOM.createDiv();
            DOM.setElementProperty(nodeCaptionDiv, "className", CLASSNAME
                    + "-caption");
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
        protected void renderChildNodes(Iterator i) {
            childNodeContainer.clear();
            childNodeContainer.setVisible(true);

            TreeNode childTree = null;
            while (i.hasNext()) {
                final UIDL childUidl = (UIDL) i.next();
                if ("widget".equals(childUidl.getTag())) {
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
                if (ie6compatnode != null) {
                    childNodeContainer.add(childTree);
                }
                childTree.updateFromUIDL(childUidl, client);
                if (ie6compatnode == null) {
                    childNodeContainer.add(childTree);
                }
            }
            if (childTree != null) {
                childTree.addStyleName("last");
            }
            childrenLoaded = true;
        }

        protected void paintWidget(UIDL uidl) {
            Paintable p = client.getPaintable(uidl);
            nodeWidget.setWidget((Widget) p);
            p.updateFromUIDL(uidl, client);

            if (isAttached()) {
                updateWidgetSize(uidl);
            }
        }

        @Override
        public void onAttach() {
            super.onAttach();
            updateWidgetSize(widgetUidl);
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
        public void onBrowserEvent(Event event) {
            //do nothing
        }

        public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
            //do nothing
        }

        public boolean hasChildComponent(Widget component) {
            return nodeWidget.getWidget() != null && nodeWidget.getWidget().equals(component);
        }

        public void updateCaption(Paintable component, UIDL uidl) {
            //todo gorodnov: implement this method when it will be needed, now  I can't see any reasons to do it
        }

        public boolean requestLayout(Set<Paintable> children) {
            return true;
        }

        public RenderSpace getAllocatedSpace(Widget child) {
            int w = child.getOffsetWidth();
            int h = child.getOffsetHeight();
            return new RenderSpace(w, h);
        }
    }
}
