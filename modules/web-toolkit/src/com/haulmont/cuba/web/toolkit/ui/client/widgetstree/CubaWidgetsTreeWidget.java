/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.widgetstree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.tree.CubaTreeWidget;
import com.vaadin.client.BrowserInfo;

/**
 */
public class CubaWidgetsTreeWidget extends CubaTreeWidget {

    public CubaWidgetsTreeWidget() {
        this.allowTextSelection = true;
    }

    public class WidgetTreeNode extends TreeNode {
        protected Widget nodeWidget;
        protected SimplePanel nodeWidgetRoot;

        public void setNodeWidget(Widget nodeWidget) {
            this.nodeWidget = nodeWidget;

            addStyleName(CLASSNAME);

            nodeCaptionDiv = DOM.createDiv();
            nodeCaptionDiv.setPropertyString("className", CLASSNAME + "-caption");

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
            nodeWidgetRoot.setWidget(nodeWidget);
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

    @Override
    protected Class<? extends Widget> getTreeNodeClass() {
        return WidgetTreeNode.class;
    }
}