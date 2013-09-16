/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.widgetstree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ui.VTree;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaWidgetsTreeWidget extends VTree {

    public class WidgetTreeNode extends TreeNode {
        protected Widget nodeWidget;
        private SimplePanel nodeWidgetRoot;

        public void setNodeWidget(Widget nodeWidget) {
            this.nodeWidget = nodeWidget;
            nodeWidgetRoot.setWidget(nodeWidget);
        }

        @Override
        protected void constructDom() {
            addStyleName(CLASSNAME);

            nodeCaptionDiv = DOM.createDiv();
            DOM.setElementProperty(nodeCaptionDiv, "className", CLASSNAME
                    + "-caption");
            Element wrapper = DOM.createDiv();
            nodeCaptionSpan = DOM.createSpan();
            DOM.appendChild(getElement(), nodeCaptionDiv);
            DOM.appendChild(nodeCaptionDiv, wrapper);
            DOM.appendChild(wrapper, nodeCaptionSpan);

            if (BrowserInfo.get().isOpera()) {
                /*
                 * Focus the caption div of the node to get keyboard navigation
                 * to work without scrolling up or down when focusing a node.
                 */
                nodeCaptionDiv.setTabIndex(-1);
            }

            FlowPanel nodeContent = new FlowPanel();
            nodeContent.setStylePrimaryName(CLASSNAME + "-content");
            setWidget(nodeContent);

            nodeWidgetRoot = new SimplePanel();
            nodeWidgetRoot.setStylePrimaryName(CLASSNAME + "-widget");
            nodeContent.add(nodeWidgetRoot);

            childNodeContainer = new FlowPanel();
            childNodeContainer.setStyleName(CLASSNAME + "-children");
            nodeContent.add(childNodeContainer);
        }

        @Override
        public void setText(String text) {
            super.setText("");
        }
    }

    @Override
    public void setStyleName(String style) {
        super.setStyleName(style + " cuba-widgetstree");
    }
}