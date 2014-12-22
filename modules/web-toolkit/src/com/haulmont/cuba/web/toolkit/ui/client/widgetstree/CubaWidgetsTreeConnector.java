/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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

/**
 * @author artamonov
 * @version $Id$
 */
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