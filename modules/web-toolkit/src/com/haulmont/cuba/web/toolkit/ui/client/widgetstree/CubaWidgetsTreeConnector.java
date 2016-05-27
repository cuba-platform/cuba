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

import com.haulmont.cuba.web.toolkit.ui.CubaWidgetsTree;
import com.haulmont.cuba.web.toolkit.ui.client.tree.CubaTreeConnector;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.HasComponentsConnector;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VTree;
import com.vaadin.shared.ui.Connect;

import java.util.Collections;
import java.util.List;

@Connect(CubaWidgetsTree.class)
public class CubaWidgetsTreeConnector extends CubaTreeConnector implements HasComponentsConnector {

    List<ComponentConnector> nodeWidgets;

    @Override
    public List<ComponentConnector> getChildComponents() {
        if (nodeWidgets == null) {
            return Collections.emptyList();
        }

        return nodeWidgets;
    }

    @Override
    public CubaWidgetsTreeWidget getWidget() {
        return (CubaWidgetsTreeWidget) super.getWidget();
    }

    @Override
    public void setChildComponents(List<ComponentConnector> childComponents) {
        this.nodeWidgets = childComponents;
    }

    @Override
    protected VTree.TreeNode createNode(UIDL childUidl) {
        if (childUidl.hasAttribute("widgetIndex")) {
            int widgetIndex = childUidl.getIntAttribute("widgetIndex");
            if (widgetIndex < nodeWidgets.size()) {
                ComponentConnector componentConnector = getChildComponents().get(widgetIndex);

                CubaWidgetsTreeWidget.WidgetTreeNode treeNode = getWidget().new WidgetTreeNode();
                treeNode.setNodeWidget(componentConnector.getWidget());
                return treeNode;
            }
        }
        // all branches should return instance of same TreeNode class
        return getWidget().new WidgetTreeNode();
    }
}