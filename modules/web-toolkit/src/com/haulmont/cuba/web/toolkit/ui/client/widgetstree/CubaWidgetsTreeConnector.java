/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.widgetstree;

import com.google.gwt.event.shared.HandlerRegistration;
import com.haulmont.cuba.web.toolkit.ui.CubaWidgetsTree;
import com.vaadin.client.*;
import com.vaadin.client.ui.VTree;
import com.vaadin.client.ui.tree.TreeConnector;
import com.vaadin.shared.ui.Connect;

import java.util.Collections;
import java.util.List;

/**
 * @author artamonov
 * @version $Id$
 */
@Connect(CubaWidgetsTree.class)
public class CubaWidgetsTreeConnector extends TreeConnector
        implements HasComponentsConnector {

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
    public void updateCaption(ComponentConnector connector) {
    }

    @Override
    public HandlerRegistration addConnectorHierarchyChangeHandler(
            ConnectorHierarchyChangeEvent.ConnectorHierarchyChangeHandler handler) {
        return ensureHandlerManager().addHandler(
                ConnectorHierarchyChangeEvent.TYPE, handler);
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
        return super.createNode(childUidl);
    }
}